package com.example.blogserver.controller.admin;

import com.example.blogserver.common.Result;
import com.example.blogserver.dto.CategorySaveDTO;
import com.example.blogserver.entity.Category;
import com.example.blogserver.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台 - 分类管理
 */
@Tag(name = "12. 后台-分类管理", description = "分类的增删改查")
@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "分类列表")
    @PreAuthorize("hasAuthority('category:list')")
    @GetMapping
    public Result<List<Category>> list() {
        return Result.success(categoryService.list());
    }

    @Operation(summary = "新增分类")
    @PreAuthorize("hasAuthority('category:create')")
    @PostMapping
    public Result<Void> create(@Valid @RequestBody CategorySaveDTO dto) {
        dto.setId(null);
        categoryService.save(dto);
        return Result.success("新增成功", null);
    }

    @Operation(summary = "修改分类")
    @PreAuthorize("hasAuthority('category:update')")
    @PutMapping
    public Result<Void> update(@Valid @RequestBody CategorySaveDTO dto) {
        categoryService.save(dto);
        return Result.success("修改成功", null);
    }

    @Operation(summary = "删除分类")
    @PreAuthorize("hasAuthority('category:delete')")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return Result.success("删除成功", null);
    }
}
