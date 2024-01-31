package com.ruoyi.iot.mobile.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.iot.mobile.constant.IotOperMsgConstant;
import com.ruoyi.iot.mobile.model.UpdateDeviceBaseInfoReqDto;
import com.ruoyi.iot.mobile.service.IFamilyDeviceService;
import com.ruoyi.iot.service.IDeviceService;
import com.ruoyi.iot.service.IFamilyService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 家庭管理Controller
 * 
 * @author renjiayue
 * @date 2022-09-07
 */
@Api(tags="a移动端----家庭设备操作相关接口")
@RestController
@RequestMapping("/mobile/deviceOper")
public class MobileDeviceOperController extends BaseController
{
    @Autowired
    private IFamilyService familyService;
    @Autowired
    private IFamilyDeviceService familyDeviceService;
    @Autowired
    private IDeviceService deviceService;


    @ApiOperation("网关进入添加设备模式")
    @ApiImplicitParams({
    	@ApiImplicitParam(name="familyId",value="家庭id"),
    	@ApiImplicitParam(name="au",value="设备唯一编码即app唯一编码 建议使用imei 用于确认请求后websocket推送的数据是否为本客户端")
    })
    @GetMapping("/startAddDevice")
    public AjaxResult startAddDevice(@RequestParam("familyId") Long familyId,@RequestParam("au") String au)
    {
    	if(familyId==null){
    		return AjaxResult.error("家庭信息为空");
    	}
    	return familyDeviceService.startAddDevice(familyId,getUserId(),au);
    }
    @ApiOperation("网关退出添加设备模式")
    @ApiImplicitParams({
    	@ApiImplicitParam(name="familyId",value="家庭id"),
    	@ApiImplicitParam(name="au",value="设备唯一编码即app唯一编码 建议使用imei 用于确认请求后websocket推送的数据是否为本客户端")
    })
    @DeleteMapping("/endAddDevice")
    public AjaxResult endAddDevice(@RequestParam("familyId") Long familyId,@RequestParam("au") String au)
    {
    	if(familyId==null){
    		return AjaxResult.error("家庭信息为空");
    	}
    	return familyDeviceService.endAddDevice(familyId,getUserId(),au);
    }
    @ApiOperation("获取所有设备(网关)最新信息----打开app后需每分钟调用一次   才可以进行设备操作->app结果推送")
    @ApiImplicitParams({
    	@ApiImplicitParam(name="familyId",value="家庭id"),
    	@ApiImplicitParam(name="au",value="设备唯一编码即app唯一编码 建议使用imei 用于确认请求后websocket推送的数据是否为本客户端")
    })
    @GetMapping("/getAllDeviceGwInfo")
    public AjaxResult getAllDeviceGwInfo(@RequestParam("familyId") Long familyId,@RequestParam("au") String au)
    {
    	if(familyId==null){
    		return AjaxResult.error("家庭信息为空");
    	}
    	return familyDeviceService.getAllDeviceGwInfo(familyId,au,getUserId());
    }
    
    @ApiOperation("添加设备(编辑设备基本信息)---"
    		+ "网关会自动添加设备 在这里仅需要设置 设备名称(总名称而非单键名称),归属房间,是否常用")
    @PostMapping("/editDevice")
    public AjaxResult edit(@RequestBody @Valid UpdateDeviceBaseInfoReqDto dto)
    {
        return familyDeviceService.updateDeviceBaseInfo(dto, getUserId());
    }
    
	@ApiOperation("删除设备")
	@ApiImplicitParam(name="deviceId",value="设备id")
	@DeleteMapping("/delete")
	public AjaxResult remove(@RequestParam("deviceId") Long deviceId,@RequestParam("au") String au)
	{
		return familyDeviceService.deleteDeviceReq(deviceId,getUserId(),au);
	}
	
	@ApiOperation("设备模型 名称修改")
	@ApiImplicitParams({
		@ApiImplicitParam(name="deviceId",value="设备id"),
		@ApiImplicitParam(name="value",value="设备名称对应物模型value"),
	})
	@PutMapping("/updateDeviceModelName")
	public AjaxResult updateDeviceModelName(@RequestParam("deviceId") Long deviceId,@RequestParam("value") String value
			)
	{
		//TODO 权限校验
		return familyDeviceService.updateDeviceModelName(deviceId, getUserId()
				,value);
	}
	@ApiOperation("设备功能调用")
	@ApiImplicitParams({
		@ApiImplicitParam(name="deviceId",value="设备id"),
		@ApiImplicitParam(name="au",value="操作设备标识符 18位以下 建议imei"),
		@ApiImplicitParam(name="param",value="设备对应物模型id"),
		@ApiImplicitParam(name="value",value="设备对应物模型value"),
		@ApiImplicitParam(name="fid",value="操作设备流水号(可用来解析结果)"),
	})
    @PutMapping("/operDeviceFun")
    public AjaxResult operDeviceFun(@RequestParam("deviceId") Long deviceId,@RequestParam("au") String au
    		,@RequestParam("param") String param,@RequestParam("value") String value
    		,@RequestParam("fid") Long fid)
    {
		//TODO 权限校验
        return familyDeviceService.operDeviceFun(deviceId, getUserId(),au
        		,param,value,fid);
    }
	@ApiOperation("设置设备常用参数")
	@ApiImplicitParams({
		@ApiImplicitParam(name="deviceId",value="设备id"),
		@ApiImplicitParam(name="au",value="操作设备标识符 18位以下 建议imei"),
		@ApiImplicitParam(name="param",value="设备对应物模型id"),
		@ApiImplicitParam(name="value",value="设备对应物模型value"),
//		@ApiImplicitParam(name="fid",value="操作设备流水号(可用来解析结果)"),
	})
    @PutMapping("/setDeviceNormalParam")
    public AjaxResult setDeviceParam(@RequestParam("deviceId") Long deviceId,@RequestParam("au") String au
    		,@RequestParam("param") String param,@RequestParam("value") String value)
    {
		//TODO 权限校验
        return familyDeviceService.setDeviceParamReq(deviceId, getUserId(),au,IotOperMsgConstant.COMMON_PARMAS_ITEM_NORMAL
        		,param,value);
    }
	@ApiOperation("获取设备常用参数")
	@ApiImplicitParams({
		@ApiImplicitParam(name="deviceId",value="设备id"),
		@ApiImplicitParam(name="au",value="操作设备标识符 18位以下 建议imei"),
//		@ApiImplicitParam(name="fid",value="操作设备流水号(可用来解析结果)"),
	})
	@GetMapping("/getDeviceNormalParam")
	public AjaxResult getDeviceNormalParam(@RequestParam("deviceId") Long deviceId,@RequestParam("au") String au)
	{
		//TODO 权限校验
		return familyDeviceService.getDeviceParamReq(deviceId, getUserId(),au,IotOperMsgConstant.COMMON_PARMAS_ITEM_NORMAL);
	}
	@ApiOperation("设置设备语音参数")
	@ApiImplicitParams({
		@ApiImplicitParam(name="deviceId",value="设备id"),
		@ApiImplicitParam(name="au",value="操作设备标识符 18位以下 建议imei"),
		@ApiImplicitParam(name="param",value="设备对应物模型id"),
		@ApiImplicitParam(name="value",value="设备对应物模型value"),
//		@ApiImplicitParam(name="fid",value="操作设备流水号(可用来解析结果)"),
	})
	@PutMapping("/setDeviceAfdParam")
	public AjaxResult setDeviceAfdParam(@RequestParam("deviceId") Long deviceId,@RequestParam("au") String au
			,@RequestParam("param") String param,@RequestParam("value") String value)
	{
		//TODO 权限校验
		return familyDeviceService.setDeviceParamReq(deviceId, getUserId(),au,IotOperMsgConstant.COMMON_PARMAS_ITEM_AFD
				,param,value);
	}
	@ApiOperation("获取设备语音参数")
	@ApiImplicitParams({
		@ApiImplicitParam(name="deviceId",value="设备id"),
		@ApiImplicitParam(name="au",value="操作设备标识符 18位以下 建议imei"),
//		@ApiImplicitParam(name="fid",value="操作设备流水号(可用来解析结果)"),
	})
	@GetMapping("/getDeviceAfdParam")
	public AjaxResult getDeviceAfdParam(@RequestParam("deviceId") Long deviceId,@RequestParam("au") String au)
	{
		//TODO 权限校验
		return familyDeviceService.getDeviceParamReq(deviceId, getUserId(),au,IotOperMsgConstant.COMMON_PARMAS_ITEM_AFD);
	}
}
