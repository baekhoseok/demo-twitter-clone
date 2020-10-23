package me.hoseok.twitterdemo.like.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeDto {
    private Long id;
    private Long postId;
    private Long accountId;
}
