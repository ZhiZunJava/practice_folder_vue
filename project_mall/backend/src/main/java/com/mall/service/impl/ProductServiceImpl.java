package com.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.entity.Product;
import com.mall.mapper.ProductMapper;
import com.mall.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 商品服务实现类
 */
@Slf4j
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Override
    public IPage<Product> getProductPage(Integer current, Integer size, String name, Long categoryId, Integer status) {
        log.info("查询商品列表 - 页码: {}, 大小: {}, 名称: {}, 分类ID: {}, 状态: {}", 
                current, size, name, categoryId, status);
        
        Page<Product> page = new Page<>(current, size);
        
        // 使用自定义查询以包含分类名称
        IPage<Product> result = baseMapper.selectProductPageWithCategory(page, name, categoryId, status);
        
        log.info("查询结果 - 总数: {}, 当前页数据: {}", result.getTotal(), result.getRecords().size());
        return result;
    }

    @Override
    public List<Product> getProductsByCategory(Long categoryId) {
        return baseMapper.selectByCategory(categoryId);
    }

    @Override
    public List<Product> searchProducts(String keyword) {
        return baseMapper.searchProducts(keyword);
    }

    @Override
    public List<Product> getHotProducts(Integer limit) {
        return baseMapper.selectHotProducts(limit);
    }

    @Override
    public boolean addProduct(Product product) {
        log.info("添加商品: {}", product.getName());
        
        // 设置默认值
        if (product.getStatus() == null) {
            product.setStatus(1);
        }
        if (product.getStock() == null) {
            product.setStock(0);
        }
        
        boolean result = save(product);
        log.info("商品添加{}: {}", result ? "成功" : "失败", product.getName());
        return result;
    }

    @Override
    public boolean updateProduct(Product product) {
        log.info("更新商品: ID={}, 名称={}", product.getId(), product.getName());
        
        boolean result = updateById(product);
        log.info("商品更新{}: ID={}", result ? "成功" : "失败", product.getId());
        return result;
    }

    @Override
    public boolean deleteProduct(Long id) {
        log.info("删除商品: ID={}", id);
        
        boolean result = removeById(id);
        log.info("商品删除{}: ID={}", result ? "成功" : "失败", id);
        return result;
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        log.info("更新商品状态: ID={}, 状态={}", id, status);
        
        Product product = new Product();
        product.setId(id);
        product.setStatus(status);
        
        boolean result = updateById(product);
        log.info("商品状态更新{}: ID={}", result ? "成功" : "失败", id);
        return result;
    }

    @Override
    public boolean reduceStock(Long productId, Integer quantity) {
        log.info("减少商品库存: 商品ID={}, 数量={}", productId, quantity);
        
        Product product = getById(productId);
        if (product == null) {
            log.error("商品不存在: ID={}", productId);
            throw new RuntimeException("商品不存在");
        }
        
        if (product.getStock() < quantity) {
            log.error("库存不足: 商品ID={}, 当前库存={}, 需要数量={}", 
                    productId, product.getStock(), quantity);
            throw new RuntimeException("库存不足");
        }
        
        product.setStock(product.getStock() - quantity);
        boolean result = updateById(product);
        log.info("库存减少{}: 商品ID={}, 剩余库存={}", 
                result ? "成功" : "失败", productId, product.getStock());
        return result;
    }
} 