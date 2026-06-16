package com.example.blogserver.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 评论（含子回复，树形结构）
 */
@Data
@Schema(description = "评论")
public class CommentVO {

    @Schema(description = "评论ID")
    private Long id;

    @Schema(description = "文章ID")
    private Long articleId;

    @Schema(description = "评论用户ID")
    private Long userId;

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "用户头像")
    private String avatar;

    @Schema(description = "评论内容")
    private String content;

    @Schema(description = "父评论ID")
    private Long parentId;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "子回复列表")
    private List<CommentVO> children;
}
