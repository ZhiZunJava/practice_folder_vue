package com.ruoyi.mall.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.mall.service.IMallCategoryService;
import com.ruoyi.mall.service.IMallProductService;
import com.ruoyi.mall.domain.MallCategory;
import com.ruoyi.mall.domain.MallProduct;

/**
 * 商城后台首页Controller
 * 
 * @author ruoyi
 * @date 2025-01-15
 */
@RestController
@RequestMapping("/mall/dashboard")
public class MallDashboardController extends BaseController
{
    @Autowired
    private IMallCategoryService mallCategoryService;
    
    @Autowired
    private IMallProductService mallProductService;

    /**
     * 获取首页统计数据
     */
    @PreAuthorize("@ss.hasPermi('mall:dashboard:list')")
    @GetMapping("/statistics")
    public AjaxResult getStatistics()
    {
        Map<String, Object> data = new HashMap<>();
        
        // 商品总数
        MallProduct productQuery = new MallProduct();
        productQuery.setStatus("0"); // 正常状态
        int productCount = mallProductService.selectMallProductList(productQuery).size();
        
        // 分类总数
        MallCategory categoryQuery = new MallCategory();
        categoryQuery.setStatus("0"); // 正常状态
        int categoryCount = mallCategoryService.selectMallCategoryList(categoryQuery).size();
        
        // 用户总数 (这里暂时模拟，实际应该调用用户服务)
        int userCount = 121;
        
        // 今日销售额 (模拟数据)
        BigDecimal todaySales = new BigDecimal("1280.50");
        
        data.put("productCount", productCount);
        data.put("categoryCount", categoryCount);
        data.put("userCount", userCount);
        data.put("todaySales", todaySales);
        
        return success(data);
    }

    /**
     * 获取图表数据
     */
    @PreAuthorize("@ss.hasPermi('mall:dashboard:list')")
    @GetMapping("/charts")
    public AjaxResult getChartData()
    {
        Map<String, Object> data = new HashMap<>();
        
        // 2022年度成交额数据 (模拟)
        String[] months = {"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"};
        double[] salesData = {8.2, 9.5, 10.8, 11.2, 12.5, 13.8, 14.2, 15.5, 13.8, 12.5, 13.2, 12.8};
        
        // 2022年订单数据 (模拟)
        int[] orderData = {450, 520, 350, 280, 380, 420, 350, 380, 450, 200, 350, 480};
        
        data.put("months", months);
        data.put("salesData", salesData);
        data.put("orderData", orderData);
        
        return success(data);
    }
} 