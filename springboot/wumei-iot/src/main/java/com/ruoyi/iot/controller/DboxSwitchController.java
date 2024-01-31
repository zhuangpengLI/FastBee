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
import com.ruoyi.iot.domain.DboxSwitch;
import com.ruoyi.iot.service.IDboxSwitchService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 配电箱空开素材Controller
 * 
 * @author renjiayue
 * @date 2022-10-08
 */
@RestController
@RequestMapping("/iot/dboxSwitch")
public class DboxSwitchController extends BaseController
{
    @Autowired
    private IDboxSwitchService dboxSwitchService;

    /**
     * 查询配电箱空开素材列表
     */
    @PreAuthorize("@ss.hasPermi('iot:dboxSwitch:list')")
    @GetMapping("/list")
    public TableDataInfo list(DboxSwitch dboxSwitch)
    {
    	//空开素材不区分型号
    	dboxSwitch.setDboxId(null);//
    	dboxSwitch.setDboxType(null);//
        startPage();
        List<DboxSwitch> list = dboxSwitchService.selectDboxSwitchList(dboxSwitch);
        return getDataTable(list);
    }

    /**
     * 导出配电箱空开素材列表
     */
    @PreAuthorize("@ss.hasPermi('iot:dboxSwitch:export')")
    @Log(title = "配电箱空开素材", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DboxSwitch dboxSwitch)
    {
    	//空开素材不区分型号
    	dboxSwitch.setDboxId(null);//
    	dboxSwitch.setDboxType(null);//
        List<DboxSwitch> list = dboxSwitchService.selectDboxSwitchList(dboxSwitch);
        ExcelUtil<DboxSwitch> util = new ExcelUtil<DboxSwitch>(DboxSwitch.class);
        util.exportExcel(response, list, "配电箱空开素材数据");
    }

    /**
     * 获取配电箱空开素材详细信息
     */
    @PreAuthorize("@ss.hasPermi('iot:dboxSwitch:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(dboxSwitchService.selectDboxSwitchById(id));
    }

    /**
     * 新增配电箱空开素材
     */
    @PreAuthorize("@ss.hasPermi('iot:dboxSwitch:add')")
    @Log(title = "配电箱空开素材", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DboxSwitch dboxSwitch)
    {
        return toAjax(dboxSwitchService.insertDboxSwitch(dboxSwitch));
    }

    /**
     * 修改配电箱空开素材
     */
    @PreAuthorize("@ss.hasPermi('iot:dboxSwitch:edit')")
    @Log(title = "配电箱空开素材", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DboxSwitch dboxSwitch)
    {
        return toAjax(dboxSwitchService.updateDboxSwitch(dboxSwitch));
    }

    /**
     * 删除配电箱空开素材
     */
    @PreAuthorize("@ss.hasPermi('iot:dboxSwitch:remove')")
    @Log(title = "配电箱空开素材", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(dboxSwitchService.deleteDboxSwitchByIds(ids));
    }
}
