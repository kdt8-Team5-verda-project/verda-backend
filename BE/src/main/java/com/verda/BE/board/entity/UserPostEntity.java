package com.verda.BE.board.entity;


import com.verda.BE.board.domain.BaseTimeEntity;
import com.verda.BE.login.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Timestamp;

@Entity
@Getter
@NoArgsConstructor
//@AllArgsConstructor
@Table(name = "user_post")
public final class UserPostEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;

//    @CreatedDate
//    private Timestamp createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private UserEntity userEntity;


    @Builder
    public UserPostEntity(Long postId, String title, String content) {
        this.postId = postId;
        this.title = title;
        this.content = content;
//        this.createdAt = createdAt;
//        this.userEntity = userEntity;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
