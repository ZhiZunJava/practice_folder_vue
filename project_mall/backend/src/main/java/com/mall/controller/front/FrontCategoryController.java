package com.mall.controller.front;

import com.mall.common.Result;
import com.mall.entity.Category;
import com.mall.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 前台分类控制器
 */
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class FrontCategoryController {

    private final CategoryService categoryService;

    /**
     * 获取分类树
     */
    @GetMapping("/tree")
    public Result<List<Category>> getCategoryTree() {
        try {
            List<Category> categoryTree = categoryService.getCategoryTree();
            return Result.success(categoryTree);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取所有分类列表
     */
    @GetMapping
    public Result<List<Category>> getCategoryList() {
        try {
            List<Category> categories = categoryService.list();
            return Result.success(categories);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
} 