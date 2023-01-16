package com.attornatus.desafio.exception_handler;

import com.attornatus.desafio.business.basic.exception.NotFoundException;
import com.attornatus.desafio.dto.global.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({NoSuchElementException.class, NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static ResponseEntity<ErrorMessage> handle(RuntimeException ex) {
        return makeResponseEntity(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({RuntimeException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static ResponseEntity<ErrorMessage> handleBadRequestExceptions(RuntimeException ex) {
        return makeResponseEntity(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public static ResponseEntity<ErrorMessage> handleAll(Exception ex) {
        return makeResponseEntity(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static ResponseEntity<ErrorMessage> makeResponseEntity(Exception ex, HttpStatus status) {
        return new ResponseEntity<>(new ErrorMessage(ex.getMessage()), status);
    }

}
