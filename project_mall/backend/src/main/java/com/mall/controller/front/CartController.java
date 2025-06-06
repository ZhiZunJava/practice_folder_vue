package com.mall.controller.front;

import com.mall.common.Result;
import com.mall.entity.Cart;
import com.mall.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 前台购物车控制器
 */
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    /**
     * 获取购物车列表
     */
    @GetMapping
    public Result<List<Cart>> getCartList(@RequestParam Long userId) {
        try {
            List<Cart> cartList = cartService.getCartList(userId);
            return Result.success(cartList);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 添加商品到购物车
     */
    @PostMapping
    public Result<String> addToCart(@RequestBody Map<String, Object> params) {
        try {
            Long userId = Long.valueOf(params.get("userId").toString());
            Long productId = Long.valueOf(params.get("productId").toString());
            Integer quantity = Integer.valueOf(params.get("quantity").toString());
            
            cartService.addToCart(userId, productId, quantity);
            return Result.success("添加成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 更新购物车商品数量
     */
    @PutMapping("/{id}")
    public Result<String> updateQuantity(@PathVariable Long id, @RequestBody Map<String, Integer> params) {
        try {
            Integer quantity = params.get("quantity");
            cartService.updateQuantity(id, quantity);
            return Result.success("更新成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除购物车商品
     */
    @DeleteMapping("/{id}")
    public Result<String> removeFromCart(@PathVariable Long id) {
        try {
            cartService.removeFromCart(id);
            return Result.success("删除成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 清空购物车
     */
    @DeleteMapping("/clear")
    public Result<String> clearCart(@RequestParam Long userId) {
        try {
            cartService.clearCart(userId);
            return Result.success("清空成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 更新商品选中状态
     */
    @PutMapping("/{id}/checked")
    public Result<String> updateChecked(@PathVariable Long id, @RequestBody Map<String, Integer> params) {
        try {
            Integer checked = params.get("checked");
            cartService.updateChecked(id, checked);
            return Result.success("更新成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 全选/取消全选
     */
    @PutMapping("/check-all")
    public Result<String> checkAll(@RequestBody Map<String, Object> params) {
        try {
            Long userId = Long.valueOf(params.get("userId").toString());
            Integer checked = Integer.valueOf(params.get("checked").toString());
            cartService.checkAll(userId, checked);
            return Result.success("操作成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
} 