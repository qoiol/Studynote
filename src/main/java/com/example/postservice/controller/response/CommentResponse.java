package com.example.postservice.controller.response;

import com.example.postservice.model.dto.CommentDTO;
import com.example.postservice.model.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
    private Long id;
    private String userName;
    private Long postId;
    private String comment;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static CommentResponse fromComment(CommentDTO comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .userName(comment.getUser().getId())
                .comment(comment.getComment())
                .registeredAt(comment.getRegisteredAt())
                .updatedAt(comment.getUpdatedAt())
                .deletedAt(comment.getDeletedAt())
                .build();
    }
}
