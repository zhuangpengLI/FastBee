package com.ruoyi.iot.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.iot.domain.Device;
import com.ruoyi.iot.mobile.service.IFamilyDeviceService;
import com.ruoyi.iot.mqtt.EmqxService;
import com.ruoyi.iot.service.IAlertLogService;
import com.ruoyi.iot.service.IDeviceService;
import com.ruoyi.iot.service.IFamilyService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 设备Controller
 * 
 * @author kerwincui
 * @date 2021-12-16
 */
@Api(tags = "b管理端----网关配电管理")
@RestController
@RequestMapping("/iot/gatewayDbox")
public class GatewayDboxController extends BaseController
{
    @Autowired
    private IDeviceService deviceService;
    @Autowired
    private IFamilyDeviceService familyDeviceService;
    @Autowired
    private IFamilyService familyService;

    @Lazy
    @Autowired
    private EmqxService emqxService;
    @Autowired
    private IAlertLogService alertLogService;

    public static void main(String[] args) {
		Date date = new Date(22100904000L);
		System.out.println(date);
	}
    /**
     * 查询设备列表
     */
    @PreAuthorize("@ss.hasPermi('iot:gateway:dbox:list')")
    @GetMapping("/list")
    @ApiOperation("配电箱列表")
    public TableDataInfo list(Device device)
    {
        startPage();
        device.setDeviceType(3);//网关设备
        return getDataTable(deviceService.selectDeviceNoRoleShortList(device));
    }
    
    @PreAuthorize("@ss.hasPermi('iot:gateway:dbox:energyData')")
    @GetMapping("/energyData")
    @ApiOperation("能耗数据")
    @ApiImplicitParams({
    	@ApiImplicitParam(name="type",value="类型 1今日 2最近7天 3最近一年 4最近7天(横轴为小时) 5最近30天"),
    	@ApiImplicitParam(name="unit",value="单位 1=0.001Kw.h   2=1Kw.h"),
    })
    public AjaxResult energyData(@RequestParam("deviceId") Long deviceId,
    		@RequestParam(value="type",defaultValue = "1") Integer type
    		,@RequestParam(value="unit",defaultValue = "1") Integer unit)
    {
    	AjaxResult energyData = familyDeviceService.energyData(deviceId,type,unit);
    	return (energyData);
    }
    @PreAuthorize("@ss.hasPermi('iot:gateway:dbox:airSwitchData')")
    @GetMapping("/airSwitchData")
    @ApiOperation("配电数据")
    public AjaxResult airSwitchData(@RequestParam("deviceId") Long deviceId)
    {
    	AjaxResult a = familyDeviceService.airSwitchData(deviceId);
    	return a;
    }
    
    /**
     * 查询设备简短列表，主页列表数据
     */
    @PreAuthorize("@ss.hasPermi('iot:gateway:deviceList')")
    @GetMapping("/deviceList")
    @ApiOperation("设备分页列表")
    public TableDataInfo shortList(@RequestParam("deviceId") Long deviceId)
    {
        startPage();
        Device old = deviceService.selectDeviceByDeviceId(deviceId);
        if(old.getBelongFamilyId()==null) {
        	return getDataTable(new ArrayList<>());
        }
        Device device = new Device();
        device.setBelongFamilyId(old.getBelongFamilyId());
        device.setDeviceType(2);//网关子设备
        return getDataTable(deviceService.selectDeviceNoRoleShortList(device));
    }
}
