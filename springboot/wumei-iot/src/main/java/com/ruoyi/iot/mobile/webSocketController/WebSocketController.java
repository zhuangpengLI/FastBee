package com.ruoyi.iot.mobile.webSocketController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.iot.mobile.service.IFamilyDeviceService;
import com.ruoyi.iot.service.IFamilyService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * 只适用于 stompsocket
 * 家庭管理Controller
 * 
 * @author renjiayue
 * @date 2022-09-07
 */
//@Api(tags="a移动端----家庭设备相关接口")
//@RestController
public class WebSocketController extends BaseController
{
	
	//使用simpMessagingTemplate发送消息
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private IFamilyService familyService;
    @Autowired
    private IFamilyDeviceService familyDeviceService;

    
    @ApiOperation("获取网关状态  isBind 是否绑定网关 0否 1是  status 网关状态 1在线 0不在线")
    @MessageMapping("/getGatewayStatus")
    public void getGatewayStatus(String familyId)
    {
    	System.out.println(familyId);
    	System.out.println("获取到消息");
    }
    
    @ApiOperation("获取网关状态  isBind 是否绑定网关 0否 1是  status 网关状态 1在线 0不在线")
    @GetMapping("/respGatewayStatus")
    public void respGatewayStatus(@RequestParam("familyId") Long familyId)
    {
    	//发送给topic
    	simpMessagingTemplate.convertAndSend("/topic", familyId);
    }
    
    
}
