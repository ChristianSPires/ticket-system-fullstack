const ticketForm = document.getElementById("ticket-form");
const titleInput = document.getElementById("title");
const descriptionInput = document.getElementById("description");
const feedbackElement = document.getElementById("feedback");
const ticketsContainer = document.getElementById("tickets-container");
const reloadButton = document.getElementById("reload-button");

function showFeedback(message, type) {
    feedbackElement.textContent = message;
    feedbackElement.className = `feedback ${type}`;
}

function hideFeedback() {
    feedbackElement.textContent = "";
    feedbackElement.className = "feedback hidden";
}

function formatDateTime(value) {
    if (!value) {
        return "-";
    }

    return new Date(value).toLocaleString();
}

function buildTicketCard(ticket) {
    const card = document.createElement("div");
    card.className = "ticket-card";

    const title = document.createElement("h3");
    title.textContent = `${ticket.id} - ${ticket.title}`;

    const meta = document.createElement("div");
    meta.className = "ticket-meta";
    meta.innerHTML = `
        <strong>Status:</strong> ${ticket.status}<br>
        <strong>Created At:</strong> ${formatDateTime(ticket.createdAt)}
    `;

    const description = document.createElement("p");
    description.className = "ticket-description";
    description.textContent = ticket.description;

    const actions = document.createElement("div");
    actions.className = "ticket-actions";

    const statusSelect = document.createElement("select");
    ["OPEN", "IN_PROGRESS", "CLOSED"].forEach(status => {
        const option = document.createElement("option");
        option.value = status;
        option.textContent = status;
        option.selected = ticket.status === status;
        statusSelect.appendChild(option);
    });

    const updateButton = document.createElement("button");
    updateButton.textContent = "Update Status";
    updateButton.addEventListener("click", async () => {
        try {
            hideFeedback();
            await updateTicketStatus(ticket.id, statusSelect.value);
            showFeedback("Ticket status updated successfully.", "success");
            await loadTickets();
        } catch (error) {
            showFeedback(error.message, "error");
        }
    });

    const deleteButton = document.createElement("button");
    deleteButton.textContent = "Delete";
    deleteButton.addEventListener("click", async () => {
        try {
            hideFeedback();
            await deleteTicket(ticket.id);
            showFeedback("Ticket deleted successfully.", "success");
            await loadTickets();
        } catch (error) {
            showFeedback(error.message, "error");
        }
    });

    actions.appendChild(statusSelect);
    actions.appendChild(updateButton);
    actions.appendChild(deleteButton);

    card.appendChild(title);
    card.appendChild(meta);
    card.appendChild(description);
    card.appendChild(actions);

    return card;
}

async function loadTickets() {
    ticketsContainer.innerHTML = "<p>Loading tickets...</p>";

    try {
        const tickets = await getAllTickets();

        if (!tickets.length) {
            ticketsContainer.innerHTML = `<p class="empty-state">No tickets found.</p>`;
            return;
        }

        ticketsContainer.innerHTML = "";
        tickets.forEach(ticket => {
            ticketsContainer.appendChild(buildTicketCard(ticket));
        });
    } catch (error) {
        ticketsContainer.innerHTML = `<p class="empty-state">${error.message}</p>`;
    }
}

ticketForm.addEventListener("submit", async (event) => {
    event.preventDefault();

    try {
        hideFeedback();

        const payload = {
            title: titleInput.value.trim(),
            description: descriptionInput.value.trim()
        };

        await createTicket(payload);

        ticketForm.reset();
        showFeedback("Ticket created successfully.", "success");
        await loadTickets();
    } catch (error) {
        showFeedback(error.message, "error");
    }
});

reloadButton.addEventListener("click", async () => {
    hideFeedback();
    await loadTickets();
});

window.addEventListener("load", async () => {
    await loadTickets();
});