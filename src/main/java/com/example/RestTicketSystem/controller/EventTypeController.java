package com.example.RestTicketSystem.controller;

import com.example.RestTicketSystem.assembler.EventTypeModelAssembler;
import com.example.RestTicketSystem.domain.EventType;
import com.example.RestTicketSystem.error.exception.ResourceAlreadyExistsException;
import com.example.RestTicketSystem.model.EventTypeModel;
import com.example.RestTicketSystem.service.EventTypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
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
    public ResponseEntity<EntityModel<EventTypeModel>> getEventTypeById(@PathVariable @Min(1) Integer id) {
        EventType eventType = eventTypeService.findById(id);
        EventTypeModel eventTypeModel = new EventTypeModel(eventType);
        EntityModel<EventTypeModel> entityModel = new EntityModel<>(eventTypeModel, WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EventTypeController.class).getEventTypeById(id)).withRel("eventTypeById"));
        return ResponseEntity.ok(entityModel);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntityModel<EventTypeModel>> createEventType(@Valid @RequestBody EventType eventType) throws ResourceAlreadyExistsException {
        if (eventType.getId() != null && eventTypeService.existsById(eventType.getId())) {
            throw new ResourceAlreadyExistsException("EventType with ID [" + eventType.getId() + "] is already exist!");
        }
        EventTypeModel eventTypeModel = new EventTypeModel(eventTypeService.saveEventType(eventType));
        EntityModel<EventTypeModel> entityModel = new EntityModel<>(eventTypeModel, WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EventTypeController.class).getEventTypeById(eventType.getId())).withRel("creationEventType"));
        return new ResponseEntity<>(entityModel, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public EventType putEventType(@Valid @RequestBody EventType newEventType, @PathVariable @Min(1) Integer id) throws ResourceAlreadyExistsException {
        if (eventTypeService.existsById(id)) {
            EventType eventType = eventTypeService.findById(id);
            BeanUtils.copyProperties(newEventType, eventType);
            eventType.setId(id);
            return eventTypeService.saveEventType(eventType);
        } else {
            return eventTypeService.saveEventType(newEventType);
        }
    }

    @PatchMapping(value = "/{id}", consumes = "application/json")
    public EventType patchEventType(@Valid @RequestBody EventType patchEventType, @PathVariable @Min(1) Integer id) throws ResourceAlreadyExistsException {
        EventType eventType = eventTypeService.findById(id);
        if (patchEventType.getEventTypeName() != null) {
            eventType.setEventTypeName(patchEventType.getEventTypeName());
        }
        return eventTypeService.saveEventType(eventType);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteEventType(@PathVariable Integer id) {
        eventTypeService.deleteById(id);
    }
}