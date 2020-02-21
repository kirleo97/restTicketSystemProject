package com.example.RestTicketSystem.model;

import com.example.RestTicketSystem.domain.EventType;
import com.example.RestTicketSystem.domain.Stadium;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.List;

@Relation(value = "stadium", collectionRelation = "stadiums")
public class StadiumModel extends RepresentationModel<StadiumModel> {
    private final String stadiumName;
    private final List<EventType> eventTypes;

    public String getStadiumName() {
        return stadiumName;
    }

    public List<EventType> getEventTypes() {
        return eventTypes;
    }

    public StadiumModel(Stadium stadium) {
        this.stadiumName = stadium.getStadiumName();
        this.eventTypes = stadium.getEventTypes();
    }
}