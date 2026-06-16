package com.example.blogserver.controller;

import com.example.blogserver.common.Result;
import com.example.blogserver.dto.user.ChangePasswordDTO;
import com.example.blogserver.dto.user.UpdateProfileDTO;
import com.example.blogserver.security.SecurityUtils;
import com.example.blogserver.service.AuthService;
import com.example.blogserver.service.UserService;
import com.example.blogserver.vo.UserInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户中心（需登录）
 */
@Tag(name = "03. 用户中心", description = "当前用户信息、修改个人资料、修改密码")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @Operation(summary = "获取当前登录用户信息", description = "含角色与权限编码")
    @GetMapping("/info")
    public Result<UserInfoVO> info() {
        return Result.success(authService.toUserInfoVO(SecurityUtils.getLoginUser()));
    }

    @Operation(summary = "修改个人信息")
    @PutMapping("/profile")
    public Result<Void> updateProfile(@Valid @RequestBody UpdateProfileDTO dto) {
        userService.updateProfile(SecurityUtils.getUserId(), dto);
        return Result.success("修改成功", null);
    }

    @Operation(summary = "修改密码")
    @PutMapping("/password")
    public Result<Void> changePassword(@Valid @RequestBody ChangePasswordDTO dto) {
        userService.changePassword(SecurityUtils.getUserId(), dto);
        return Result.success("密码修改成功", null);
    }
}
