package com.example.RestTicketSystem.model;

import com.example.RestTicketSystem.domain.EventType;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

// But EventTypeModel extends
//RepresentationModel to inherit a list of Link object and methods to manage the list of links.
@Relation(value = "eventType", collectionRelation = "eventTypes")
public class EventTypeModel extends RepresentationModel<EventTypeModel> {
    private final String eventTypeName;

    public String getEventTypeName() {
        return eventTypeName;
    }

    public EventTypeModel(EventType eventType) {
        this.eventTypeName = eventType.getEventTypeName();
    }
}