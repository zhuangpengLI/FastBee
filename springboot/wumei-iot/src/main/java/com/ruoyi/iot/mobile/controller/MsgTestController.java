package com.ruoyi.iot.mobile.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.iot.util.JPushUtil;
import com.ruoyi.system.otherDto.SysUserWithFamilyStat;
import com.ruoyi.system.service.ISysUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;


/**
 * @Description:
 * @author Hevin QQ:390330302 E-mail:xunibidev@gmail.com
 * @date: create in 10:47 2019/6/28
 * @Modified:
 */
@Api(tags="测试接口")
@RestController
@RequestMapping("/smstest")
public class MsgTestController extends BaseController{
    @Autowired
    private JPushUtil jPushUtil;
    @Autowired
    private ISysUserService sysUserService;
    
    


    @ApiOperation("发送消息通知测试接口")
    @ApiImplicitParams({
        @ApiImplicitParam(dataType = "String", name = "type", value = "类型 0只发送自定义消息(不通知) 1发送通知及消息", required = true,paramType = "query"),
        @ApiImplicitParam(dataType = "Long", name = "memberId", value = "通知会员id", required = true,paramType = "query"),
    })
    @PostMapping("sendNotice")
    public AjaxResult sendNotice(Long memberId,String type,String content) throws Exception{
    	if("0".equals(type)){
    		Map<String, String> extras = new HashMap<String,String>();
    		extras.put("app", "智能家居");
			jPushUtil.sendMessageToMemberId(memberId,content, extras );
    	}else if("1".equals(type)){
    		Map<String, String> extras = new HashMap<String,String>();
    		extras.put("app", "智能家居");
    		jPushUtil.sendAlertAndMessageToMemberId(memberId,"智能家居ID业务通知",content, extras );
    	}else{
    		return error("type非法");
    	}
        
        return success("发送成功");
    }
    @ApiOperation("发送消息通知测试接口通过手机号")
    @ApiImplicitParams({
    	@ApiImplicitParam(dataType = "String", name = "type", value = "类型 0只发送自定义消息(不通知) 1发送通知及消息", required = true,paramType = "query"),
    	@ApiImplicitParam(dataType = "String", name = "phone", value = "通知会员手机号", required = true,paramType = "query"),
    })
    @PostMapping("sendNoticeByPhone")
    public AjaxResult sendNoticeByPhone(String phone,String type,String content) throws Exception{
    	
    	Long memberId = null;
    	SysUser user = new SysUser();
    	user.setUserType("01");//00系统用户 01 app用户
    	user.setPhonenumber(phone);
        List<SysUserWithFamilyStat> list = sysUserService.selectUserFamilyList(user);
        if(list.isEmpty()) {
        	return error("未查询到用户");
        }
        memberId = list.get(0).getUserId();
    	if("0".equals(type)){
    		Map<String, String> extras = new HashMap<String,String>();
    		extras.put("app", "智能家居");
    		jPushUtil.sendMessageToMemberId(memberId,content, extras );
    	}else if("1".equals(type)){
    		Map<String, String> extras = new HashMap<String,String>();
    		extras.put("app", "智能家居");
    		jPushUtil.sendAlertAndMessageToMemberId(memberId,"智能家居手机业务通知",content, extras );
    	}else{
    		return error("type非法");
    	}
    	
    	return success("发送成功");
    }
}
