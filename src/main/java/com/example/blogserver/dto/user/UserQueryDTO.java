package com.example.blogserver.dto.user;

import com.example.blogserver.dto.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户查询（后台）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户查询参数")
public class UserQueryDTO extends PageQuery {

    @Schema(description = "关键词（用户名/邮箱/昵称模糊）")
    private String keyword;

    @Schema(description = "状态：0禁用 1正常")
    private Integer status;
}
