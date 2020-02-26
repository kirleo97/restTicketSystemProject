package com.example.RestTicketSystem.error.exception;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(Integer id) {
        super("Event id not found: " + id);
    }
}