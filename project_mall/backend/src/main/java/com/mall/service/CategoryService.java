package com.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.entity.Category;

import java.util.List;

/**
 * 分类服务接口
 */
public interface CategoryService extends IService<Category> {

    /**
     * 获取分类树形结构
     */
    List<Category> getCategoryTree();

    /**
     * 添加分类
     */
    boolean addCategory(Category category);

    /**
     * 更新分类
     */
    boolean updateCategory(Category category);

    /**
     * 删除分类
     */
    boolean deleteCategory(Long id);
} 