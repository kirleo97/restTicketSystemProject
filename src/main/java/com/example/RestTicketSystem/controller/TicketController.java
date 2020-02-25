package com.example.RestTicketSystem.controller;

import com.example.RestTicketSystem.assembler.TicketModelAssembler;
import com.example.RestTicketSystem.domain.Stadium;
import com.example.RestTicketSystem.domain.Ticket;
import com.example.RestTicketSystem.error.StadiumNotFoundException;
import com.example.RestTicketSystem.error.TicketNotFoundException;
import com.example.RestTicketSystem.model.StadiumModel;
import com.example.RestTicketSystem.model.TicketModel;
import com.example.RestTicketSystem.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/ticket", produces = "application/json")
@CrossOrigin(origins = "*")
public class TicketController {
    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    public CollectionModel<TicketModel> getAllTickets() {
        List<Ticket> tickets = ticketService.findAll();
        CollectionModel<TicketModel> collectionModel = new TicketModelAssembler(TicketController.class, TicketModel.class).toCollectionModel(tickets);
        collectionModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TicketController.class).getAllTickets()).withRel("allTickets"));
        return collectionModel;
    }

    @GetMapping("{id}")
    public EntityModel<TicketModel> getTicketById(@PathVariable Integer id) {
        //return ticketService.findById(id).orElseThrow(() -> new TicketNotFoundException(id));
        Ticket ticket = ticketService.findById(id).orElseThrow(() -> new TicketNotFoundException(id));
        TicketModel ticketModel = new TicketModel(ticket);
        EntityModel<TicketModel> entityModel = new EntityModel<>(ticketModel, WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TicketController.class).getTicketById(id)).withRel("ticketById"));
        return entityModel;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = "application/json")
    public Ticket createTicket(@RequestBody Ticket ticket) {
        return ticketService.saveTicket(ticket);
    }

    @PutMapping("/{id}")
    public Ticket putTicket(@RequestBody Ticket newTicket, @PathVariable Integer id) {
        Ticket ticket = ticketService.findById(id).orElse(null);
        if (ticket != null) {
            ticket.setEventOfTicket(newTicket.getEventOfTicket());
            ticket.setSectorOfTicket(newTicket.getSectorOfTicket());
            ticket.setSeatNumber(newTicket.getSeatNumber());
            ticket.setTicketCost(newTicket.getTicketCost());
            ticket.setTicketBought(newTicket.isTicketBought());
            return ticketService.saveTicket(ticket);
        }
        return ticketService.saveTicket(newTicket);
    }

    /*@PatchMapping(value = "/{id}", consumes = "application/json")
    public Stadium patchStadium(@RequestBody Map<String, Object> update, @PathVariable Integer id) {
        Stadium stadium = stadiumService.findById(id).orElseThrow(() -> new StadiumNotFoundException(id));
        if (update.containsKey("stadiumName")) {
            stadium.setStadiumName((String) update.get("stadiumName"));
        }
        if (update.containsKey("eventTypes")) {
            stadium.setEventTypes((List<EventType>) update.get("eventTypes"));
        }
        return stadiumService.saveStadium(stadium);
    }*/

    /*@PatchMapping(value = "/{id}", consumes = "application/json")
    public Stadium patchStadium(@PathVariable Integer id, @RequestBody Stadium patchStadium) {
        Stadium stadium = stadiumService.findById(id).orElseThrow(() -> new ManagerNotFoundException(id));
        //Set<String> fields = new HashSet<>();
        if (patchStadium.getStadiumName() != null) {
            stadium.setStadiumName(stadium.getStadiumName());
        }
        if (patchStadium.getEventTypes()!= null) {
            stadium.setEventTypes(patchStadium.getEventTypes());
        }
        *//*if (fields.size() != 0) {
            throw new StadiumUnsupportedFieldPatchException(fields);
        }*//*
        return stadiumService.saveStadium(stadium);
    }*/

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteTicket(@PathVariable Integer id) {
        try {
            ticketService.deleteById(id);
        } catch (EmptyResultDataAccessException e) { }
    }
}