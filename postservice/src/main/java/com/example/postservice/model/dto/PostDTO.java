package com.example.postservice.model.dto;

import com.example.postservice.model.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostDTO {
    private Long id;
    private UserDTO user;
    private String title;
    private String content;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static PostDTO fromPost(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setUser(UserDTO.fromUser(post.getUser()));
        postDTO.setTitle(post.getTitle());
        postDTO.setContent(post.getContent());
        postDTO.setRegisteredAt(post.getRegisteredAt());
        postDTO.setUpdatedAt(post.getUpdatedAt());
        postDTO.setDeletedAt(post.getDeletedAt());
        return postDTO;
    }
}
