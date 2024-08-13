package com.ejetool.account.api.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ejetool.common.exception.BadRequestException;
import com.ejetool.common.exception.NotFoundDataException;
import com.ejetool.account.api.dto.error.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class RestExceptionAdvice {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse badRequestException(BadRequestException e) {
        log.info("BadRequest.", e);
        
        return new ErrorResponse(HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public ErrorResponse notFoundDataException(NotFoundDataException e) {
        log.info("NotFoundData.");
        return new ErrorResponse(HttpStatus.NOT_FOUND);
    }

    // @ResponseStatus(HttpStatus.UNAUTHORIZED)
    // @ExceptionHandler(AccessDeniedException.class)
    // public ErrorResponse notFoundDataException(AccessDeniedException e) {
    //     log.info("Unauthorized.");
    //     return new ErrorResponse("Unauthorized", HttpStatus.UNAUTHORIZED.value());
    // }

    // @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    // @ExceptionHandler
    // public ErrorResponse exception(Exception e) {
    //     log.error("Exception. ", e);
    //     return new ErrorResponse("A problem occurred inside the server.", HttpStatus.INTERNAL_SERVER_ERROR.value());
    // }
}
