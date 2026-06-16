package com.example.blogserver.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT 配置（前缀 blog.jwt）
 */
@Data
@Component
@ConfigurationProperties(prefix = "blog.jwt")
public class JwtProperties {

    /** HS256 密钥 */
    private String secret;

    /** 有效期（毫秒） */
    private Long expire;

    /** 请求头名称 */
    private String header = "Authorization";

    /** 令牌前缀 */
    private String tokenPrefix = "Bearer ";
}
