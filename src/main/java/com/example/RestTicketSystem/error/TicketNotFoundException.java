package com.example.RestTicketSystem.error;

public class TicketNotFoundException extends RuntimeException {
    public TicketNotFoundException(Integer id) {
        super("Stadium id not found: " + id);
    }
}
