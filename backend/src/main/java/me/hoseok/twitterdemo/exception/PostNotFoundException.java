package me.hoseok.twitterdemo.exception;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(String message) {
        super( message );
    }
}
