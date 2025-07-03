package com.example.thesimpleeventapp.exception.VoteExceptions;

public class TimeExpiredException extends RuntimeException {
    public TimeExpiredException(String message) {
        super(message);
    }
}
