package com.example.RestTicketSystem.service;

import com.example.RestTicketSystem.controller.EventTypeController;
import com.example.RestTicketSystem.domain.EventType;
import com.example.RestTicketSystem.error.exception.ResourceAlreadyExistsException;
import com.example.RestTicketSystem.model.EventTypeModel;
import com.example.RestTicketSystem.repository.EventTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventTypeService {
    private final EventTypeRepository eventTypeRepository;

    @Autowired
    public EventTypeService(EventTypeRepository eventTypeRepository) {
        this.eventTypeRepository = eventTypeRepository;
    }

    public boolean existsById(Integer id) {
        return eventTypeRepository.existsById(id);
    }

    public EventType findById(Integer id) throws ResourceNotFoundException {
        EventType eventType = eventTypeRepository.findById(id).orElse(null);
        if (eventType == null) {
            throw new ResourceNotFoundException("EventType with ID " + id + " doesn't exist!");
        } else
            return eventType;
    }

    public List<EventType> findAll() {
        return eventTypeRepository.findAll();
    }

    public EventType findByName(String eventTypeName) {
        return eventTypeRepository.findByEventTypeName(eventTypeName);
    }

    public EventType saveEventType(EventType eventType) throws ResourceAlreadyExistsException {
        String eventTypeName = eventType.getEventTypeName();
        EventType checkEventType = findByName(eventTypeName);
        if ( (checkEventType != null) && (!checkEventType.getId().equals(eventType.getId())) ) {
            throw new ResourceAlreadyExistsException("EventType with name [" + eventTypeName + "] is already exist!");
        }
        return eventTypeRepository.save(eventType);
    }

    /*public EventType updateEventType(EventType eventType) throws ResourceNotFoundException {
        if (!existsById(eventType.getId())) {
            throw new ResourceNotFoundException("EventType with ID " + eventType.getId() + " doesn't exist!");
        }
        return eventTypeRepository.save(eventType);
    }*/

    public void deleteById(Integer id) {
        if (!existsById(id)) {
            throw new ResourceNotFoundException("EventType with ID " + id + " doesn't exist!");
        }
        eventTypeRepository.deleteById(id);
    }

    public EntityModel<EventTypeModel> getEntityModel(EventType eventType, String relation) {
        EventTypeModel eventTypeModel = new EventTypeModel(eventType);
        EntityModel<EventTypeModel> entityModel = new EntityModel<>(eventTypeModel, WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EventTypeController.class).getEventTypeById(eventType.getId())).withRel(relation));
        return entityModel;
    }

    /*public boolean isValidationForEventTypeSuccessful(EventType eventType, BindingResult bindingResult) {
        EventType checkEventType = eventTypeRepository.findByEventTypeName(eventType.getEventTypeName());
        if (checkEventType != null) {
            if (!checkEventType.getId().equals(eventType.getId())) {
                bindingResult.addError(new FieldError("eventType", "eventTypeName", "EventType with name '" + eventType.getEventTypeName() + "' already exists. Please enter a different name."));
            }
        }
        return !bindingResult.hasErrors();
    }*/
}