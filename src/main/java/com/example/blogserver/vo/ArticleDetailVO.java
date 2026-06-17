package com.example.blogserver.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 文章详情（含正文、目录、顶部背景图）
 */
@Data
@Schema(description = "文章详情")
public class ArticleDetailVO {

    @Schema(description = "文章ID")
    private Long id;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "摘要")
    private String summary;

    @Schema(description = "正文（Markdown）")
    private String content;

    @Schema(description = "缩略图 / 顶部背景图")
    private String thumbnail;

    @Schema(description = "目录（由正文标题解析，用于文章内容目录）")
    private List<TocItem> toc;

    @Schema(description = "分类ID")
    private Long categoryId;

    @Schema(description = "分类名称")
    private String categoryName;

    @Schema(description = "作者ID")
    private Long authorId;

    @Schema(description = "作者昵称")
    private String authorName;

    @Schema(description = "作者头像")
    private String authorAvatar;

    @Schema(description = "标签名称列表")
    private List<String> tags;

    @Schema(description = "标签ID列表（编辑回显用，与 ArticleSaveDTO.tagIds 对应）")
    private List<Long> tagIds;

    @Schema(description = "访问数")
    private Integer viewCount;

    @Schema(description = "点赞数")
    private Integer likeCount;

    @Schema(description = "评论数")
    private Integer commentCount;

    @Schema(description = "是否公开：0否 1是")
    private Integer isPublic;

    @Schema(description = "是否置顶：0否 1是")
    private Integer top;

    @Schema(description = "是否需要密码访问")
    private Boolean hasPassword;

    @Schema(description = "是否需要输入密码（true 时 content/toc 被隐藏，需调用解锁接口）")
    private Boolean needPassword;

    @Schema(description = "访问密码（仅后台编辑回显返回，公开接口不返回）")
    private String password;

    @Schema(description = "状态：0草稿 1已发布")
    private Integer status;

    @Schema(description = "发布时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
