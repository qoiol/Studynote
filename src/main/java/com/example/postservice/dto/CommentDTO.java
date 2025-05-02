package com.example.postservice.dto;

import com.example.postservice.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private Long id;
    private String userName;
    private Long postId;
    private String comment;
    private Timestamp registeredAt;
    private Timestamp updatedAt;

    public static CommentDTO fromComment(Comment comment) {
        return CommentDTO.builder()
                .id(comment.getId())
                .userName(comment.getUser().getId())
                .comment(comment.getComment())
                .registeredAt(comment.getRegisteredAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
