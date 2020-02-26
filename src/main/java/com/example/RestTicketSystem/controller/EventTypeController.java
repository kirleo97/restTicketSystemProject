package com.example.RestTicketSystem.controller;

import com.example.RestTicketSystem.assembler.EventTypeModelAssembler;
import com.example.RestTicketSystem.domain.EventType;
import com.example.RestTicketSystem.error.exception.EventTypeNotFoundException;
import com.example.RestTicketSystem.model.EventTypeModel;
import com.example.RestTicketSystem.service.EventTypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
import java.util.Map;

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
        /*List<EventType> eventTypes = eventTypeService.findAll();
        CollectionModel<EventTypeModel> eventTypeModels = new EventTypeModelAssembler(EventTypeController.class, EventTypeModel.class).toCollectionModel(eventTypes);
        CollectionModel<EntityModel<EventType>> collectionModel = CollectionModel.wrap(eventTypes);

        recentResources.add(new Link("http://localhost:8080/eventType/recent", "recents"));
        collectionModel.add(WebMvcLinkBuilder.linkTo(EventTypeController.class).slash("recent").withRel("recents"));
        collectionModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EventTypeController.class).getAllEventTypes()).withRel("recents"))*/;


        List<EventType> eventTypes = eventTypeService.findAll();
        CollectionModel<EventTypeModel> collectionModel = new EventTypeModelAssembler(EventTypeController.class, EventTypeModel.class).toCollectionModel(eventTypes);
        collectionModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EventTypeController.class).getAllEventTypes()).withRel("allEventTypes"));
        return ResponseEntity.ok().body(collectionModel);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public EventType createEventType(@Valid @RequestBody EventType eventType) {
        return eventTypeService.saveEventType(eventType);
    }

    @GetMapping("/{id}")
    public EntityModel<EventTypeModel> getEventTypeById(@PathVariable @Min(1) Integer id) {
        /*EventType eventType = eventTypeService.findById(id).get();
        return eventType == null ? new ResponseEntity<>(null, HttpStatus.NOT_FOUND) : new ResponseEntity<>(eventType, HttpStatus.OK);*/
        EventType eventType = eventTypeService.findById(id).orElseThrow(() -> new EventTypeNotFoundException(id));
        EventTypeModel eventTypeModel = new EventTypeModel(eventType);
        EntityModel<EventTypeModel> entityModel = new EntityModel<>(eventTypeModel, WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EventTypeController.class).getEventTypeById(id)).withRel("eventTypeById"));
        return entityModel;
    }

    @PutMapping("/{id}")
    public EventType putEventType(@Valid @RequestBody EventType newEventType, @PathVariable @Min(1) Integer id) {
        EventType eventType = eventTypeService.findById(id).orElse(null);
        if (eventType != null) {
            //eventType.setEventTypeName(newEventType.getEventTypeName());
            BeanUtils.copyProperties(newEventType, eventType);
            eventType.setId(newEventType.getId());
            return eventTypeService.saveEventType(eventType);
        }
        return eventTypeService.saveEventType(newEventType);
    }

    /*@PatchMapping(value = "/{id}", consumes = "application/json")
    public EventType patchEventType(@PathVariable Integer id, @RequestBody EventType patchEventType) {
        EventType eventType = eventTypeService.findById(id).orElseThrow(() -> new EventTypeNotFoundException(id));
        //Set<String> fields = new HashSet<>();
        if (patchEventType.getEventTypeName() != null) {
            eventType.setEventTypeName(patchEventType.getEventTypeName());
        }
        *//*if (fields.size() != 0) {
            throw new EventTypeUnsupportedFieldPatchException(fields);
        }*//*
        return eventTypeService.saveEventType(eventType);
    }*/

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public EventType patchEventType(@RequestBody Map<String, Object> update, @PathVariable Integer id) {
        EventType eventType = eventTypeService.findById(id).orElseThrow(() -> new EventTypeNotFoundException(id));
        if (update.containsKey("eventTypeName")) {
            eventType.setEventTypeName((String) update.get("eventTypeName"));
        }
        return eventTypeService.saveEventType(eventType);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteEventType(@PathVariable Integer id) {
        try {
            eventTypeService.deleteById(id);
        } catch (EmptyResultDataAccessException e) { }
    }

    /*@GetMapping("/recent")
    public Iterable<EventType> getRecentEventTypes() {
        PageRequest pageRequest = PageRequest.of(0, 12, Sort.by("createdAt").descending());
        return eventTypeService.findRecentEventTypes(pageRequest);
    }*/

    /*@GetMapping("/recent")
    public CollectionModel<EntityModel<EventType>> getRecentEventTypes() {
        PageRequest pageRequest = PageRequest.of(0, 12, Sort.by("eventTypeName").descending());
        List<EventType> eventTypes = eventTypeService.findRecentEventTypes(pageRequest);
        CollectionModel<EventTypeModel> eventTypeModels = new EventTypeModelAssembler(EventTypeController.class, EventTypeModel.class).toCollectionModel(eventTypes);
        CollectionModel<Even>
        CollectionModel<EntityModel<EventType>> collectionModel = CollectionModel.wrap(eventTypes);

        //recentResources.add(new Link("http://localhost:8080/eventType/recent", "recents"));
        //collectionModel.add(WebMvcLinkBuilder.linkTo(EventTypeController.class).slash("recent").withRel("recents"));
        collectionModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EventTypeController.class).getRecentEventTypes()).withRel("recents"));
        return collectionModel;
    }*/

    /*@GetMapping("/{id}")
    public EventType getEventTypeById(@PathVariable("id") Integer id) {
        return eventTypeService.findById(id);
    }*/

    /*@PatchMapping(path = "/{eventTypeId}", consumes = "application/json")
    public EventType updateEventType(@PathVariable("eventTypeId") Integer eventTypeId, @RequestBody EventType patchEventType) {
        EventType updatedEventType = eventTypeService.findById(eventTypeId);
        if (patchEventType.getEventTypeName() != null) {
            updatedEventType.setEventTypeName(patchEventType.getEventTypeName());
        }
        return eventTypeService.saveEventType(updatedEventType);
    }*/
}