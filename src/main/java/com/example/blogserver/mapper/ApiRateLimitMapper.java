package com.example.blogserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.blogserver.entity.ApiRateLimit;
import org.apache.ibatis.annotations.Mapper;

/**
 * 接口限流规则 Mapper
 */
@Mapper
public interface ApiRateLimitMapper extends BaseMapper<ApiRateLimit> {
}
