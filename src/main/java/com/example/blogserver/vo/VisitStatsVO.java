package com.example.blogserver.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * 网站访问量统计
 */
@Data
@Builder
@Schema(description = "网站访问量统计")
public class VisitStatsVO {

    @Schema(description = "总访问量(PV)")
    private Long totalPv;

    @Schema(description = "总独立访客(UV，按IP去重)")
    private Long totalUv;

    @Schema(description = "今日访问量(PV)")
    private Long todayPv;

    @Schema(description = "今日独立访客(UV)")
    private Long todayUv;
}
