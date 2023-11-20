package com.verda.BE.board.dto.responsedto;

import com.verda.BE.board.entity.UserPostEntity;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardResponseDTO {
    private Long postId;
    private String title;
    private String content;
//    private LocalDateTime createdAt;

    public BoardResponseDTO(UserPostEntity entity) {
        this.postId = entity.getPostId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
//        this.createdAt = entity.getCreatedAt().getTime();
//        this.createdAt = entity.getCreatedAt();
    }

}
