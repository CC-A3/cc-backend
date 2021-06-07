package com.cloudcomputing.exceptions;

public class ClientNotFoundException extends RuntimeException{
    public ClientNotFoundException(String msg) {
        super(msg);
    }
}
