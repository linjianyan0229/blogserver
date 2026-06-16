package com.example.blogserver.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 站点配置项（批量更新用）
 */
@Data
@Schema(description = "站点配置项")
public class SiteConfigItemDTO {

    @Schema(description = "配置键", requiredMode = Schema.RequiredMode.REQUIRED, example = "site_name")
    @NotBlank(message = "配置键不能为空")
    private String configKey;

    @Schema(description = "配置值", example = "我的博客")
    private String configValue;

    @Schema(description = "配置中文名（新增配置项时可传）", example = "网站名称")
    private String name;

    @Schema(description = "类型：text/textarea/image/color（新增配置项时可传）", example = "text")
    private String type;

    @Schema(description = "排序")
    private Integer sort;
}
