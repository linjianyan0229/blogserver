package com.example.blogserver.controller;

import com.example.blogserver.common.PageResult;
import com.example.blogserver.common.Result;
import com.example.blogserver.dto.article.ArticleQueryDTO;
import com.example.blogserver.entity.Category;
import com.example.blogserver.entity.FriendLink;
import com.example.blogserver.entity.Tag;
import com.example.blogserver.service.*;
import com.example.blogserver.vo.ArticleDetailVO;
import com.example.blogserver.vo.ArticleListVO;
import com.example.blogserver.vo.CommentVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 公开接口（游客与登录用户均可访问）
 */
@io.swagger.v3.oas.annotations.tags.Tag(name = "02. 公开接口", description = "游客可访问：文章列表/详情、分类、标签、友链、评论")
@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class PublicController {

    private final ArticleService articleService;
    private final CategoryService categoryService;
    private final TagService tagService;
    private final FriendLinkService friendLinkService;
    private final CommentService commentService;
    private final SiteConfigService siteConfigService;

    @Operation(summary = "站点基础配置", description = "返回网站名称/图标、首页背景、导航栏文字/Logo、首页介绍、页脚文本等，前端按 key 取用")
    @GetMapping("/site-config")
    public Result<java.util.Map<String, String>> siteConfig() {
        return Result.success(siteConfigService.getPublicConfig());
    }

    @Operation(summary = "文章列表", description = "分页查询已发布文章，含缩略图/摘要/标题/点赞数/评论数/访问数/标签")
    @GetMapping("/articles")
    public Result<PageResult<ArticleListVO>> articles(ArticleQueryDTO query) {
        return Result.success(articleService.publicPage(query));
    }

    @Operation(summary = "文章详情", description = "公开文章游客可看；非公开文章需登录。含正文、目录(TOC)、顶部背景图，访问数自动+1")
    @GetMapping("/articles/{id}")
    public Result<ArticleDetailVO> articleDetail(@Parameter(description = "文章ID") @PathVariable Long id) {
        return Result.success(articleService.getPublicDetail(id));
    }

    @Operation(summary = "解锁加密文章", description = "对设有访问密码的文章，输入正确密码后返回完整正文")
    @PostMapping("/articles/{id}/unlock")
    public Result<ArticleDetailVO> unlock(@Parameter(description = "文章ID", required = true) @PathVariable Long id,
                                          @Valid @RequestBody com.example.blogserver.dto.article.ArticleUnlockDTO dto) {
        return Result.success("解锁成功", articleService.unlock(id, dto.getPassword()));
    }

    @Operation(summary = "文章点赞", description = "返回最新点赞数")
    @PostMapping("/articles/{id}/like")
    public Result<Integer> like(@Parameter(description = "文章ID", required = true) @PathVariable Long id) {
        return Result.success("点赞成功", articleService.like(id));
    }

    @Operation(summary = "文章评论列表", description = "树形结构（顶级评论 + 回复）")
    @GetMapping("/articles/{id}/comments")
    public Result<List<CommentVO>> comments(@Parameter(description = "文章ID", required = true) @PathVariable Long id) {
        return Result.success(commentService.listByArticle(id));
    }

    @Operation(summary = "全部分类")
    @GetMapping("/categories")
    public Result<List<Category>> categories() {
        return Result.success(categoryService.list());
    }

    @Operation(summary = "全部标签")
    @GetMapping("/tags")
    public Result<List<Tag>> tags() {
        return Result.success(tagService.list());
    }

    @Operation(summary = "友情链接", description = "仅返回显示状态的友链")
    @GetMapping("/links")
    public Result<List<FriendLink>> links() {
        return Result.success(friendLinkService.publicList());
    }
}
