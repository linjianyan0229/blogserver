package com.example.blogserver.controller.admin;

import com.example.blogserver.common.PageResult;
import com.example.blogserver.common.Result;
import com.example.blogserver.entity.VisitLog;
import com.example.blogserver.service.VisitService;
import com.example.blogserver.vo.VisitStatsVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 后台 - 访问统计与日志
 */
@Tag(name = "22. 后台-访问统计", description = "查看网站访问量与访问日志（含IP）")
@RestController
@RequestMapping("/admin/visit")
@RequiredArgsConstructor
public class AdminVisitController {

    private final VisitService visitService;

    @Operation(summary = "访问量统计")
    @PreAuthorize("hasAuthority('visit:list')")
    @GetMapping("/stats")
    public Result<VisitStatsVO> stats() {
        return Result.success(visitService.getStats());
    }

    @Operation(summary = "访问日志分页", description = "可按 IP 模糊过滤")
    @PreAuthorize("hasAuthority('visit:list')")
    @GetMapping("/logs")
    public Result<PageResult<VisitLog>> logs(
            @Parameter(description = "IP 模糊过滤") @RequestParam(required = false) String ip,
            @Parameter(description = "页码，默认1") @RequestParam(defaultValue = "1") Long page,
            @Parameter(description = "每页大小，默认10") @RequestParam(defaultValue = "10") Long size) {
        return Result.success(visitService.adminPage(ip, page, size));
    }
}
