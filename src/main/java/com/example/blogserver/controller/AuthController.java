package com.example.blogserver.controller;

import com.example.blogserver.common.Result;
import com.example.blogserver.dto.auth.LoginDTO;
import com.example.blogserver.dto.auth.RegisterDTO;
import com.example.blogserver.dto.auth.ResetPasswordDTO;
import com.example.blogserver.dto.auth.SendCodeDTO;
import com.example.blogserver.service.AuthService;
import com.example.blogserver.vo.LoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 认证接口（无需登录）
 */
@Tag(name = "01. 认证模块", description = "注册、登录、找回密码、邮箱验证码")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "发送邮箱验证码", description = "type=register 注册 / type=reset 重置密码")
    @PostMapping("/send-code")
    public Result<Void> sendCode(@Valid @RequestBody SendCodeDTO dto) {
        authService.sendCode(dto);
        return Result.success("验证码已发送，请查收邮箱", null);
    }

    @Operation(summary = "邮箱注册", description = "需先获取邮箱验证码")
    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody RegisterDTO dto) {
        authService.register(dto);
        return Result.success("注册成功，请登录", null);
    }

    @Operation(summary = "邮箱密码登录", description = "返回 JWT 令牌与用户信息")
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO dto) {
        return Result.success("登录成功", authService.login(dto));
    }

    @Operation(summary = "忘记密码 - 重置密码", description = "通过邮箱验证码重置为新密码")
    @PostMapping("/reset-password")
    public Result<Void> resetPassword(@Valid @RequestBody ResetPasswordDTO dto) {
        authService.resetPassword(dto);
        return Result.success("密码重置成功，请使用新密码登录", null);
    }

    @Operation(summary = "退出登录", description = "无状态 JWT，客户端丢弃令牌即可")
    @PostMapping("/logout")
    public Result<Void> logout() {
        return Result.success("已退出登录", null);
    }
}
