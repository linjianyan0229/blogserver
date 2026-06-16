package com.example.blogserver.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 友链保存
 */
@Data
@Schema(description = "友链保存参数")
public class FriendLinkSaveDTO {

    @Schema(description = "友链ID（修改时传）")
    private Long id;

    @Schema(description = "网站名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "网站名称不能为空")
    private String name;

    @Schema(description = "网站地址", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "网站地址不能为空")
    private String url;

    @Schema(description = "网站LOGO")
    private String logo;

    @Schema(description = "网站描述")
    private String description;

    @Schema(description = "排序", example = "0")
    private Integer sort = 0;

    @Schema(description = "状态：0下线 1显示", example = "1")
    private Integer status = 1;
}
