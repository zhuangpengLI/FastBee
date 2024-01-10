package com.fastbee.data.controller;

import com.fastbee.common.annotation.Log;
import com.fastbee.common.core.controller.BaseController;
import com.fastbee.common.core.domain.AjaxResult;
import com.fastbee.common.core.page.TableDataInfo;
import com.fastbee.common.enums.BusinessType;
import com.fastbee.common.utils.poi.ExcelUtil;
import com.fastbee.iot.domain.DeviceLog;
import com.fastbee.iot.model.HistoryModel;
import com.fastbee.iot.model.MonitorModel;
import com.fastbee.iot.service.IDeviceLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 设备日志Controller
 * 
 * @author kerwincui
 * @date 2022-01-13
 */
@Api(tags = "设备日志模块")
@RestController
@RequestMapping("/iot/deviceLog")
public class DeviceLogController extends BaseController
{
    @Autowired
    private IDeviceLogService deviceLogService;

    /**
     * 查询设备日志列表
     */
    @ApiOperation("查询设备日志列表")
    @PreAuthorize("@ss.hasPermi('iot:device:list')")
    @GetMapping("/list")
    public TableDataInfo list(DeviceLog deviceLog)
    {
        startPage();
        List<DeviceLog> list = deviceLogService.selectDeviceLogList(deviceLog);
        return getDataTable(list);
    }

    /**
     * 查询设备的监测数据
     */
    @ApiOperation("查询设备的监测数据")
    @PreAuthorize("@ss.hasPermi('iot:device:list')")
    @GetMapping("/monitor")
    public TableDataInfo monitorList(DeviceLog deviceLog)
    {
        List<MonitorModel> list = deviceLogService.selectMonitorList(deviceLog);
        return getDataTable(list);
    }

    /**
     * 导出设备日志列表
     */
    @ApiOperation("导出设备日志列表")
    @PreAuthorize("@ss.hasPermi('iot:device:export')")
    @Log(title = "设备日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DeviceLog deviceLog)
    {
        List<DeviceLog> list = deviceLogService.selectDeviceLogList(deviceLog);
        ExcelUtil<DeviceLog> util = new ExcelUtil<DeviceLog>(DeviceLog.class);
        util.exportExcel(response, list, "设备日志数据");
    }

    /**
     * 获取设备日志详细信息
     */
    @ApiOperation("获取设备日志详细信息")
    @PreAuthorize("@ss.hasPermi('iot:device:query')")
    @GetMapping(value = "/{logId}")
    public AjaxResult getInfo(@PathVariable("logId") Long logId)
    {
        return AjaxResult.success(deviceLogService.selectDeviceLogByLogId(logId));
    }

    /**
     * 新增设备日志
     */
    @ApiOperation("新增设备日志")
    @PreAuthorize("@ss.hasPermi('iot:device:add')")
    @Log(title = "设备日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DeviceLog deviceLog)
    {
        return toAjax(deviceLogService.insertDeviceLog(deviceLog));
    }

    /**
     * 修改设备日志
     */
    @ApiOperation("修改设备日志")
    @PreAuthorize("@ss.hasPermi('iot:device:edit')")
    @Log(title = "设备日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DeviceLog deviceLog)
    {
        return toAjax(deviceLogService.updateDeviceLog(deviceLog));
    }

    /**
     * 删除设备日志
     */
    @ApiOperation("删除设备日志")
    @PreAuthorize("@ss.hasPermi('iot:device:remove')")
    @Log(title = "设备日志", businessType = BusinessType.DELETE)
	@DeleteMapping("/{logIds}")
    public AjaxResult remove(@PathVariable Long[] logIds)
    {
        return toAjax(deviceLogService.deleteDeviceLogByLogIds(logIds));
    }

    /**
     * 查询设备的历史数据
     */
    @ApiOperation("查询设备的历史数据")
    @PreAuthorize("@ss.hasPermi('iot:device:list')")
    @GetMapping("/history")
    public AjaxResult historyList(DeviceLog deviceLog)
    {
        Map<String, List<HistoryModel>> resultMap = deviceLogService.selectHistoryList(deviceLog);
        return AjaxResult.success(resultMap);
    }
}
