package com.example.thesimpleeventapp.exception.UserExceptions;

public class PasswordMissmatchException extends RuntimeException {
    public PasswordMissmatchException(String message) {
        super(message);
    }
}
