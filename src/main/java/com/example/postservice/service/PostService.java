package com.example.postservice.service;

import com.example.postservice.model.AlarmArgs;
import com.example.postservice.model.AlarmType;
import com.example.postservice.model.dto.UserDTO;
import com.example.postservice.model.entity.*;
import com.example.postservice.model.dto.CommentDTO;
import com.example.postservice.model.dto.PostDTO;
import com.example.postservice.exception.ErrorCode;
import com.example.postservice.exception.PostApplicationException;
import com.example.postservice.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Log4j2
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final AlarmRepository alarmRepository;

    @Transactional
    public void create(String title, String content, Integer userId) {
//        // user 검증
//        User user = userRepository.findByUsername(username).orElseThrow(() -> new PostApplicationException(ErrorCode.USER_NOT_FOUND));
        //등록
        postRepository.save(Post.builder().title(title).content(content).user(User.builder().id(userId).build()).build());
    }

    @Transactional
    public void update(Long id, String title, String content, Integer userId) {
        //post 검증
        Post post = postRepository.findById(id).orElseThrow(() -> new PostApplicationException(ErrorCode.POST_NOT_FOUND));
        //post 작성자 검증
        if (!userId.equals(post.getUser().getId())) throw new PostApplicationException(ErrorCode.INVALID_PERMISSION);

        post.setTitle(title);
        post.setContent(content);

        //update
        postRepository.save(post);
    }

    @Transactional
    public void delete(Long id, Integer userId) {
        //post 검증
        Post post = postRepository.findById(id).orElseThrow(() -> new PostApplicationException(ErrorCode.POST_NOT_FOUND));
        //post 작성자 검증
        if (!post.getUser().getId().equals(userId)) throw new PostApplicationException(ErrorCode.INVALID_PERMISSION);

        // 좋아요,댓글 삭제
        likeRepository.deleteAllByPost(post);
        commentRepository.deleteAllByPost(post);

        postRepository.delete(post);
    }

    public Page<PostDTO> list(Pageable pageable) {
        return postRepository.findAllPosts(pageable).map(PostDTO::fromPost);
    }

    @Transactional
    public Page<PostDTO> my(Pageable pageable, Integer userId) {
        return postRepository.findAllByUserId(userId, pageable).map(PostDTO::fromPost);
    }

    @Transactional
    public void addLike(Long id, Integer userId) {
        // post 검증
        Post post = postRepository.findById(id).orElseThrow(() -> new PostApplicationException(ErrorCode.POST_NOT_FOUND));
        // 좋아요 눌렀는지 검증
        likeRepository.findByUserIdAndPost(userId, post).ifPresent((it) -> {
            throw new PostApplicationException(ErrorCode.ALREADY_LIKED, String.format("user %d already like post %d", userId, id));
        });
        // 저장
        likeRepository.save(Like.builder().post(post).user(User.builder().id(userId).build()).build());
        // 알람 발생
        alarmRepository.save(Alarm.builder()
                .alarmType(AlarmType.NEW_LIKE_ON_POST)
                .args(new AlarmArgs(userId, post.getId()))
                .user(post.getUser())
                .build()
        );

    }

    public long countLike(Long id) {
        //post 검증
        Post post = postRepository.findById(id).orElseThrow(() -> new PostApplicationException(ErrorCode.POST_NOT_FOUND));

        //like 개수 가져오기
        return likeRepository.countByPost(post);
    }

    @Transactional
    public void addComment(Long id, String comment, Integer userId) {
        // post 검증
        Post post = postRepository.findById(id).orElseThrow(() -> new PostApplicationException(ErrorCode.POST_NOT_FOUND));
        // 댓글 등록
        commentRepository.save(Comment.builder().comment(comment).user(User.builder().id(userId).build()).post(post).build());
        // 알람 발생
        alarmRepository.save(Alarm.builder()
                .alarmType(AlarmType.NEW_COMMENT_ON_POST)
                .args(new AlarmArgs(userId, post.getId()))
                .user(post.getUser())
                .build()
        );
    }

    public Page<CommentDTO> commentList(Pageable pageable, Long id) {
        // post 검증
        Post post = postRepository.findById(id).orElseThrow(() -> new PostApplicationException(ErrorCode.POST_NOT_FOUND));

        //댓글 가져오기
        return commentRepository.findAllByPost(pageable, post).map(CommentDTO::fromComment);
    }
}
