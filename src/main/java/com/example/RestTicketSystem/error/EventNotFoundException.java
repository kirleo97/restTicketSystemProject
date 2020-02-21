package com.example.RestTicketSystem.error;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(Integer id) {
        super("Event id not found: " + id);
    }
}