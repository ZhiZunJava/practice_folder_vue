package com.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 商品Mapper接口
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {

    /**
     * 分页查询商品列表（包含分类名称）
     */
    @Select({
        "<script>",
        "SELECT p.*, c.name as categoryName FROM product p ",
        "LEFT JOIN category c ON p.category_id = c.id ",
        "WHERE p.deleted = 0 ",
        "<if test='name != null and name != \"\"'>",
        "AND p.name LIKE CONCAT('%', #{name}, '%') ",
        "</if>",
        "<if test='categoryId != null'>",
        "AND p.category_id = #{categoryId} ",
        "</if>",
        "<if test='status != null'>",
        "AND p.status = #{status} ",
        "</if>",
        "ORDER BY p.id DESC",
        "</script>"
    })
    IPage<Product> selectProductPageWithCategory(
            Page<Product> page,
            @Param("name") String name,
            @Param("categoryId") Long categoryId,
            @Param("status") Integer status
    );

    /**
     * 根据分类ID查询商品列表
     */
    @Select("SELECT p.*, c.name as categoryName FROM product p " +
            "LEFT JOIN category c ON p.category_id = c.id " +
            "WHERE p.category_id = #{categoryId} AND p.deleted = 0 AND p.status = 1 " +
            "ORDER BY p.id DESC")
    List<Product> selectByCategory(@Param("categoryId") Long categoryId);

    /**
     * 搜索商品
     */
    @Select("SELECT p.*, c.name as categoryName FROM product p " +
            "LEFT JOIN category c ON p.category_id = c.id " +
            "WHERE p.name LIKE CONCAT('%', #{keyword}, '%') " +
            "AND p.deleted = 0 AND p.status = 1 " +
            "ORDER BY p.id DESC")
    List<Product> searchProducts(@Param("keyword") String keyword);

    /**
     * 获取热门商品
     */
    @Select("SELECT p.*, c.name as categoryName FROM product p " +
            "LEFT JOIN category c ON p.category_id = c.id " +
            "WHERE p.deleted = 0 AND p.status = 1 " +
            "ORDER BY p.id DESC LIMIT #{limit}")
    List<Product> selectHotProducts(@Param("limit") Integer limit);
} 