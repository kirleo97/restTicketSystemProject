package com.example.RestTicketSystem.controller;

import com.example.RestTicketSystem.assembler.EventModelAssembler;
import com.example.RestTicketSystem.domain.Event;
import com.example.RestTicketSystem.domain.EventType;
import com.example.RestTicketSystem.domain.Manager;
import com.example.RestTicketSystem.domain.Stadium;
import com.example.RestTicketSystem.error.EventNotFoundException;
import com.example.RestTicketSystem.model.EventModel;
import com.example.RestTicketSystem.service.EventService;
import com.example.RestTicketSystem.service.EventTypeService;
import com.example.RestTicketSystem.service.ManagerService;
import com.example.RestTicketSystem.service.StadiumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/event", produces = "application/json")
@CrossOrigin(origins = "*")
public class EventController {
    private final EventService eventService;
    private final EventTypeService eventTypeService;
    private final StadiumService stadiumService;
    private final ManagerService managerService;

    @Autowired
    public EventController(EventService eventService, EventTypeService eventTypeService, StadiumService stadiumService, ManagerService managerService) {
        this.eventService = eventService;
        this.eventTypeService = eventTypeService;
        this.stadiumService = stadiumService;
        this.managerService = managerService;
    }

    @GetMapping
    public CollectionModel<EventModel> getAllEvents() {
        List<Event> events = eventService.findAll();
        CollectionModel<EventModel> collectionModel = new EventModelAssembler(EventController.class, EventModel.class).toCollectionModel(events);
        collectionModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EventController.class).getAllEvents()).withRel("allEvents"));
        return collectionModel;
    }

    @GetMapping("{id}")
    public Event getEventById(@PathVariable Integer id) {
        return eventService.findById(id).orElseThrow(() -> new EventNotFoundException(id));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = "application/json")
    public Event createEvent(@RequestBody Event event) {
        return eventService.saveEvent(event);
    }

    @PutMapping("/{id}")
    public Event putEvent(@RequestBody Event newEvent, @PathVariable Integer id) {
        Event event = eventService.findById(id).orElse(null);
        if (event != null) {
            event.setEventType(newEvent.getEventType());
            event.setEventName(newEvent.getEventName());
            event.setDateOfEvent(newEvent.getDateOfEvent());
            event.setStadiumOfEvent(newEvent.getStadiumOfEvent());
            event.setStartOfPreparationOfStadium(newEvent.getStartOfPreparationOfStadium());
            event.setEndOfDismantleOfStadium(newEvent.getEndOfDismantleOfStadium());
            event.setEventManager(newEvent.getEventManager());
            return eventService.saveEvent(event);
        }
        return eventService.saveEvent(newEvent);
    }

    @PatchMapping(value = "/{id}", consumes = "application/json")
    public Event patchEvent(@RequestBody Map<String, Object> update, @PathVariable Integer id) {
        Event event = eventService.findById(id).orElseThrow(() -> new EventNotFoundException(id));
        if (update.containsKey("eventType")) {
            event.setEventType((EventType) update.get("eventType"));
        }
        if (update.containsKey("eventName")) {
            event.setEventName((String) update.get("eventName"));
        }
        if (update.containsKey("dateOfEvent")) {
            event.setDateOfEvent((LocalDateTime) update.get("dateOfEvent"));
        }
        if (update.containsKey("stadiumOfEvent")) {
            event.setStadiumOfEvent((Stadium) update.get("stadiumOfEvent"));
        }
        if (update.containsKey("startOfPreparationOfStadium")) {
            event.setStartOfPreparationOfStadium((LocalDate) update.get("startOfPreparationOfStadium"));
        }
        if (update.containsKey("endOfDismantleOfStadium")) {
            event.setEndOfDismantleOfStadium((LocalDate) update.get("endOfDismantleOfStadium"));
        }
        if (update.containsKey("eventManager")) {
            event.setEventManager((Manager) update.get("eventManager"));
        }
        return eventService.saveEvent(event);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteEvent(@PathVariable Integer id) {
        try {
            eventService.deleteById(id);
        } catch (EmptyResultDataAccessException e) { }
    }
}