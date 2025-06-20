package com.example.thesimpleeventapp.exception;

public class TimeExpiredException extends RuntimeException {
    public TimeExpiredException(String message) {
        super(message);
    }
}
