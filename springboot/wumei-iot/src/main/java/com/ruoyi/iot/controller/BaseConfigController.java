package com.ruoyi.iot.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.otherDomain.BaseConfig;
import com.ruoyi.system.otherService.IBaseConfigService;

/**
 * 系统参数Controller
 * 
 * @author renjiayue
 * @date 2022-09-09
 */
@RestController
@RequestMapping("/iot/baseConfig")
public class BaseConfigController extends BaseController
{
    @Autowired
    private IBaseConfigService baseConfigService;

    /**
     * 查询系统参数列表
     */
    @PreAuthorize("@ss.hasPermi('iot:baseConfig:list')")
    @GetMapping("/list")
    public TableDataInfo list(BaseConfig baseConfig)
    {
        startPage();
        List<BaseConfig> list = baseConfigService.selectBaseConfigList(baseConfig);
        return getDataTable(list);
    }

    /**
     * 导出系统参数列表
     */
    @PreAuthorize("@ss.hasPermi('iot:baseConfig:export')")
    @Log(title = "系统参数", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BaseConfig baseConfig)
    {
        List<BaseConfig> list = baseConfigService.selectBaseConfigList(baseConfig);
        ExcelUtil<BaseConfig> util = new ExcelUtil<BaseConfig>(BaseConfig.class);
        util.exportExcel(response, list, "系统参数数据");
    }

    /**
     * 获取系统参数详细信息
     */
    @PreAuthorize("@ss.hasPermi('iot:baseConfig:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(baseConfigService.selectBaseConfigById(id));
    }
    
    /**
     * 获取系统参数详细信息
     */
    @PreAuthorize("@ss.hasPermi('iot:baseConfig:query')")
    @GetMapping(value = "/getOnlyInfo")
    public AjaxResult getOnlyInfo()
    {
    	return AjaxResult.success(baseConfigService.selectOneBaseConfig());
    }

    /**
     * 新增系统参数
     */
    @PreAuthorize("@ss.hasPermi('iot:baseConfig:add')")
    @Log(title = "系统参数", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BaseConfig baseConfig)
    {
        return toAjax(baseConfigService.insertBaseConfig(baseConfig));
    }

    /**
     * 修改系统参数
     */
    @PreAuthorize("@ss.hasPermi('iot:baseConfig:edit')")
    @Log(title = "系统参数", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BaseConfig baseConfig)
    {
        return toAjax(baseConfigService.updateBaseConfig(baseConfig));
    }

    /**
     * 删除系统参数
     */
    @PreAuthorize("@ss.hasPermi('iot:baseConfig:remove')")
    @Log(title = "系统参数", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(baseConfigService.deleteBaseConfigByIds(ids));
    }
}
