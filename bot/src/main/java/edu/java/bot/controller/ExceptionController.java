package edu.java.bot.controller;

import edu.java.bot.dto.ApiErrorResponse;
import java.util.Arrays;
import java.util.List;
import edu.java.bot.exceptions.ThereIsNoSuchLinkException;
import edu.java.bot.exceptions.TooManyRequestsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ApiErrorResponse handleFieldException(MethodArgumentNotValidException ex) {

        return new ApiErrorResponse(
            "Invalid request content",
            ex.getStatusCode().toString(),
            ex.getTitleMessageCode(),
            ex.getMessage(),
            List.of(ex.getSuppressedFields())
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ApiErrorResponse handleEmptyRequestBodyException(HttpMessageNotReadableException ex) {

        return new ApiErrorResponse(
            "cannot be read",
            HttpStatus.BAD_REQUEST.toString(),
            ex.toString(),
            ex.getMessage(),
            List.of()
        );
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public ApiErrorResponse handleThereIsNoSuchLinkException(ThereIsNoSuchLinkException ex) {
        return new ApiErrorResponse(
            "there is no such link",
            HttpStatus.NOT_FOUND.toString(),
            "ThereIsNoSuchLinkException",
            ex.getMessage(),
            Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }

    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    @ExceptionHandler
    public ApiErrorResponse handleTooManyRequestsException(TooManyRequestsException ex) {
        return new ApiErrorResponse(
            "too many requests",
            HttpStatus.NOT_FOUND.toString(),
            "TooManyRequestsException",
            ex.getMessage(),
            Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }
}
