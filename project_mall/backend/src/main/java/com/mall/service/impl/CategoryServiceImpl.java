package com.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.entity.Category;
import com.mall.mapper.CategoryMapper;
import com.mall.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 分类服务实现类
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Override
    public List<Category> getCategoryTree() {
        List<Category> allCategories = baseMapper.selectCategoryTree();
        return buildCategoryTree(allCategories, 0L);
    }

    /**
     * 构建分类树
     */
    private List<Category> buildCategoryTree(List<Category> allCategories, Long parentId) {
        return allCategories.stream()
                .filter(category -> category.getParentId().equals(parentId))
                .peek(category -> {
                    List<Category> children = buildCategoryTree(allCategories, category.getId());
                    category.setChildren(children);
                })
                .collect(Collectors.toList());
    }

    @Override
    public boolean addCategory(Category category) {
        // 设置默认值
        if (category.getParentId() == null) {
            category.setParentId(0L);
        }
        if (category.getSort() == null) {
            category.setSort(0);
        }
        if (category.getStatus() == null) {
            category.setStatus(1);
        }
        
        return save(category);
    }

    @Override
    public boolean updateCategory(Category category) {
        return updateById(category);
    }

    @Override
    public boolean deleteCategory(Long id) {
        // 检查是否有子分类
        long count = count(new LambdaQueryWrapper<Category>()
                .eq(Category::getParentId, id));
        
        if (count > 0) {
            throw new RuntimeException("该分类下存在子分类，无法删除");
        }
        
        return removeById(id);
    }
} 