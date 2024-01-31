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
import com.ruoyi.iot.domain.DboxEle;
import com.ruoyi.iot.service.IDboxEleService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 网关电量统计Controller
 * 
 * @author renjiayue
 * @date 2023-08-02
 */
@RestController
//@RequestMapping("/iot/dboxEle")
public class DboxEleController extends BaseController
{
    @Autowired
    private IDboxEleService dboxEleService;

    /**
     * 查询网关电量统计列表
     */
    @PreAuthorize("@ss.hasPermi('iot:dboxEle:list')")
    @GetMapping("/list")
    public TableDataInfo list(DboxEle dboxEle)
    {
        startPage();
        List<DboxEle> list = dboxEleService.selectDboxEleList(dboxEle);
        return getDataTable(list);
    }

    /**
     * 导出网关电量统计列表
     */
    @PreAuthorize("@ss.hasPermi('iot:dboxEle:export')")
    @Log(title = "网关电量统计", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DboxEle dboxEle)
    {
        List<DboxEle> list = dboxEleService.selectDboxEleList(dboxEle);
        ExcelUtil<DboxEle> util = new ExcelUtil<DboxEle>(DboxEle.class);
        util.exportExcel(response, list, "网关电量统计数据");
    }

    /**
     * 获取网关电量统计详细信息
     */
    @PreAuthorize("@ss.hasPermi('iot:dboxEle:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(dboxEleService.selectDboxEleById(id));
    }

    /**
     * 新增网关电量统计
     */
    @PreAuthorize("@ss.hasPermi('iot:dboxEle:add')")
    @Log(title = "网关电量统计", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DboxEle dboxEle)
    {
        return toAjax(dboxEleService.insertDboxEle(dboxEle));
    }

    /**
     * 修改网关电量统计
     */
    @PreAuthorize("@ss.hasPermi('iot:dboxEle:edit')")
    @Log(title = "网关电量统计", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DboxEle dboxEle)
    {
        return toAjax(dboxEleService.updateDboxEle(dboxEle));
    }

    /**
     * 删除网关电量统计
     */
    @PreAuthorize("@ss.hasPermi('iot:dboxEle:remove')")
    @Log(title = "网关电量统计", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(dboxEleService.deleteDboxEleByIds(ids));
    }
}
