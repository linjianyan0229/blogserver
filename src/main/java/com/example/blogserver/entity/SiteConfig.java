package com.example.blogserver.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 站点基础配置实体（key-value）
 */
@Data
@TableName("site_config")
@Schema(description = "站点配置")
public class SiteConfig implements Serializable {

    @TableId(type = IdType.AUTO)
    @Schema(description = "配置ID")
    private Long id;

    @Schema(description = "配置键")
    private String configKey;

    @Schema(description = "配置值")
    private String configValue;

    @Schema(description = "配置中文名（后台展示）")
    private String name;

    @Schema(description = "类型：text单行/textarea多行/image图片/color颜色")
    private String type;

    @Schema(description = "排序")
    private Integer sort;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
