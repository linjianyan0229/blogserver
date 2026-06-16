package com.example.blogserver.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 权限/菜单实体
 */
@Data
@TableName("permission")
@Schema(description = "权限/菜单")
public class Permission implements Serializable {

    @TableId(type = IdType.AUTO)
    @Schema(description = "权限ID")
    private Long id;

    @Schema(description = "权限名称")
    private String name;

    @Schema(description = "权限编码，如 article:create")
    private String code;

    @Schema(description = "类型：1目录 2菜单 3按钮")
    private Integer type;

    @Schema(description = "父权限ID")
    private Long parentId;

    @Schema(description = "前端路由路径")
    private String path;

    @Schema(description = "前端组件")
    private String component;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "排序")
    private Integer sort;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
