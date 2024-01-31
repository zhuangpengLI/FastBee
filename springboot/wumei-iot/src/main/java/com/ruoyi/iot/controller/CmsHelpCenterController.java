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
import com.ruoyi.iot.domain.CmsHelpCenter;
import com.ruoyi.iot.domain.CmsHelpCenterCategory;
import com.ruoyi.iot.service.ICmsHelpCenterCategoryService;
import com.ruoyi.iot.service.ICmsHelpCenterService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 帮助中心内容Controller
 * 
 * @author renjiayue
 * @date 2022-09-09
 */
@RestController
@RequestMapping("/iot/helpCenter")
public class CmsHelpCenterController extends BaseController
{
    @Autowired
    private ICmsHelpCenterService cmsHelpCenterService;
    @Autowired
    private ICmsHelpCenterCategoryService cmsHelpCenterCategoryService;

    /**
     * 查询帮助中心内容列表
     */
    @PreAuthorize("@ss.hasPermi('iot:helpCenter:list')")
    @GetMapping("/list")
    public TableDataInfo list(CmsHelpCenter cmsHelpCenter)
    {
        startPage();
        List<CmsHelpCenter> list = cmsHelpCenterService.selectCmsHelpCenterList(cmsHelpCenter);
        return getDataTable(list);
    }
    
    /**
     * 导出帮助中心内容列表
     */
    @PreAuthorize("@ss.hasPermi('iot:helpCenter:export')")
    @Log(title = "帮助中心内容", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CmsHelpCenter cmsHelpCenter)
    {
        List<CmsHelpCenter> list = cmsHelpCenterService.selectCmsHelpCenterList(cmsHelpCenter);
        ExcelUtil<CmsHelpCenter> util = new ExcelUtil<CmsHelpCenter>(CmsHelpCenter.class);
        util.exportExcel(response, list, "帮助中心内容数据");
    }

    /**
     * 获取帮助中心内容详细信息
     */
    @PreAuthorize("@ss.hasPermi('iot:helpCenter:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(cmsHelpCenterService.selectCmsHelpCenterById(id));
    }

    /**
     * 新增帮助中心内容
     */
    @PreAuthorize("@ss.hasPermi('iot:helpCenter:add')")
    @Log(title = "帮助中心内容", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CmsHelpCenter cmsHelpCenter)
    {
        return toAjax(cmsHelpCenterService.insertCmsHelpCenter(cmsHelpCenter));
    }

    /**
     * 修改帮助中心内容
     */
    @PreAuthorize("@ss.hasPermi('iot:helpCenter:edit')")
    @Log(title = "帮助中心内容", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CmsHelpCenter cmsHelpCenter)
    {
        return toAjax(cmsHelpCenterService.updateCmsHelpCenter(cmsHelpCenter));
    }

    /**
     * 删除帮助中心内容
     */
    @PreAuthorize("@ss.hasPermi('iot:helpCenter:remove')")
    @Log(title = "帮助中心内容", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(cmsHelpCenterService.deleteCmsHelpCenterByIds(ids));
    }
}
