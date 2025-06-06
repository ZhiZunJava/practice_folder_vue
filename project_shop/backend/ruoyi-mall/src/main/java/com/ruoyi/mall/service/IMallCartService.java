package com.ruoyi.mall.service;

import java.util.List;
import com.ruoyi.mall.domain.MallCart;

/**
 * 购物车Service接口
 * 
 * @author ruoyi
 * @date 2025-01-15
 */
public interface IMallCartService 
{
    /**
     * 查询购物车
     * 
     * @param cartId 购物车主键
     * @return 购物车
     */
    public MallCart selectMallCartByCartId(Long cartId);

    /**
     * 查询购物车列表
     * 
     * @param mallCart 购物车
     * @return 购物车集合
     */
    public List<MallCart> selectMallCartList(MallCart mallCart);

    /**
     * 新增购物车
     * 
     * @param mallCart 购物车
     * @return 结果
     */
    public int insertMallCart(MallCart mallCart);

    /**
     * 修改购物车
     * 
     * @param mallCart 购物车
     * @return 结果
     */
    public int updateMallCart(MallCart mallCart);

    /**
     * 批量删除购物车
     * 
     * @param cartIds 需要删除的购物车主键集合
     * @return 结果
     */
    public int deleteMallCartByCartIds(Long[] cartIds);

    /**
     * 删除购物车信息
     * 
     * @param cartId 购物车主键
     * @return 结果
     */
    public int deleteMallCartByCartId(Long cartId);

    /**
     * 根据用户ID查询购物车列表
     * 
     * @param userId 用户ID
     * @return 购物车集合
     */
    public List<MallCart> selectCartByUserId(Long userId);

    /**
     * 添加商品到购物车
     * 
     * @param userId 用户ID
     * @param productId 商品ID
     * @param quantity 数量
     * @return 结果
     */
    public int addToCart(Long userId, Long productId, Long quantity);

    /**
     * 更新购物车商品数量
     * 
     * @param userId 用户ID
     * @param productId 商品ID
     * @param quantity 数量
     * @return 结果
     */
    public int updateCartQuantity(Long userId, Long productId, Long quantity);

    /**
     * 清空用户购物车
     * 
     * @param userId 用户ID
     * @return 结果
     */
    public int clearCartByUserId(Long userId);
} 