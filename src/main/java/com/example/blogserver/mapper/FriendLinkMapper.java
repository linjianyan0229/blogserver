package com.example.blogserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.blogserver.entity.FriendLink;
import org.apache.ibatis.annotations.Mapper;

/**
 * 友链 Mapper
 */
@Mapper
public interface FriendLinkMapper extends BaseMapper<FriendLink> {
}
