package com.ruoyi.mall.mapper;

import java.util.List;
import com.ruoyi.mall.domain.MallCart;
import org.apache.ibatis.annotations.Param;

/**
 * 购物车Mapper接口
 * 
 * @author ruoyi
 * @date 2025-01-15
 */
public interface MallCartMapper 
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
     * 删除购物车
     * 
     * @param cartId 购物车主键
     * @return 结果
     */
    public int deleteMallCartByCartId(Long cartId);

    /**
     * 批量删除购物车
     * 
     * @param cartIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMallCartByCartIds(Long[] cartIds);

    /**
     * 根据用户ID查询购物车列表
     * 
     * @param userId 用户ID
     * @return 购物车集合
     */
    public List<MallCart> selectCartByUserId(Long userId);

    /**
     * 根据用户ID和商品ID查询购物车
     * 
     * @param userId 用户ID
     * @param productId 商品ID
     * @return 购物车
     */
    public MallCart selectCartByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);

    /**
     * 清空用户购物车
     * 
     * @param userId 用户ID
     * @return 结果
     */
    public int clearCartByUserId(Long userId);
} 