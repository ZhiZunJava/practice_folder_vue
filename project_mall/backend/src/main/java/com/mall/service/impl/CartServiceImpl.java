package com.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.entity.Cart;
import com.mall.entity.Product;
import com.mall.mapper.CartMapper;
import com.mall.service.CartService;
import com.mall.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 购物车服务实现类
 */
@Service
@RequiredArgsConstructor
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {

    private final ProductService productService;

    @Override
    public List<Cart> getCartList(Long userId) {
        return baseMapper.selectCartWithProduct(userId);
    }

    @Override
    public boolean addToCart(Long userId, Long productId, Integer quantity) {
        // 检查商品是否存在且有效
        Product product = productService.getById(productId);
        if (product == null || product.getStatus() == 0) {
            throw new RuntimeException("商品不存在或已下架");
        }

        // 检查库存
        if (product.getStock() < quantity) {
            throw new RuntimeException("库存不足");
        }

        // 查询购物车中是否已有该商品
        Cart existCart = getOne(new LambdaQueryWrapper<Cart>()
                .eq(Cart::getUserId, userId)
                .eq(Cart::getProductId, productId));

        if (existCart != null) {
            // 更新数量
            existCart.setQuantity(existCart.getQuantity() + quantity);
            return updateById(existCart);
        } else {
            // 新增购物车记录
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setProductId(productId);
            cart.setQuantity(quantity);
            cart.setChecked(1);
            return save(cart);
        }
    }

    @Override
    public boolean updateQuantity(Long cartId, Integer quantity) {
        Cart cart = getById(cartId);
        if (cart == null) {
            throw new RuntimeException("购物车记录不存在");
        }

        // 检查库存
        Product product = productService.getById(cart.getProductId());
        if (product.getStock() < quantity) {
            throw new RuntimeException("库存不足");
        }

        cart.setQuantity(quantity);
        return updateById(cart);
    }

    @Override
    public boolean removeFromCart(Long cartId) {
        return removeById(cartId);
    }

    @Override
    public boolean clearCart(Long userId) {
        return remove(new LambdaQueryWrapper<Cart>()
                .eq(Cart::getUserId, userId));
    }

    @Override
    public boolean updateChecked(Long cartId, Integer checked) {
        return update(new LambdaUpdateWrapper<Cart>()
                .eq(Cart::getId, cartId)
                .set(Cart::getChecked, checked));
    }

    @Override
    public boolean checkAll(Long userId, Integer checked) {
        return update(new LambdaUpdateWrapper<Cart>()
                .eq(Cart::getUserId, userId)
                .set(Cart::getChecked, checked));
    }
} 