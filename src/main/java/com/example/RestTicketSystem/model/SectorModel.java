package com.example.RestTicketSystem.model;

import com.example.RestTicketSystem.domain.Sector;
import com.example.RestTicketSystem.domain.Stadium;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(value = "sector", collectionRelation = "sectors")
public class SectorModel extends RepresentationModel<SectorModel> {
    private final Stadium stadium;
    private final String sectorName;
    private final Integer numberOfSeats;

    public Stadium getStadium() {
        return stadium;
    }

    public String getSectorName() {
        return sectorName;
    }

    public Integer getNumberOfSeats() {
        return numberOfSeats;
    }

    public SectorModel(Sector sector) {
        this.stadium = sector.getStadium();
        this.sectorName = sector.getSectorName();
        this.numberOfSeats = sector.getNumberOfSeats();
    }
}