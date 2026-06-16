package com.example.blogserver.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * 登录成功返回
 */
@Data
@Builder
@Schema(description = "登录结果")
public class LoginVO {

    @Schema(description = "JWT 访问令牌")
    private String token;

    @Schema(description = "令牌前缀", example = "Bearer ")
    private String tokenPrefix;

    @Schema(description = "有效期（秒）")
    private Long expiresIn;

    @Schema(description = "用户信息")
    private UserInfoVO userInfo;
}
