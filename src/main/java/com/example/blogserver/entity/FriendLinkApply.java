package com.example.blogserver.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 友链申请实体
 */
@Data
@TableName("friend_link_apply")
@Schema(description = "友链申请")
public class FriendLinkApply implements Serializable {

    @TableId(type = IdType.AUTO)
    @Schema(description = "申请ID")
    private Long id;

    @Schema(description = "网站名称")
    private String name;

    @Schema(description = "网站地址")
    private String url;

    @Schema(description = "网站LOGO")
    private String logo;

    @Schema(description = "网站描述")
    private String description;

    @Schema(description = "联系邮箱")
    private String applyEmail;

    @Schema(description = "申请用户ID")
    private Long applyUserId;

    @Schema(description = "审核状态：PENDING待审核 APPROVED已通过 REJECTED已拒绝")
    private String status;

    @Schema(description = "审核备注/拒绝原因")
    private String auditRemark;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "申请时间")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
