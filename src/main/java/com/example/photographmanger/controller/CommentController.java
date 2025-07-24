package com.example.photographmanger.controller;

import com.example.photographmanger.dto.CommentCreateDTO;
import com.example.photographmanger.entity.Comment;
import com.example.photographmanger.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "评论管理")
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "创建评论")
    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody CommentCreateDTO dto) {
        return ResponseEntity.ok(commentService.createComment(dto));
    }

    @Operation(summary = "获取所有评论")
    @GetMapping
    public ResponseEntity<List<Comment>> getAllComments() {
        return ResponseEntity.ok(commentService.getAllComments());
    }

    @Operation(summary = "删除评论")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(
            @PathVariable Long commentId,
            @RequestParam Long userId) {
        if (commentService.deleteComment(commentId, userId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}