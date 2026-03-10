package com.christianpires.ticket_api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class TicketApiApplicationMainTest {

    @Test
    void shouldRunMainMethod() {
        assertDoesNotThrow(() -> TicketApiApplication.main(new String[]{}));
    }
}