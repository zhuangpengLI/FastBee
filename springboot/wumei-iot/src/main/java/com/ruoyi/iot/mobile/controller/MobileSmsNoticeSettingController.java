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
import com.ruoyi.iot.service.IMsgSmsNoticeSettingService;
import com.ruoyi.system.otherDto.MsgSettingEnum;
import com.ruoyi.system.otherDto.SmsSettingEnum;

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
@Api(tags="aaa移动端----短信通知设置接口")
@RestController
@RequestMapping("/mobile/msgSmsSetting")
public class MobileSmsNoticeSettingController extends BaseController
{
    @Autowired
    private IMsgSmsNoticeSettingService msgSmsNoticeSettingService;
    @Autowired
    private IFamilyDeviceService familyDeviceService;
    @Autowired
    private IFamilyService familyService;

    /**
     * 修改消息设置
     */
    @ApiOperation("设置通知状态")
    @ApiImplicitParams({
    	@ApiImplicitParam(name="identifier",value = "标识符  DEV_TOTAL(设备短信通知)"),
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
    		SmsSettingEnum settingEnum = SmsSettingEnum.selectSettingByIdentifier(identifier);
    		if(settingEnum==null) {
    			return AjaxResult.error("设置失败");
    		}else {
    			return msgSmsNoticeSettingService.updateMsgNoticeSetting(settingEnum, busId, isDisabled, getUserId());
    		}
    	}
    }
    
    @ApiOperation("查询单个通知状态")
    @ApiImplicitParams({
    	@ApiImplicitParam(name="identifier",value = "标识符  DEV_TOTAL(设备短信通知)"),
    	@ApiImplicitParam(name="busId",value = "业务id  DEV_FAMILY,DEV_DEVICE 需要传家庭id/设备id"),
    })
    @GetMapping("/getOne")
    public AjaxResult getOne(@RequestParam("identifier")String identifier
    		,@RequestParam(value="busId",required = false)String busId)
    {
    	if(identifier==null) {
    		return AjaxResult.error("\"查询失败\"");
    	}else {
    		SmsSettingEnum settingEnum = SmsSettingEnum.selectSettingByIdentifier(identifier);
    		if(settingEnum==null) {
    			return AjaxResult.error("查询失败");
    		}else {
    			return msgSmsNoticeSettingService.getOneNoticeSetting(settingEnum, busId, getUserId());
    		}
    	}
    }
}
