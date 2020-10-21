package me.hoseok.twitterdemo.comment.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private String content;

    public CommentDto(String content) {
        this.content = content;
    }
}
