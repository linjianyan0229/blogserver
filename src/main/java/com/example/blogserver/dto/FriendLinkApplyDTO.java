package com.example.blogserver.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 友链申请请求（用户提交）
 */
@Data
@Schema(description = "友链申请参数")
public class FriendLinkApplyDTO {

    @Schema(description = "网站名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三的博客")
    @NotBlank(message = "网站名称不能为空")
    private String name;

    @Schema(description = "网站地址", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://zhangsan.com")
    @NotBlank(message = "网站地址不能为空")
    private String url;

    @Schema(description = "网站LOGO", example = "https://zhangsan.com/logo.png")
    private String logo;

    @Schema(description = "网站描述", example = "记录技术与生活")
    private String description;

    @Schema(description = "联系邮箱", example = "zhangsan@example.com")
    @Email(message = "联系邮箱格式不正确")
    private String applyEmail;
}
