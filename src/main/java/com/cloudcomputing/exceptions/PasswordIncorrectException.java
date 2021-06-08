package com.cloudcomputing.exceptions;

public class PasswordIncorrectException extends RuntimeException {
    public PasswordIncorrectException(String msg) {
        super(msg);
    }
}
