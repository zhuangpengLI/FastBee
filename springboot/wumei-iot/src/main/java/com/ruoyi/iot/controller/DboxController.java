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
import com.ruoyi.iot.domain.Dbox;
import com.ruoyi.iot.service.IDboxService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 配电箱配置Controller
 * 
 * @author renjiayue
 * @date 2022-10-08
 */
@RestController
@RequestMapping("/iot/dbox")
public class DboxController extends BaseController
{
    @Autowired
    private IDboxService dboxService;

    /**
     * 查询配电箱配置列表
     */
    @PreAuthorize("@ss.hasPermi('iot:dbox:list')")
    @GetMapping("/list")
    public TableDataInfo list(Dbox dbox)
    {
        startPage();
        List<Dbox> list = dboxService.selectDboxList(dbox);
        return getDataTable(list);
    }

    /**
     * 导出配电箱配置列表
     */
    @PreAuthorize("@ss.hasPermi('iot:dbox:export')")
    @Log(title = "配电箱配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Dbox dbox)
    {
        List<Dbox> list = dboxService.selectDboxList(dbox);
        ExcelUtil<Dbox> util = new ExcelUtil<Dbox>(Dbox.class);
        util.exportExcel(response, list, "配电箱配置数据");
    }

    /**
     * 获取配电箱配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('iot:dbox:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(dboxService.selectDboxById(id));
    }

    /**
     * 新增配电箱配置
     */
    @PreAuthorize("@ss.hasPermi('iot:dbox:add')")
    @Log(title = "配电箱配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Dbox dbox)
    {
        return toAjax(dboxService.insertDbox(dbox));
    }

    /**
     * 修改配电箱配置
     */
    @PreAuthorize("@ss.hasPermi('iot:dbox:edit')")
    @Log(title = "配电箱配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Dbox dbox)
    {
        return toAjax(dboxService.updateDbox(dbox));
    }

    /**
     * 删除配电箱配置
     */
    @PreAuthorize("@ss.hasPermi('iot:dbox:remove')")
    @Log(title = "配电箱配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(dboxService.deleteDboxByIds(ids));
    }
}
