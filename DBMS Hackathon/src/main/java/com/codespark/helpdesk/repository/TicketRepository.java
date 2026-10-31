package com.codespark.helpdesk.repository;

import com.codespark.helpdesk.model.Status;
import com.codespark.helpdesk.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByCustomer_IdOrderByCreatedAtDesc(Long customerId);
    List<Ticket> findByAgent_IdOrderByCreatedAtDesc(Long agentId);
    // SLA breach check: due date passed and not yet resolved/closed
    List<Ticket> findByDueAtBeforeAndStatusNotIn(LocalDateTime now, List<Status> doneStatuses);
}
