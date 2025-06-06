package com.ruoyi.mall.controller.api;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.mall.domain.MallCart;
import com.ruoyi.mall.service.IMallCartService;

/**
 * 购物车API Controller
 * 
 * @author ruoyi
 * @date 2025-01-15
 */
@RestController
@RequestMapping("/api/mall/cart")
public class MallCartApiController extends BaseController
{
    @Autowired
    private IMallCartService mallCartService;

    /**
     * 获取购物车列表
     */
    @GetMapping("/list")
    public AjaxResult getCartList()
    {
        Long userId = SecurityUtils.getUserId();
        List<MallCart> list = mallCartService.selectCartByUserId(userId);
        
        // 计算购物车总金额
        BigDecimal totalAmount = list.stream()
                .map(MallCart::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return AjaxResult.success()
                .put("list", list)
                .put("totalAmount", totalAmount)
                .put("totalCount", list.size());
    }

    /**
     * 添加商品到购物车
     */
    @PostMapping("/add")
    public AjaxResult addToCart(@RequestParam("productId") Long productId,
                                @RequestParam(value = "quantity", defaultValue = "1") Long quantity)
    {
        Long userId = SecurityUtils.getUserId();
        
        if (quantity <= 0)
        {
            return error("商品数量必须大于0");
        }
        
        int result = mallCartService.addToCart(userId, productId, quantity);
        return toAjax(result);
    }

    /**
     * 更新购物车商品数量
     */
    @PutMapping("/update")
    public AjaxResult updateCartQuantity(@RequestParam("productId") Long productId,
                                         @RequestParam("quantity") Long quantity)
    {
        Long userId = SecurityUtils.getUserId();
        
        if (quantity < 0)
        {
            return error("商品数量不能小于0");
        }
        
        int result = mallCartService.updateCartQuantity(userId, productId, quantity);
        return toAjax(result);
    }

    /**
     * 删除购物车商品
     */
    @DeleteMapping("/remove/{cartId}")
    public AjaxResult removeFromCart(@PathVariable("cartId") Long cartId)
    {
        // 可以添加验证，确保当前用户只能删除自己的购物车项
        MallCart cart = mallCartService.selectMallCartByCartId(cartId);
        if (cart == null)
        {
            return error("购物车项不存在");
        }
        
        Long userId = SecurityUtils.getUserId();
        if (!cart.getUserId().equals(userId))
        {
            return error("无权限操作");
        }
        
        int result = mallCartService.deleteMallCartByCartId(cartId);
        return toAjax(result);
    }

    /**
     * 批量删除购物车商品
     */
    @DeleteMapping("/remove")
    public AjaxResult removeFromCart(@RequestBody Long[] cartIds)
    {
        Long userId = SecurityUtils.getUserId();
        
        // 验证所有购物车项都属于当前用户
        for (Long cartId : cartIds)
        {
            MallCart cart = mallCartService.selectMallCartByCartId(cartId);
            if (cart == null || !cart.getUserId().equals(userId))
            {
                return error("无权限操作");
            }
        }
        
        int result = mallCartService.deleteMallCartByCartIds(cartIds);
        return toAjax(result);
    }

    /**
     * 清空购物车
     */
    @DeleteMapping("/clear")
    public AjaxResult clearCart()
    {
        Long userId = SecurityUtils.getUserId();
        int result = mallCartService.clearCartByUserId(userId);
        return toAjax(result);
    }

    /**
     * 获取购物车商品数量
     */
    @GetMapping("/count")
    public AjaxResult getCartCount()
    {
        Long userId = SecurityUtils.getUserId();
        List<MallCart> list = mallCartService.selectCartByUserId(userId);
        
        Long totalQuantity = list.stream()
                .map(MallCart::getQuantity)
                .reduce(0L, Long::sum);
        
        return success(totalQuantity);
    }
} 