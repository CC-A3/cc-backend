package com.cloudcomputing.exceptions;

public class EmailConflictException extends RuntimeException{
    public EmailConflictException(String msg) {
        super(msg);
    }
}
