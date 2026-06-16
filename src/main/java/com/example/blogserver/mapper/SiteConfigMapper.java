package com.example.blogserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.blogserver.entity.SiteConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * 站点配置 Mapper
 */
@Mapper
public interface SiteConfigMapper extends BaseMapper<SiteConfig> {
}
