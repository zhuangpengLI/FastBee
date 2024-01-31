package com.ruoyi.iot.mobile.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.iot.domain.AlertLog;
import com.ruoyi.iot.domain.DeviceLog;
import com.ruoyi.iot.domain.FamilyUserRela;
import com.ruoyi.iot.dto.AlertLogAndFamilyWithUser;
import com.ruoyi.iot.service.IAlertLogService;
import com.ruoyi.iot.service.IDeviceLogService;
import com.ruoyi.iot.service.IFamilyService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 设备日志Controller
 * 
 * @author kerwincui
 * @date 2022-01-13
 */
@Api(tags="a移动端----设备日志相关")
@RestController
@RequestMapping("/mobile/deviceLog")
public class MobileDeviceLogController extends BaseController
{
    @Autowired
    private IDeviceLogService deviceLogService;
    
    @Autowired
    private IAlertLogService alertLogService;
    
    @Autowired
    private IFamilyService familyService;

    /**
     * 查询设备日志列表
     */
    @ApiOperation("查询设备日志")
    @GetMapping("/logList")
    public TableDataInfo logList(@RequestParam("deviceId") Long deviceId
    		,@RequestParam(value = "logType",defaultValue = "2") Integer logType
    		,@RequestParam(value="beginTime",required = false) String beginTime
    		,@RequestParam(value="endTime",required = false) String endTime)
    {
    	DeviceLog deviceLog = new DeviceLog();
    	deviceLog.setDeviceId(deviceId);
    	deviceLog.setLogType(logType);//2是操控日志
    	Map<String, Object> params = deviceLog.getParams();
    	if(beginTime!=null && beginTime.length()==10 && StringUtils.isBlank(endTime)) {
    		endTime = beginTime+" 23:59:59";
    		beginTime = beginTime+" 00:00:00";
    	}
    	params.put("beginCreateTime",beginTime);
    	params.put("endCreateTime",endTime);
        startPage();
        List<DeviceLog> list = deviceLogService.selectDeviceLogListWithUserInfo(deviceLog);
        return getDataTable(list);
    }

    @ApiOperation("查询设备报警日志")
    @GetMapping("/alertLogList")
    public TableDataInfo alertLogList(@RequestParam("deviceId") Long deviceId,@RequestParam(value="beginTime",required = false) String beginTime
    		,@RequestParam(value="endTime",required = false) String endTime)
    {
	  	AlertLog alertLog = new AlertLog();
	  	alertLog.setAlertLevel(98L);//报警 (99 是故障)
		alertLog.setDeviceId(deviceId);
		Map<String, Object> params = alertLog.getParams();
		if(beginTime!=null && beginTime.length()==10 && StringUtils.isBlank(endTime)) {
    		endTime = beginTime+" 23:59:59";
    		beginTime = beginTime+" 00:00:00";
    	}
    	params.put("beginCreateTime",beginTime);
    	params.put("endCreateTime",endTime);
        startPage();
        List<AlertLog> list = alertLogService.selectAlertLogList(alertLog);
        return getDataTable(list);
    }
    
    @ApiOperation("查询设备日志及报警日志")
    @GetMapping("/allLogList")
    public TableDataInfo allLogList(@RequestParam(value="deviceId",required = false) Long deviceId
    		,@RequestParam(value="beginTime",required = false) String beginTime
    		,@RequestParam(value="familyId",required = false) Long familyId
    		,@RequestParam(value="roomId",required = false) Long roomId
    		,@RequestParam(value="deviceName",required = false) String deviceName
    		,@RequestParam(value="endTime",required = false) String endTime)
    {
    	DeviceLog deviceLog = new DeviceLog();
    	deviceLog.setDeviceId(deviceId);
    	deviceLog.setDeviceName(deviceName);
    	deviceLog.setRoomId(roomId);
    	deviceLog.setFamilyId(familyId);
    	List<FamilyUserRela> list2 = familyService.selectUserListByFamilyIdAndUserId(null,getUserId());
    	List<Long> familyIds = list2.stream().map(FamilyUserRela::getFamilyId).collect(Collectors.toList());
    	deviceLog.setFamilyIds(familyIds);
    	Map<String, Object> params = deviceLog.getParams();
    	if(beginTime!=null && beginTime.length()==10 && StringUtils.isBlank(endTime)) {
    		endTime = beginTime+" 23:59:59";
    		beginTime = beginTime+" 00:00:00";
    	}
    	params.put("beginCreateTime",beginTime);
    	params.put("endCreateTime",endTime);
        startPage();
        List<DeviceLog> list = deviceLogService.selectAllDeviceLogListWithUserInfo(deviceLog);
        return getDataTable(list);
    }

    
    
    /**
     * 查询设备的监测数据
     */
//    @GetMapping("/monitor")
//    public TableDataInfo monitorList(DeviceLog deviceLog)
//    {
//        List<MonitorModel> list = deviceLogService.selectMonitorList(deviceLog);
//        return getDataTable(list);
//    }

}
