package com.christianpires.ticket_api.service;

import com.christianpires.ticket_api.dto.CreateTicketRequest;
import com.christianpires.ticket_api.dto.TicketResponse;
import com.christianpires.ticket_api.dto.UpdateTicketStatusRequest;
import com.christianpires.ticket_api.entity.Ticket;
import com.christianpires.ticket_api.enums.TicketStatus;
import com.christianpires.ticket_api.exception.TicketNotFoundException;
import com.christianpires.ticket_api.mapper.TicketMapper;
import com.christianpires.ticket_api.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    private TicketService ticketService;

    @BeforeEach
    void setUp() {
        TicketMapper ticketMapper = new TicketMapper();
        ticketService = new TicketService(ticketRepository, ticketMapper);
    }

    @Test
    void shouldCreateTicketSuccessfully() {
        CreateTicketRequest request = new CreateTicketRequest();
        request.setTitle("Printer issue");
        request.setDescription("Printer is offline");

        Ticket savedTicket = Ticket.builder()
                .id(1L)
                .title("Printer issue")
                .description("Printer is offline")
                .status(TicketStatus.OPEN)
                .createdAt(LocalDateTime.now())
                .build();

        when(ticketRepository.save(any(Ticket.class))).thenReturn(savedTicket);

        TicketResponse response = ticketService.createTicket(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Printer issue", response.getTitle());
        assertEquals("Printer is offline", response.getDescription());
        assertEquals(TicketStatus.OPEN, response.getStatus());

        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    void shouldReturnAllTickets() {
        Ticket ticket = Ticket.builder()
                .id(1L)
                .title("Network issue")
                .description("Internet is down")
                .status(TicketStatus.IN_PROGRESS)
                .createdAt(LocalDateTime.now())
                .build();

        when(ticketRepository.findAll()).thenReturn(List.of(ticket));

        List<TicketResponse> responses = ticketService.getAllTickets();

        assertEquals(1, responses.size());
        assertEquals("Network issue", responses.get(0).getTitle());
        assertEquals(TicketStatus.IN_PROGRESS, responses.get(0).getStatus());

        verify(ticketRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnTicketById() {
        Ticket ticket = Ticket.builder()
                .id(1L)
                .title("Email issue")
                .description("Email not syncing")
                .status(TicketStatus.OPEN)
                .createdAt(LocalDateTime.now())
                .build();

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        TicketResponse response = ticketService.getTicketById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Email issue", response.getTitle());
        assertEquals("Email not syncing", response.getDescription());
        assertEquals(TicketStatus.OPEN, response.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenTicketByIdNotFound() {
        when(ticketRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(TicketNotFoundException.class, () -> ticketService.getTicketById(99L));
    }

    @Test
    void shouldUpdateTicketStatusSuccessfully() {
        Ticket existingTicket = Ticket.builder()
                .id(1L)
                .title("Printer issue")
                .description("Printer is offline")
                .status(TicketStatus.OPEN)
                .createdAt(LocalDateTime.now())
                .build();

        UpdateTicketStatusRequest request = new UpdateTicketStatusRequest();
        request.setStatus(TicketStatus.CLOSED);

        Ticket updatedTicket = Ticket.builder()
                .id(1L)
                .title("Printer issue")
                .description("Printer is offline")
                .status(TicketStatus.CLOSED)
                .createdAt(existingTicket.getCreatedAt())
                .build();

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(existingTicket));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(updatedTicket);

        TicketResponse response = ticketService.updateTicketStatus(1L, request);

        assertNotNull(response);
        assertEquals(TicketStatus.CLOSED, response.getStatus());

        verify(ticketRepository, times(1)).findById(1L);
        verify(ticketRepository, times(1)).save(existingTicket);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingTicket() {
        UpdateTicketStatusRequest request = new UpdateTicketStatusRequest();
        request.setStatus(TicketStatus.CLOSED);

        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TicketNotFoundException.class, () -> ticketService.updateTicketStatus(1L, request));
    }

    @Test
    void shouldDeleteTicketSuccessfully() {
        Ticket ticket = Ticket.builder()
                .id(1L)
                .title("Printer issue")
                .description("Printer is offline")
                .status(TicketStatus.OPEN)
                .createdAt(LocalDateTime.now())
                .build();

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        ticketService.deleteTicket(1L);

        verify(ticketRepository, times(1)).findById(1L);
        verify(ticketRepository, times(1)).delete(ticket);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingTicket() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TicketNotFoundException.class, () -> ticketService.deleteTicket(1L));
    }
}