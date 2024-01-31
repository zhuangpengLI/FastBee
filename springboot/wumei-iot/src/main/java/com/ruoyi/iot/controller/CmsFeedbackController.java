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
import com.ruoyi.iot.domain.CmsFeedback;
import com.ruoyi.iot.service.ICmsFeedbackService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 意见反馈Controller
 * 
 * @author renjiayue
 * @date 2022-09-07
 */
@RestController
@RequestMapping("/iot/feedback")
public class CmsFeedbackController extends BaseController
{
    @Autowired
    private ICmsFeedbackService cmsFeedbackService;

    /**
     * 查询意见反馈列表
     */
    @PreAuthorize("@ss.hasPermi('iot:feedback:list')")
    @GetMapping("/list")
    public TableDataInfo list(CmsFeedback cmsFeedback)
    {
        startPage();
        List<CmsFeedback> list = cmsFeedbackService.selectCmsFeedbackList(cmsFeedback);
        return getDataTable(list);
    }

    /**
     * 导出意见反馈列表
     */
    @PreAuthorize("@ss.hasPermi('iot:feedback:export')")
    @Log(title = "意见反馈", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CmsFeedback cmsFeedback)
    {
        List<CmsFeedback> list = cmsFeedbackService.selectCmsFeedbackList(cmsFeedback);
        ExcelUtil<CmsFeedback> util = new ExcelUtil<CmsFeedback>(CmsFeedback.class);
        util.exportExcel(response, list, "意见反馈数据");
    }

    /**
     * 获取意见反馈详细信息
     */
    @PreAuthorize("@ss.hasPermi('iot:feedback:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(cmsFeedbackService.selectCmsFeedbackById(id));
    }

    /**
     * 新增意见反馈
     */
    @PreAuthorize("@ss.hasPermi('iot:feedback:add')")
    @Log(title = "意见反馈", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CmsFeedback cmsFeedback)
    {
        return toAjax(cmsFeedbackService.insertCmsFeedback(cmsFeedback));
    }

    /**
     * 修改意见反馈
     */
    @PreAuthorize("@ss.hasPermi('iot:feedback:edit')")
    @Log(title = "意见反馈", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CmsFeedback cmsFeedback)
    {
        return toAjax(cmsFeedbackService.updateCmsFeedback(cmsFeedback));
    }

    /**
     * 删除意见反馈
     */
    @PreAuthorize("@ss.hasPermi('iot:feedback:remove')")
    @Log(title = "意见反馈", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(cmsFeedbackService.deleteCmsFeedbackByIds(ids));
    }
}
