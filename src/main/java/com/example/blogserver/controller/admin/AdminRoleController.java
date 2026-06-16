package com.example.blogserver.controller.admin;

import com.example.blogserver.common.Result;
import com.example.blogserver.dto.role.AssignPermissionDTO;
import com.example.blogserver.dto.role.RoleSaveDTO;
import com.example.blogserver.entity.Role;
import com.example.blogserver.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台 - 角色管理与权限分配（权限配置页面）
 */
@Tag(name = "17. 后台-角色权限", description = "角色增删改查、为角色分配权限")
@RestController
@RequestMapping("/admin/roles")
@RequiredArgsConstructor
public class AdminRoleController {

    private final RoleService roleService;

    @Operation(summary = "角色列表")
    @PreAuthorize("hasAuthority('role:list')")
    @GetMapping
    public Result<List<Role>> list() {
        return Result.success(roleService.list());
    }

    @Operation(summary = "查询角色已分配的权限ID", description = "用于权限配置页面回显勾选")
    @PreAuthorize("hasAuthority('role:list')")
    @GetMapping("/{id}/permissions")
    public Result<List<Long>> permissionIds(@PathVariable Long id) {
        return Result.success(roleService.getPermissionIds(id));
    }

    @Operation(summary = "新增角色")
    @PreAuthorize("hasAuthority('role:create')")
    @PostMapping
    public Result<Void> create(@Valid @RequestBody RoleSaveDTO dto) {
        dto.setId(null);
        roleService.save(dto);
        return Result.success("新增成功", null);
    }

    @Operation(summary = "修改角色")
    @PreAuthorize("hasAuthority('role:update')")
    @PutMapping
    public Result<Void> update(@Valid @RequestBody RoleSaveDTO dto) {
        roleService.save(dto);
        return Result.success("修改成功", null);
    }

    @Operation(summary = "删除角色")
    @PreAuthorize("hasAuthority('role:delete')")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        roleService.delete(id);
        return Result.success("删除成功", null);
    }

    @Operation(summary = "为角色分配权限", description = "权限配置页面提交")
    @PreAuthorize("hasAuthority('role:assign')")
    @PostMapping("/assign-permissions")
    public Result<Void> assignPermissions(@Valid @RequestBody AssignPermissionDTO dto) {
        roleService.assignPermissions(dto.getRoleId(), dto.getPermissionIds());
        return Result.success("分配成功", null);
    }
}
