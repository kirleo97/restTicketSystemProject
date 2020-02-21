package com.example.RestTicketSystem.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({EventTypeNotFoundException.class, ManagerNotFoundException.class, StadiumNotFoundException.class, EventNotFoundException.class})
    public void eventTypeHandleNotFound(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler({EventTypeUnsupportedFieldPatchException.class, ManagerUnsupportedFieldPatchException.class, StadiumUnsupportedFieldPatchException.class})
    public void EventTypeUnsupportedFieldPatch(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.METHOD_NOT_ALLOWED.value());
    }
}