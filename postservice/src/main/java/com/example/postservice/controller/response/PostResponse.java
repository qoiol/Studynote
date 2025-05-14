package com.example.postservice.controller.response;

import com.example.postservice.model.dto.PostDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostResponse {
    private Long id;
    private UserInfoResponse user;
    private String title;
    private String body;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static PostResponse fromPost(PostDTO post) {
        PostResponse postResponse = new PostResponse();
        postResponse.setId(post.getId());
        postResponse.setUser(UserInfoResponse.fromUserDTO(post.getUser()));
        postResponse.setTitle(post.getTitle());
        postResponse.setBody(post.getContent());
        postResponse.setRegisteredAt(post.getRegisteredAt());
        postResponse.setUpdatedAt(post.getUpdatedAt());
        postResponse.setDeletedAt(post.getDeletedAt());
        return postResponse;
    }
}
