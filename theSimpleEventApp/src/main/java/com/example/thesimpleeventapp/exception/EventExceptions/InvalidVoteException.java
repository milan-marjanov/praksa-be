package com.example.thesimpleeventapp.exception.EventExceptions;

public class InvalidVoteException extends RuntimeException {
    public InvalidVoteException(String message) {
        super(message);
    }
}
