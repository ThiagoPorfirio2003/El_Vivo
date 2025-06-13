package com.porfirio.elvivo.exception;

public class DniAlreadyExistsException extends RuntimeException {
    public DniAlreadyExistsException(String message)
    {
        super(message);
    }
}
