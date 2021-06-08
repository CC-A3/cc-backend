package com.cloudcomputing.exceptions;

import com.cloudcomputing.exceptions.ErrorDto;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.sql.rowset.WebRowSet;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RequiredArgsConstructor
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


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception exception) {
        log.info("There is an exception occurred", exception);
        return ResponseEntity.status(SERVICE_UNAVAILABLE)
                .body(new ErrorDto(exception.getMessage(), "error"));
    }
}
