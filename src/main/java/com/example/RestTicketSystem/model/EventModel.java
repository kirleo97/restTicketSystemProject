package com.example.RestTicketSystem.model;

import com.example.RestTicketSystem.assembler.EventTypeModelAssembler;
import com.example.RestTicketSystem.assembler.ManagerModelAssembler;
import com.example.RestTicketSystem.assembler.StadiumModelAssembler;
import com.example.RestTicketSystem.controller.EventTypeController;
import com.example.RestTicketSystem.controller.ManagerController;
import com.example.RestTicketSystem.controller.StadiumController;
import com.example.RestTicketSystem.domain.Event;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Relation(value = "event", collectionRelation = "events")
public class EventModel extends RepresentationModel<EventModel> {
    private final EventTypeModel eventType;
    private final String eventName;
    private final LocalDateTime dateOfEvent;
    private final StadiumModel stadiumOfEvent;
    private final LocalDate startOfPreparationOfStadium;
    private final LocalDate endOfDismantleOfStadium;
    private final ManagerModel eventManager;
    private static final StadiumModelAssembler stadiumModelAssembler = new StadiumModelAssembler(StadiumController.class, StadiumModel.class);
    private static final ManagerModelAssembler managerModelAssembler = new ManagerModelAssembler(ManagerController.class, ManagerModel.class);
    private static final EventTypeModelAssembler eventTypeModelAssembler = new EventTypeModelAssembler(EventTypeController.class, EventTypeModel.class);

    public EventTypeModel getEventType() {
        return eventType;
    }

    public String getEventName() {
        return eventName;
    }

    public LocalDateTime getDateOfEvent() {
        return dateOfEvent;
    }

    public LocalDate getStartOfPreparationOfStadium() {
        return startOfPreparationOfStadium;
    }

    public LocalDate getEndOfDismantleOfStadium() {
        return endOfDismantleOfStadium;
    }

    public StadiumModel getStadiumOfEvent() {
        return stadiumOfEvent;
    }

    public ManagerModel getEventManager() {
        return eventManager;
    }

    public EventModel(Event event) {
        this.eventType = eventTypeModelAssembler.toModel(event.getEventType());
        this.eventName = event.getEventName();
        this.dateOfEvent = event.getDateOfEvent();
        this.stadiumOfEvent = stadiumModelAssembler.toModel(event.getStadiumOfEvent());
        this.startOfPreparationOfStadium = event.getStartOfPreparationOfStadium();
        this.endOfDismantleOfStadium = event.getEndOfDismantleOfStadium();
        this.eventManager = managerModelAssembler.toModel(event.getEventManager());
    }
}