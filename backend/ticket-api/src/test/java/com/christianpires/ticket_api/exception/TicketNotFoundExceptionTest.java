package com.christianpires.ticket_api.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TicketNotFoundExceptionTest {

    @Test
    void shouldBuildCorrectMessage() {
        TicketNotFoundException exception = new TicketNotFoundException(7L);

        assertEquals("Ticket not found with id: 7", exception.getMessage());
    }
}