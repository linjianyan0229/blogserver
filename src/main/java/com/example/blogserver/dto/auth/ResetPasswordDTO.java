package com.example.blogserver.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 重置密码请求（忘记密码场景：邮箱 + 验证码 + 新密码）
 */
@Data
@Schema(description = "重置密码请求")
public class ResetPasswordDTO {

    @Schema(description = "邮箱", example = "zhangsan@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    @Schema(description = "邮箱验证码", example = "123456", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "验证码不能为空")
    private String code;

    @Schema(description = "新密码", example = "newpass123", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 32, message = "密码长度需为 6-32 位")
    private String newPassword;
}
