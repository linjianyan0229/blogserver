package com.example.blogserver.dto.article;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * 文章新增/修改（后台）
 */
@Data
@Schema(description = "文章保存参数")
public class ArticleSaveDTO {

    @Schema(description = "文章ID（修改时传）")
    private Long id;

    @Schema(description = "标题", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "标题不能为空")
    private String title;

    @Schema(description = "摘要")
    private String summary;

    @Schema(description = "正文（Markdown）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "正文不能为空")
    private String content;

    @Schema(description = "缩略图 / 顶部背景图URL")
    private String thumbnail;

    @Schema(description = "分类ID")
    private Long categoryId;

    @Schema(description = "标签ID列表")
    private List<Long> tagIds;

    @Schema(description = "是否公开（游客可见）：0否 1是", example = "1")
    private Integer isPublic = 1;

    @Schema(description = "是否置顶：0否 1是", example = "0")
    private Integer top = 0;

    @Schema(description = "访问密码（设置后读全文需输入；传空字符串表示清除密码；不传表示不修改）")
    private String password;

    @Schema(description = "状态：0草稿 1已发布", example = "1")
    private Integer status = 1;
}
