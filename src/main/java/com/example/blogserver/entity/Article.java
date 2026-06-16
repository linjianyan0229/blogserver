package com.example.blogserver.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文章实体
 */
@Data
@TableName("article")
@Schema(description = "文章")
public class Article implements Serializable {

    @TableId(type = IdType.AUTO)
    @Schema(description = "文章ID")
    private Long id;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "摘要")
    private String summary;

    @Schema(description = "正文（Markdown）")
    private String content;

    @Schema(description = "缩略图/顶部背景图URL")
    private String thumbnail;

    @Schema(description = "分类ID")
    private Long categoryId;

    @Schema(description = "作者ID")
    private Long authorId;

    @Schema(description = "访问数")
    private Integer viewCount;

    @Schema(description = "点赞数")
    private Integer likeCount;

    @Schema(description = "评论数")
    private Integer commentCount;

    @Schema(description = "是否公开（游客可见）：0否 1是")
    private Integer isPublic;

    @Schema(description = "是否置顶：0否 1是")
    private Integer top;

    @Schema(description = "状态：0草稿 1已发布")
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @TableLogic
    @TableField(select = false)
    @Schema(hidden = true)
    private Integer deleted;
}
