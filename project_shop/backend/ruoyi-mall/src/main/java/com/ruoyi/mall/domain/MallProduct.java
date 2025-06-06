package com.ruoyi.mall.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 商品对象 mall_product
 * 
 * @author ruoyi
 * @date 2025-01-15
 */
public class MallProduct extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 商品ID */
    private Long productId;

    /** 分类ID */
    @Excel(name = "分类ID")
    private Long categoryId;

    /** 商品名称 */
    @Excel(name = "商品名称")
    private String productName;

    /** 商品简称 */
    @Excel(name = "商品简称")
    private String productShortName;

    /** 商品描述 */
    @Excel(name = "商品描述")
    private String productDesc;

    /** 商品主图 */
    @Excel(name = "商品主图")
    private String productImage;

    /** 商品图片（多张） */
    @Excel(name = "商品图片")
    private String productImages;

    /** 原价 */
    @Excel(name = "原价")
    private BigDecimal originalPrice;

    /** 现价 */
    @Excel(name = "现价")
    private BigDecimal currentPrice;

    /** 库存 */
    @Excel(name = "库存")
    private Long stock;

    /** 销量 */
    @Excel(name = "销量")
    private Long sales;

    /** 商品标签 */
    @Excel(name = "商品标签")
    private String tag;

    /** 是否推荐（0否 1是） */
    @Excel(name = "是否推荐", readConverterExp = "0=否,1=是")
    private String isRecommend;

    /** 是否新品（0否 1是） */
    @Excel(name = "是否新品", readConverterExp = "0=否,1=是")
    private String isNew;

    /** 是否热销（0否 1是） */
    @Excel(name = "是否热销", readConverterExp = "0=否,1=是")
    private String isHot;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 分类名称 */
    private String categoryName;

    public void setProductId(Long productId) 
    {
        this.productId = productId;
    }

    public Long getProductId() 
    {
        return productId;
    }
    public void setCategoryId(Long categoryId) 
    {
        this.categoryId = categoryId;
    }

    public Long getCategoryId() 
    {
        return categoryId;
    }
    public void setProductName(String productName) 
    {
        this.productName = productName;
    }

    public String getProductName() 
    {
        return productName;
    }
    public void setProductShortName(String productShortName) 
    {
        this.productShortName = productShortName;
    }

    public String getProductShortName() 
    {
        return productShortName;
    }
    public void setProductDesc(String productDesc) 
    {
        this.productDesc = productDesc;
    }

    public String getProductDesc() 
    {
        return productDesc;
    }
    public void setProductImage(String productImage) 
    {
        this.productImage = productImage;
    }

    public String getProductImage() 
    {
        return productImage;
    }
    public void setProductImages(String productImages) 
    {
        this.productImages = productImages;
    }

    public String getProductImages() 
    {
        return productImages;
    }
    public void setOriginalPrice(BigDecimal originalPrice) 
    {
        this.originalPrice = originalPrice;
    }

    public BigDecimal getOriginalPrice() 
    {
        return originalPrice;
    }
    public void setCurrentPrice(BigDecimal currentPrice) 
    {
        this.currentPrice = currentPrice;
    }

    public BigDecimal getCurrentPrice() 
    {
        return currentPrice;
    }
    public void setStock(Long stock) 
    {
        this.stock = stock;
    }

    public Long getStock() 
    {
        return stock;
    }
    public void setSales(Long sales) 
    {
        this.sales = sales;
    }

    public Long getSales() 
    {
        return sales;
    }
    public void setTag(String tag) 
    {
        this.tag = tag;
    }

    public String getTag() 
    {
        return tag;
    }
    public void setIsRecommend(String isRecommend) 
    {
        this.isRecommend = isRecommend;
    }

    public String getIsRecommend() 
    {
        return isRecommend;
    }
    public void setIsNew(String isNew) 
    {
        this.isNew = isNew;
    }

    public String getIsNew() 
    {
        return isNew;
    }
    public void setIsHot(String isHot) 
    {
        this.isHot = isHot;
    }

    public String getIsHot() 
    {
        return isHot;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("productId", getProductId())
            .append("categoryId", getCategoryId())
            .append("productName", getProductName())
            .append("productShortName", getProductShortName())
            .append("productDesc", getProductDesc())
            .append("productImage", getProductImage())
            .append("productImages", getProductImages())
            .append("originalPrice", getOriginalPrice())
            .append("currentPrice", getCurrentPrice())
            .append("stock", getStock())
            .append("sales", getSales())
            .append("tag", getTag())
            .append("isRecommend", getIsRecommend())
            .append("isNew", getIsNew())
            .append("isHot", getIsHot())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
} 