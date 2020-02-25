package com.example.RestTicketSystem.assembler;

import com.example.RestTicketSystem.domain.Ticket;
import com.example.RestTicketSystem.model.TicketModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

public class TicketModelAssembler extends RepresentationModelAssemblerSupport<Ticket, TicketModel> {
    public TicketModelAssembler(Class<?> controllerClass, Class<TicketModel> resourceType) {
        super(controllerClass, resourceType);
    }

    @Override
    protected TicketModel instantiateModel(Ticket entity) {
        return new TicketModel(entity);
    }

    @Override
    public TicketModel toModel(Ticket entity) {
        return createModelWithId(entity.getId(), entity);
    }
}
