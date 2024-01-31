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
import com.ruoyi.iot.domain.DeviceOtaUpdate;
import com.ruoyi.iot.service.IDeviceOtaUpdateService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 设备升级Controller
 * 
 * @author renjiayue
 * @date 2022-09-27
 */
@RestController
@RequestMapping("/iot/deviceOta")
public class DeviceOtaUpdateController extends BaseController
{
    @Autowired
    private IDeviceOtaUpdateService deviceOtaUpdateService;

    /**
     * 查询设备升级列表
     */
    @PreAuthorize("@ss.hasPermi('iot:deviceOta:list')")
    @GetMapping("/list")
    public TableDataInfo list(DeviceOtaUpdate deviceOtaUpdate)
    {
        startPage();
        List<DeviceOtaUpdate> list = deviceOtaUpdateService.selectDeviceOtaUpdateList(deviceOtaUpdate);
        return getDataTable(list);
    }

    /**
     * 导出设备升级列表
     */
    @PreAuthorize("@ss.hasPermi('iot:deviceOta:export')")
    @Log(title = "设备升级", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DeviceOtaUpdate deviceOtaUpdate)
    {
        List<DeviceOtaUpdate> list = deviceOtaUpdateService.selectDeviceOtaUpdateList(deviceOtaUpdate);
        ExcelUtil<DeviceOtaUpdate> util = new ExcelUtil<DeviceOtaUpdate>(DeviceOtaUpdate.class);
        util.exportExcel(response, list, "设备升级数据");
    }

    /**
     * 获取设备升级详细信息
     */
    @PreAuthorize("@ss.hasPermi('iot:deviceOta:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(deviceOtaUpdateService.selectDeviceOtaUpdateById(id));
    }

    /**
     * 新增设备升级
     */
    @PreAuthorize("@ss.hasPermi('iot:deviceOta:add')")
    @Log(title = "设备升级", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DeviceOtaUpdate deviceOtaUpdate)
    {
        return toAjax(deviceOtaUpdateService.insertDeviceOtaUpdate(deviceOtaUpdate));
    }

    /**
     * 修改设备升级
     */
    @PreAuthorize("@ss.hasPermi('iot:deviceOta:edit')")
    @Log(title = "设备升级", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DeviceOtaUpdate deviceOtaUpdate)
    {
        return toAjax(deviceOtaUpdateService.updateDeviceOtaUpdate(deviceOtaUpdate));
    }

    /**
     * 删除设备升级
     */
    @PreAuthorize("@ss.hasPermi('iot:deviceOta:remove')")
    @Log(title = "设备升级", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(deviceOtaUpdateService.deleteDeviceOtaUpdateByIds(ids));
    }
}
