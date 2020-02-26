package com.example.RestTicketSystem.error.exception.notFound;

public class StadiumNotFoundException extends RuntimeException {
    public StadiumNotFoundException(Integer id) {
        super("Stadium id not found: " + id);
    }
}