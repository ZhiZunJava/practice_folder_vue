package com.ruoyi.mall.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.mall.domain.MallCategory;
import com.ruoyi.mall.service.IMallCategoryService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 商品分类Controller
 * 
 * @author ruoyi
 * @date 2025-01-15
 */
@RestController
@RequestMapping("/mall/category")
public class MallCategoryController extends BaseController
{
    @Autowired
    private IMallCategoryService mallCategoryService;

    /**
     * 查询商品分类列表
     */
    @PreAuthorize("@ss.hasPermi('mall:category:list')")
    @GetMapping("/list")
    public TableDataInfo list(MallCategory mallCategory)
    {
        startPage();
        List<MallCategory> list = mallCategoryService.selectMallCategoryList(mallCategory);
        return getDataTable(list);
    }

    /**
     * 查询分类树结构
     */
    @PreAuthorize("@ss.hasPermi('mall:category:list')")
    @GetMapping("/treeList")
    public AjaxResult treeList(MallCategory mallCategory)
    {
        List<MallCategory> list = mallCategoryService.selectCategoryTreeList(mallCategory);
        return success(list);
    }

    /**
     * 导出商品分类列表
     */
    @PreAuthorize("@ss.hasPermi('mall:category:export')")
    @Log(title = "商品分类", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MallCategory mallCategory)
    {
        List<MallCategory> list = mallCategoryService.selectMallCategoryList(mallCategory);
        ExcelUtil<MallCategory> util = new ExcelUtil<MallCategory>(MallCategory.class);
        util.exportExcel(response, list, "商品分类数据");
    }

    /**
     * 获取商品分类详细信息
     */
    @PreAuthorize("@ss.hasPermi('mall:category:query')")
    @GetMapping(value = "/{categoryId}")
    public AjaxResult getInfo(@PathVariable("categoryId") Long categoryId)
    {
        return success(mallCategoryService.selectMallCategoryByCategoryId(categoryId));
    }

    /**
     * 新增商品分类
     */
    @PreAuthorize("@ss.hasPermi('mall:category:add')")
    @Log(title = "商品分类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MallCategory mallCategory)
    {
        return toAjax(mallCategoryService.insertMallCategory(mallCategory));
    }

    /**
     * 修改商品分类
     */
    @PreAuthorize("@ss.hasPermi('mall:category:edit')")
    @Log(title = "商品分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MallCategory mallCategory)
    {
        return toAjax(mallCategoryService.updateMallCategory(mallCategory));
    }

    /**
     * 删除商品分类
     */
    @PreAuthorize("@ss.hasPermi('mall:category:remove')")
    @Log(title = "商品分类", businessType = BusinessType.DELETE)
	@DeleteMapping("/{categoryIds}")
    public AjaxResult remove(@PathVariable Long[] categoryIds)
    {
        // 检查是否有子分类
        for (Long categoryId : categoryIds)
        {
            if (mallCategoryService.hasChildByCategoryId(categoryId))
            {
                return error("存在下级分类，不允许删除");
            }
        }
        return toAjax(mallCategoryService.deleteMallCategoryByCategoryIds(categoryIds));
    }
} 