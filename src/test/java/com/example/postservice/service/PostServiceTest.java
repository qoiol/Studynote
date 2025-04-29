package com.example.postservice.service;

import com.example.postservice.domain.Post;
import com.example.postservice.exception.ErrorCode;
import com.example.postservice.exception.PostApplicationException;
import com.example.postservice.repository.PostRepository;
import com.example.postservice.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
public class PostServiceTest {
    @Autowired
    private PostService postService;


    @MockitoBean
    private UserRepository userRepository;
    @MockitoBean
    private PostRepository postRepository;

    @Test
    void 수정테스트() {
        String title = "수정 제목";
        String content = "수정 내용";

        String userid = "user1";

        when(userRepository.findById(userid)).thenReturn(Optional.empty());
        when(postRepository.findById(any())).thenReturn(mock(Optional.class));
        when(postRepository.save(any())).thenReturn(mock(Post.class));

        PostApplicationException e = Assertions.assertThrows(PostApplicationException.class, () -> postService.update(1L, title, content, userid));
        Assertions.assertEquals(e.getErrorCode(), ErrorCode.USER_NOT_FOUND);

    }

}
