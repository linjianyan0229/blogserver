package com.example.blogserver.controller;

import com.example.blogserver.common.Result;
import com.example.blogserver.service.VisitService;
import com.example.blogserver.vo.VisitStatsVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 网站访问统计（公开）
 */
@Tag(name = "07. 访问统计", description = "获取访客IP、上报访问、查询网站访问量(PV/UV)")
@RestController
@RequestMapping("/public/visit")
@RequiredArgsConstructor
public class VisitController {

    private final VisitService visitService;

    @Operation(summary = "获取访客IP", description = "返回当前请求的客户端 IP")
    @GetMapping("/ip")
    public Result<String> ip(HttpServletRequest request) {
        return Result.success(visitService.getClientIp(request));
    }

    @Operation(summary = "上报一次访问", description = "前端每次页面访问调用，记录IP/路径，返回最新总访问量(PV)")
    @PostMapping
    public Result<Long> record(@Parameter(description = "访问的页面路径") @RequestParam(required = false) String path,
                               HttpServletRequest request) {
        return Result.success(visitService.record(request, path));
    }

    @Operation(summary = "网站访问量统计", description = "总PV/UV、今日PV/UV")
    @GetMapping("/stats")
    public Result<VisitStatsVO> stats() {
        return Result.success(visitService.getStats());
    }
}
