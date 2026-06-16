package com.example.blogserver.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.blogserver.common.PageResult;
import com.example.blogserver.common.ResultCode;
import com.example.blogserver.common.exception.BusinessException;
import com.example.blogserver.dto.FriendLinkApplyDTO;
import com.example.blogserver.entity.FriendLink;
import com.example.blogserver.entity.FriendLinkApply;
import com.example.blogserver.mapper.FriendLinkApplyMapper;
import com.example.blogserver.mapper.FriendLinkMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 友链申请服务：用户申请 + 后台审核
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FriendLinkApplyService {

    public static final String PENDING = "PENDING";
    public static final String APPROVED = "APPROVED";
    public static final String REJECTED = "REJECTED";

    private final FriendLinkApplyMapper applyMapper;
    private final FriendLinkMapper friendLinkMapper;

    /**
     * 用户提交友链申请
     */
    public Long apply(FriendLinkApplyDTO dto, Long userId) {
        FriendLinkApply apply = new FriendLinkApply();
        BeanUtils.copyProperties(dto, apply);
        apply.setApplyUserId(userId);
        apply.setStatus(PENDING);
        applyMapper.insert(apply);
        log.info("收到友链申请：{}（{}）申请人ID={}", dto.getName(), dto.getUrl(), userId);
        return apply.getId();
    }

    /**
     * 后台分页查询申请（可按状态过滤）
     */
    public PageResult<FriendLinkApply> adminPage(String status, Long page, Long size) {
        IPage<FriendLinkApply> result = applyMapper.selectPage(new Page<>(page, size),
                Wrappers.<FriendLinkApply>lambdaQuery()
                        .eq(StringUtils.hasText(status), FriendLinkApply::getStatus, status)
                        .orderByDesc(FriendLinkApply::getCreateTime));
        return PageResult.of(result);
    }

    /**
     * 审核通过：标记 APPROVED 并自动写入友链（显示中）
     */
    @Transactional(rollbackFor = Exception.class)
    public void approve(Long id) {
        FriendLinkApply apply = getPending(id);
        // 写入正式友链
        FriendLink link = new FriendLink();
        link.setName(apply.getName());
        link.setUrl(apply.getUrl());
        link.setLogo(apply.getLogo());
        link.setDescription(apply.getDescription());
        link.setSort(0);
        link.setStatus(1);
        friendLinkMapper.insert(link);
        // 更新申请状态
        FriendLinkApply update = new FriendLinkApply();
        update.setId(id);
        update.setStatus(APPROVED);
        applyMapper.updateById(update);
        log.info("友链申请已通过并加入友链：{}（friendLinkId={}）", apply.getName(), link.getId());
    }

    /**
     * 审核拒绝
     */
    @Transactional(rollbackFor = Exception.class)
    public void reject(Long id, String remark) {
        getPending(id);
        FriendLinkApply update = new FriendLinkApply();
        update.setId(id);
        update.setStatus(REJECTED);
        update.setAuditRemark(remark);
        applyMapper.updateById(update);
        log.info("友链申请已拒绝：id={}，原因：{}", id, remark);
    }

    private FriendLinkApply getPending(Long id) {
        FriendLinkApply apply = applyMapper.selectById(id);
        if (apply == null) {
            throw new BusinessException(ResultCode.RESOURCE_NOT_EXIST);
        }
        if (!PENDING.equals(apply.getStatus())) {
            throw new BusinessException("该申请已审核，请勿重复操作");
        }
        return apply;
    }
}
