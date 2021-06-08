package com.cloudcomputing.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(value = {ClientNotFoundException.class})
    public ResponseEntity<ErrorDto> handleClientNotFoundException(ClientNotFoundException e) {
        return ResponseEntity.status(NOT_FOUND)
                .body(new ErrorDto("CLIENT_NOT_FOUND", e.getLocalizedMessage()));
    }

    @ExceptionHandler(value = {EmailConflictException.class})
    public ResponseEntity<ErrorDto> handleEmailConflictException(EmailConflictException e) {
        return ResponseEntity.status(CONFLICT)
                .body(new ErrorDto("EMAIL_HAS_BEEN_TAKEN", e.getLocalizedMessage()));
    }

    @ExceptionHandler(value = {PasswordIncorrectException.class})
    public ResponseEntity<ErrorDto> handlePasswordIncorrectException(PasswordIncorrectException e) {
        return ResponseEntity.status(BAD_REQUEST)
                .body(new ErrorDto("INCORRECT_PRE_PASSWORD", e.getLocalizedMessage()));
    }

    @ExceptionHandler(value = {BadCredentialsException.class})
    public ResponseEntity<ErrorDto> handleBadCredentialsException(BadCredentialsException e) {
        return ResponseEntity.status(BAD_REQUEST)
                .body(new ErrorDto("BAD_CREDENTIALS", e.getLocalizedMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception exception) {
        log.info("There is an exception occurred", exception);
        return ResponseEntity.status(SERVICE_UNAVAILABLE)
                .body(new ErrorDto(exception.getMessage(), "error"));
    }
}
