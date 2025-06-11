package com.porfirio.elvivo.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String message)
    {
        super(message);
    }
}
