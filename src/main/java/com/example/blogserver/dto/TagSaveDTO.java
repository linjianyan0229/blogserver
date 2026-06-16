package com.example.blogserver.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 标签保存
 */
@Data
@Schema(description = "标签保存参数")
public class TagSaveDTO {

    @Schema(description = "标签ID（修改时传）")
    private Long id;

    @Schema(description = "标签名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "标签名称不能为空")
    private String name;
}
