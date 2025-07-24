package com.example.photographmanger.service;

import com.example.photographmanger.dto.CommentCreateDTO;
import com.example.photographmanger.entity.Comment;
import java.util.List;

public interface CommentService {
    /**
     * 创建评论
     * @param dto 评论创建DTO
     * @return 创建的评论实体
     */
    Comment createComment(CommentCreateDTO dto);
    
    /**
     * 获取所有顶级评论（无父评论的评论）
     * @return 顶级评论列表
     */
    List<Comment> getTopLevelComments();
    
    /**
     * 获取指定评论的所有回复
     * @param parentId 父评论ID
     * @return 回复列表
     */
    List<Comment> getRepliesByParentId(Long parentId);
    
    /**
     * 获取用户的所有评论
     * @param userId 用户ID
     * @return 用户评论列表
     */
    List<Comment> getCommentsByUserId(Long userId);
    
    /**
     * 删除评论
     * @param commentId 评论ID
     * @param userId 用户ID（用于验证权限）
     * @return 是否删除成功
     */
    boolean deleteComment(Long commentId, Long userId);
    List<Comment> getAllComments();
}