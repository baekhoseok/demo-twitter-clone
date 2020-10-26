package me.hoseok.twitterdemo.post.payload;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.hoseok.twitterdemo.account.Account;
import me.hoseok.twitterdemo.comment.Comment;
import me.hoseok.twitterdemo.image.Image;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostReq {
    @NotBlank(message = "content is required")
    @Size(min = 2, max = 256)
    private String content;
    private String location;
    private List<String> images;
}
