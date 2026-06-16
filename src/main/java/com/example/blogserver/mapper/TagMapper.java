package com.example.blogserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.blogserver.entity.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 标签 Mapper
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

    /**
     * 查询文章关联的标签
     */
    @Select("SELECT t.* FROM tag t JOIN article_tag at ON t.id = at.tag_id " +
            "WHERE at.article_id = #{articleId} AND t.deleted = 0")
    List<Tag> selectByArticleId(Long articleId);
}
