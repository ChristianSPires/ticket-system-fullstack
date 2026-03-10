package com.christianpires.ticket_api.repository;

import com.christianpires.ticket_api.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}