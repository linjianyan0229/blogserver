package com.example.blogserver.controller;

import com.example.blogserver.common.Result;
import com.example.blogserver.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传（需登录）
 */
@Tag(name = "05. 文件上传", description = "上传图片（缩略图、顶部背景、头像等）")
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @Operation(summary = "上传图片", description = "返回可直接访问的图片URL，用于文章缩略图/顶部背景、用户头像等")
    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        return Result.success("上传成功", fileService.uploadImage(file));
    }
}
