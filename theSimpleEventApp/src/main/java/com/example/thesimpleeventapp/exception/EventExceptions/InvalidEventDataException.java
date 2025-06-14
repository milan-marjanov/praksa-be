package com.example.thesimpleeventapp.exception.EventExceptions;

public class InvalidEventDataException extends RuntimeException {
    public InvalidEventDataException(String message) {
        super(message);
    }
}
