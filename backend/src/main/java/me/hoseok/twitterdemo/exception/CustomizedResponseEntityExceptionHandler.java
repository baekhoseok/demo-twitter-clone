package me.hoseok.twitterdemo.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public final ResponseEntity handleUsernameAlreadyExistsException(UsernameAlreadyExistsException e, WebRequest webRequest) {
        UsernameAlreadyExistsResponse response = new UsernameAlreadyExistsResponse(e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler
    public final ResponseEntity handlePostNotFoundException(PostNotFoundException e, WebRequest webRequest) {
        PostNotFoundResponse response = new PostNotFoundResponse( e.getMessage() );
        return ResponseEntity.badRequest().body( response );
    }

    @ExceptionHandler
    public final ResponseEntity handleCommentNotFoundException(CommentNotFoundException e, WebRequest webRequest) {
        CommentNotFoundResponse response = new CommentNotFoundResponse( e.getMessage() );
        return ResponseEntity.badRequest().body( response );
    }

    @ExceptionHandler
    public final ResponseEntity handleInvalidArgumentsException(InvalidArgumentsException e, WebRequest webRequest) {
        InvalidArgumentsResponse response = new InvalidArgumentsResponse(e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler
    public final ResponseEntity handleAccountNotFoundException(AccountNotFoundException e, WebRequest webRequest) {
        AccountNotFoundResponse response = new AccountNotFoundResponse(e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }
}
