package com.ruoyi.iot.mobile.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
import com.ruoyi.iot.domain.Family;
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
@Api(tags = "a移动端----网关配电管理")
@RestController
@RequestMapping("/mobile/gatewayDbox")
public class MobileGatewayDboxController extends BaseController
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

    @GetMapping("/energyData")
    @ApiOperation("能耗数据")
    @ApiImplicitParams({
    	@ApiImplicitParam(name="type",value="类型 1今日 2最近7天 3最近一年  4最近7天(横轴为小时) 5最近30天"),
    	@ApiImplicitParam(name="unit",value="单位 1=0.001Kw.h   2=1Kw.h"),
    })
    public AjaxResult energyData(@RequestParam("familyId") Long familyId,
    		@RequestParam(value="type",defaultValue = "1") Integer type
    		,@RequestParam(value="unit",defaultValue = "1") Integer unit)
    {
    	Family family = familyService.selectFamilyOnlyByFamilyId(familyId);
    	String gatewaySn = family.getGatewaySn();
    	if(StringUtils.isBlank(gatewaySn)) {
    		return AjaxResult.error("家庭未绑定网关");
    	}
    	Device device = deviceService.selectDeviceBySerialNumber(gatewaySn);
    	AjaxResult energyData = familyDeviceService.energyData(device.getDeviceId(),type,unit);
    	return (energyData);
    }
    @GetMapping("/energyDataByDay")
    @ApiOperation("能耗数据按日查询")
    @ApiImplicitParams({
    	@ApiImplicitParam(name="year",value="年  例如 2022"),
    	@ApiImplicitParam(name="month",value="月 例如 1"),
    	@ApiImplicitParam(name="day",value="日 例如 28"),
    })
    public AjaxResult energyDataByDay(@RequestParam("familyId") Long familyId,
    		@RequestParam(value="year",defaultValue = "2022") Integer year
    		,@RequestParam(value="month",defaultValue = "1") Integer month
    		,@RequestParam(value="day",defaultValue = "1") Integer day)
    {
    	Family family = familyService.selectFamilyOnlyByFamilyId(familyId);
    	String gatewaySn = family.getGatewaySn();
    	if(StringUtils.isBlank(gatewaySn)) {
    		return AjaxResult.error("家庭未绑定网关");
    	}
    	Device device = deviceService.selectDeviceBySerialNumber(gatewaySn);
    	AjaxResult energyData = familyDeviceService.energyDataByDay(device.getDeviceId(),year,month,day);
    	return energyData;
    }
    @GetMapping("/energyDataByMonth")
    @ApiOperation("能耗数据按月查询")
    @ApiImplicitParams({
    	@ApiImplicitParam(name="year",value="年  例如 2022"),
    	@ApiImplicitParam(name="month",value="月 例如 1"),
    })
    public AjaxResult energyDataByMonth(@RequestParam("familyId") Long familyId,
    		@RequestParam(value="year",defaultValue = "2022") Integer year
    		,@RequestParam(value="month",defaultValue = "1") Integer month)
    {
    	Family family = familyService.selectFamilyOnlyByFamilyId(familyId);
    	String gatewaySn = family.getGatewaySn();
    	if(StringUtils.isBlank(gatewaySn)) {
    		return AjaxResult.error("家庭未绑定网关");
    	}
    	Device device = deviceService.selectDeviceBySerialNumber(gatewaySn);
    	AjaxResult energyData = familyDeviceService.energyDataByMonth(device.getDeviceId(),year,month);
    	return energyData;
    }
    @GetMapping("/energyDataByYear")
    @ApiOperation("能耗数据按年查询")
    @ApiImplicitParams({
    	@ApiImplicitParam(name="year",value="年  例如 2022"),
    })
    public AjaxResult energyDataByYear(@RequestParam("familyId") Long familyId,
    		@RequestParam(value="year",defaultValue = "2022") Integer year)
    {
    	Family family = familyService.selectFamilyOnlyByFamilyId(familyId);
    	String gatewaySn = family.getGatewaySn();
    	if(StringUtils.isBlank(gatewaySn)) {
    		return AjaxResult.error("家庭未绑定网关");
    	}
    	Device device = deviceService.selectDeviceBySerialNumber(gatewaySn);
    	AjaxResult energyData = familyDeviceService.energyDataByYear(device.getDeviceId(),year);
    	return (energyData);
    }
    @GetMapping("/airSwitchData")
    @ApiOperation("配电数据")
    public AjaxResult airSwitchData(@RequestParam("familyId") Long familyId)
    {
    	Family family = familyService.selectFamilyOnlyByFamilyId(familyId);
    	String gatewaySn = family.getGatewaySn();
    	if(StringUtils.isBlank(gatewaySn)) {
    		return AjaxResult.error("家庭网关信息有误");
    	}
    	Device device = deviceService.selectDeviceBySerialNumber(gatewaySn);
    	AjaxResult a = familyDeviceService.airSwitchData(device.getDeviceId());
    	return a;
    }
}
