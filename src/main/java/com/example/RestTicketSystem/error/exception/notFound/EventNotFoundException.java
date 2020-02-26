package com.example.RestTicketSystem.error.exception.notFound;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(Integer id) {
        super("Event id not found: " + id);
    }
}