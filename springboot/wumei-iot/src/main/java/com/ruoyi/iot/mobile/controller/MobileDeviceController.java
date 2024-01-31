package com.ruoyi.iot.mobile.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.iot.domain.MsgOperMsg;
import com.ruoyi.iot.mobile.model.InviteDeviceUserReqDto;
import com.ruoyi.iot.mobile.respModel.DeviceBriefRespInfo;
import com.ruoyi.iot.mobile.respModel.DeviceShareInfoRespDto;
import com.ruoyi.iot.mobile.service.IFamilyDeviceService;
import com.ruoyi.iot.mobile.transferModel.MessageAndResponseTransfer;
import com.ruoyi.iot.service.IDeviceService;
import com.ruoyi.iot.service.IFamilyService;
import com.ruoyi.iot.service.IMsgOperMsgService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * 家庭管理Controller
 * 
 * @author renjiayue
 * @date 2022-09-07
 */
@Api(tags="a移动端----家庭设备相关接口")
@RestController
@RequestMapping("/mobile/device")
public class MobileDeviceController extends BaseController
{
    @Autowired
    private IFamilyService familyService;
    @Autowired
    private IFamilyDeviceService familyDeviceService;
    @Autowired
    private IDeviceService deviceService;
    @Autowired
    private MessageAndResponseTransfer transfer;
    @Autowired
    private IMsgOperMsgService msgOperMsgService;

    
    @ApiOperation("获取网关状态  isBind 是否绑定网关 0否 1是  status 网关状态 1在线 0不在线")
    @GetMapping("/getGatewayStatus")
    public AjaxResult getGatewayStatus(@RequestParam("familyId") Long familyId)
    {
    	return familyDeviceService.getGatewayStatus(familyId,getUserId());
    }
    @ApiOperation("获取网关状态 status 网关状态 1在线 0不在线")
    @GetMapping("/getGatewayStatusBySn")
    public AjaxResult getGatewayStatusBySn(@RequestParam("gatewaySn") String gatewaySn)
    {
    	return familyDeviceService.getGatewayStatus(gatewaySn);
    }
    
    
    /**
     * 获取设备详细信息
     */
    @GetMapping(value = "getInfo")
    @ApiOperation("获取设备详情")
    public AjaxResult getInfo(@RequestParam("deviceId") Long deviceId)
    {
        return AjaxResult.success(deviceService.selectDeviceByDeviceId(deviceId));
    }
    
    @ApiOperation("查看家庭/房间设备列表")
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(value="roomId",required = false) Long roomId
    		,@RequestParam("familyId") Long familyId)
    {
        startPage();
        List<DeviceBriefRespInfo> list = familyDeviceService.selectRoomDeviceList(roomId,familyId, getUserId());
        return getDataTable(list);
    }
    
    @ApiOperation("查看家庭常用设备列表")
    @GetMapping("/familyUsualList")
    public TableDataInfo familyUsualList(@RequestParam("familyId") Long familyId)
    {
    	startPage();
    	List<DeviceBriefRespInfo> list = familyDeviceService.selectFamilyUsualDeviceList(familyId, getUserId());
    	return getDataTable(list);
    }
    
    @ApiOperation("查看未分配设备列表")
    @GetMapping("/notInRoomList")
    public TableDataInfo notInRoomList(@RequestParam("familyId") Long familyId)
    {
    	startPage();
    	List<DeviceBriefRespInfo> list = familyDeviceService.selectFamilyNotInRoomDeviceList(familyId, getUserId());
    	return getDataTable(list);
    }
    
    /**
     * 邀请家庭成员
     */
    @ApiOperation("邀请家庭成员(分享设备)")
    @PostMapping("/invite")
    public AjaxResult invite(@RequestBody @Valid InviteDeviceUserReqDto req)
    {
    	return familyDeviceService.inviteDeviceUser(req,getUserId());
    }
    
    @ApiOperation("设备分享列表查询")
    @ApiImplicitParam(name="status",value="状态 00 未处理 01已同意 02已拒绝 98已失效(别的请求已同意) 99已过期")
    @GetMapping("/shareDeviceList")
    public TableDataInfo shareDeviceList(@RequestParam(value = "status",required = false) String status
    		,@RequestParam("deviceId")Long deviceId)
    {
        startPage();
        MsgOperMsg msgOperMsg = new MsgOperMsg();
        msgOperMsg.setSendUserId(getUserId());
        msgOperMsg.setIsOper(1);//需操作消息
        msgOperMsg.setStatus(status);
        msgOperMsg.setMsgType("02");//设备分享
        msgOperMsg.setDeviceId(deviceId);
		List<DeviceShareInfoRespDto> list = msgOperMsgService.selectShareMsgWithRecDetailList(msgOperMsg );
		list.forEach(dto->{
			//手机号脱敏
			String receiveUserPhone = dto.getReceiveUserPhone();
			dto.setReceiveUserPhone(String.join("****", receiveUserPhone.substring(0, 3),receiveUserPhone.substring(7)));
		});
        return getDataTable(list);
    }
}
