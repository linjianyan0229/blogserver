package com.example.blogserver.config;

import com.example.blogserver.config.properties.UploadProperties;
import com.example.blogserver.interceptor.RateLimitInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.nio.file.Paths;

/**
 * Web MVC 配置：静态资源映射 + 限流拦截器
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final UploadProperties uploadProperties;
    private final RateLimitInterceptor rateLimitInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 绝对路径，结尾加分隔符
        String location = Paths.get(uploadProperties.getPath()).toAbsolutePath().normalize()
                + File.separator;
        registry.addResourceHandler(uploadProperties.getUrlPrefix() + "/**")
                .addResourceLocations("file:" + location);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rateLimitInterceptor)
                .addPathPatterns("/**")
                // 静态资源与文档不限流
                .excludePathPatterns(uploadProperties.getUrlPrefix() + "/**",
                        "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**");
    }
}
