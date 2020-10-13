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
}
