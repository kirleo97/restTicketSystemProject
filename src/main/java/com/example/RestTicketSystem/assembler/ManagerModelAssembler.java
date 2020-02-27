package com.example.RestTicketSystem.assembler;

import com.example.RestTicketSystem.domain.Manager;
import com.example.RestTicketSystem.model.ManagerModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

public class ManagerModelAssembler extends RepresentationModelAssemblerSupport<Manager, ManagerModel> {

    public ManagerModelAssembler(Class<?> controllerClass, Class<ManagerModel> resourceType) {
        super(controllerClass, resourceType);
    }

    @Override
    protected ManagerModel instantiateModel(Manager entity) {
        return new ManagerModel(entity);
    }

    @Override
    public ManagerModel toModel(Manager entity) {
        return createModelWithId(entity.getId(), entity);
    }
}