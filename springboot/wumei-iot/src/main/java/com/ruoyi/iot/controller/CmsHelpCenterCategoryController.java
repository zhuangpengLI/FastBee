package com.ruoyi.iot.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
import com.ruoyi.iot.domain.CmsHelpCenterCategory;
import com.ruoyi.iot.service.ICmsHelpCenterCategoryService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 帮助分类Controller
 * 
 * @author renjiayue
 * @date 2022-09-09
 */
@RestController
@RequestMapping("/iot/helpCenterCategory")
public class CmsHelpCenterCategoryController extends BaseController
{
    @Autowired
    private ICmsHelpCenterCategoryService cmsHelpCenterCategoryService;

    /**
     * 查询帮助分类列表
     */
    @PreAuthorize("@ss.hasPermi('iot:helpCenterCategory:list')")
    @GetMapping("/list")
    public TableDataInfo list(CmsHelpCenterCategory cmsHelpCenterCategory)
    {
        startPage();
        List<CmsHelpCenterCategory> list = cmsHelpCenterCategoryService.selectCmsHelpCenterCategoryList(cmsHelpCenterCategory);
        return getDataTable(list);
    }
    
    /**
     * 查询帮助分类列表
     */
//    @PreAuthorize("@ss.hasPermi('iot:helpCenter:list')")
    @GetMapping("/categoryListNoPage")
    public TableDataInfo categoryListNoPage(CmsHelpCenterCategory cmsHelpCenterCategory)
    {
        List<CmsHelpCenterCategory> list = cmsHelpCenterCategoryService.selectCmsHelpCenterCategoryList(cmsHelpCenterCategory);
        return getDataTable(list);
    }

    /**
     * 导出帮助分类列表
     */
    @PreAuthorize("@ss.hasPermi('iot:helpCenterCategory:export')")
    @Log(title = "帮助分类", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CmsHelpCenterCategory cmsHelpCenterCategory)
    {
        List<CmsHelpCenterCategory> list = cmsHelpCenterCategoryService.selectCmsHelpCenterCategoryList(cmsHelpCenterCategory);
        ExcelUtil<CmsHelpCenterCategory> util = new ExcelUtil<CmsHelpCenterCategory>(CmsHelpCenterCategory.class);
        util.exportExcel(response, list, "帮助分类数据");
    }

    /**
     * 获取帮助分类详细信息
     */
    @PreAuthorize("@ss.hasPermi('iot:helpCenterCategory:query')")
    @GetMapping(value = "/{categoryId}")
    public AjaxResult getInfo(@PathVariable("categoryId") Long categoryId)
    {
        return AjaxResult.success(cmsHelpCenterCategoryService.selectCmsHelpCenterCategoryByCategoryId(categoryId));
    }

    /**
     * 新增帮助分类
     */
    @PreAuthorize("@ss.hasPermi('iot:helpCenterCategory:add')")
    @Log(title = "帮助分类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CmsHelpCenterCategory cmsHelpCenterCategory)
    {
        return toAjax(cmsHelpCenterCategoryService.insertCmsHelpCenterCategory(cmsHelpCenterCategory));
    }

    /**
     * 修改帮助分类
     */
    @PreAuthorize("@ss.hasPermi('iot:helpCenterCategory:edit')")
    @Log(title = "帮助分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CmsHelpCenterCategory cmsHelpCenterCategory)
    {
        return toAjax(cmsHelpCenterCategoryService.updateCmsHelpCenterCategory(cmsHelpCenterCategory));
    }

    /**
     * 删除帮助分类
     */
    @PreAuthorize("@ss.hasPermi('iot:helpCenterCategory:remove')")
    @Log(title = "帮助分类", businessType = BusinessType.DELETE)
	@DeleteMapping("/{categoryIds}")
    public AjaxResult remove(@PathVariable Long[] categoryIds)
    {
        return toAjax(cmsHelpCenterCategoryService.deleteCmsHelpCenterCategoryByCategoryIds(categoryIds));
    }
}
