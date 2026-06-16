package com.example.blogserver.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 接口限流规则实体（API 管理配置）
 */
@Data
@TableName("api_rate_limit")
@Schema(description = "接口限流规则")
public class ApiRateLimit implements Serializable {

    @TableId(type = IdType.AUTO)
    @Schema(description = "规则ID")
    private Long id;

    @Schema(description = "规则名称")
    private String name;

    @Schema(description = "接口路径（Ant 风格，不含 context-path，如 /auth/**）")
    private String urlPattern;

    @Schema(description = "请求方法：ALL/GET/POST/PUT/DELETE")
    private String method;

    @Schema(description = "限流维度：IP / USER / GLOBAL")
    private String dimension;

    @Schema(description = "窗口内最大请求次数")
    private Integer maxRequests;

    @Schema(description = "时间窗口（秒）")
    private Integer windowSeconds;

    @Schema(description = "是否启用：0停用 1启用")
    private Integer enabled;

    @Schema(description = "备注")
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
