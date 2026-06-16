package com.example.blogserver.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 友情链接实体
 */
@Data
@TableName("friend_link")
@Schema(description = "友链")
public class FriendLink implements Serializable {

    @TableId(type = IdType.AUTO)
    @Schema(description = "友链ID")
    private Long id;

    @Schema(description = "网站名称")
    private String name;

    @Schema(description = "网站地址")
    private String url;

    @Schema(description = "网站LOGO")
    private String logo;

    @Schema(description = "网站描述")
    private String description;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "状态：0下线 1显示")
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @TableLogic
    @TableField(select = false)
    @Schema(hidden = true)
    private Integer deleted;
}
