package com.example.photographmanger.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CommentCreateDTO {
    @NotBlank(message = "评论内容不能为空")
    @Size(max = 1000, message = "评论内容不能超过1000个字符")
    private String text;

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    private Long parentId; // 可为null，null表示是顶级评论
}