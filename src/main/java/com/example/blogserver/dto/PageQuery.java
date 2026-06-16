package com.example.blogserver.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 分页查询基类
 */
@Data
@Schema(description = "分页查询参数")
public class PageQuery {

    @Schema(description = "页码（从1开始）", example = "1")
    private Long page = 1L;

    @Schema(description = "每页大小", example = "10")
    private Long size = 10L;
}
