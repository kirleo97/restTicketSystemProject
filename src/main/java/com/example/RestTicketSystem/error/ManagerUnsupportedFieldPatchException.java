package com.example.RestTicketSystem.error;

import java.util.Set;

public class ManagerUnsupportedFieldPatchException extends RuntimeException {
    public ManagerUnsupportedFieldPatchException(Set<String> fields) {
        super("Field " + fields.toString() + " update is not allow.");
    }
}
