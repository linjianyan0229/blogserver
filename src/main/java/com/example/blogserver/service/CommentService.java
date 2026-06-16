package com.example.blogserver.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.blogserver.common.PageResult;
import com.example.blogserver.common.ResultCode;
import com.example.blogserver.common.exception.BusinessException;
import com.example.blogserver.dto.PageQuery;
import com.example.blogserver.dto.comment.CommentCreateDTO;
import com.example.blogserver.entity.Comment;
import com.example.blogserver.entity.User;
import com.example.blogserver.mapper.ArticleMapper;
import com.example.blogserver.mapper.CommentMapper;
import com.example.blogserver.mapper.UserMapper;
import com.example.blogserver.vo.CommentVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 评论服务
 */
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentMapper commentMapper;
    private final ArticleMapper articleMapper;
    private final UserMapper userMapper;

    /**
     * 查询某文章的评论（树形）
     */
    public List<CommentVO> listByArticle(Long articleId) {
        List<Comment> comments = commentMapper.selectList(Wrappers.<Comment>lambdaQuery()
                .eq(Comment::getArticleId, articleId)
                .eq(Comment::getStatus, 1)
                .orderByAsc(Comment::getCreateTime));
        if (comments.isEmpty()) {
            return Collections.emptyList();
        }
        Map<Long, User> userMap = loadUsers(comments.stream().map(Comment::getUserId).collect(Collectors.toSet()));
        List<CommentVO> all = comments.stream().map(c -> toVO(c, userMap)).collect(Collectors.toList());
        return buildTree(all);
    }

    /**
     * 发表评论
     */
    @Transactional(rollbackFor = Exception.class)
    public Long add(CommentCreateDTO dto, Long userId) {
        if (articleMapper.selectById(dto.getArticleId()) == null) {
            throw new BusinessException(ResultCode.RESOURCE_NOT_EXIST);
        }
        Comment comment = new Comment();
        comment.setArticleId(dto.getArticleId());
        comment.setUserId(userId);
        comment.setContent(dto.getContent());
        comment.setParentId(dto.getParentId() == null ? 0L : dto.getParentId());
        comment.setStatus(1);
        commentMapper.insert(comment);
        articleMapper.updateCommentCount(dto.getArticleId(), 1);
        return comment.getId();
    }

    /**
     * 用户删除自己的评论
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteOwn(Long id, Long userId) {
        Comment comment = commentMapper.selectById(id);
        if (comment == null) {
            throw new BusinessException(ResultCode.RESOURCE_NOT_EXIST);
        }
        if (!Objects.equals(comment.getUserId(), userId)) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
        commentMapper.deleteById(id);
        articleMapper.updateCommentCount(comment.getArticleId(), -1);
    }

    /**
     * 后台分页查询评论
     */
    public PageResult<CommentVO> adminPage(PageQuery query) {
        IPage<Comment> page = commentMapper.selectPage(new Page<>(query.getPage(), query.getSize()),
                Wrappers.<Comment>lambdaQuery().orderByDesc(Comment::getCreateTime));
        Map<Long, User> userMap = loadUsers(page.getRecords().stream()
                .map(Comment::getUserId).collect(Collectors.toSet()));
        List<CommentVO> records = page.getRecords().stream().map(c -> toVO(c, userMap)).collect(Collectors.toList());
        return PageResult.of(page, records);
    }

    /**
     * 后台删除评论
     */
    @Transactional(rollbackFor = Exception.class)
    public void adminDelete(Long id) {
        Comment comment = commentMapper.selectById(id);
        if (comment == null) {
            throw new BusinessException(ResultCode.RESOURCE_NOT_EXIST);
        }
        commentMapper.deleteById(id);
        articleMapper.updateCommentCount(comment.getArticleId(), -1);
    }

    // ==================== 私有方法 ====================

    private CommentVO toVO(Comment c, Map<Long, User> userMap) {
        CommentVO vo = new CommentVO();
        BeanUtils.copyProperties(c, vo);
        User user = userMap.get(c.getUserId());
        if (user != null) {
            vo.setNickname(user.getNickname());
            vo.setAvatar(user.getAvatar());
        }
        return vo;
    }

    /**
     * 平铺列表构建为两级树（顶级评论 + 其回复）
     */
    private List<CommentVO> buildTree(List<CommentVO> all) {
        Map<Long, List<CommentVO>> childrenMap = all.stream()
                .filter(c -> c.getParentId() != null && c.getParentId() != 0)
                .collect(Collectors.groupingBy(CommentVO::getParentId));
        List<CommentVO> roots = new ArrayList<>();
        for (CommentVO c : all) {
            if (c.getParentId() == null || c.getParentId() == 0) {
                c.setChildren(childrenMap.getOrDefault(c.getId(), new ArrayList<>()));
                roots.add(c);
            }
        }
        return roots;
    }

    private Map<Long, User> loadUsers(Set<Long> ids) {
        if (ids.isEmpty()) {
            return Collections.emptyMap();
        }
        return userMapper.selectByIds(ids).stream().collect(Collectors.toMap(User::getId, u -> u));
    }
}
