package com.example.blogserver.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.blogserver.common.ResultCode;
import com.example.blogserver.common.exception.BusinessException;
import com.example.blogserver.dto.FriendLinkSaveDTO;
import com.example.blogserver.entity.FriendLink;
import com.example.blogserver.mapper.FriendLinkMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 友链服务
 */
@Service
@RequiredArgsConstructor
public class FriendLinkService {

    private final FriendLinkMapper friendLinkMapper;

    /**
     * 公开友链（仅显示中，按排序）
     */
    public List<FriendLink> publicList() {
        return friendLinkMapper.selectList(Wrappers.<FriendLink>lambdaQuery()
                .eq(FriendLink::getStatus, 1)
                .orderByAsc(FriendLink::getSort));
    }

    /**
     * 后台全部友链
     */
    public List<FriendLink> adminList() {
        return friendLinkMapper.selectList(Wrappers.<FriendLink>lambdaQuery().orderByAsc(FriendLink::getSort));
    }

    public void save(FriendLinkSaveDTO dto) {
        FriendLink link = new FriendLink();
        BeanUtils.copyProperties(dto, link);
        if (dto.getId() == null) {
            friendLinkMapper.insert(link);
        } else {
            friendLinkMapper.updateById(link);
        }
    }

    public void delete(Long id) {
        if (friendLinkMapper.deleteById(id) == 0) {
            throw new BusinessException(ResultCode.RESOURCE_NOT_EXIST);
        }
    }
}
