package com.n26.challenge.aop;

import com.n26.challenge.exception.TransactionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Created by renz on 12/17/2017.
 */
@ControllerAdvice
public class NoContentHandler {

    @ExceptionHandler(TransactionException.class)
    public ResponseEntity<?> handleInvalidTimestampException() {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
