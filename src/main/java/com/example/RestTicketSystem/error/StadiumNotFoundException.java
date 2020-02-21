package com.example.RestTicketSystem.error;

public class StadiumNotFoundException extends RuntimeException {
    public StadiumNotFoundException(Integer id) {
        super("Stadium id not found: " + id);
    }
}