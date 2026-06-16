package com.example.blogserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.blogserver.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * 文章 Mapper
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 访问数 +1（原子自增）
     */
    @Update("UPDATE article SET view_count = view_count + 1 WHERE id = #{id}")
    int incrViewCount(Long id);

    /**
     * 点赞数 +1
     */
    @Update("UPDATE article SET like_count = like_count + 1 WHERE id = #{id}")
    int incrLikeCount(Long id);

    /**
     * 评论数增减
     */
    @Update("UPDATE article SET comment_count = comment_count + #{delta} WHERE id = #{id}")
    int updateCommentCount(Long id, int delta);
}
