package com.ruoyi.mall.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.mall.mapper.MallProductMapper;
import com.ruoyi.mall.domain.MallProduct;
import com.ruoyi.mall.service.IMallProductService;

/**
 * 商品Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-01-15
 */
@Service
public class MallProductServiceImpl implements IMallProductService 
{
    @Autowired
    private MallProductMapper mallProductMapper;

    /**
     * 查询商品
     * 
     * @param productId 商品主键
     * @return 商品
     */
    @Override
    public MallProduct selectMallProductByProductId(Long productId)
    {
        return mallProductMapper.selectMallProductByProductId(productId);
    }

    /**
     * 查询商品列表
     * 
     * @param mallProduct 商品
     * @return 商品
     */
    @Override
    public List<MallProduct> selectMallProductList(MallProduct mallProduct)
    {
        return mallProductMapper.selectMallProductList(mallProduct);
    }

    /**
     * 新增商品
     * 
     * @param mallProduct 商品
     * @return 结果
     */
    @Override
    public int insertMallProduct(MallProduct mallProduct)
    {
        mallProduct.setCreateTime(DateUtils.getNowDate());
        return mallProductMapper.insertMallProduct(mallProduct);
    }

    /**
     * 修改商品
     * 
     * @param mallProduct 商品
     * @return 结果
     */
    @Override
    public int updateMallProduct(MallProduct mallProduct)
    {
        mallProduct.setUpdateTime(DateUtils.getNowDate());
        return mallProductMapper.updateMallProduct(mallProduct);
    }

    /**
     * 批量删除商品
     * 
     * @param productIds 需要删除的商品主键
     * @return 结果
     */
    @Override
    public int deleteMallProductByProductIds(Long[] productIds)
    {
        return mallProductMapper.deleteMallProductByProductIds(productIds);
    }

    /**
     * 删除商品信息
     * 
     * @param productId 商品主键
     * @return 结果
     */
    @Override
    public int deleteMallProductByProductId(Long productId)
    {
        return mallProductMapper.deleteMallProductByProductId(productId);
    }

    /**
     * 查询推荐商品列表
     * 
     * @return 商品集合
     */
    @Override
    public List<MallProduct> selectRecommendProductList()
    {
        return mallProductMapper.selectRecommendProductList();
    }

    /**
     * 查询新品列表
     * 
     * @return 商品集合
     */
    @Override
    public List<MallProduct> selectNewProductList()
    {
        return mallProductMapper.selectNewProductList();
    }

    /**
     * 查询热销商品列表
     * 
     * @return 商品集合
     */
    @Override
    public List<MallProduct> selectHotProductList()
    {
        return mallProductMapper.selectHotProductList();
    }

    /**
     * 根据分类ID查询商品列表
     * 
     * @param categoryId 分类ID
     * @return 商品集合
     */
    @Override
    public List<MallProduct> selectProductsByCategoryId(Long categoryId)
    {
        return mallProductMapper.selectProductsByCategoryId(categoryId);
    }
} 