package com.example.blogserver.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文章目录项（由 Markdown 标题解析）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "文章目录项")
public class TocItem {

    @Schema(description = "标题层级：1=# 2=## 3=###")
    private Integer level;

    @Schema(description = "标题文本")
    private String text;

    @Schema(description = "锚点（用于页面内跳转）")
    private String anchor;
}
