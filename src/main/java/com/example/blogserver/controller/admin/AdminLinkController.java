package com.example.blogserver.controller.admin;

import com.example.blogserver.common.Result;
import com.example.blogserver.dto.FriendLinkSaveDTO;
import com.example.blogserver.entity.FriendLink;
import com.example.blogserver.service.FriendLinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台 - 友链管理
 */
@Tag(name = "14. 后台-友链管理", description = "友情链接的增删改查")
@RestController
@RequestMapping("/admin/links")
@RequiredArgsConstructor
public class AdminLinkController {

    private final FriendLinkService friendLinkService;

    @Operation(summary = "友链列表（含下线）")
    @PreAuthorize("hasAuthority('link:list')")
    @GetMapping
    public Result<List<FriendLink>> list() {
        return Result.success(friendLinkService.adminList());
    }

    @Operation(summary = "新增友链")
    @PreAuthorize("hasAuthority('link:create')")
    @PostMapping
    public Result<Void> create(@Valid @RequestBody FriendLinkSaveDTO dto) {
        dto.setId(null);
        friendLinkService.save(dto);
        return Result.success("新增成功", null);
    }

    @Operation(summary = "修改友链")
    @PreAuthorize("hasAuthority('link:update')")
    @PutMapping
    public Result<Void> update(@Valid @RequestBody FriendLinkSaveDTO dto) {
        friendLinkService.save(dto);
        return Result.success("修改成功", null);
    }

    @Operation(summary = "删除友链")
    @PreAuthorize("hasAuthority('link:delete')")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        friendLinkService.delete(id);
        return Result.success("删除成功", null);
    }
}
