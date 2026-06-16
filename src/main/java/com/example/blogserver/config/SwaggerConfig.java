package com.example.blogserver.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger / OpenAPI 3 中文文档配置
 * 访问地址：http://localhost:8080/api/swagger-ui.html
 */
@Configuration
public class SwaggerConfig {

    private static final String SECURITY_SCHEME = "JWT";

    @Bean
    public OpenAPI blogOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("博客后端系统 API 文档")
                        .description("一套成熟的博客后端：邮箱登录注册、忘记/重置密码、RBAC 权限、" +
                                "文章/分类/标签/评论/友链增删改查、后台管理。\n\n" +
                                "**鉴权说明**：先调用登录接口获取 token，点击右上角 “Authorize” 按钮填入即可访问受保护接口。")
                        .version("1.0.0")
                        .contact(new Contact().name("Blog Server").email("admin@blog.com"))
                        .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0")))
                // 全局 JWT 鉴权
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME))
                .components(new Components().addSecuritySchemes(SECURITY_SCHEME,
                        new SecurityScheme()
                                .name(SECURITY_SCHEME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("请输入登录获取的 JWT 令牌（无需手动加 Bearer 前缀）")));
    }
}
