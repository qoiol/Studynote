package com.example.postservice.service;

import com.example.postservice.model.AlarmArgs;
import com.example.postservice.model.AlarmType;
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

@Log4j2
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final AlarmRepository alarmRepository;

    @Transactional
    public void create(String title, String content, String userId) {
        // user 검증
        User user = userRepository.findById(userId).orElseThrow(() -> new PostApplicationException(ErrorCode.USER_NOT_FOUND));
        //등록
        postRepository.save(Post.builder().title(title).content(content).user(user).build());
    }

    @Transactional
    public void update(Long id, String title, String content, String name) {
        //user 검증
        User user = userRepository.findById(name).orElseThrow(() -> new PostApplicationException(ErrorCode.USER_NOT_FOUND));
        //post 검증
        Post post = postRepository.findById(id).orElseThrow(() -> new PostApplicationException(ErrorCode.POST_NOT_FOUND));
        //post 작성자 검증
        if (post.getUser() != user) throw new PostApplicationException(ErrorCode.INVALID_PERMISSION);

        post.setTitle(title);
        post.setContent(content);

        //update
        postRepository.save(post);
    }

    @Transactional
    public void delete(Long id, String name) {
        //user 검증
        User user = userRepository.findById(name).orElseThrow(() -> new PostApplicationException(ErrorCode.USER_NOT_FOUND));
        //post 검증
        Post post = postRepository.findById(id).orElseThrow(() -> new PostApplicationException(ErrorCode.POST_NOT_FOUND));
        //post 작성자 검증
        if (post.getUser() != user) throw new PostApplicationException(ErrorCode.INVALID_PERMISSION);

        postRepository.deleteById(id);
    }

    public Page<PostDTO> list(Pageable pageable) {
        return postRepository.findAll(pageable).map(PostDTO::fromPost);
    }

    @Transactional
    public Page<PostDTO> my(Pageable pageable, String userId) {
        // user 검증
        User user = userRepository.findById(userId).orElseThrow(() -> new PostApplicationException(ErrorCode.USER_NOT_FOUND));
        return postRepository.findAllByUser(user, pageable).map(PostDTO::fromPost);
    }

    @Transactional
    public void addLike(Long id, String name) {
        // user 검증
        User user = userRepository.findById(name).orElseThrow(() -> new PostApplicationException(ErrorCode.USER_NOT_FOUND));
        // post 검증
        Post post = postRepository.findById(id).orElseThrow(() -> new PostApplicationException(ErrorCode.POST_NOT_FOUND));
        // 좋아요 눌렀는지 검증
        likeRepository.findByUserAndPost(user, post).ifPresent((it) -> {
            throw new PostApplicationException(ErrorCode.ALREADY_LIKED, String.format("user %s already like post %d", name, id));
        });
        // 저장
        likeRepository.save(Like.builder().post(post).user(user).build());
        // 알람 발생
        alarmRepository.save(Alarm.builder()
                .alarmType(AlarmType.NEW_LIKE_ON_POST)
                .args(new AlarmArgs(user.getId(), post.getId()))
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
    public void addComment(Long id, String comment, String name) {
        // user 검증
        User user = userRepository.findById(name).orElseThrow(() -> new PostApplicationException(ErrorCode.USER_NOT_FOUND));
        // post 검증
        Post post = postRepository.findById(id).orElseThrow(() -> new PostApplicationException(ErrorCode.POST_NOT_FOUND));
        // 댓글 등록
        commentRepository.save(Comment.builder().comment(comment).user(user).post(post).build());
        // 알람 발생
        alarmRepository.save(Alarm.builder()
                .alarmType(AlarmType.NEW_COMMENT_ON_POST)
                .args(new AlarmArgs(user.getId(), post.getId()))
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
