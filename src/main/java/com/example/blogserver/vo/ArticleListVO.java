package com.example.blogserver.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 文章列表项（含缩略图、摘要、标题、点赞数、评论数、访问数）
 */
@Data
@Schema(description = "文章列表项")
public class ArticleListVO {

    @Schema(description = "文章ID")
    private Long id;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "摘要")
    private String summary;

    @Schema(description = "缩略图")
    private String thumbnail;

    @Schema(description = "分类ID")
    private Long categoryId;

    @Schema(description = "分类名称")
    private String categoryName;

    @Schema(description = "作者昵称")
    private String authorName;

    @Schema(description = "作者头像")
    private String authorAvatar;

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

    @Schema(description = "状态：0草稿 1已发布（后台列表区分草稿/已发布）")
    private Integer status;

    @Schema(description = "标签列表")
    private List<String> tags;

    @Schema(description = "发布时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
