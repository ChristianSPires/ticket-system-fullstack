package com.christianpires.ticket_api.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void shouldHandleTicketNotFoundException() {
        TicketNotFoundException exception = new TicketNotFoundException(10L);

        ResponseEntity<ApiErrorResponse> response = handler.handleTicketNotFound(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().getStatus());
        assertEquals("Not Found", response.getBody().getError());
        assertEquals("Ticket not found with id: 10", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void shouldHandleGenericException() {
        Exception exception = new Exception("Unexpected error");

        ResponseEntity<ApiErrorResponse> response = handler.handleGenericException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().getStatus());
        assertEquals("Internal Server Error", response.getBody().getError());
        assertEquals("Unexpected error", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }
}