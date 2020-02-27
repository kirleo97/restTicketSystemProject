package com.example.RestTicketSystem.error.exception;

import java.util.ArrayList;
import java.util.List;

public class EventDataException extends Exception {
    List<Exception> exceptions = new ArrayList<>();

    public List<Exception> getExceptions() {
        return exceptions;
    }

    public void setExceptions(List<Exception> exceptions) {
        this.exceptions = exceptions;
    }

    public EventDataException() {
    }

    public EventDataException(List<Exception> exceptions) {
        this.exceptions = exceptions;
    }
}