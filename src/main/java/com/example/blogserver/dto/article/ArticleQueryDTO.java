package com.example.blogserver.dto.article;

import com.example.blogserver.dto.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文章列表查询
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "文章查询参数")
public class ArticleQueryDTO extends PageQuery {

    @Schema(description = "分类ID")
    private Long categoryId;

    @Schema(description = "标签ID")
    private Long tagId;

    @Schema(description = "关键词（标题/摘要模糊）")
    private String keyword;

    @Schema(description = "状态：0草稿 1已发布（仅后台可用）")
    private Integer status;
}
