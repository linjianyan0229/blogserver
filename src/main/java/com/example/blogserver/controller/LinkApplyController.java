package com.example.blogserver.controller;

import com.example.blogserver.common.Result;
import com.example.blogserver.dto.FriendLinkApplyDTO;
import com.example.blogserver.entity.FriendLinkApply;
import com.example.blogserver.mapper.FriendLinkApplyMapper;
import com.example.blogserver.security.SecurityUtils;
import com.example.blogserver.service.FriendLinkApplyService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 友链申请（需登录，用户可申请加入友链）
 */
@Tag(name = "06. 友链申请", description = "用户提交友链申请、查看自己的申请记录")
@RestController
@RequestMapping("/link-apply")
@RequiredArgsConstructor
public class LinkApplyController {

    private final FriendLinkApplyService linkApplyService;
    private final FriendLinkApplyMapper applyMapper;

    @Operation(summary = "提交友链申请", description = "申请加入友链，提交后进入待审核，由管理员审核通过后展示")
    @PostMapping
    public Result<Long> apply(@Valid @RequestBody FriendLinkApplyDTO dto) {
        return Result.success("申请已提交，请等待审核", linkApplyService.apply(dto, SecurityUtils.getUserId()));
    }

    @Operation(summary = "我的友链申请记录", description = "查看当前用户提交过的友链申请及其审核状态")
    @GetMapping("/mine")
    public Result<List<FriendLinkApply>> mine() {
        return Result.success(applyMapper.selectList(Wrappers.<FriendLinkApply>lambdaQuery()
                .eq(FriendLinkApply::getApplyUserId, SecurityUtils.getUserId())
                .orderByDesc(FriendLinkApply::getCreateTime)));
    }
}
