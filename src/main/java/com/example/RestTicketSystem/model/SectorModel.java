package com.example.RestTicketSystem.model;

import com.example.RestTicketSystem.assembler.StadiumModelAssembler;
import com.example.RestTicketSystem.controller.StadiumController;
import com.example.RestTicketSystem.domain.Sector;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(value = "sector", collectionRelation = "sectors")
public class SectorModel extends RepresentationModel<SectorModel> {
    private final StadiumModel stadium;
    private final String sectorName;
    private final Integer numberOfSeats;
    private static final StadiumModelAssembler stadiumModelAssembler = new StadiumModelAssembler(StadiumController.class, StadiumModel.class);

    public StadiumModel getStadium() {
        return stadium;
    }

    public String getSectorName() {
        return sectorName;
    }

    public Integer getNumberOfSeats() {
        return numberOfSeats;
    }

    public SectorModel(Sector sector) {
        this.stadium = stadiumModelAssembler.toModel(sector.getStadium());
        this.sectorName = sector.getSectorName();
        this.numberOfSeats = sector.getNumberOfSeats();
    }
}