package com.example.blogserver.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * 角色新增/修改
 */
@Data
@Schema(description = "角色保存参数")
public class RoleSaveDTO {

    @Schema(description = "角色ID（修改时传）")
    private Long id;

    @Schema(description = "角色名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "角色名称不能为空")
    private String name;

    @Schema(description = "角色编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "角色编码不能为空")
    private String code;

    @Schema(description = "角色描述")
    private String description;

    @Schema(description = "状态：0禁用 1正常", example = "1")
    private Integer status = 1;

    @Schema(description = "权限ID列表")
    private List<Long> permissionIds;
}
