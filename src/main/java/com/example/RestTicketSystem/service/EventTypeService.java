package com.example.RestTicketSystem.service;

import com.example.RestTicketSystem.domain.EventType;
import com.example.RestTicketSystem.repository.EventTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Optional;

@Service
public class EventTypeService {
    private final EventTypeRepository eventTypeRepository;

    @Autowired
    public EventTypeService(EventTypeRepository eventTypeRepository) {
        this.eventTypeRepository = eventTypeRepository;
    }

    public Optional<EventType> findById(Integer id) {
        return eventTypeRepository.findById(id);
    }

    public List<EventType> findAll() {
        return eventTypeRepository.findAll();
    }

    public EventType findByName(String eventTypeName) {
        return eventTypeRepository.findByEventTypeName(eventTypeName);
    }

    //public List<EventType> findRecentEventTypes(PageRequest pageRequest) { return eventTypeRepository.findAll(pageRequest).getContent(); }

    public EventType saveEventType(EventType eventType) {
        return eventTypeRepository.save(eventType);
    }

    public void deleteById(Integer id) {
        eventTypeRepository.deleteById(id);
    }

    public boolean isValidationForEventTypeSuccessful(EventType eventType, BindingResult bindingResult) {
        EventType checkEventType = eventTypeRepository.findByEventTypeName(eventType.getEventTypeName());
        if (checkEventType != null) {
            if (!checkEventType.getId().equals(eventType.getId())) {
                bindingResult.addError(new FieldError("eventType", "eventTypeName", "EventType with name '" + eventType.getEventTypeName() + "' already exists. Please enter a different name."));
            }
        }
        return !bindingResult.hasErrors();
    }
}