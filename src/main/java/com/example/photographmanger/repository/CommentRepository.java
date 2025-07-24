package com.example.photographmanger.repository;

import com.example.photographmanger.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByParentIdIsNullOrderByCreatedTimeDesc();

    List<Comment> findByParentIdOrderByCreatedTimeAsc(Long parentId);

    List<Comment> findByUserIdOrderByCreatedTimeDesc(Long userId);

    int deleteByIdAndUserId(Long id, Long userId);
    List<Comment> findAllByOrderByCreatedTimeDesc();
}