package com.example.RestTicketSystem.error.exception;

public class SectorNotFoundException extends RuntimeException {
    public SectorNotFoundException(Integer id) {
        super("Sector id not found: " + id);
    }
}
