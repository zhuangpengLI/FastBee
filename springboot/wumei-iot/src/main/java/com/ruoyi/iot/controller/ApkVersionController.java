package com.ruoyi.iot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.iot.domain.ApkVersion;
import com.ruoyi.iot.service.IApkVersionService;

/**
 * 升级管理Controller
 * 
 * @author renjiayue
 * @date 2022-09-09
 */
@RestController
@RequestMapping("/iot/appVersion")
public class ApkVersionController extends BaseController
{
    @Autowired
    private IApkVersionService apkVersionService;

    /**
     * 查询升级管理列表
     */
    @PreAuthorize("@ss.hasPermi('iot:appVersion:list')")
    @GetMapping("/list")
    public TableDataInfo list(ApkVersion apkVersion)
    {
        startPage();
        List<ApkVersion> list = apkVersionService.selectApkVersionList(apkVersion);
        return getDataTable(list);
    }

    /**
     * 新增升级管理
     */
    @PreAuthorize("@ss.hasPermi('iot:appVersion:add')")
    @Log(title = "升级管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ApkVersion apkVersion)
    {
    	apkVersion.setApkType("1");//客户端
        return toAjax(apkVersionService.insertApkVersion(apkVersion));
    }

    /**
     * 版本回退管理
     */
    @PreAuthorize("@ss.hasPermi('iot:appVersion:reverse')")
    @Log(title = "升级管理", businessType = BusinessType.OTHER)
	@DeleteMapping("/{apkId}")
    public AjaxResult remove(@PathVariable Long apkId)
    {
    	//TODO
        return toAjax(1);
    }
}
