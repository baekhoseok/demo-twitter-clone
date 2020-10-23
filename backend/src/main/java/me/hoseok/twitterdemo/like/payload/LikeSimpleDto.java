package me.hoseok.twitterdemo.like.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LikeSimpleDto {
    @JsonIgnore
    private Long postId;
    private Long accountId;

    @QueryProjection
    public LikeSimpleDto( Long postId, Long accountId) {
        this.postId = postId;
        this.accountId = accountId;
    }
}
