package com.example.blogserver.controller.admin;

import com.example.blogserver.common.Result;
import com.example.blogserver.dto.TagSaveDTO;
import com.example.blogserver.entity.Tag;
import com.example.blogserver.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台 - 标签管理
 */
@io.swagger.v3.oas.annotations.tags.Tag(name = "13. 后台-标签管理", description = "标签的增删改查")
@RestController
@RequestMapping("/admin/tags")
@RequiredArgsConstructor
public class AdminTagController {

    private final TagService tagService;

    @Operation(summary = "标签列表")
    @PreAuthorize("hasAuthority('tag:list')")
    @GetMapping
    public Result<List<Tag>> list() {
        return Result.success(tagService.list());
    }

    @Operation(summary = "新增标签")
    @PreAuthorize("hasAuthority('tag:create')")
    @PostMapping
    public Result<Void> create(@Valid @RequestBody TagSaveDTO dto) {
        dto.setId(null);
        tagService.save(dto);
        return Result.success("新增成功", null);
    }

    @Operation(summary = "修改标签")
    @PreAuthorize("hasAuthority('tag:update')")
    @PutMapping
    public Result<Void> update(@Valid @RequestBody TagSaveDTO dto) {
        tagService.save(dto);
        return Result.success("修改成功", null);
    }

    @Operation(summary = "删除标签")
    @PreAuthorize("hasAuthority('tag:delete')")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        tagService.delete(id);
        return Result.success("删除成功", null);
    }
}
