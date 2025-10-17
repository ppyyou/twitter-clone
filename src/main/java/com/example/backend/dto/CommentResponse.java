package com.example.backend.dto;

import com.example.backend.entity.Comment;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentResponse {
    private Long id;
    private String content;
    private Long authorId;
    private String authorUsername;
    private LocalDateTime createdAt;

    public static CommentResponse from(Comment c) {
        return CommentResponse.builder()
            .id(c.getId())
            .content(c.getContent())
            .authorId(c.getAuthor() != null ? c.getAuthor().getId() : null)
            .authorUsername(c.getAuthor() != null ? c.getAuthor().getUsername() : null)
            .createdAt(c.getCreatedAt())
            .build();
    }
}


