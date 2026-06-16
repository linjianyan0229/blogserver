package com.example.blogserver.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 分配用户角色
 */
@Data
@Schema(description = "分配角色参数")
public class AssignRoleDTO {

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Schema(description = "角色ID列表", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "角色列表不能为空")
    private List<Long> roleIds;
}
