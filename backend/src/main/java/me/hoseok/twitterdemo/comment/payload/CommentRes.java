package me.hoseok.twitterdemo.comment.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentRes {
    private Long id;
    @JsonIgnore
    private Long postId;
    private String content;
}
