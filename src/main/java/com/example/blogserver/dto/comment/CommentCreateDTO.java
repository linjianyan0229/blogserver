package com.example.blogserver.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 发表评论请求
 */
@Data
@Schema(description = "发表评论请求")
public class CommentCreateDTO {

    @Schema(description = "文章ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "文章ID不能为空")
    private Long articleId;

    @Schema(description = "评论内容", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "评论内容不能为空")
    @Size(max = 1000, message = "评论内容不能超过1000字")
    private String content;

    @Schema(description = "父评论ID（回复时传，顶级评论传0或不传）", example = "0")
    private Long parentId = 0L;
}
