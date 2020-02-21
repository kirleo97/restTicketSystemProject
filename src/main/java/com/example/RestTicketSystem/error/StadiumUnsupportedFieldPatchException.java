package com.example.RestTicketSystem.error;

import java.util.Set;

public class StadiumUnsupportedFieldPatchException extends RuntimeException {
    public StadiumUnsupportedFieldPatchException(Set<String> fields) {
        super("Field " + fields.toString() + " update is not allow.");
    }
}