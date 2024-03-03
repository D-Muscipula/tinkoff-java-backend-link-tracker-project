package edu.java.contoller;

import edu.java.dto.response.ApiErrorResponse;
import edu.java.exceptions.ChatAlreadyExistsException;
import edu.java.exceptions.ChatDoesntExistException;
import edu.java.exceptions.IsNotSupposedToBeDeleted;
import edu.java.exceptions.IsNotSupposedToBeTracked;
import edu.java.exceptions.LinkAlreadyTrackedException;
import edu.java.exceptions.ThereIsNoSuchLinkException;
import edu.java.exceptions.UserIsNotRegisteredException;
import java.util.Arrays;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public ApiErrorResponse handleChatDoesntExistException(ChatDoesntExistException ex) {
        return new ApiErrorResponse(
            "chat does not exist",
            HttpStatus.NOT_FOUND.toString(),
            "ChatDoesntExistException",
            ex.getMessage(),
            Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler
    public ApiErrorResponse handleChatAlreadyExistsException(ChatAlreadyExistsException ex) {
        return new ApiErrorResponse(
            "chat already exist",
            HttpStatus.CONFLICT.toString(),
            "ChatAlreadyExistsException",
            ex.getMessage(),
            Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler
    public ApiErrorResponse handleIsNotSupposedToBeTracked(IsNotSupposedToBeTracked ex) {
        return new ApiErrorResponse(
            "link is not supposed to be tracked",
            HttpStatus.CONFLICT.toString(),
            "IsNotSupposedToBeTracked",
            ex.getMessage(),
            Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler
    public ApiErrorResponse handleIsNotSupposedToBeDeleted(IsNotSupposedToBeDeleted ex) {
        return new ApiErrorResponse(
            "link is not supposed to be deleted",
            HttpStatus.CONFLICT.toString(),
            "IsNotSupposedToBeDeleted",
            ex.getMessage(),
            Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ApiErrorResponse handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
        return new ApiErrorResponse(
            "Missing required request headers",
            HttpStatus.BAD_REQUEST.toString(),
            "MissingRequestHeaderException",
            ex.getMessage(),
            Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ApiErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return new ApiErrorResponse(
            "invalid field value",
            HttpStatus.BAD_REQUEST.toString(),
            ex.getTitleMessageCode(),
            "MethodArgumentNotValidException",
            Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ApiErrorResponse handleEmptyRequestBodyException(HttpMessageNotReadableException ex) {
        return new ApiErrorResponse(
            "request body missing",
            HttpStatus.BAD_REQUEST.toString(),
            "HttpMessageNotReadableException",
            ex.getMessage(),
            Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler
    public ApiErrorResponse handleUserIsNotRegisteredException(UserIsNotRegisteredException ex) {
        return new ApiErrorResponse(
            "user is not registered",
            HttpStatus.UNAUTHORIZED.toString(),
            "UserIsNotRegisteredException",
            ex.getMessage(),
            Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler
    public ApiErrorResponse handleLinkAlreadyTrackedException(LinkAlreadyTrackedException ex) {
        return new ApiErrorResponse(
            "link already tracked",
            HttpStatus.CONFLICT.toString(),
            "LinkAlreadyTrackedException",
            ex.getMessage(),
            Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public ApiErrorResponse handleThereIsNoSuchLinkException(ThereIsNoSuchLinkException ex) {
        return new ApiErrorResponse(
            "link was not tracked",
            HttpStatus.BAD_REQUEST.toString(),
            "ThereIsNoSuchLinkException",
            ex.getMessage(),
            Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }
}
