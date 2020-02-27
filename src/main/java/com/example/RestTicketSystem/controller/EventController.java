package com.example.RestTicketSystem.controller;

import com.example.RestTicketSystem.assembler.EventModelAssembler;
import com.example.RestTicketSystem.domain.Event;
import com.example.RestTicketSystem.error.exception.EventDataException;
import com.example.RestTicketSystem.error.exception.ResourceAlreadyExistsException;
import com.example.RestTicketSystem.model.EventModel;
import com.example.RestTicketSystem.service.EventService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(path = "/event", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
@Validated
public class EventController {
    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EventModel>> getAllEvents() {
        List<Event> events = eventService.findAll();
        CollectionModel<EventModel> collectionModel = new EventModelAssembler(EventController.class, EventModel.class).toCollectionModel(events);
        collectionModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EventController.class).getAllEvents()).withRel("allEvents"));
        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("{id}")
    public ResponseEntity<EventModel> getEventById(@PathVariable @Min(1) Integer id) {
        Event event = eventService.findById(id);
        EventModel eventModel = new EventModel(event);
        eventModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EventController.class).getEventById(id)).withRel("eventById"));
        return ResponseEntity.ok(eventModel);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventModel> createEvent(@Valid @RequestBody Event event) throws ResourceAlreadyExistsException, EventDataException {
        Integer id = event.getId();
        if (id != null && eventService.existsById(id)) {
            throw new ResourceAlreadyExistsException("Event with ID [" + event.getId() + "] is already exist!");
        }
        Event savedEvent = eventService.saveEvent(event);
        EventModel eventModel = new EventModel(savedEvent);
        eventModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EventController.class).getEventById(savedEvent.getId())).withRel("createdEvent"));
        return new ResponseEntity<>(eventModel, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventModel> putEvent(@Valid @RequestBody Event newEvent, @PathVariable @Min(1) Integer id) throws EventDataException {
        if (eventService.existsById(id)) {
            Event event = eventService.findById(id);
            BeanUtils.copyProperties(newEvent, event);
            event.setId(id);
            Event updatedEvent = eventService.saveEvent(event);
            EventModel eventModel = new EventModel(updatedEvent);
            eventModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EventController.class).getEventById(id)).withRel("updatedEvent"));
            return ResponseEntity.ok(eventModel);
        } else {
            Event savedEvent = eventService.saveEvent(newEvent);
            return new ResponseEntity<>(new EventModel(savedEvent).add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EventController.class).getEventById(savedEvent.getId())).withRel("createdEvent")), HttpStatus.CREATED);
        }
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventModel> patchEvent(@Valid @RequestBody Event patchEvent, @PathVariable @Min(1) Integer id) throws EventDataException {
        Event event = eventService.findById(id);
        if (patchEvent.getEventType() != null) {
            event.setEventType(patchEvent.getEventType());
        }
        if (patchEvent.getEventName() != null) {
            event.setEventName(patchEvent.getEventName());
        }
        if (patchEvent.getDateOfEvent() != null) {
            event.setDateOfEvent(patchEvent.getDateOfEvent());
        }
        if (patchEvent.getStadiumOfEvent() != null) {
            event.setStadiumOfEvent(patchEvent.getStadiumOfEvent());
        }
        if (patchEvent.getStartOfPreparationOfStadium() != null) {
            event.setStartOfPreparationOfStadium(patchEvent.getStartOfPreparationOfStadium());
        }
        if (patchEvent.getEndOfDismantleOfStadium() != null) {
            event.setEndOfDismantleOfStadium(patchEvent.getEndOfDismantleOfStadium());
        }
        if (patchEvent.getEventManager() != null) {
            event.setEventManager(patchEvent.getEventManager());
        }
        Event updatedEvent = eventService.saveEvent(event);
        return ResponseEntity.ok(new EventModel(updatedEvent).add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EventController.class).getEventById(id)).withRel("updatedEvent")));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteEvent(@PathVariable @Min(1) Integer id) {
        eventService.deleteById(id);
    }
}