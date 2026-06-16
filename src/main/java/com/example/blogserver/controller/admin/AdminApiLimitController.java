package com.example.blogserver.controller.admin;

import com.example.blogserver.common.Result;
import com.example.blogserver.dto.ApiRateLimitSaveDTO;
import com.example.blogserver.entity.ApiRateLimit;
import com.example.blogserver.service.RateLimitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台 - 接口限流管理（API 管理配置）
 */
@Tag(name = "19. 后台-接口限流", description = "动态配置接口限流规则，修改即时生效（10秒内）")
@RestController
@RequestMapping("/admin/api-limits")
@RequiredArgsConstructor
public class AdminApiLimitController {

    private final RateLimitService rateLimitService;

    @Operation(summary = "限流规则列表")
    @PreAuthorize("hasAuthority('api:list')")
    @GetMapping
    public Result<List<ApiRateLimit>> list() {
        return Result.success(rateLimitService.list());
    }

    @Operation(summary = "新增限流规则")
    @PreAuthorize("hasAuthority('api:create')")
    @PostMapping
    public Result<Void> create(@Valid @RequestBody ApiRateLimitSaveDTO dto) {
        ApiRateLimit rule = new ApiRateLimit();
        BeanUtils.copyProperties(dto, rule);
        rule.setId(null);
        rateLimitService.save(rule);
        return Result.success("新增成功", null);
    }

    @Operation(summary = "修改限流规则")
    @PreAuthorize("hasAuthority('api:update')")
    @PutMapping
    public Result<Void> update(@Valid @RequestBody ApiRateLimitSaveDTO dto) {
        ApiRateLimit rule = new ApiRateLimit();
        BeanUtils.copyProperties(dto, rule);
        rateLimitService.save(rule);
        return Result.success("修改成功", null);
    }

    @Operation(summary = "删除限流规则")
    @PreAuthorize("hasAuthority('api:delete')")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "规则ID", required = true) @PathVariable Long id) {
        rateLimitService.delete(id);
        return Result.success("删除成功", null);
    }
}
