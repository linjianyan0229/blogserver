package com.example.blogserver.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 网站访问日志实体
 */
@Data
@TableName("visit_log")
@Schema(description = "访问日志")
public class VisitLog implements Serializable {

    @TableId(type = IdType.AUTO)
    @Schema(description = "访问ID")
    private Long id;

    @Schema(description = "访客IP")
    private String ip;

    @Schema(description = "访问路径")
    private String path;

    @Schema(description = "来源页")
    private String referer;

    @Schema(description = "浏览器UA")
    private String userAgent;

    @Schema(description = "登录用户ID（游客为空）")
    private Long userId;

    @Schema(description = "访问日期")
    private LocalDate visitDate;

    @Schema(description = "访问时间")
    private LocalDateTime createTime;
}
