package com.example.blogserver.controller.admin;

import com.example.blogserver.common.Result;
import com.example.blogserver.dto.SiteConfigItemDTO;
import com.example.blogserver.entity.SiteConfig;
import com.example.blogserver.service.SiteConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台 - 站点基础配置管理
 */
@Tag(name = "20. 后台-站点配置", description = "配置网站名称/图标、首页背景、导航、介绍、页脚等")
@RestController
@RequestMapping("/admin/site-config")
@RequiredArgsConstructor
public class AdminSiteConfigController {

    private final SiteConfigService siteConfigService;

    @Operation(summary = "站点配置列表", description = "含配置中文名与类型，供后台表单渲染")
    @PreAuthorize("hasAuthority('config:list')")
    @GetMapping
    public Result<List<SiteConfig>> list() {
        return Result.success(siteConfigService.listAll());
    }

    @Operation(summary = "批量更新站点配置", description = "按 key 更新值；key 不存在则新增。修改即时生效（缓存自动刷新）")
    @PreAuthorize("hasAuthority('config:update')")
    @PutMapping
    public Result<Void> update(@Valid @RequestBody List<SiteConfigItemDTO> items) {
        siteConfigService.batchUpdate(items);
        return Result.success("配置已保存", null);
    }
}
