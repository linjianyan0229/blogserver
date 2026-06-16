package com.example.blogserver.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 分类保存
 */
@Data
@Schema(description = "分类保存参数")
public class CategorySaveDTO {

    @Schema(description = "分类ID（修改时传）")
    private Long id;

    @Schema(description = "分类名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "分类名称不能为空")
    private String name;

    @Schema(description = "分类描述")
    private String description;

    @Schema(description = "排序", example = "0")
    private Integer sort = 0;
}
