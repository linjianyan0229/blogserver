package com.example.blogserver.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 文章-标签关联实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("article_tag")
public class ArticleTag implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long articleId;

    private Long tagId;

    public ArticleTag(Long articleId, Long tagId) {
        this.articleId = articleId;
        this.tagId = tagId;
    }
}
