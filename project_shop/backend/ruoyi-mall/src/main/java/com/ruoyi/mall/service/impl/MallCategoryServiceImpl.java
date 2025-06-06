package com.ruoyi.mall.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.mall.mapper.MallCategoryMapper;
import com.ruoyi.mall.domain.MallCategory;
import com.ruoyi.mall.service.IMallCategoryService;

/**
 * 商品分类Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-01-15
 */
@Service
public class MallCategoryServiceImpl implements IMallCategoryService 
{
    @Autowired
    private MallCategoryMapper mallCategoryMapper;

    /**
     * 查询商品分类
     * 
     * @param categoryId 商品分类主键
     * @return 商品分类
     */
    @Override
    public MallCategory selectMallCategoryByCategoryId(Long categoryId)
    {
        return mallCategoryMapper.selectMallCategoryByCategoryId(categoryId);
    }

    /**
     * 查询商品分类列表
     * 
     * @param mallCategory 商品分类
     * @return 商品分类
     */
    @Override
    public List<MallCategory> selectMallCategoryList(MallCategory mallCategory)
    {
        return mallCategoryMapper.selectMallCategoryList(mallCategory);
    }

    /**
     * 新增商品分类
     * 
     * @param mallCategory 商品分类
     * @return 结果
     */
    @Override
    public int insertMallCategory(MallCategory mallCategory)
    {
        mallCategory.setCreateTime(DateUtils.getNowDate());
        return mallCategoryMapper.insertMallCategory(mallCategory);
    }

    /**
     * 修改商品分类
     * 
     * @param mallCategory 商品分类
     * @return 结果
     */
    @Override
    public int updateMallCategory(MallCategory mallCategory)
    {
        mallCategory.setUpdateTime(DateUtils.getNowDate());
        return mallCategoryMapper.updateMallCategory(mallCategory);
    }

    /**
     * 批量删除商品分类
     * 
     * @param categoryIds 需要删除的商品分类主键
     * @return 结果
     */
    @Override
    public int deleteMallCategoryByCategoryIds(Long[] categoryIds)
    {
        return mallCategoryMapper.deleteMallCategoryByCategoryIds(categoryIds);
    }

    /**
     * 删除商品分类信息
     * 
     * @param categoryId 商品分类主键
     * @return 结果
     */
    @Override
    public int deleteMallCategoryByCategoryId(Long categoryId)
    {
        return mallCategoryMapper.deleteMallCategoryByCategoryId(categoryId);
    }

    /**
     * 构建前端所需要树结构
     * 
     * @param categorys 分类列表
     * @return 树结构列表
     */
    @Override
    public List<MallCategory> buildCategoryTree(List<MallCategory> categorys)
    {
        List<MallCategory> returnList = new ArrayList<MallCategory>();
        List<Long> tempList = categorys.stream().map(MallCategory::getCategoryId).collect(Collectors.toList());
        for (Iterator<MallCategory> iterator = categorys.iterator(); iterator.hasNext();)
        {
            MallCategory category = (MallCategory) iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(category.getParentId()))
            {
                recursionFn(categorys, category);
                returnList.add(category);
            }
        }
        if (returnList.isEmpty())
        {
            returnList = categorys;
        }
        return returnList;
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<MallCategory> list, MallCategory t)
    {
        // 得到子节点列表
        List<MallCategory> childList = getChildList(list, t);
        t.setChildren(childList);
        for (MallCategory tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<MallCategory> getChildList(List<MallCategory> list, MallCategory t)
    {
        List<MallCategory> tlist = new ArrayList<MallCategory>();
        Iterator<MallCategory> it = list.iterator();
        while (it.hasNext())
        {
            MallCategory n = (MallCategory) it.next();
            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().longValue() == t.getCategoryId().longValue())
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<MallCategory> list, MallCategory t)
    {
        return getChildList(list, t).size() > 0;
    }

    /**
     * 查询分类树结构信息
     * 
     * @param mallCategory 分类信息
     * @return 分类树信息集合
     */
    @Override
    public List<MallCategory> selectCategoryTreeList(MallCategory mallCategory)
    {
        List<MallCategory> categorys = mallCategoryMapper.selectMallCategoryList(mallCategory);
        return buildCategoryTree(categorys);
    }

    /**
     * 是否存在子节点
     * 
     * @param categoryId 分类ID
     * @return 结果
     */
    @Override
    public boolean hasChildByCategoryId(Long categoryId)
    {
        int result = mallCategoryMapper.hasChildByCategoryId(categoryId);
        return result > 0;
    }
} 