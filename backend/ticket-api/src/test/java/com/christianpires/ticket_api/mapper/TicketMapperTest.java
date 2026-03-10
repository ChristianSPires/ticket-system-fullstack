package com.christianpires.ticket_api.mapper;

import com.christianpires.ticket_api.dto.TicketResponse;
import com.christianpires.ticket_api.entity.Ticket;
import com.christianpires.ticket_api.enums.TicketStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TicketMapperTest {

    private final TicketMapper ticketMapper = new TicketMapper();

    @Test
    void shouldMapTicketToResponse() {
        LocalDateTime createdAt = LocalDateTime.now();

        Ticket ticket = Ticket.builder()
                .id(1L)
                .title("Printer issue")
                .description("Printer is offline")
                .status(TicketStatus.OPEN)
                .createdAt(createdAt)
                .build();

        TicketResponse response = ticketMapper.toResponse(ticket);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Printer issue", response.getTitle());
        assertEquals("Printer is offline", response.getDescription());
        assertEquals(TicketStatus.OPEN, response.getStatus());
        assertEquals(createdAt, response.getCreatedAt());
    }
}