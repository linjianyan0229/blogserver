package com.example.blogserver.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 权限树节点（用于权限配置页面）
 */
@Data
@Schema(description = "权限树节点")
public class PermissionTreeVO {

    @Schema(description = "权限ID")
    private Long id;

    @Schema(description = "权限名称")
    private String name;

    @Schema(description = "权限编码")
    private String code;

    @Schema(description = "类型：1目录 2菜单 3按钮")
    private Integer type;

    @Schema(description = "父ID")
    private Long parentId;

    @Schema(description = "路由路径")
    private String path;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "子节点")
    private List<PermissionTreeVO> children;
}
