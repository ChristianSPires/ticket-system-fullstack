const API_BASE_URL = "http://localhost:8080/tickets";

async function getAllTickets() {
    const response = await fetch(API_BASE_URL);

    if (!response.ok) {
        throw new Error("Failed to load tickets.");
    }

    return await response.json();
}

async function createTicket(ticketData) {
    const response = await fetch(API_BASE_URL, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(ticketData)
    });

    if (!response.ok) {
        const error = await safeReadError(response);
        throw new Error(error);
    }

    return await response.json();
}

async function updateTicketStatus(id, status) {
    const response = await fetch(`${API_BASE_URL}/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ status })
    });

    if (!response.ok) {
        const error = await safeReadError(response);
        throw new Error(error);
    }

    return await response.json();
}

async function deleteTicket(id) {
    const response = await fetch(`${API_BASE_URL}/${id}`, {
        method: "DELETE"
    });

    if (!response.ok) {
        const error = await safeReadError(response);
        throw new Error(error);
    }
}

async function safeReadError(response) {
    try {
        const data = await response.json();
        return data.message || "Unexpected API error.";
    } catch {
        return "Unexpected API error.";
    }
}