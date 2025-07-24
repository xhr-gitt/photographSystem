package com.example.photographmanger.service.impl;

import com.example.photographmanger.dto.CommentCreateDTO;
import com.example.photographmanger.entity.Comment;
import com.example.photographmanger.repository.CommentRepository;
import com.example.photographmanger.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    @Transactional
    public Comment createComment(CommentCreateDTO dto) {
        Comment comment = new Comment();
        comment.setText(dto.getText());
        comment.setUserId(dto.getUserId());
        comment.setParentId(dto.getParentId());
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> getTopLevelComments() {
        return commentRepository.findByParentIdIsNullOrderByCreatedTimeDesc();
    }

    @Override
    public List<Comment> getRepliesByParentId(Long parentId) {
        return commentRepository.findByParentIdOrderByCreatedTimeAsc(parentId);
    }

    @Override
    public List<Comment> getCommentsByUserId(Long userId) {
        return commentRepository.findByUserIdOrderByCreatedTimeDesc(userId);
    }

    @Override
    @Transactional
    public boolean deleteComment(Long commentId, Long userId) {
        return commentRepository.findById(commentId)
                .map(comment -> {
                    if (!comment.getUserId().equals(userId)) {
                        throw new SecurityException("无权删除此评论");
                    }
                    commentRepository.delete(comment);
                    return true;
                })
                .orElse(false);
    }
    @Override
    public List<Comment> getAllComments() {
        // 获取所有评论并按创建时间排序
        return commentRepository.findAllByOrderByCreatedTimeDesc();
    }
}