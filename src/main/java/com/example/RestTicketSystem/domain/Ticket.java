package com.example.RestTicketSystem.domain;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "TICKET_ID")
    private Integer id;

    @NotNull
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "TICKET_EVENT", nullable = false)
    private Event eventOfTicket;

    @NotNull
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "TICKET_SECTOR", nullable = false)
    private Sector sectorOfTicket;

    @NotNull
    @Column(name = "TICKET_SEATNUMBER", nullable = false)
    @Positive
    private Integer seatNumber;

    @NotNull
    @DecimalMin(message = "Ticket cost should be positive",
            value = "0")
    @Column(name = "TICKET_COST", nullable = false)
    private BigDecimal ticketCost;

    @Column(name = "TICKET_ISFREE", nullable = false)
    @Autowired
    private boolean isTicketBought = false;

    public Ticket() {
    }

    @Autowired
    public Ticket(Event eventOfTicket, Sector sectorOfTicket, int seatNumber, BigDecimal ticketCost) {
        this.eventOfTicket = eventOfTicket;
        this.sectorOfTicket = sectorOfTicket;
        this.seatNumber = seatNumber;
        this.ticketCost = ticketCost;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Event getEventOfTicket() {
        return eventOfTicket;
    }

    public void setEventOfTicket(Event eventOfTicket) {
        this.eventOfTicket = eventOfTicket;
    }

    public Sector getSectorOfTicket() { return sectorOfTicket; }

    public void setSectorOfTicket(Sector sectorOfTicket) {
        this.sectorOfTicket = sectorOfTicket;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public BigDecimal getTicketCost() {
        return ticketCost;
    }

    public void setTicketCost(BigDecimal ticketCost) {
        this.ticketCost = ticketCost;
    }

    public boolean isTicketBought() {
        return isTicketBought;
    }

    public void setTicketBought(boolean ticketBought) {
        isTicketBought = ticketBought;
    }
}