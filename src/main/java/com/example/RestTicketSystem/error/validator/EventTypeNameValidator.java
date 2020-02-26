package com.example.RestTicketSystem.error.validator;

import com.example.RestTicketSystem.service.EventTypeService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EventTypeNameValidator implements ConstraintValidator<EventTypeName, String> {
    private final EventTypeService eventTypeService;

    public EventTypeNameValidator(EventTypeService eventTypeService) {
        this.eventTypeService = eventTypeService;
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return eventTypeService.findByName(s) == null;
    }
}