package com.example.blogserver.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.blogserver.common.PageResult;
import com.example.blogserver.common.ResultCode;
import com.example.blogserver.common.exception.BusinessException;
import com.example.blogserver.dto.article.ArticleQueryDTO;
import com.example.blogserver.dto.article.ArticleSaveDTO;
import com.example.blogserver.entity.*;
import com.example.blogserver.mapper.*;
import com.example.blogserver.security.SecurityUtils;
import com.example.blogserver.util.MarkdownUtil;
import com.example.blogserver.vo.ArticleDetailVO;
import com.example.blogserver.vo.ArticleListVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 文章服务：公开浏览 + 后台管理
 */
@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleMapper articleMapper;
    private final ArticleTagMapper articleTagMapper;
    private final TagMapper tagMapper;
    private final CategoryMapper categoryMapper;
    private final UserMapper userMapper;

    // ==================== 公开接口 ====================

    /**
     * 公开文章分页（仅已发布，列表对游客与用户均可见）
     */
    public PageResult<ArticleListVO> publicPage(ArticleQueryDTO query) {
        return doPage(query, 1);
    }

    /**
     * 文章详情：公开文章游客可看；非公开文章需登录
     */
    @Transactional(rollbackFor = Exception.class)
    public ArticleDetailVO getPublicDetail(Long id) {
        Article article = articleMapper.selectById(id);
        if (article == null || article.getStatus() == null || article.getStatus() != 1) {
            throw new BusinessException(ResultCode.RESOURCE_NOT_EXIST);
        }
        boolean isPublic = article.getIsPublic() != null && article.getIsPublic() == 1;
        if (!isPublic && !SecurityUtils.isAuthenticated()) {
            throw new BusinessException(ResultCode.ARTICLE_NOT_PUBLIC);
        }
        // 访问数 +1
        articleMapper.incrViewCount(id);
        article.setViewCount((article.getViewCount() == null ? 0 : article.getViewCount()) + 1);
        return toDetailVO(article);
    }

    /**
     * 点赞，返回最新点赞数
     */
    public Integer like(Long id) {
        Article article = articleMapper.selectById(id);
        if (article == null) {
            throw new BusinessException(ResultCode.RESOURCE_NOT_EXIST);
        }
        articleMapper.incrLikeCount(id);
        return (article.getLikeCount() == null ? 0 : article.getLikeCount()) + 1;
    }

    // ==================== 后台接口 ====================

    /**
     * 后台文章分页（含草稿）
     */
    public PageResult<ArticleListVO> adminPage(ArticleQueryDTO query) {
        return doPage(query, null);
    }

    /**
     * 后台获取文章（含正文与标签ID，供编辑回显）
     */
    public ArticleDetailVO getByIdForEdit(Long id) {
        Article article = articleMapper.selectById(id);
        if (article == null) {
            throw new BusinessException(ResultCode.RESOURCE_NOT_EXIST);
        }
        return toDetailVO(article);
    }

    /**
     * 新增或修改文章
     */
    @Transactional(rollbackFor = Exception.class)
    public Long save(ArticleSaveDTO dto, Long authorId) {
        Article article = new Article();
        BeanUtils.copyProperties(dto, article, "tagIds");
        if (dto.getId() == null) {
            article.setAuthorId(authorId);
            article.setViewCount(0);
            article.setLikeCount(0);
            article.setCommentCount(0);
            articleMapper.insert(article);
        } else {
            articleMapper.updateById(article);
            // 清除旧标签关联
            articleTagMapper.delete(Wrappers.<ArticleTag>lambdaQuery().eq(ArticleTag::getArticleId, dto.getId()));
        }
        // 重建标签关联
        if (dto.getTagIds() != null) {
            for (Long tagId : dto.getTagIds()) {
                articleTagMapper.insert(new ArticleTag(article.getId(), tagId));
            }
        }
        return article.getId();
    }

    /**
     * 删除文章（逻辑删除 + 解除标签关联）
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        if (articleMapper.selectById(id) == null) {
            throw new BusinessException(ResultCode.RESOURCE_NOT_EXIST);
        }
        articleMapper.deleteById(id);
        articleTagMapper.delete(Wrappers.<ArticleTag>lambdaQuery().eq(ArticleTag::getArticleId, id));
    }

    // ==================== 私有方法 ====================

    /**
     * @param onlyStatus 限定状态（1=仅已发布，null=全部）
     */
    private PageResult<ArticleListVO> doPage(ArticleQueryDTO query, Integer onlyStatus) {
        // 标签过滤：先取该标签下的文章ID
        List<Long> articleIdsByTag = null;
        if (query.getTagId() != null) {
            articleIdsByTag = articleTagMapper.selectList(
                            Wrappers.<ArticleTag>lambdaQuery().eq(ArticleTag::getTagId, query.getTagId()))
                    .stream().map(ArticleTag::getArticleId).collect(Collectors.toList());
            if (articleIdsByTag.isEmpty()) {
                return PageResult.empty(query.getPage(), query.getSize());
            }
        }

        LambdaQueryWrapper<Article> wrapper = Wrappers.lambdaQuery();
        if (onlyStatus != null) {
            wrapper.eq(Article::getStatus, onlyStatus);
        } else if (query.getStatus() != null) {
            wrapper.eq(Article::getStatus, query.getStatus());
        }
        if (query.getCategoryId() != null) {
            wrapper.eq(Article::getCategoryId, query.getCategoryId());
        }
        if (articleIdsByTag != null) {
            wrapper.in(Article::getId, articleIdsByTag);
        }
        if (StringUtils.hasText(query.getKeyword())) {
            wrapper.and(w -> w.like(Article::getTitle, query.getKeyword())
                    .or().like(Article::getSummary, query.getKeyword()));
        }
        // 置顶优先，再按时间倒序
        wrapper.orderByDesc(Article::getTop).orderByDesc(Article::getCreateTime);

        IPage<Article> page = articleMapper.selectPage(new Page<>(query.getPage(), query.getSize()), wrapper);
        List<ArticleListVO> records = enrichList(page.getRecords());
        return PageResult.of(page, records);
    }

    /**
     * 批量填充分类名、作者、标签，避免 N+1
     */
    private List<ArticleListVO> enrichList(List<Article> articles) {
        if (articles.isEmpty()) {
            return Collections.emptyList();
        }
        Map<Long, String> categoryNames = loadCategoryNames(articles.stream()
                .map(Article::getCategoryId).filter(Objects::nonNull).collect(Collectors.toSet()));
        Map<Long, User> authors = loadAuthors(articles.stream()
                .map(Article::getAuthorId).filter(Objects::nonNull).collect(Collectors.toSet()));

        return articles.stream().map(a -> {
            ArticleListVO vo = new ArticleListVO();
            BeanUtils.copyProperties(a, vo);
            vo.setCategoryName(categoryNames.get(a.getCategoryId()));
            User author = authors.get(a.getAuthorId());
            if (author != null) {
                vo.setAuthorName(author.getNickname());
                vo.setAuthorAvatar(author.getAvatar());
            }
            vo.setTags(tagMapper.selectByArticleId(a.getId()).stream().map(Tag::getName).collect(Collectors.toList()));
            return vo;
        }).collect(Collectors.toList());
    }

    private ArticleDetailVO toDetailVO(Article article) {
        ArticleDetailVO vo = new ArticleDetailVO();
        BeanUtils.copyProperties(article, vo);
        // 分类名
        if (article.getCategoryId() != null) {
            Category category = categoryMapper.selectById(article.getCategoryId());
            if (category != null) {
                vo.setCategoryName(category.getName());
            }
        }
        // 作者
        if (article.getAuthorId() != null) {
            User author = userMapper.selectById(article.getAuthorId());
            if (author != null) {
                vo.setAuthorName(author.getNickname());
                vo.setAuthorAvatar(author.getAvatar());
            }
        }
        // 标签
        vo.setTags(tagMapper.selectByArticleId(article.getId()).stream().map(Tag::getName).collect(Collectors.toList()));
        // 目录
        vo.setToc(MarkdownUtil.extractToc(article.getContent()));
        return vo;
    }

    private Map<Long, String> loadCategoryNames(Set<Long> ids) {
        if (ids.isEmpty()) {
            return Collections.emptyMap();
        }
        return categoryMapper.selectByIds(ids).stream()
                .collect(Collectors.toMap(Category::getId, Category::getName));
    }

    private Map<Long, User> loadAuthors(Set<Long> ids) {
        if (ids.isEmpty()) {
            return Collections.emptyMap();
        }
        return userMapper.selectByIds(ids).stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));
    }
}
