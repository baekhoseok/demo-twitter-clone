package me.hoseok.twitterdemo.post;

import lombok.*;
import me.hoseok.twitterdemo.account.Account;
import me.hoseok.twitterdemo.comment.Comment;
import me.hoseok.twitterdemo.common.BaseEntity;
import me.hoseok.twitterdemo.hashtag.PostHashTag;
import me.hoseok.twitterdemo.image.Image;
import me.hoseok.twitterdemo.like.Like;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "content", "location", "account", "comments", "images"})
public class Post extends BaseEntity {

    @Id @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String content;
    private String location;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval=true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval=true)
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval=true)
    private Set<Like> likes = new HashSet<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval=true)
    private Set<PostHashTag> postHashTags = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Post retweet;

    public Post(String content, String location, Account account) {
        this.content = content;
        this.location = location;
        this.account = account;
    }

    public Post(String content, String location) {
        this.content = content;
        this.location = location;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setPost(this);
    }

    public void addImage(Image image){
        this.images.add(image);
        image.setPost(this);
    }

    public void setOwner(Account account) {
        this.account = account;
    }



}
