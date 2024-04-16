package com.vladima.gamingrental.request.exception_handlers;

import com.vladima.gamingrental.helpers.EntityOperationException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class EntitiesExceptionHandler {

    @Data
    @AllArgsConstructor
    public static class ExceptionFormat {
        String message, details;
    }
    @ExceptionHandler({EntityOperationException.class})
    public ResponseEntity<Map<String, String>> operationException(EntityOperationException e) {
        Map<String, String> response = new HashMap<>();
        response.put("message", e.getMessage());
        if (!e.getExtraInfo().isBlank()) response.put("details", e.getExtraInfo());
        return new ResponseEntity<>(response, e.getStatus());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Map<String, String>> operationException(MethodArgumentNotValidException e) {
        Map<String, String> response = new HashMap<>();
        String fieldName = e.getBindingResult().getFieldError().getField();
        var fieldValue = e.getBindingResult().getFieldError().getRejectedValue();
        String fieldMessage = e.getBindingResult().getFieldError().getDefaultMessage();
        response.put("details", MessageFormat.format("Invalid value {0} for {1}", fieldValue, fieldName));
        response.put("message", fieldMessage);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Map<String, String>> operationException(MethodArgumentTypeMismatchException e) {
        Map<String, String> response = new HashMap<>();
        String fieldName = e.getPropertyName();
        var fieldValue = e.getValue();
        String fieldMessage = MessageFormat.format("Invalid {0}", fieldName);
        response.put("details", MessageFormat.format("Invalid value {0} for {1}", fieldValue, fieldName));
        response.put("message", fieldMessage);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}