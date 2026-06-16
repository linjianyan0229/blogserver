package com.example.blogserver.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 用户信息（含角色与权限，不含密码）
 */
@Data
@Schema(description = "用户信息")
public class UserInfoVO {

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

    @Schema(description = "角色编码集合")
    private Set<String> roles;

    @Schema(description = "权限编码集合")
    private Set<String> permissions;

    @Schema(description = "注册时间")
    private LocalDateTime createTime;
}
