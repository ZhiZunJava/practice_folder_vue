package com.ruoyi.mall.service;

import java.util.List;
import com.ruoyi.mall.domain.MallCategory;

/**
 * 商品分类Service接口
 * 
 * @author ruoyi
 * @date 2025-01-15
 */
public interface IMallCategoryService 
{
    /**
     * 查询商品分类
     * 
     * @param categoryId 商品分类主键
     * @return 商品分类
     */
    public MallCategory selectMallCategoryByCategoryId(Long categoryId);

    /**
     * 查询商品分类列表
     * 
     * @param mallCategory 商品分类
     * @return 商品分类集合
     */
    public List<MallCategory> selectMallCategoryList(MallCategory mallCategory);

    /**
     * 新增商品分类
     * 
     * @param mallCategory 商品分类
     * @return 结果
     */
    public int insertMallCategory(MallCategory mallCategory);

    /**
     * 修改商品分类
     * 
     * @param mallCategory 商品分类
     * @return 结果
     */
    public int updateMallCategory(MallCategory mallCategory);

    /**
     * 批量删除商品分类
     * 
     * @param categoryIds 需要删除的商品分类主键集合
     * @return 结果
     */
    public int deleteMallCategoryByCategoryIds(Long[] categoryIds);

    /**
     * 删除商品分类信息
     * 
     * @param categoryId 商品分类主键
     * @return 结果
     */
    public int deleteMallCategoryByCategoryId(Long categoryId);

    /**
     * 构建前端所需要树结构
     * 
     * @param categorys 分类列表
     * @return 树结构列表
     */
    public List<MallCategory> buildCategoryTree(List<MallCategory> categorys);

    /**
     * 查询分类树结构信息
     * 
     * @param mallCategory 分类信息
     * @return 分类树信息集合
     */
    public List<MallCategory> selectCategoryTreeList(MallCategory mallCategory);

    /**
     * 是否存在子节点
     * 
     * @param categoryId 分类ID
     * @return 结果
     */
    public boolean hasChildByCategoryId(Long categoryId);
} 