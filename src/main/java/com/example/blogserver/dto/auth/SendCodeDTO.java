package com.example.blogserver.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 发送邮箱验证码请求
 */
@Data
@Schema(description = "发送邮箱验证码请求")
public class SendCodeDTO {

    @Schema(description = "邮箱", example = "zhangsan@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    @Schema(description = "验证码类型：register注册 / reset重置密码", example = "register",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "验证码类型不能为空")
    @Pattern(regexp = "register|reset", message = "类型只能为 register 或 reset")
    private String type;
}
