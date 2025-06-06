package com.ruoyi.mall.mapper;

import java.util.List;
import com.ruoyi.mall.domain.MallProduct;

/**
 * 商品Mapper接口
 * 
 * @author ruoyi
 * @date 2025-01-15
 */
public interface MallProductMapper 
{
    /**
     * 查询商品
     * 
     * @param productId 商品主键
     * @return 商品
     */
    public MallProduct selectMallProductByProductId(Long productId);

    /**
     * 查询商品列表
     * 
     * @param mallProduct 商品
     * @return 商品集合
     */
    public List<MallProduct> selectMallProductList(MallProduct mallProduct);

    /**
     * 新增商品
     * 
     * @param mallProduct 商品
     * @return 结果
     */
    public int insertMallProduct(MallProduct mallProduct);

    /**
     * 修改商品
     * 
     * @param mallProduct 商品
     * @return 结果
     */
    public int updateMallProduct(MallProduct mallProduct);

    /**
     * 删除商品
     * 
     * @param productId 商品主键
     * @return 结果
     */
    public int deleteMallProductByProductId(Long productId);

    /**
     * 批量删除商品
     * 
     * @param productIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMallProductByProductIds(Long[] productIds);

    /**
     * 查询推荐商品列表
     * 
     * @return 商品集合
     */
    public List<MallProduct> selectRecommendProductList();

    /**
     * 查询新品列表
     * 
     * @return 商品集合
     */
    public List<MallProduct> selectNewProductList();

    /**
     * 查询热销商品列表
     * 
     * @return 商品集合
     */
    public List<MallProduct> selectHotProductList();

    /**
     * 根据分类ID查询商品列表
     * 
     * @param categoryId 分类ID
     * @return 商品集合
     */
    public List<MallProduct> selectProductsByCategoryId(Long categoryId);
} 