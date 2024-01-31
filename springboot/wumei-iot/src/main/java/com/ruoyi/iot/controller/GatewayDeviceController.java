package com.ruoyi.iot.controller;

import java.util.List;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.iot.domain.AlertLog;
import com.ruoyi.iot.domain.Device;
import com.ruoyi.iot.domain.DeviceOtaUpdate;
import com.ruoyi.iot.mobile.service.IFamilyDeviceService;
import com.ruoyi.iot.mqtt.EmqxService;
import com.ruoyi.iot.service.IAlertLogService;
import com.ruoyi.iot.service.IDeviceOtaUpdateService;
import com.ruoyi.iot.service.IDeviceService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 设备Controller
 * 
 * @author kerwincui
 * @date 2021-12-16
 */
@Api(tags = "b管理端----网关管理")
@RestController
@RequestMapping("/iot/gateway")
public class GatewayDeviceController extends BaseController
{
    @Autowired
    private IDeviceService deviceService;
    @Autowired
    private IFamilyDeviceService familyDeviceService;

    @Lazy
    @Autowired
    private EmqxService emqxService;
    @Autowired
    private IAlertLogService alertLogService;
    @Autowired
    private IDeviceOtaUpdateService deviceOtaUpdateService;
    /**
     * 查询设备列表
     */
    @PreAuthorize("@ss.hasPermi('iot:gateway:list')")
    @GetMapping("/list")
    @ApiOperation("网关分页列表")
    public TableDataInfo list(Device device)
    {
        startPage();
        device.setDeviceType(3);//网关设备
        return getDataTable(deviceService.selectNoRoleDeviceList(device));
    }
    
    /**
     * 查询设备列表
     */
    @PreAuthorize("@ss.hasPermi('iot:gateway:alertList')")
    @GetMapping("/alertList")
    @ApiOperation("设备上报数据列表")
    public TableDataInfo alertList(@RequestParam("deviceId") Long deviceId)
    {
        startPage();
        AlertLog log = new AlertLog();
        log.setDeviceId(deviceId);
		return getDataTable(alertLogService.selectAlertLogList(log ));
    }

    /**
     * 查询设备简短列表，主页列表数据
     */
    @PreAuthorize("@ss.hasPermi('iot:gateway:list')")
    @GetMapping("/shortList")
    @ApiOperation("网关设备分页简短列表")
    public TableDataInfo shortList(Device device)
    {
        startPage();
        device.setDeviceType(3);//网关设备
        return getDataTable(deviceService.selectDeviceNoRoleShortList(device));
    }
    
    @PreAuthorize("@ss.hasPermi('iot:gateway:delete')")
    @DeleteMapping("/delGateway")
    @ApiOperation("删除网关")
    public AjaxResult delGateway(@RequestParam("deviceId") Long deviceId) throws SchedulerException
    {
    	AjaxResult a = familyDeviceService.delGateway(deviceId);
    	return a;
    }
    @PreAuthorize("@ss.hasPermi('iot:gateway:unBind')")
    @PostMapping("/unBindGateway")
    @ApiOperation("解绑网关")
    public AjaxResult unBindGateway(@RequestParam("deviceId") Long deviceId) throws SchedulerException
    {
    	AjaxResult a = familyDeviceService.unBindGateway(deviceId);
    	return a;
    }
    @PreAuthorize("@ss.hasPermi('iot:gateway:restart')")
    @PostMapping("/restartGateway")
    @ApiOperation("网关重启")
    public AjaxResult restartGateway(@RequestParam("deviceId") Long deviceId)
    {
    	return AjaxResult.error("暂不支持的操作 TODO");
    }
    @PreAuthorize("@ss.hasPermi('iot:gateway:upgrade')")
    @PostMapping("/upgradeGateway")
    @ApiOperation("升级网关")
    public AjaxResult upgradeGateway(@RequestParam("deviceId") Long deviceId,@RequestParam("firmwarePath") String firmwarePath,@RequestParam("remark") String remark
    		,@RequestParam(name = "version",required = false) String version)
    {
    	return familyDeviceService.upgradeGateway(deviceId, firmwarePath,version, remark);
    }
    /**
     * 查询设备升级列表
     */
    @PreAuthorize("@ss.hasPermi('iot:gateway:upgradeRecord')")
    @ApiOperation("网关升级记录")
    @GetMapping("/upgradeList")
    public TableDataInfo upgradeList(@RequestParam("deviceId") Long deviceId)
    {
    	DeviceOtaUpdate deviceOtaUpdate = new DeviceOtaUpdate();
//    	deviceOtaUpdate.setStatus(1);//升级成功的数据
//    	deviceOtaUpdate.setFamilyId(familyId);
    	deviceOtaUpdate.setDeviceId(deviceId);
        startPage();
        List<DeviceOtaUpdate> list = deviceOtaUpdateService.selectDeviceOtaUpdateList(deviceOtaUpdate);
        return getDataTable(list);
    }
    
    @PreAuthorize("@ss.hasPermi('iot:gateway:batchUpgrade')")
    @PostMapping("/batchUpgradeGateway")
    @ApiOperation("批量升级网关")
    public AjaxResult batchUpgradeGateway(@RequestParam("firmwarePath") String firmwarePath,@RequestParam("remark") String remark
    		,@RequestParam(name = "version",required = false) String version)
    {
    	return familyDeviceService.upgradeAllGateway(firmwarePath, version, remark);
    }
}
