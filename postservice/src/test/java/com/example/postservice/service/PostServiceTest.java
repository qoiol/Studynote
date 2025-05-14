package com.example.postservice.service;

import com.example.postservice.model.entity.Post;
import com.example.postservice.model.entity.User;
import com.example.postservice.exception.ErrorCode;
import com.example.postservice.exception.PostApplicationException;
import com.example.postservice.fixture.PostFixture;
import com.example.postservice.fixture.UserFixture;
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

        Integer userid = 1;

        when(userRepository.findById(1)).thenReturn(Optional.empty());
        when(postRepository.findById(any())).thenReturn(mock(Optional.class));
        when(postRepository.save(any())).thenReturn(mock(Post.class));

        PostApplicationException e = Assertions.assertThrows(PostApplicationException.class, () -> postService.update(1L, title, content, userid));
        Assertions.assertEquals(e.getErrorCode(), ErrorCode.USER_NOT_FOUND);

    }


    @Test
    void 포스트_삭제_실패() {
        Integer userid = 1;
        Long postid = 1L;

        // 유저 정보 X
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        PostApplicationException e1 = Assertions.assertThrows(PostApplicationException.class, () -> postService.delete(postid, userid));
        Assertions.assertEquals(e1.getErrorCode(), ErrorCode.USER_NOT_FOUND);

        // post 정보 X
        when(userRepository.findById(any())).thenReturn(Optional.of(mock(User.class)));
        when(postRepository.findById(any())).thenReturn(Optional.empty());

        PostApplicationException e2 = Assertions.assertThrows(PostApplicationException.class, () -> postService.delete(postid, userid));
        Assertions.assertEquals(e2.getErrorCode(), ErrorCode.POST_NOT_FOUND);

        // post 작성자 불일치
        User user1 = UserFixture.get(1, "test", "1234");
        User user2 = UserFixture.get(2, "user", "1234");
        Post post = PostFixture.get(postid, user1);

        when(userRepository.findById(2)).thenReturn(Optional.of(user2));
        when(postRepository.findById(any())).thenReturn(Optional.of(post));

        PostApplicationException e3 = Assertions.assertThrows(PostApplicationException.class, () -> postService.delete(postid, userid));
        Assertions.assertEquals(e3.getErrorCode(), ErrorCode.INVALID_PERMISSION);
    }

    @Test
    void 포스트_삭제_성공() {
        Integer userId = 1;
        String username = "user";
        Long postid = 1L;

        User user = UserFixture.get(userId, username, "1234");
        Post post = PostFixture.get(postid, user);


        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(postRepository.findById(postid)).thenReturn(Optional.of(post));

        Assertions.assertDoesNotThrow(() -> postService.delete(postid, userId));
    }
}
