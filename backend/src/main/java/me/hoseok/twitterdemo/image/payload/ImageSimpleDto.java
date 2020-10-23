package me.hoseok.twitterdemo.image.payload;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ImageSimpleDto {
    private Long id;
    private Long postId;
    private String url;

    @QueryProjection
    public ImageSimpleDto(Long id, Long postId, String url) {
        this.id = id;
        this.postId = postId;
        this.url = url;
    }
}
