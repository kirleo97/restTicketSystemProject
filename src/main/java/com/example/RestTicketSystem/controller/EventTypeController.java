package com.example.RestTicketSystem.controller;

import com.example.RestTicketSystem.assembler.EventTypeModelAssembler;
import com.example.RestTicketSystem.domain.EventType;
import com.example.RestTicketSystem.error.exception.ResourceAlreadyExistsException;
import com.example.RestTicketSystem.model.EventTypeModel;
import com.example.RestTicketSystem.service.EventTypeService;
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
@RequestMapping(path = "/eventType", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
@Validated
public class EventTypeController {
    private final EventTypeService eventTypeService;

    @Autowired
    public EventTypeController(EventTypeService eventTypeService) {
        this.eventTypeService = eventTypeService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EventTypeModel>> getAllEventTypes() {
        List<EventType> eventTypes = eventTypeService.findAll();
        CollectionModel<EventTypeModel> collectionModel = new EventTypeModelAssembler(EventTypeController.class, EventTypeModel.class).toCollectionModel(eventTypes);
        collectionModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EventTypeController.class).getAllEventTypes()).withRel("allEventTypes"));
        return ResponseEntity.ok().body(collectionModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventTypeModel> getEventTypeById(@PathVariable @Min(1) Integer id) {
        EventType eventType = eventTypeService.findById(id);
        EventTypeModel eventTypeModel = new EventTypeModel(eventType);
        eventTypeModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EventTypeController.class).getEventTypeById(id)).withRel("eventTypeById"));
        return ResponseEntity.ok(eventTypeModel);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventTypeModel> createEventType(@Valid @RequestBody EventType eventType) throws ResourceAlreadyExistsException {
        Integer id = eventType.getId();
        if (id != null && eventTypeService.existsById(id)) {
            throw new ResourceAlreadyExistsException("EventType with ID [" + eventType.getId() + "] is already exist!");
        }
        EventType savedEventType = eventTypeService.saveEventType(eventType);
        EventTypeModel eventTypeModel = new EventTypeModel(savedEventType);
        eventTypeModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EventTypeController.class).getEventTypeById(savedEventType.getId())).withRel("createdEventType"));
        return new ResponseEntity<>(eventTypeModel, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventTypeModel> putEventType(@Valid @RequestBody EventType newEventType, @PathVariable @Min(1) Integer id) throws ResourceAlreadyExistsException {
        if (eventTypeService.existsById(id)) {
            EventType eventType = eventTypeService.findById(id);
            BeanUtils.copyProperties(newEventType, eventType);
            eventType.setId(id);
            EventType updatedEventType = eventTypeService.saveEventType(eventType);
            EventTypeModel eventTypeModel = new EventTypeModel(updatedEventType);
            eventTypeModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EventTypeController.class).getEventTypeById(updatedEventType.getId())).withRel("updatedEventType"));
            return ResponseEntity.ok(eventTypeModel);
        } else {
            EventType savedEventType = eventTypeService.saveEventType(newEventType);
            return new ResponseEntity<>(new EventTypeModel(savedEventType).add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EventTypeController.class).getEventTypeById(savedEventType.getId())).withRel("savedEventType")), HttpStatus.CREATED);
        }
    }

    @PatchMapping(value = "/{id}", consumes = "application/json")
    public ResponseEntity<EventTypeModel> patchEventType(@Valid @RequestBody EventType patchEventType, @PathVariable @Min(1) Integer id) throws ResourceAlreadyExistsException {
        EventType eventType = eventTypeService.findById(id);
        if (patchEventType.getEventTypeName() != null) {
            eventType.setEventTypeName(patchEventType.getEventTypeName());
        }
        EventType updatedEventType = eventTypeService.saveEventType(eventType);
        return ResponseEntity.ok(new EventTypeModel(updatedEventType).add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EventTypeController.class).getEventTypeById(updatedEventType.getId())).withRel("updatedEventType")));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteEventType(@PathVariable Integer id) {
        eventTypeService.deleteById(id);
    }
}