package me.hoseok.twitterdemo.comment;

import lombok.RequiredArgsConstructor;
import me.hoseok.twitterdemo.account.Account;
import me.hoseok.twitterdemo.account.AccountRepository;
import me.hoseok.twitterdemo.comment.payload.CommentReq;
import me.hoseok.twitterdemo.comment.payload.CommentDto;
import me.hoseok.twitterdemo.exception.CommentNotFoundException;
import me.hoseok.twitterdemo.exception.InvalidArgumentsException;
import me.hoseok.twitterdemo.exception.PostNotFoundException;
import me.hoseok.twitterdemo.post.Post;
import me.hoseok.twitterdemo.post.PostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final AccountRepository accountRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    public Comment createComment(Long accountId, Long postId, CommentReq req) {
        Account account = accountRepository.findById( accountId ).get();
        Optional<Post> postOptional = postRepository.findById( postId );
        postOptional.orElseThrow(() -> new PostNotFoundException( "Post not found" ) );

        Post post = postOptional.get();

        Comment comment = modelMapper.map( req, Comment.class );
        comment.setAccount( account );
        comment.setPost( post );
        return commentRepository.save( comment );
    }

    public Comment patchComment(Long accountId, Long postId, Long commentId, CommentReq req) {
        Comment comment = commentRepository.findByIdAndPostId(commentId, postId);
        if (comment == null) {
            throw new CommentNotFoundException("Comment not found");
        }

        if (accountId != comment.getAccount().getId()) {
            throw new InvalidArgumentsException( "Can not access this comment" );
        }
        comment.setContent( req.getContent() );
        return comment;
    }

    public void deleteComment(Long accountId, Long postId, Long commentId) {
        Comment comment = commentRepository.findByIdAndPostId(commentId, postId);
        if (comment == null) {
            throw new CommentNotFoundException("Comment not found");
        }

        if (accountId != comment.getAccount().getId()) {
            throw new InvalidArgumentsException( "Can not access this comment" );
        }
        commentRepository.delete(comment);
    }
}
