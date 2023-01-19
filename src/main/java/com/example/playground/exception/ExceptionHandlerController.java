package com.example.playground.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerController {


    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorRecord> handle(ApplicationException e, Locale locale) {
        log.info("exception caught {}", e.getMessage(), e);
        ResourceBundle bundle = ResourceBundle.getBundle("errors", locale);

        String responseBody = bundle.getString(e.getErrorCode().toLowerCase());
        log.debug("response: {}", responseBody);
        ErrorRecord errorRecord = new ErrorRecord(responseBody, OffsetDateTime.now(ZoneId.of("Europe/Paris")));
        return new ResponseEntity<>(errorRecord, HttpStatus.BAD_REQUEST);
    }

    record ErrorRecord(String message, OffsetDateTime dateTime) {

    }

}
