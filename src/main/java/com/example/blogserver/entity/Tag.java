package com.example.blogserver.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文章标签实体
 */
@Data
@TableName("tag")
@Schema(description = "标签")
public class Tag implements Serializable {

    @TableId(type = IdType.AUTO)
    @Schema(description = "标签ID")
    private Long id;

    @Schema(description = "标签名称")
    private String name;

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
