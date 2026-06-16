package com.example.blogserver.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.blogserver.common.ResultCode;
import com.example.blogserver.common.exception.BusinessException;
import com.example.blogserver.dto.TagSaveDTO;
import com.example.blogserver.entity.ArticleTag;
import com.example.blogserver.entity.Tag;
import com.example.blogserver.mapper.ArticleTagMapper;
import com.example.blogserver.mapper.TagMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 标签服务
 */
@Service
@RequiredArgsConstructor
public class TagService {

    private final TagMapper tagMapper;
    private final ArticleTagMapper articleTagMapper;

    public List<Tag> list() {
        return tagMapper.selectList(Wrappers.<Tag>lambdaQuery().orderByDesc(Tag::getId));
    }

    public void save(TagSaveDTO dto) {
        boolean nameExists = tagMapper.exists(Wrappers.<Tag>lambdaQuery()
                .eq(Tag::getName, dto.getName())
                .ne(dto.getId() != null, Tag::getId, dto.getId()));
        if (nameExists) {
            throw new BusinessException("标签名称已存在");
        }
        Tag tag = new Tag();
        BeanUtils.copyProperties(dto, tag);
        if (dto.getId() == null) {
            tagMapper.insert(tag);
        } else {
            tagMapper.updateById(tag);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        if (tagMapper.deleteById(id) == 0) {
            throw new BusinessException(ResultCode.RESOURCE_NOT_EXIST);
        }
        // 解除文章关联
        articleTagMapper.delete(Wrappers.<ArticleTag>lambdaQuery().eq(ArticleTag::getTagId, id));
    }
}
