package me.hoseok.twitterdemo.image.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class ImageViewDto {
    private Long id;
    @JsonIgnore
    private Long postId;
    private String src;

    @QueryProjection
    public ImageViewDto(Long id, Long postId, String src) {
        this.id = id;
        this.postId = postId;
        this.src = src;
    }
}
