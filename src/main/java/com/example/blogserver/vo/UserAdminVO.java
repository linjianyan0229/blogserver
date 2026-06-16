package com.example.blogserver.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 后台用户列表项（含角色）
 */
@Data
@Schema(description = "后台用户信息")
public class UserAdminVO {

    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "个人简介")
    private String bio;

    @Schema(description = "性别：0未知 1男 2女")
    private Integer gender;

    @Schema(description = "状态：0禁用 1正常")
    private Integer status;

    @Schema(description = "最后登录时间")
    private LocalDateTime lastLoginTime;

    @Schema(description = "注册时间")
    private LocalDateTime createTime;

    @Schema(description = "角色ID列表")
    private List<Long> roleIds;

    @Schema(description = "角色名称列表")
    private List<String> roleNames;
}
