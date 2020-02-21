package com.example.RestTicketSystem.assembler;

import com.example.RestTicketSystem.domain.Event;
import com.example.RestTicketSystem.model.EventModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

public class EventModelAssembler extends RepresentationModelAssemblerSupport<Event, EventModel> {
    public EventModelAssembler(Class<?> controllerClass, Class<EventModel> resourceType) {
        super(controllerClass, resourceType);
    }

    @Override
    protected EventModel instantiateModel(Event entity) {
        return new EventModel(entity);
    }

    @Override
    public EventModel toModel(Event entity) {
        return createModelWithId(entity.getId(), entity);
    }
}