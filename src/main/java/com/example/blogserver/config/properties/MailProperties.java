package com.example.blogserver.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 邮件业务配置（前缀 blog.mail）
 */
@Data
@Component
@ConfigurationProperties(prefix = "blog.mail")
public class MailProperties {

    /** 发件人显示名 */
    private String fromName = "博客系统";

    /** 验证码有效期（分钟） */
    private Integer codeExpireMinutes = 5;
}
