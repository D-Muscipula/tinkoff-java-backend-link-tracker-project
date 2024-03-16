package edu.java.contoller;

import dto.response.ApiErrorResponse;
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
            "chat_does_not_exist",
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
            "chat_already_exist",
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
            "link_is_not_supposed_to_be_tracked",
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
            "link_is_not_supposed_to_be_deleted",
            "IsNotSupposedToBeDeleted",
            ex.getMessage(),
            Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ApiErrorResponse handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
        return new ApiErrorResponse(
            "missing required request headers",
            "missing_headers",
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
            "incorrect_value",
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
            "body_missing",
            "HttpMessageNotReadableException",
            ex.getMessage(),
            Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler
    public ApiErrorResponse handleUserIsNotRegisteredException(UserIsNotRegisteredException ex) {
        return new ApiErrorResponse(
            "tgUser is not registered",
            "user_is_not_registered",
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
            "already_tracked",
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
            "was_not_tracked",
            "ThereIsNoSuchLinkException",
            ex.getMessage(),
            Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }
}
