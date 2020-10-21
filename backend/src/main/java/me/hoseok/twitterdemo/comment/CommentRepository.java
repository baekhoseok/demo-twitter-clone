package me.hoseok.twitterdemo.comment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Comment findByPostId(Long id);

    Comment findByIdAndPostId(Long commentId, Long postId);
}
