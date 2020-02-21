package com.example.RestTicketSystem.error;

public class ManagerNotFoundException extends RuntimeException {
    public ManagerNotFoundException(Integer id) {
        super("EventType id not found: " + id);
    }
}