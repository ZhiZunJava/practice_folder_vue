package com.mall.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 购物车实体类
 */
@Data
@TableName("cart")
public class Cart {

    /**
     * 购物车ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 商品数量
     */
    private Integer quantity;

    /**
     * 是否选中：0-未选中，1-已选中
     */
    private Integer checked;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    // 以下为非数据库字段，用于查询时展示商品信息
    
    /**
     * 商品名称（非数据库字段）
     */
    @TableField(exist = false)
    private String productName;
    
    /**
     * 商品主图（非数据库字段）
     */
    @TableField(exist = false)
    private String productImage;
    
    /**
     * 商品价格（非数据库字段）
     */
    @TableField(exist = false)
    private BigDecimal price;
} 