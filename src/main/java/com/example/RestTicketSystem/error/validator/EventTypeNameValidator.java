/*
package com.example.RestTicketSystem.error.validator;

import com.example.RestTicketSystem.service.EventTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class EventTypeNameValidator implements ConstraintValidator<EventTypeName, String> {
    @Autowired
    private EventTypeService eventTypeService;

    public EventTypeNameValidator() {
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return eventTypeService.findByName(s) == null;
    }
}*/