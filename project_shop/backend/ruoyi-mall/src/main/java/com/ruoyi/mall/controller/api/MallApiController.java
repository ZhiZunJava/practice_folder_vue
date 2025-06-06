package com.ruoyi.mall.controller.api;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.mall.domain.MallCategory;
import com.ruoyi.mall.domain.MallProduct;
import com.ruoyi.mall.service.IMallCategoryService;
import com.ruoyi.mall.service.IMallProductService;

/**
 * 微商城前台API Controller
 * 
 * @author ruoyi
 * @date 2025-01-15
 */
@Anonymous
@RestController
@RequestMapping("/api/mall")
public class MallApiController extends BaseController
{
    @Autowired
    private IMallCategoryService mallCategoryService;
    
    @Autowired
    private IMallProductService mallProductService;

    /**
     * 获取分类列表（树形结构）
     */
    @GetMapping("/categories")
    public AjaxResult getCategoryTree()
    {
        MallCategory category = new MallCategory();
        category.setStatus("0"); // 只查询正常状态的分类
        List<MallCategory> list = mallCategoryService.selectCategoryTreeList(category);
        return success(list);
    }

    /**
     * 获取一级分类列表
     */
    @GetMapping("/categories/top")
    public AjaxResult getTopCategories()
    {
        MallCategory category = new MallCategory();
        category.setParentId(0L);
        category.setStatus("0");
        List<MallCategory> list = mallCategoryService.selectMallCategoryList(category);
        return success(list);
    }

    /**
     * 获取推荐商品列表
     */
    @GetMapping("/products/recommend")
    public AjaxResult getRecommendProducts()
    {
        List<MallProduct> list = mallProductService.selectRecommendProductList();
        return success(list);
    }

    /**
     * 获取新品列表
     */
    @GetMapping("/products/new")
    public AjaxResult getNewProducts()
    {
        List<MallProduct> list = mallProductService.selectNewProductList();
        return success(list);
    }

    /**
     * 获取热销商品列表
     */
    @GetMapping("/products/hot")
    public AjaxResult getHotProducts()
    {
        List<MallProduct> list = mallProductService.selectHotProductList();
        return success(list);
    }

    /**
     * 根据分类ID获取商品列表
     */
    @GetMapping("/products/category/{categoryId}")
    public TableDataInfo getProductsByCategory(@PathVariable("categoryId") Long categoryId,
                                               @RequestParam(defaultValue = "1") Integer pageNum,
                                               @RequestParam(defaultValue = "10") Integer pageSize)
    {
        startPage();
        List<MallProduct> list = mallProductService.selectProductsByCategoryId(categoryId);
        return getDataTable(list);
    }

    /**
     * 搜索商品
     */
    @GetMapping("/products/search")
    public TableDataInfo searchProducts(@RequestParam("keyword") String keyword,
                                        @RequestParam(defaultValue = "1") Integer pageNum,
                                        @RequestParam(defaultValue = "10") Integer pageSize)
    {
        startPage();
        MallProduct product = new MallProduct();
        product.setProductName(keyword);
        product.setStatus("0"); // 只查询正常状态的商品
        List<MallProduct> list = mallProductService.selectMallProductList(product);
        return getDataTable(list);
    }

    /**
     * 获取商品详情
     */
    @GetMapping("/products/{productId}")
    public AjaxResult getProductDetail(@PathVariable("productId") Long productId)
    {
        MallProduct product = mallProductService.selectMallProductByProductId(productId);
        if (product == null || !"0".equals(product.getStatus()))
        {
            return error("商品不存在或已下架");
        }
        return success(product);
    }

    /**
     * 获取商品列表（分页）
     */
    @GetMapping("/products")
    public TableDataInfo getProducts(@RequestParam(defaultValue = "1") Integer pageNum,
                                     @RequestParam(defaultValue = "10") Integer pageSize,
                                     @RequestParam(required = false) Long categoryId,
                                     @RequestParam(required = false) String isRecommend,
                                     @RequestParam(required = false) String isNew,
                                     @RequestParam(required = false) String isHot)
    {
        startPage();
        MallProduct product = new MallProduct();
        product.setCategoryId(categoryId);
        product.setIsRecommend(isRecommend);
        product.setIsNew(isNew);
        product.setIsHot(isHot);
        product.setStatus("0"); // 只查询正常状态的商品
        List<MallProduct> list = mallProductService.selectMallProductList(product);
        return getDataTable(list);
    }
} 