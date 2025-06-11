package com.porfirio.elvivo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleEmailAlreadyExistsException(EmailAlreadyExistsException exception)
    {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                Map.of(
                        "error", "EMAIL_ALREADY_EXISTS",
                        "message", exception.getMessage()));
    }
}
