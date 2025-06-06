package com.ruoyi.mall.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.mall.mapper.MallCartMapper;
import com.ruoyi.mall.domain.MallCart;
import com.ruoyi.mall.service.IMallCartService;

/**
 * 购物车Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-01-15
 */
@Service
public class MallCartServiceImpl implements IMallCartService 
{
    @Autowired
    private MallCartMapper mallCartMapper;

    /**
     * 查询购物车
     * 
     * @param cartId 购物车主键
     * @return 购物车
     */
    @Override
    public MallCart selectMallCartByCartId(Long cartId)
    {
        return mallCartMapper.selectMallCartByCartId(cartId);
    }

    /**
     * 查询购物车列表
     * 
     * @param mallCart 购物车
     * @return 购物车
     */
    @Override
    public List<MallCart> selectMallCartList(MallCart mallCart)
    {
        return mallCartMapper.selectMallCartList(mallCart);
    }

    /**
     * 新增购物车
     * 
     * @param mallCart 购物车
     * @return 结果
     */
    @Override
    public int insertMallCart(MallCart mallCart)
    {
        mallCart.setCreateTime(DateUtils.getNowDate());
        return mallCartMapper.insertMallCart(mallCart);
    }

    /**
     * 修改购物车
     * 
     * @param mallCart 购物车
     * @return 结果
     */
    @Override
    public int updateMallCart(MallCart mallCart)
    {
        mallCart.setUpdateTime(DateUtils.getNowDate());
        return mallCartMapper.updateMallCart(mallCart);
    }

    /**
     * 批量删除购物车
     * 
     * @param cartIds 需要删除的购物车主键
     * @return 结果
     */
    @Override
    public int deleteMallCartByCartIds(Long[] cartIds)
    {
        return mallCartMapper.deleteMallCartByCartIds(cartIds);
    }

    /**
     * 删除购物车信息
     * 
     * @param cartId 购物车主键
     * @return 结果
     */
    @Override
    public int deleteMallCartByCartId(Long cartId)
    {
        return mallCartMapper.deleteMallCartByCartId(cartId);
    }

    /**
     * 根据用户ID查询购物车列表
     * 
     * @param userId 用户ID
     * @return 购物车集合
     */
    @Override
    public List<MallCart> selectCartByUserId(Long userId)
    {
        return mallCartMapper.selectCartByUserId(userId);
    }

    /**
     * 添加商品到购物车
     * 
     * @param userId 用户ID
     * @param productId 商品ID
     * @param quantity 数量
     * @return 结果
     */
    @Override
    public int addToCart(Long userId, Long productId, Long quantity)
    {
        // 先查询是否已存在该商品
        MallCart existCart = mallCartMapper.selectCartByUserIdAndProductId(userId, productId);
        
        if (existCart != null)
        {
            // 如果已存在，增加数量
            existCart.setQuantity(existCart.getQuantity() + quantity);
            existCart.setUpdateTime(DateUtils.getNowDate());
            return mallCartMapper.updateMallCart(existCart);
        }
        else
        {
            // 如果不存在，新增购物车项
            MallCart newCart = new MallCart();
            newCart.setUserId(userId);
            newCart.setProductId(productId);
            newCart.setQuantity(quantity);
            newCart.setCreateTime(DateUtils.getNowDate());
            return mallCartMapper.insertMallCart(newCart);
        }
    }

    /**
     * 更新购物车商品数量
     * 
     * @param userId 用户ID
     * @param productId 商品ID
     * @param quantity 数量
     * @return 结果
     */
    @Override
    public int updateCartQuantity(Long userId, Long productId, Long quantity)
    {
        MallCart cart = mallCartMapper.selectCartByUserIdAndProductId(userId, productId);
        if (cart != null)
        {
            if (quantity <= 0)
            {
                // 如果数量小于等于0，删除购物车项
                return mallCartMapper.deleteMallCartByCartId(cart.getCartId());
            }
            else
            {
                // 更新数量
                cart.setQuantity(quantity);
                cart.setUpdateTime(DateUtils.getNowDate());
                return mallCartMapper.updateMallCart(cart);
            }
        }
        return 0;
    }

    /**
     * 清空用户购物车
     * 
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public int clearCartByUserId(Long userId)
    {
        return mallCartMapper.clearCartByUserId(userId);
    }
} 