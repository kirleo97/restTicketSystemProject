package com.example.RestTicketSystem.error;

import java.util.Set;

public class EventTypeUnsupportedFieldPatchException extends RuntimeException {

    public EventTypeUnsupportedFieldPatchException(Set<String> fields) {
        super("Field " + fields.toString() + " update is not allow.");
    }
}