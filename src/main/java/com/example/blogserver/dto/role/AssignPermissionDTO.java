package com.example.blogserver.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 角色分配权限
 */
@Data
@Schema(description = "分配权限参数")
public class AssignPermissionDTO {

    @Schema(description = "角色ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "角色ID不能为空")
    private Long roleId;

    @Schema(description = "权限ID列表", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "权限列表不能为空")
    private List<Long> permissionIds;
}
