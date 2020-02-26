package com.example.RestTicketSystem.error.exception.notFound;

public class EventTypeNotFoundException extends RuntimeException {
    public EventTypeNotFoundException(Integer id) {
        super("EventType id not found: " + id);
    }
}