package com.example.blogserver.dto.article;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 文章密码解锁请求
 */
@Data
@Schema(description = "文章密码解锁请求")
public class ArticleUnlockDTO {

    @Schema(description = "访问密码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "请输入访问密码")
    private String password;
}
