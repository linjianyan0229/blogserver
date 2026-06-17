package com.example.blogserver.controller.admin;

import com.example.blogserver.common.Result;
import com.example.blogserver.dto.SiteConfigItemDTO;
import com.example.blogserver.entity.SiteConfig;
import com.example.blogserver.service.MailService;
import com.example.blogserver.service.SiteConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台 - 站点基础配置管理
 */
@Tag(name = "20. 后台-站点配置", description = "配置网站名称/图标、首页背景、导航、介绍、页脚等")
@RestController
@RequestMapping("/admin/site-config")
@RequiredArgsConstructor
@Validated
public class AdminSiteConfigController {

    private final SiteConfigService siteConfigService;
    private final MailService mailService;

    @Operation(summary = "站点配置列表", description = "含配置中文名与类型，供后台表单渲染")
    @PreAuthorize("hasAuthority('config:list')")
    @GetMapping
    public Result<List<SiteConfig>> list() {
        return Result.success(siteConfigService.listAll());
    }

    @Operation(summary = "批量更新站点配置", description = "按 key 更新值；key 不存在则新增。修改即时生效（缓存自动刷新）。" +
            "可在线修改邮箱配置：mail_host/mail_port/mail_username/mail_password/mail_from_name/mail_ssl_enable")
    @PreAuthorize("hasAuthority('config:update')")
    @PutMapping
    public Result<Void> update(@Valid @RequestBody List<SiteConfigItemDTO> items) {
        siteConfigService.batchUpdate(items);
        return Result.success("配置已保存", null);
    }

    @Operation(summary = "发送测试邮件", description = "用当前邮箱(SMTP)配置发送一封测试邮件，验证配置是否可用")
    @PreAuthorize("hasAuthority('config:update')")
    @PostMapping("/test-mail")
    public Result<Void> testMail(
            @Parameter(description = "收件邮箱", required = true)
            @RequestParam @NotBlank(message = "收件邮箱不能为空") @Email(message = "邮箱格式不正确") String to) {
        mailService.sendTest(to);
        return Result.success("测试邮件已发送，请查收 " + to, null);
    }
}
