package com.christianpires.ticket_api.controller;

import com.christianpires.ticket_api.dto.CreateTicketRequest;
import com.christianpires.ticket_api.dto.TicketResponse;
import com.christianpires.ticket_api.dto.UpdateTicketStatusRequest;
import com.christianpires.ticket_api.enums.TicketStatus;
import com.christianpires.ticket_api.exception.GlobalExceptionHandler;
import com.christianpires.ticket_api.exception.TicketNotFoundException;
import com.christianpires.ticket_api.service.TicketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicketController.class)
@Import(GlobalExceptionHandler.class)
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TicketService ticketService;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Test
    void shouldCreateTicketSuccessfully() throws Exception {
        CreateTicketRequest request = new CreateTicketRequest();
        request.setTitle("Printer issue");
        request.setDescription("Printer offline");

        TicketResponse response = TicketResponse.builder()
                .id(1L)
                .title("Printer issue")
                .description("Printer offline")
                .status(TicketStatus.OPEN)
                .createdAt(LocalDateTime.now())
                .build();

        when(ticketService.createTicket(any(CreateTicketRequest.class))).thenReturn(response);

        mockMvc.perform(post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Printer issue"))
                .andExpect(jsonPath("$.status").value("OPEN"));
    }

    @Test
    void shouldReturnInternalServerErrorWhenCreateTicketRequestIsInvalid() throws Exception {
        CreateTicketRequest request = new CreateTicketRequest();
        request.setTitle("");
        request.setDescription("");

        mockMvc.perform(post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void shouldReturnAllTickets() throws Exception {
        TicketResponse response = TicketResponse.builder()
                .id(1L)
                .title("Network issue")
                .description("Internet down")
                .status(TicketStatus.IN_PROGRESS)
                .createdAt(LocalDateTime.now())
                .build();

        when(ticketService.getAllTickets()).thenReturn(List.of(response));

        mockMvc.perform(get("/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Network issue"))
                .andExpect(jsonPath("$[0].status").value("IN_PROGRESS"));
    }

    @Test
    void shouldReturnTicketById() throws Exception {
        TicketResponse response = TicketResponse.builder()
                .id(1L)
                .title("Email issue")
                .description("Email not syncing")
                .status(TicketStatus.OPEN)
                .createdAt(LocalDateTime.now())
                .build();

        when(ticketService.getTicketById(1L)).thenReturn(response);

        mockMvc.perform(get("/tickets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Email issue"));
    }

    @Test
    void shouldReturnNotFoundWhenTicketByIdDoesNotExist() throws Exception {
        when(ticketService.getTicketById(99L)).thenThrow(new TicketNotFoundException(99L));

        mockMvc.perform(get("/tickets/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Ticket not found with id: 99"));
    }

    @Test
    void shouldUpdateTicketStatusSuccessfully() throws Exception {
        UpdateTicketStatusRequest request = new UpdateTicketStatusRequest();
        request.setStatus(TicketStatus.CLOSED);

        TicketResponse response = TicketResponse.builder()
                .id(1L)
                .title("Printer issue")
                .description("Printer offline")
                .status(TicketStatus.CLOSED)
                .createdAt(LocalDateTime.now())
                .build();

        when(ticketService.updateTicketStatus(eq(1L), any(UpdateTicketStatusRequest.class))).thenReturn(response);

        mockMvc.perform(put("/tickets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CLOSED"));
    }

    @Test
    void shouldReturnInternalServerErrorWhenUpdateStatusRequestIsInvalid() throws Exception {
        UpdateTicketStatusRequest request = new UpdateTicketStatusRequest();

        mockMvc.perform(put("/tickets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void shouldDeleteTicketSuccessfully() throws Exception {
        mockMvc.perform(delete("/tickets/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistingTicket() throws Exception {
        doThrow(new TicketNotFoundException(1L)).when(ticketService).deleteTicket(1L);

        mockMvc.perform(delete("/tickets/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Ticket not found with id: 1"));
    }
}