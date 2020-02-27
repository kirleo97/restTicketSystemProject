package com.example.RestTicketSystem.domain;

import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "EVENT_ID")
    private Integer id;

    @NotNull(message = "You have to choose type of event")
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "EVENT_EVENTTYPE", nullable = false)
    private EventType eventType;

    @NotBlank(message = "The event's name field can't be empty!")
    @Length(min = 2,  message = "The length must be more than 1 letter")
    @Column(name = "EVENT_NAME", nullable = false)
    private String eventName;

    @NotNull(message = "You have to choose date of event")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Future(message = "The event cannot be held earlier than the current date. Please enter other information.")
    @Column(name = "EVENT_DATE", nullable = false)
    private LocalDateTime dateOfEvent;

    @NotNull(message = "You have to choose stadium of event")
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "STADIUM_ID", nullable = false)
    private Stadium stadiumOfEvent;

    @NotNull(message = "You have to choose start of preparation period of event")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Future(message = "Preparation for the event can't begin earlier than the current date. Please enter other information.")
    @Column(name = "event_startOfPreparationOfStadium", nullable = false)
    private LocalDate startOfPreparationOfStadium;

    @NotNull(message = "You have to choose end of preparation period of event")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Future(message = "The dismantling of the arena could not be completed earlier than the current date. Please enter other information.")
    @Column(name = "event_endOfDismantleOfStadium", nullable = false)
    private LocalDate endOfDismantleOfStadium;

    @NotNull(message = "You have to choose manager of event")
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "MANAGER_ID", nullable = false)
    private Manager eventManager;

    public Event() {
    }

    @Autowired
    public Event(EventType eventType, String eventName, LocalDateTime dateOfEvent, Stadium stadiumOfEvent, LocalDate startOfPreparationOfStadium, LocalDate endOfDismantleOfStadium, Manager eventManager) {
        this.eventType = eventType;
        this.eventName = eventName;
        this.dateOfEvent = dateOfEvent;
        this.stadiumOfEvent = stadiumOfEvent;
        this.startOfPreparationOfStadium = startOfPreparationOfStadium;
        this.endOfDismantleOfStadium = endOfDismantleOfStadium;
        this.eventManager = eventManager;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public LocalDateTime getDateOfEvent() {
        return dateOfEvent;
    }

    public void setDateOfEvent(LocalDateTime dateOfEvent) {
        this.dateOfEvent = dateOfEvent;
    }

    public Stadium getStadiumOfEvent() {
        return stadiumOfEvent;
    }

    public void setStadiumOfEvent(Stadium stadiumOfEvent) {
        this.stadiumOfEvent = stadiumOfEvent;
    }

    public LocalDate getStartOfPreparationOfStadium() {
        return startOfPreparationOfStadium;
    }

    public void setStartOfPreparationOfStadium(LocalDate startOfPreparationOfStadium) {
        this.startOfPreparationOfStadium = startOfPreparationOfStadium;
    }

    public LocalDate getEndOfDismantleOfStadium() {
        return endOfDismantleOfStadium;
    }

    public void setEndOfDismantleOfStadium(LocalDate endOfDismantleOfStadium) {
        this.endOfDismantleOfStadium = endOfDismantleOfStadium;
    }

    public Manager getEventManager() {
        return eventManager;
    }

    public void setEventManager(Manager eventManager) {
        this.eventManager = eventManager;
    }
}