package com.example.blogserver.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 限流规则保存（API 管理配置）
 */
@Data
@Schema(description = "限流规则保存参数")
public class ApiRateLimitSaveDTO {

    @Schema(description = "规则ID（修改时传）")
    private Long id;

    @Schema(description = "规则名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "登录限流")
    @NotBlank(message = "规则名称不能为空")
    private String name;

    @Schema(description = "接口路径（Ant 风格，不含 context-path）", requiredMode = Schema.RequiredMode.REQUIRED, example = "/auth/login")
    @NotBlank(message = "接口路径不能为空")
    private String urlPattern;

    @Schema(description = "请求方法：ALL/GET/POST/PUT/DELETE", example = "POST", defaultValue = "ALL")
    @Pattern(regexp = "ALL|GET|POST|PUT|DELETE", message = "方法只能为 ALL/GET/POST/PUT/DELETE")
    private String method = "ALL";

    @Schema(description = "限流维度：IP / USER / GLOBAL", example = "IP", defaultValue = "IP")
    @Pattern(regexp = "IP|USER|GLOBAL", message = "维度只能为 IP/USER/GLOBAL")
    private String dimension = "IP";

    @Schema(description = "窗口内最大请求次数", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    @NotNull(message = "最大请求次数不能为空")
    @Min(value = 1, message = "最大请求次数至少为1")
    private Integer maxRequests;

    @Schema(description = "时间窗口（秒）", requiredMode = Schema.RequiredMode.REQUIRED, example = "60")
    @NotNull(message = "时间窗口不能为空")
    @Min(value = 1, message = "时间窗口至少为1秒")
    private Integer windowSeconds;

    @Schema(description = "是否启用：0停用 1启用", example = "1", defaultValue = "1")
    private Integer enabled = 1;

    @Schema(description = "备注")
    private String remark;
}
