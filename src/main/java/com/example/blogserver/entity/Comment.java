package com.example.blogserver.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 评论实体
 */
@Data
@TableName("comment")
@Schema(description = "评论")
public class Comment implements Serializable {

    @TableId(type = IdType.AUTO)
    @Schema(description = "评论ID")
    private Long id;

    @Schema(description = "文章ID")
    private Long articleId;

    @Schema(description = "评论用户ID")
    private Long userId;

    @Schema(description = "评论内容")
    private String content;

    @Schema(description = "父评论ID（0为顶级评论）")
    private Long parentId;

    @Schema(description = "状态：0隐藏 1显示")
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
