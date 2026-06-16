package com.example.blogserver.controller.admin;

import com.example.blogserver.common.Result;
import com.example.blogserver.entity.Permission;
import com.example.blogserver.service.PermissionService;
import com.example.blogserver.vo.PermissionTreeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 后台 - 权限管理（权限配置页面数据源）
 */
@Tag(name = "18. 后台-权限菜单", description = "权限树/列表，供权限配置页面使用")
@RestController
@RequestMapping("/admin/permissions")
@RequiredArgsConstructor
public class AdminPermissionController {

    private final PermissionService permissionService;

    @Operation(summary = "权限树", description = "目录-菜单-按钮三级树，用于权限配置页面勾选")
    @PreAuthorize("hasAnyAuthority('permission:list','role:list')")
    @GetMapping("/tree")
    public Result<List<PermissionTreeVO>> tree() {
        return Result.success(permissionService.listTree());
    }

    @Operation(summary = "权限列表（平铺）")
    @PreAuthorize("hasAnyAuthority('permission:list','role:list')")
    @GetMapping
    public Result<List<Permission>> list() {
        return Result.success(permissionService.listAll());
    }
}
