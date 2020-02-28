package com.example.RestTicketSystem.assembler;

import com.example.RestTicketSystem.domain.Sector;
import com.example.RestTicketSystem.model.SectorModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

public class SectorModelAssembler extends RepresentationModelAssemblerSupport<Sector, SectorModel> {

    public SectorModelAssembler(Class<?> controllerClass, Class<SectorModel> resourceType) {
        super(controllerClass, resourceType);
    }

    @Override
    protected SectorModel instantiateModel(Sector entity) {
        return new SectorModel(entity);
    }

    @Override
    public SectorModel toModel(Sector entity) {
        return createModelWithId(entity.getId(), entity);
    }
}