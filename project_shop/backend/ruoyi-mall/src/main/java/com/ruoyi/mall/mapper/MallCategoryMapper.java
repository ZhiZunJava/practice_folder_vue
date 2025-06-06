package com.ruoyi.mall.mapper;

import java.util.List;
import com.ruoyi.mall.domain.MallCategory;

/**
 * 商品分类Mapper接口
 * 
 * @author ruoyi
 * @date 2025-01-15
 */
public interface MallCategoryMapper 
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
     * 删除商品分类
     * 
     * @param categoryId 商品分类主键
     * @return 结果
     */
    public int deleteMallCategoryByCategoryId(Long categoryId);

    /**
     * 批量删除商品分类
     * 
     * @param categoryIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMallCategoryByCategoryIds(Long[] categoryIds);

    /**
     * 查询子分类数量
     * 
     * @param parentId 父分类ID
     * @return 结果
     */
    public int selectChildCategoryCount(Long parentId);

    /**
     * 是否存在子节点
     * 
     * @param categoryId 分类ID
     * @return 结果
     */
    public int hasChildByCategoryId(Long categoryId);
} 