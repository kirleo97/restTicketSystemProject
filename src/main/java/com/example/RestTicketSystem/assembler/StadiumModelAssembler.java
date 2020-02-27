package com.example.RestTicketSystem.assembler;

import com.example.RestTicketSystem.domain.Stadium;
import com.example.RestTicketSystem.model.StadiumModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

public class StadiumModelAssembler extends RepresentationModelAssemblerSupport<Stadium, StadiumModel> {

    public StadiumModelAssembler(Class<?> controllerClass, Class<StadiumModel> resourceType) {
        super(controllerClass, resourceType);
    }

    @Override
    protected StadiumModel instantiateModel(Stadium entity) {
        return new StadiumModel(entity);
    }

    @Override
    public StadiumModel toModel(Stadium entity) {
        return createModelWithId(entity.getId(), entity);
    }
}
