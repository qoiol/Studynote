package com.example.postservice.service;

import com.example.postservice.domain.Post;
import com.example.postservice.domain.User;
import com.example.postservice.dto.PostDTO;
import com.example.postservice.exception.ErrorCode;
import com.example.postservice.exception.PostApplicationException;
import com.example.postservice.repository.PostRepository;
import com.example.postservice.repository.UserRepository;
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

    @Transactional
    public void create(String title, String content, String userId) {
        // user 검증
        User user = userRepository.findById(userId).orElseThrow(() -> new PostApplicationException(ErrorCode.USER_NOT_FOUND));
        //등록
        postRepository.save(Post.builder().title(title).content(content).user(user).build());
    }

    public void update(Long id, String title, String content, String name) {
        //user 검증
        User user = userRepository.findById(name).orElseThrow(() -> new PostApplicationException(ErrorCode.USER_NOT_FOUND));
        //post 검증
        Post post = postRepository.findById(id).orElseThrow(() -> new PostApplicationException(ErrorCode.POST_NOT_FOUND));
        if(post.getUser() != user) throw new PostApplicationException(ErrorCode.INVALID_PERMISSION);

        post.setTitle(title);
        post.setContent(content);

        //update
        postRepository.save(post);
    }

    public void delete(Long id, String name) {
        //user 검증
        User user = userRepository.findById(name).orElseThrow(() -> new PostApplicationException(ErrorCode.USER_NOT_FOUND));
        //post 검증
        Post post = postRepository.findById(id).orElseThrow(() -> new PostApplicationException(ErrorCode.POST_NOT_FOUND));
        if(post.getUser() != user) throw new PostApplicationException(ErrorCode.INVALID_PERMISSION);

        postRepository.deleteById(id);
    }

    public Page<PostDTO> list(Pageable pageable) {
        return postRepository.findAll(pageable).map(PostDTO::fromPost);
    }

    public Page<PostDTO> my(Pageable pageable, String userId) {
        // user 검증
        User user = userRepository.findById(userId).orElseThrow(() -> new PostApplicationException(ErrorCode.USER_NOT_FOUND));

        return postRepository.findAllByUser(user, pageable).map(PostDTO::fromPost);
    }
}
