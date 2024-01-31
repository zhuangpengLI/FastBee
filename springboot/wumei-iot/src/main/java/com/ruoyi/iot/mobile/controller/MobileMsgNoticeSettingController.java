package com.ruoyi.iot.mobile.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.iot.domain.MsgNoticeSetting;
import com.ruoyi.iot.mobile.respModel.DeviceNoticeSetting;
import com.ruoyi.iot.mobile.service.IFamilyDeviceService;
import com.ruoyi.iot.service.IFamilyService;
import com.ruoyi.iot.service.IMsgNoticeSettingService;
import com.ruoyi.system.otherDto.MsgSettingEnum;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 消息设置Controller
 * 
 * @author renjiayue
 * @date 2022-10-21
 */
@Api(tags="aaa移动端----消息通知设置接口")
@RestController
@RequestMapping("/mobile/msgSetting")
public class MobileMsgNoticeSettingController extends BaseController
{
    @Autowired
    private IMsgNoticeSettingService msgNoticeSettingService;
    @Autowired
    private IFamilyDeviceService familyDeviceService;
    @Autowired
    private IFamilyService familyService;

    @ApiOperation("查询子级设置列表")
    @ApiImplicitParams({
    	@ApiImplicitParam(name="parentIdentifier",value = "暂时只支持 空 和 SYS_TOTAL,空查询的是所有最外层设置,SYS_TOTAL 查询的是系统通知下的子级通知"),
    })
    @GetMapping("/childList")
    public TableDataInfo childList(@RequestParam(value="parentIdentifier" ,required = false) String parentIdentifier)
    {
    	MsgSettingEnum query = null;
    	if(parentIdentifier==null) {
    		query = null;
    	}else {
    		MsgSettingEnum settingEnum = MsgSettingEnum.selectSettingByIdentifier(parentIdentifier);
    		if(settingEnum==null) {
    			return  getDataTable(new ArrayList<MsgNoticeSetting>());
    		}else {
    			query = settingEnum;
    		}
    	}
    	if(MsgSettingEnum.DEV_TOTAL.equals(query)) {
    		logger.error("暂不支持设备总通知下的所有家庭数据查询");
    		return  getDataTable(new ArrayList<MsgNoticeSetting>());
    	}
    	if(MsgSettingEnum.DEV_FAMILY.equals(query)) {
    		logger.error("暂不支持家庭总通知下的所有设备数据查询");
    		return  getDataTable(new ArrayList<MsgNoticeSetting>());
    	}
    	if(MsgSettingEnum.DEV_DEVICE.equals(query)) {
    		logger.error("暂不支持单个设备通知下的数据查询--不会有数据");
    		return  getDataTable(new ArrayList<MsgNoticeSetting>());
    	}
    	List<MsgSettingEnum> childs = MsgSettingEnum.selectChildrenSetting(query);
        List<MsgNoticeSetting> list = new ArrayList<MsgNoticeSetting>();
        for(MsgSettingEnum e:childs) {
        	MsgNoticeSetting setting = null;
        	if(setting==null) {
        		//未设置的都是没禁用的
        		setting = new MsgNoticeSetting();
        		setting.setIdentifier(e.getIdentifier());
        		setting.setSettingName(e.getIdentifierName());
        		setting.setIsDisabled(0);//未禁用
        	}
			list.add(setting );
        }
        return getDataTable(list);
    }
    @ApiOperation("查询设备设置列表")
    @ApiImplicitParams({
    	@ApiImplicitParam(name="familyId",value = "家庭id"),
    })
    @GetMapping("/deviceList")
    public TableDataInfo deviceList(@RequestParam(value="familyId" ,required = true) Long familyId)
    {
    	boolean perm = familyService.isCommonPerm(familyId, getUserId());
    	if(!perm) {
    		logger.error("权限不足");
    		return  getDataTable(new ArrayList<MsgNoticeSetting>());
    	}
    	DeviceNoticeSetting device = new DeviceNoticeSetting();
    	device.setSettingUserId(getUserId());
    	device.setBelongFamilyId(familyId);
    	device.setIdentifier(MsgSettingEnum.DEV_DEVICE.getIdentifier());
    	startPage();
		List<DeviceNoticeSetting> list = familyDeviceService.selectDeviceNoticeSettingList(device);
    	return getDataTable(list);
    }


    /**
     * 修改消息设置
     */
    @ApiOperation("设置通知状态")
    @ApiImplicitParams({
    	@ApiImplicitParam(name="identifier",value = "标识符 SYS_TOTAL(系统通知) SYS_FAMILY_SHARE(家庭共享) SYS_DEVICE_SHARE(设备共享) SYS_OFFICAL(官方消息) DEV_TOTAL(设备通知) DEV_FAMILY(设备通知-家庭) DEV_DEVICE(设备通知-家庭-设备)"),
    	@ApiImplicitParam(name="busId",value = "业务id  DEV_FAMILY,DEV_DEVICE 需要传家庭id/设备id"),
    	@ApiImplicitParam(name="isDisabled",value = "是否禁用通知 0否 1是"),
    })
    @PutMapping("/edit")
    public AjaxResult edit(@RequestParam("identifier")String identifier
    		,@RequestParam(value="busId",required = false)String busId
    		,@RequestParam("isDisabled")Integer isDisabled)
    {
    	if(identifier==null) {
    		return AjaxResult.error("设置失败");
    	}else {
    		MsgSettingEnum settingEnum = MsgSettingEnum.selectSettingByIdentifier(identifier);
    		if(settingEnum==null) {
    			return AjaxResult.error("设置失败");
    		}else {
    			return msgNoticeSettingService.updateMsgNoticeSetting(settingEnum, busId, isDisabled, getUserId());
    		}
    	}
    }
    
    @ApiOperation("查询单个通知状态")
    @ApiImplicitParams({
    	@ApiImplicitParam(name="identifier",value = "标识符 SYS_TOTAL(系统通知) SYS_FAMILY_SHARE(家庭共享) SYS_DEVICE_SHARE(设备共享) SYS_OFFICAL(官方消息) DEV_TOTAL(设备通知) DEV_FAMILY(设备通知-家庭) DEV_DEVICE(设备通知-家庭-设备)"),
    	@ApiImplicitParam(name="busId",value = "业务id  DEV_FAMILY,DEV_DEVICE 需要传家庭id/设备id"),
    })
    @GetMapping("/getOne")
    public AjaxResult getOne(@RequestParam("identifier")String identifier
    		,@RequestParam(value="busId",required = false)String busId)
    {
    	if(identifier==null) {
    		return AjaxResult.error("\"查询失败\"");
    	}else {
    		MsgSettingEnum settingEnum = MsgSettingEnum.selectSettingByIdentifier(identifier);
    		if(settingEnum==null) {
    			return AjaxResult.error("查询失败");
    		}else {
    			return msgNoticeSettingService.getOneNoticeSetting(settingEnum, busId, getUserId());
    		}
    	}
    }
}
