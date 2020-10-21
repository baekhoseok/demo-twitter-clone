package me.hoseok.twitterdemo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.websocket.server.ServerEndpoint;

@AllArgsConstructor
@Getter @Setter
public class CommentNotFoundResponse {
    private String comment;
}
