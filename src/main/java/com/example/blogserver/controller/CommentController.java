package com.example.blogserver.controller;

import com.example.blogserver.common.Result;
import com.example.blogserver.dto.comment.CommentCreateDTO;
import com.example.blogserver.security.SecurityUtils;
import com.example.blogserver.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 评论（需登录）
 */
@Tag(name = "04. 评论", description = "发表评论、删除自己的评论")
@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "发表评论")
    @PostMapping
    public Result<Long> add(@Valid @RequestBody CommentCreateDTO dto) {
        return Result.success("评论成功", commentService.add(dto, SecurityUtils.getUserId()));
    }

    @Operation(summary = "删除自己的评论")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        commentService.deleteOwn(id, SecurityUtils.getUserId());
        return Result.success("删除成功", null);
    }
}
