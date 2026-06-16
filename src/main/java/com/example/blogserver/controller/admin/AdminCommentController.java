package com.example.blogserver.controller.admin;

import com.example.blogserver.common.PageResult;
import com.example.blogserver.common.Result;
import com.example.blogserver.dto.PageQuery;
import com.example.blogserver.service.CommentService;
import com.example.blogserver.vo.CommentVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 后台 - 评论管理
 */
@Tag(name = "15. 后台-评论管理", description = "评论分页与删除")
@RestController
@RequestMapping("/admin/comments")
@RequiredArgsConstructor
public class AdminCommentController {

    private final CommentService commentService;

    @Operation(summary = "评论分页")
    @PreAuthorize("hasAuthority('comment:list')")
    @GetMapping
    public Result<PageResult<CommentVO>> page(PageQuery query) {
        return Result.success(commentService.adminPage(query));
    }

    @Operation(summary = "删除评论")
    @PreAuthorize("hasAuthority('comment:delete')")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        commentService.adminDelete(id);
        return Result.success("删除成功", null);
    }
}
