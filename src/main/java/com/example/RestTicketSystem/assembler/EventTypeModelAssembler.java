package com.example.RestTicketSystem.assembler;

import com.example.RestTicketSystem.domain.EventType;
import com.example.RestTicketSystem.model.EventTypeModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

public class EventTypeModelAssembler extends RepresentationModelAssemblerSupport<EventType, EventTypeModel> {

    public EventTypeModelAssembler(Class<?> controllerClass, Class<EventTypeModel> resourceType) {
        super(controllerClass, resourceType);
    }

    @Override
    protected EventTypeModel instantiateModel(EventType entity) {
        return new EventTypeModel(entity);
    }

    @Override
    public EventTypeModel toModel(EventType entity) {
        return createModelWithId(entity.getId(), entity);
    }
}