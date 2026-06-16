package com.example.blogserver.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 文件上传配置（前缀 blog.upload）
 */
@Data
@Component
@ConfigurationProperties(prefix = "blog.upload")
public class UploadProperties {

    /** 本地存储根目录 */
    private String path = "./uploads";

    /** 访问 URL 前缀（不含 context-path），与静态资源映射一致 */
    private String urlPrefix = "/uploads";

    /** 单文件最大字节数，默认 5MB */
    private Long maxSize = 5 * 1024 * 1024L;

    /** 允许的图片扩展名 */
    private List<String> allowedExt = List.of("jpg", "jpeg", "png", "gif", "webp", "bmp");
}
