package com.example.blogserver.controller.admin;

import com.example.blogserver.common.PageResult;
import com.example.blogserver.common.Result;
import com.example.blogserver.dto.user.AssignRoleDTO;
import com.example.blogserver.dto.user.UserQueryDTO;
import com.example.blogserver.dto.user.UserSaveDTO;
import com.example.blogserver.service.UserService;
import com.example.blogserver.vo.UserAdminVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 后台 - 用户管理
 */
@Tag(name = "16. 后台-用户管理", description = "用户的增删改查与分配角色")
@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    @Operation(summary = "用户分页")
    @PreAuthorize("hasAuthority('user:list')")
    @GetMapping
    public Result<PageResult<UserAdminVO>> page(UserQueryDTO query) {
        return Result.success(userService.adminPage(query));
    }

    @Operation(summary = "用户详情")
    @PreAuthorize("hasAuthority('user:list')")
    @GetMapping("/{id}")
    public Result<UserAdminVO> detail(@PathVariable Long id) {
        return Result.success(userService.getById(id));
    }

    @Operation(summary = "新增用户")
    @PreAuthorize("hasAuthority('user:create')")
    @PostMapping
    public Result<Void> create(@Valid @RequestBody UserSaveDTO dto) {
        dto.setId(null);
        userService.save(dto);
        return Result.success("新增成功", null);
    }

    @Operation(summary = "修改用户")
    @PreAuthorize("hasAuthority('user:update')")
    @PutMapping
    public Result<Void> update(@Valid @RequestBody UserSaveDTO dto) {
        userService.save(dto);
        return Result.success("修改成功", null);
    }

    @Operation(summary = "删除用户")
    @PreAuthorize("hasAuthority('user:delete')")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return Result.success("删除成功", null);
    }

    @Operation(summary = "分配角色")
    @PreAuthorize("hasAuthority('user:update')")
    @PostMapping("/assign-roles")
    public Result<Void> assignRoles(@Valid @RequestBody AssignRoleDTO dto) {
        userService.assignRoles(dto.getUserId(), dto.getRoleIds());
        return Result.success("分配成功", null);
    }
}
