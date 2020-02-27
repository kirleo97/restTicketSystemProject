package com.example.RestTicketSystem.error;

import com.example.RestTicketSystem.error.exception.EventDataException;
import com.example.RestTicketSystem.error.exception.ResourceAlreadyExistsException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Create a @ControllerAdvice to handle the custom exceptions thrown by the controller
@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // Let Spring handle the exception, we just override the status code
    /*@ExceptionHandler({EventTypeNotFoundException.class, ManagerNotFoundException.class, StadiumNotFoundException.class, EventNotFoundException.class, SectorNotFoundException.class, TicketNotFoundException.class})
    public void eventTypeHandleNotFound(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value());
    }*/

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<CustomErrorResponse> customHandleNotFound(Exception ex) {
        CustomErrorResponse error = new CustomErrorResponse();
        error.setTimestamp(LocalDateTime.now());
        error.getErrors().add(ex.getMessage());
        error.setStatus(HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ResourceAlreadyExistsException.class})
    public ResponseEntity<CustomErrorResponse> customHandleExist(Exception ex) {
        CustomErrorResponse error = new CustomErrorResponse();
        error.setTimestamp(LocalDateTime.now());
        error.getErrors().add(ex.getMessage());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({EventDataException.class})
    public ResponseEntity<CustomErrorResponse> customHandleEventData(EventDataException eventDataException) {
        CustomErrorResponse error = new CustomErrorResponse();
        error.setTimestamp(LocalDateTime.now());
        for (Exception e : eventDataException.getExceptions()) {
            error.getErrors().add(e.getMessage());
        }
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /*@ExceptionHandler(ConstraintViolationException.class)
    public void constraintViolationException(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }*/

    // error handle for @Valid
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());

        //Get all errors
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        body.put("errors", errors);
        return new ResponseEntity<>(body, headers, status);
    }
}