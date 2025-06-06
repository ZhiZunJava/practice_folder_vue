package com.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.entity.Product;

import java.util.List;

/**
 * 商品服务接口
 */
public interface ProductService extends IService<Product> {

    /**
     * 分页查询商品
     */
    IPage<Product> getProductPage(Integer current, Integer size, String name, Long categoryId, Integer status);

    /**
     * 根据分类ID查询商品
     */
    List<Product> getProductsByCategory(Long categoryId);

    /**
     * 搜索商品
     */
    List<Product> searchProducts(String keyword);

    /**
     * 获取热门商品
     */
    List<Product> getHotProducts(Integer limit);

    /**
     * 添加商品
     */
    boolean addProduct(Product product);

    /**
     * 更新商品
     */
    boolean updateProduct(Product product);

    /**
     * 删除商品
     */
    boolean deleteProduct(Long id);

    /**
     * 更新商品状态
     */
    boolean updateStatus(Long id, Integer status);

    /**
     * 减少商品库存
     */
    boolean reduceStock(Long productId, Integer quantity);
} 