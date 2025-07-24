package com.example.photographmanger.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "text", nullable = false, columnDefinition = "TEXT")
    private String text; // 评论内容
    @Column(name = "user_id")
    private Long userId; // 评论用户ID（不使用@ManyToOne）
    @Column(name = "parent_id")
    private Long parentId; // 父评论ID（不使用@ManyToOne）
    @CreatedDate
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @Column(name = "created_time")
    protected LocalDateTime createdTime;
    @PrePersist
    protected void onCreate() {
        this.createdTime=LocalDateTime.now();
    }
}