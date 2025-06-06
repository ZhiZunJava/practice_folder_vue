package com.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.entity.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 购物车Mapper接口
 */
@Mapper
public interface CartMapper extends BaseMapper<Cart> {

    /**
     * 获取用户购物车详情（包含商品信息）
     */
    @Select("SELECT c.*, p.name as productName, p.main_image as productImage, p.price " +
            "FROM cart c " +
            "LEFT JOIN product p ON c.product_id = p.id " +
            "WHERE c.user_id = #{userId} AND p.deleted = 0 AND p.status = 1 " +
            "ORDER BY c.create_time DESC")
    List<Cart> selectCartWithProduct(@Param("userId") Long userId);
} 