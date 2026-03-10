package com.christianpires.ticket_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpenApiConfigTest {

    private final OpenApiConfig openApiConfig = new OpenApiConfig();

    @Test
    void shouldCreateOpenApiBean() {
        OpenAPI openAPI = openApiConfig.ticketApiInfo();

        assertNotNull(openAPI);
        assertNotNull(openAPI.getInfo());
        assertEquals("Ticket System API", openAPI.getInfo().getTitle());
        assertEquals("REST API for managing technical support tickets", openAPI.getInfo().getDescription());
        assertEquals("1.0.0", openAPI.getInfo().getVersion());
    }
}