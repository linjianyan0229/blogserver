package com.example.blogserver.controller.admin;

import com.example.blogserver.common.PageResult;
import com.example.blogserver.common.Result;
import com.example.blogserver.entity.FriendLinkApply;
import com.example.blogserver.service.FriendLinkApplyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 后台 - 友链申请审核
 */
@Tag(name = "21. 后台-友链审核", description = "审核用户提交的友链申请：通过（自动加入友链）/拒绝")
@RestController
@RequestMapping("/admin/link-applies")
@RequiredArgsConstructor
public class AdminLinkApplyController {

    private final FriendLinkApplyService linkApplyService;

    @Operation(summary = "友链申请分页", description = "可按状态过滤：PENDING待审核 / APPROVED已通过 / REJECTED已拒绝")
    @PreAuthorize("hasAuthority('link:audit')")
    @GetMapping
    public Result<PageResult<FriendLinkApply>> page(
            @Parameter(description = "审核状态：PENDING/APPROVED/REJECTED，不传查全部") @RequestParam(required = false) String status,
            @Parameter(description = "页码，默认1") @RequestParam(defaultValue = "1") Long page,
            @Parameter(description = "每页大小，默认10") @RequestParam(defaultValue = "10") Long size) {
        return Result.success(linkApplyService.adminPage(status, page, size));
    }

    @Operation(summary = "审核通过", description = "通过后自动写入友链并显示")
    @PreAuthorize("hasAuthority('link:audit')")
    @PostMapping("/{id}/approve")
    public Result<Void> approve(@Parameter(description = "申请ID", required = true) @PathVariable Long id) {
        linkApplyService.approve(id);
        return Result.success("已通过，已加入友链", null);
    }

    @Operation(summary = "审核拒绝")
    @PreAuthorize("hasAuthority('link:audit')")
    @PostMapping("/{id}/reject")
    public Result<Void> reject(@Parameter(description = "申请ID", required = true) @PathVariable Long id,
                               @Parameter(description = "拒绝原因") @RequestParam(required = false) String remark) {
        linkApplyService.reject(id, remark);
        return Result.success("已拒绝", null);
    }
}
