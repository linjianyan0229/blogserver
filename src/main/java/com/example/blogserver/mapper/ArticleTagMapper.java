package com.example.blogserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.blogserver.entity.ArticleTag;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文章标签关联 Mapper
 */
@Mapper
public interface ArticleTagMapper extends BaseMapper<ArticleTag> {
}
