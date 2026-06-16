package com.example.blogserver.service;

import com.example.blogserver.common.exception.BusinessException;
import com.example.blogserver.config.properties.UploadProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 文件上传服务（本地存储，按日期分目录）
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final UploadProperties uploadProperties;
    private final Environment environment;

    /**
     * 上传图片，返回可访问的相对 URL（含 context-path）
     */
    public String uploadImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("上传文件不能为空");
        }
        if (file.getSize() > uploadProperties.getMaxSize()) {
            throw new BusinessException("文件大小超过限制（最大 "
                    + (uploadProperties.getMaxSize() / 1024 / 1024) + "MB）");
        }
        String original = StringUtils.cleanPath(
                file.getOriginalFilename() == null ? "" : file.getOriginalFilename());
        String ext = StringUtils.getFilenameExtension(original);
        if (ext == null || !uploadProperties.getAllowedExt().contains(ext.toLowerCase())) {
            throw new BusinessException("仅支持图片格式：" + uploadProperties.getAllowedExt());
        }

        // 按 yyyy/MM 分目录
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM"));
        String filename = UUID.randomUUID().toString().replace("-", "") + "." + ext.toLowerCase();
        Path dir = Paths.get(uploadProperties.getPath()).toAbsolutePath().normalize().resolve(datePath);
        try {
            Files.createDirectories(dir);
            Path target = dir.resolve(filename);
            file.transferTo(target);
            log.info("文件上传成功：{}", target);
        } catch (IOException e) {
            log.error("文件上传失败：{}", e.getMessage(), e);
            throw new BusinessException("文件上传失败：" + e.getMessage());
        }

        String contextPath = environment.getProperty("server.servlet.context-path", "");
        // 形如 /api/uploads/2026/06/xxx.png
        return contextPath + uploadProperties.getUrlPrefix() + "/" + datePath + "/" + filename;
    }
}
