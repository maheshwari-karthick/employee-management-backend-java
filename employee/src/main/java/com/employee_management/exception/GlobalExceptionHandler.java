package com.employee_management.exception;

import io.jsonwebtoken.security.SignatureException;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException notFoundException) {
        log.error("Not found exception", notFoundException);
        return new ResponseEntity<>("Not found exception : " + notFoundException.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<String> handleNotFoundException(SignatureException signatureException) {
        log.error("Signature exception", signatureException);
        return new ResponseEntity<>("Invalid token : " + signatureException.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleBadRequestException(BadRequestException badRequestException) {
        log.error("Bad request exception", badRequestException);
        return new ResponseEntity<>("Bad request exception : " + badRequestException.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<String> handleNumberFormatException(NumberFormatException numberFormatException) {
        log.error("Number format exception", numberFormatException);
        return new ResponseEntity<>("Number format exception : " + numberFormatException.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException illegalException) {
        log.error("Illegal argument exception", illegalException);
        return new ResponseEntity<>("Illegal argument exception : " + illegalException.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException httpMessageNotReadableException) {
        log.error("Http Message Not Readable Exception", httpMessageNotReadableException);
        return new ResponseEntity<>("Http Message Not Readable Exception : " + httpMessageNotReadableException.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException dataIntegrityViolationException) {
        log.error("Data Integrity Violation", dataIntegrityViolationException);
        return new ResponseEntity<>("Data Integrity Violation : " + dataIntegrityViolationException.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseEntity<String> handleHttpMessageConversionException(HttpMessageConversionException httpMessageConversionException) {
        log.error("Http Message Conversion Exception", httpMessageConversionException);
        return new ResponseEntity<>("Http Message Conversion Exception : " + httpMessageConversionException.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException runtimeException) {
        log.error("Runtime exception", runtimeException);
        return new ResponseEntity<>("Runtime exception : " + runtimeException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
