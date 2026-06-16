package com.example.blogserver.controller.admin;

import com.example.blogserver.common.PageResult;
import com.example.blogserver.common.Result;
import com.example.blogserver.dto.article.ArticleQueryDTO;
import com.example.blogserver.dto.article.ArticleSaveDTO;
import com.example.blogserver.security.SecurityUtils;
import com.example.blogserver.service.ArticleService;
import com.example.blogserver.vo.ArticleDetailVO;
import com.example.blogserver.vo.ArticleListVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 后台 - 文章管理
 */
@Tag(name = "11. 后台-文章管理", description = "文章的增删改查（需相应权限）")
@RestController
@RequestMapping("/admin/articles")
@RequiredArgsConstructor
public class AdminArticleController {

    private final ArticleService articleService;

    @Operation(summary = "文章分页（含草稿）")
    @PreAuthorize("hasAuthority('article:list')")
    @GetMapping
    public Result<PageResult<ArticleListVO>> page(ArticleQueryDTO query) {
        return Result.success(articleService.adminPage(query));
    }

    @Operation(summary = "文章详情（编辑回显）")
    @PreAuthorize("hasAuthority('article:list')")
    @GetMapping("/{id}")
    public Result<ArticleDetailVO> detail(@PathVariable Long id) {
        return Result.success(articleService.getByIdForEdit(id));
    }

    @Operation(summary = "新增文章")
    @PreAuthorize("hasAuthority('article:create')")
    @PostMapping
    public Result<Long> create(@Valid @RequestBody ArticleSaveDTO dto) {
        dto.setId(null);
        return Result.success("新增成功", articleService.save(dto, SecurityUtils.getUserId()));
    }

    @Operation(summary = "修改文章")
    @PreAuthorize("hasAuthority('article:update')")
    @PutMapping
    public Result<Long> update(@Valid @RequestBody ArticleSaveDTO dto) {
        return Result.success("修改成功", articleService.save(dto, SecurityUtils.getUserId()));
    }

    @Operation(summary = "删除文章")
    @PreAuthorize("hasAuthority('article:delete')")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        articleService.delete(id);
        return Result.success("删除成功", null);
    }
}
