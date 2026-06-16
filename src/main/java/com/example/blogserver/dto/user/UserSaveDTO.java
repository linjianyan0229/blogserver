package com.example.blogserver.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * 用户新增/修改（后台）
 */
@Data
@Schema(description = "用户保存参数")
public class UserSaveDTO {

    @Schema(description = "用户ID（修改时传）")
    private Long id;

    @Schema(description = "用户名", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "用户名不能为空")
    private String username;

    @Schema(description = "邮箱", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    @Schema(description = "密码（新增必填；修改时留空则不变）")
    private String password;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "个人简介")
    private String bio;

    @Schema(description = "性别：0未知 1男 2女", example = "0")
    private Integer gender = 0;

    @Schema(description = "状态：0禁用 1正常", example = "1")
    private Integer status = 1;

    @Schema(description = "角色ID列表")
    private List<Long> roleIds;
}
