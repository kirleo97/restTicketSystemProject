package com.example.RestTicketSystem.model;

import com.example.RestTicketSystem.domain.Event;
import com.example.RestTicketSystem.domain.Sector;
import com.example.RestTicketSystem.domain.Ticket;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;

public class TicketModel extends RepresentationModel<TicketModel> {
    private final Event eventOfTicket;
    private final Sector sectorOfTicket;
    private final Integer seatNumber;
    private final BigDecimal ticketCost;
    private final boolean isTicketBought;

    public Event getEventOfTicket() {
        return eventOfTicket;
    }

    public Sector getSectorOfTicket() {
        return sectorOfTicket;
    }

    public Integer getSeatNumber() {
        return seatNumber;
    }

    public BigDecimal getTicketCost() {
        return ticketCost;
    }

    public boolean isTicketBought() {
        return isTicketBought;
    }

    public TicketModel(Ticket ticket) {
        this.eventOfTicket = ticket.getEventOfTicket();
        this.sectorOfTicket = ticket.getSectorOfTicket();
        this.seatNumber = ticket.getSeatNumber();
        this.ticketCost = ticket.getTicketCost();
        this.isTicketBought = ticket.isTicketBought();
    }
}
