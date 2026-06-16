package com.example.blogserver.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 修改个人信息
 */
@Data
@Schema(description = "个人信息修改")
public class UpdateProfileDTO {

    @Schema(description = "昵称")
    @Size(max = 50, message = "昵称不能超过50字")
    private String nickname;

    @Schema(description = "头像URL")
    private String avatar;

    @Schema(description = "个人简介")
    @Size(max = 500, message = "简介不能超过500字")
    private String bio;

    @Schema(description = "性别：0未知 1男 2女")
    private Integer gender;
}
