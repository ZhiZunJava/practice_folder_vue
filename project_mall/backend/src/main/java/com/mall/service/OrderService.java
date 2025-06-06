package com.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.entity.Order;

/**
 * 订单服务接口
 */
public interface OrderService extends IService<Order> {

    /**
     * 创建订单
     */
    String createOrder(Long userId, String receiverName, String receiverPhone, String receiverAddress);

    /**
     * 分页查询用户订单
     */
    IPage<Order> getUserOrderPage(Long userId, Integer current, Integer size, Integer status);

    /**
     * 分页查询所有订单（管理员）
     */
    IPage<Order> getOrderPage(Integer current, Integer size, String orderNo, Long userId, Integer status);

    /**
     * 取消订单
     */
    boolean cancelOrder(Long orderId);

    /**
     * 确认收货
     */
    boolean confirmOrder(Long orderId);

    /**
     * 更新订单状态
     */
    boolean updateOrderStatus(Long orderId, Integer status);

    /**
     * 发货
     */
    boolean shipOrder(Long orderId);
} 