package com.example.RestTicketSystem.error;

public class EventTypeNotFoundException extends RuntimeException {
    public EventTypeNotFoundException(Integer id) {
        super("EventType id not found: " + id);
    }
}