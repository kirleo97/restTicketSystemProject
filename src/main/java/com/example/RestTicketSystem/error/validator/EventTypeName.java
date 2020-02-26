package com.example.RestTicketSystem.error.validator;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = EventTypeNameValidator.class)
@Documented
public @interface EventTypeName {
    String message() default "EventType with the same name is already exist!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
