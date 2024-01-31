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
import com.ruoyi.iot.domain.CmsUserAgreement;
import com.ruoyi.iot.service.ICmsUserAgreementService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 用户协议Controller
 * 
 * @author renjiayue
 * @date 2022-09-09
 */
@RestController
@RequestMapping("/iot/agreement")
public class CmsUserAgreementController extends BaseController
{
    @Autowired
    private ICmsUserAgreementService cmsUserAgreementService;

    /**
     * 查询用户协议列表
     */
    @PreAuthorize("@ss.hasPermi('iot:agreement:list')")
    @GetMapping("/list")
    public TableDataInfo list(CmsUserAgreement cmsUserAgreement)
    {
        startPage();
        List<CmsUserAgreement> list = cmsUserAgreementService.selectCmsUserAgreementList(cmsUserAgreement);
        return getDataTable(list);
    }

    /**
     * 导出用户协议列表
     */
    @PreAuthorize("@ss.hasPermi('iot:agreement:export')")
    @Log(title = "用户协议", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CmsUserAgreement cmsUserAgreement)
    {
        List<CmsUserAgreement> list = cmsUserAgreementService.selectCmsUserAgreementList(cmsUserAgreement);
        ExcelUtil<CmsUserAgreement> util = new ExcelUtil<CmsUserAgreement>(CmsUserAgreement.class);
        util.exportExcel(response, list, "用户协议数据");
    }

    /**
     * 获取用户协议详细信息
     */
    @PreAuthorize("@ss.hasPermi('iot:agreement:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(cmsUserAgreementService.selectCmsUserAgreementById(id));
    }

    /**
     * 新增用户协议
     */
    @PreAuthorize("@ss.hasPermi('iot:agreement:add')")
    @Log(title = "用户协议", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CmsUserAgreement cmsUserAgreement)
    {
        return toAjax(cmsUserAgreementService.insertCmsUserAgreement(cmsUserAgreement));
    }

    /**
     * 修改用户协议
     */
    @PreAuthorize("@ss.hasPermi('iot:agreement:edit')")
    @Log(title = "用户协议", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CmsUserAgreement cmsUserAgreement)
    {
        return toAjax(cmsUserAgreementService.updateCmsUserAgreement(cmsUserAgreement));
    }

    /**
     * 删除用户协议
     */
    @PreAuthorize("@ss.hasPermi('iot:agreement:remove')")
    @Log(title = "用户协议", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(cmsUserAgreementService.deleteCmsUserAgreementByIds(ids));
    }
}
