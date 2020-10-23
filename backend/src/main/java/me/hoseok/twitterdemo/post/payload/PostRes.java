package me.hoseok.twitterdemo.post.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRes {
    private Long id;
    private String content;
    private String location;
//    private List<String> images = new ArrayList<>();
}
