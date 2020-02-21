package com.example.RestTicketSystem.model;

import com.example.RestTicketSystem.domain.Event;
import com.example.RestTicketSystem.domain.EventType;
import com.example.RestTicketSystem.domain.Manager;
import com.example.RestTicketSystem.domain.Stadium;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Relation(value = "event", collectionRelation = "events")
public class EventModel extends RepresentationModel<EventModel> {
    private final EventType eventType;
    private final String eventName;
    private final LocalDateTime dateOfEvent;
    private final Stadium stadiumOfEvent;
    private final LocalDate startOfPreparationOfStadium;
    private final LocalDate endOfDismantleOfStadium;
    private final Manager eventManager;

    public EventType getEventType() {
        return eventType;
    }

    public String getEventName() {
        return eventName;
    }

    public LocalDateTime getDateOfEvent() {
        return dateOfEvent;
    }

    public Stadium getStadiumOfEvent() {
        return stadiumOfEvent;
    }

    public LocalDate getStartOfPreparationOfStadium() {
        return startOfPreparationOfStadium;
    }

    public LocalDate getEndOfDismantleOfStadium() {
        return endOfDismantleOfStadium;
    }

    public Manager getEventManager() {
        return eventManager;
    }

    public EventModel(Event event) {
        this.eventType = event.getEventType();
        this.eventName = event.getEventName();
        this.dateOfEvent = event.getDateOfEvent();
        this.stadiumOfEvent = event.getStadiumOfEvent();
        this.startOfPreparationOfStadium = event.getStartOfPreparationOfStadium();
        this.endOfDismantleOfStadium = event.getEndOfDismantleOfStadium();
        this.eventManager = event.getEventManager();
    }
}
