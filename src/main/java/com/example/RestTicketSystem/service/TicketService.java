package com.example.RestTicketSystem.service;

import com.example.RestTicketSystem.domain.Event;
import com.example.RestTicketSystem.domain.Sector;
import com.example.RestTicketSystem.domain.Ticket;
import com.example.RestTicketSystem.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;

    @Autowired
    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Optional<Ticket> findById(Integer id) {
        return ticketRepository.findById(id);
    }

    public List<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    public Ticket saveTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public void deleteById(Integer id) {
        ticketRepository.deleteById(id);
    }

    public List<Ticket> findAllByEvent(Event event) { return ticketRepository.findAllByEventOfTicket(event); }

    public Ticket findByEventOfTicketAndSectorOfTicketAndSeatNumber(Event eventOfTicket, Sector sectorOfTicket, Integer seatNumber) {
        return ticketRepository.findByEventOfTicketAndSectorOfTicketAndSeatNumber(eventOfTicket, sectorOfTicket, seatNumber);
    }

    public int checkFromSeatNumber(int fromSeatNumber) {
        return fromSeatNumber <= 0 ? 1 : fromSeatNumber;
    }

    public int checkToSeatNumber(int toSeatNumber, Sector sector) {
        int sectorNumberOfSeats = sector.getNumberOfSeats();
        return toSeatNumber > sectorNumberOfSeats ? sectorNumberOfSeats : toSeatNumber;
    }

    public int checkCost(int cost) {
        return cost < 0 ? 0 : cost;
    }

    public boolean isTicketWithEventAndSectorAndNumberExist(Event event, Sector sector, Integer seatNumber) {
        return findByEventOfTicketAndSectorOfTicketAndSeatNumber(event, sector, seatNumber) != null ? true : false;
    }

    public int checkSeatNumber(Sector sector, int seatNumber) {
        seatNumber = seatNumber <= 0 ? 1 : seatNumber;
        int maxNumberOfSeats = sector.getNumberOfSeats();
        return seatNumber > maxNumberOfSeats ? maxNumberOfSeats : seatNumber;
    }

    public Page<Ticket> findPaginated(List<Ticket> tickets, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Ticket> list;

        if (tickets.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, tickets.size());
            list = tickets.subList(startItem, toIndex);
        }

        Page<Ticket> bookPage
                = new PageImpl<Ticket>(list, PageRequest.of(currentPage, pageSize), tickets.size());

        return bookPage;
    }
}