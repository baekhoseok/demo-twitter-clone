package me.hoseok.twitterdemo.post.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRes {
    private Long id;
    private String content;
    private String location;
}
