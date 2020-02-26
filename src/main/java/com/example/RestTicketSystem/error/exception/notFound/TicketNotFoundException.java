package com.example.RestTicketSystem.error.exception.notFound;

public class TicketNotFoundException extends RuntimeException {
    public TicketNotFoundException(Integer id) {
        super("Stadium id not found: " + id);
    }
}
