package com.example.blogserver.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.blogserver.common.ResultCode;
import com.example.blogserver.common.exception.BusinessException;
import com.example.blogserver.dto.CategorySaveDTO;
import com.example.blogserver.entity.Category;
import com.example.blogserver.mapper.ArticleMapper;
import com.example.blogserver.mapper.CategoryMapper;
import com.example.blogserver.entity.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 分类服务
 */
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryMapper categoryMapper;
    private final ArticleMapper articleMapper;

    /**
     * 全部分类（按排序）
     */
    public List<Category> list() {
        return categoryMapper.selectList(Wrappers.<Category>lambdaQuery().orderByAsc(Category::getSort));
    }

    public void save(CategorySaveDTO dto) {
        boolean nameExists = categoryMapper.exists(Wrappers.<Category>lambdaQuery()
                .eq(Category::getName, dto.getName())
                .ne(dto.getId() != null, Category::getId, dto.getId()));
        if (nameExists) {
            throw new BusinessException("分类名称已存在");
        }
        Category category = new Category();
        BeanUtils.copyProperties(dto, category);
        if (dto.getId() == null) {
            categoryMapper.insert(category);
        } else {
            categoryMapper.updateById(category);
        }
    }

    public void delete(Long id) {
        long count = articleMapper.selectCount(Wrappers.<Article>lambdaQuery().eq(Article::getCategoryId, id));
        if (count > 0) {
            throw new BusinessException("该分类下存在文章，无法删除");
        }
        if (categoryMapper.deleteById(id) == 0) {
            throw new BusinessException(ResultCode.RESOURCE_NOT_EXIST);
        }
    }
}
