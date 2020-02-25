package com.example.RestTicketSystem.repository;

import com.example.RestTicketSystem.domain.Event;
import com.example.RestTicketSystem.domain.Sector;
import com.example.RestTicketSystem.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    List<Ticket> findAllByEventOfTicket(Event eventOfTicket);
    Ticket findByEventOfTicketAndSectorOfTicketAndSeatNumber(Event eventOfTicket, Sector sectorOfTicket, Integer seatNumber);
}