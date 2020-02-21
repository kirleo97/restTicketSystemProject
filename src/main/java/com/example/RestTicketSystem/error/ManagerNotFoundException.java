package com.example.RestTicketSystem.error;

public class ManagerNotFoundException extends RuntimeException {
    public ManagerNotFoundException(Integer id) {
        super("Manager id not found: " + id);
    }
}