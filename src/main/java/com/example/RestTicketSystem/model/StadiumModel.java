package com.example.RestTicketSystem.model;

import com.example.RestTicketSystem.assembler.EventTypeModelAssembler;
import com.example.RestTicketSystem.controller.EventTypeController;
import com.example.RestTicketSystem.domain.Stadium;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(value = "stadium", collectionRelation = "stadiums")
public class StadiumModel extends RepresentationModel<StadiumModel> {
    private final String stadiumName;
    private final Iterable<EventTypeModel> eventTypes;
    private static final EventTypeModelAssembler eventTypeModelAssembler = new EventTypeModelAssembler(EventTypeController.class, EventTypeModel.class);

    public String getStadiumName() {
        return stadiumName;
    }

    public Iterable<EventTypeModel> getEventTypes() {
        return eventTypes;
    }

    public StadiumModel(Stadium stadium) {
        this.stadiumName = stadium.getStadiumName();
        this.eventTypes = eventTypeModelAssembler.toCollectionModel(stadium.getEventTypes());
    }
}