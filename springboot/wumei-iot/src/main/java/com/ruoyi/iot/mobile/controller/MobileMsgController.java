package com.ruoyi.iot.mobile.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.iot.domain.MsgOperMsg;
import com.ruoyi.iot.service.IMsgOperMsgService;
import com.ruoyi.system.otherDomain.Msg;
import com.ruoyi.system.otherService.IMsgService;
import com.ruoyi.system.service.ISysNoticeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 消息通知Controller
 * 
 * @author renjiayue
 * @date 2022-10-06
 */
@Api(tags="aa移动端----消息接口")
@RestController
@RequestMapping("/mobile/iotMsg")
public class MobileMsgController extends BaseController
{
    @Autowired
    private IMsgService msgService;
    @Autowired
    private IMsgOperMsgService msgOperMsgService;
    

    /**
     * 查询消息通知列表
     */
    @ApiOperation("查询家庭消息列表")
    @GetMapping("/familyMsgList")
    public TableDataInfo familyMsgList(@RequestParam(value = "familyId",required =false) Long familyId
    		,@RequestParam(value = "roomId",required =false) Long roomId
    		,@RequestParam(value = "deviceId",required =false) Long deviceId
    		,@RequestParam(value="beginTime",required = false) String beginTime
    		,@RequestParam(value="endTime",required = false) String endTime)
    {
    	Msg msg = new Msg();
    	msg.setMsgType("99");//家庭消息
    	msg.setFamilyId(familyId);
    	msg.setRoomId(roomId);
    	msg.setDeviceId(deviceId);
    	msg.setUserId(getUserId());
    	Map<String, Object> params = msg.getParams();
    	params.put("beginCreateTime",beginTime);
    	params.put("endCreateTime",endTime);
        startPage();
        List<Msg> list = msgService.selectMsgList(msg);
        return getDataTable(list);
    }
    @ApiOperation("查询所有未读(未操作)消息统计")
    @GetMapping("/noReadAllMsgCount")
    public AjaxResult noReadAllMsgCount()
    {
    	//未读消息
    	Msg msg = new Msg();
    	msg.setUserId(getUserId());
    	msg.setIsRead(0);
    	List<Msg> list = msgService.selectMsgList(msg);
    	
    	//未操作的需处理消息
    	MsgOperMsg msgOperMsg = new MsgOperMsg();
    	msgOperMsg.setReceiveUserId(getUserId());
    	msgOperMsg.setIsOper(1);//需操作消息
    	msgOperMsg.setStatus("00");
    	List<MsgOperMsg> list2 = msgOperMsgService.selectMsgOperMsgList(msgOperMsg );
    	
    	return AjaxResult.success(list.size()+list2.size());
    }
    @ApiOperation("查询未读家庭消息统计")
    @GetMapping("/noReadFamilyMsgCount")
    public AjaxResult noReadFamilyMsg()
    {
    	Msg msg = new Msg();
    	msg.setMsgType("99");//家庭消息
    	msg.setUserId(getUserId());
    	msg.setIsRead(0);
    	List<Msg> list = msgService.selectMsgList(msg);
    	return AjaxResult.success(list.size());
    }
    
    @ApiOperation("查询系统消息列表")
    @GetMapping("/systemMsgList")
    public TableDataInfo systemMsgList(
    		@RequestParam(value="beginTime",required = false) String beginTime
    		,@RequestParam(value="endTime",required = false) String endTime)
    {
    	Msg msg = new Msg();
    	msg.setUserId(getUserId());
    	Map<String, Object> params = msg.getParams();
    	params.put("beginCreateTime",beginTime);
    	params.put("endCreateTime",endTime);
    	startPage();
    	List<Msg> list = msgService.selectSystemMsgList(msg);
    	return getDataTable(list);
    }
    @ApiOperation("查询未读系统消息统计")
    @GetMapping("/noReadSystemMsgCount")
    public AjaxResult noReadSystemMsgCount()
    {
    	Msg msg = new Msg();
    	msg.setUserId(getUserId());
    	msg.setIsRead(0);
    	List<Msg> list = msgService.selectSystemMsgList(msg);
    	return AjaxResult.success(list.size());
    }
    

    /**
     * 获取消息通知详细信息
     */
    @ApiOperation("获取消息通知详细信息")
    @GetMapping(value = "/getInfo")
    public AjaxResult getInfo(@RequestParam("id") Long id)
    {
        return AjaxResult.success(msgService.selectMsgById(id));
    }
    
    @ApiOperation("单条消息已读")
    @PutMapping("/read")
    public AjaxResult read(@RequestParam("id") Long id)
    {
    	return msgService.updateMsgRead(id, getUserId());
    }
    
    @ApiOperation("所有系统消息已读")
    @PutMapping("/readAllSystemMsg")
    public AjaxResult readAllSystemMsg()
    {
    	return msgService.updateSystemMsgRead(getUserId());
    }
    
    @ApiOperation("所有家庭消息已读")
    @PutMapping("/readAllFamilyMsg")
    public AjaxResult readAllFamilyMsg()
    {
    	return msgService.updateFamilyMsgRead(getUserId());
    }
    
    @ApiOperation("所有消息已读")
    @PutMapping("/readAllMsg")
    public AjaxResult readAllMsg()
    {
    	return msgService.updateAllMsgRead(getUserId());
    }


    /**
     * 删除消息通知
     */
    @ApiOperation("删除消息")
	@DeleteMapping("/delete")
    public AjaxResult remove(@RequestParam("ids") Long[] ids)
    {
        return toAjax(msgService.deleteMsgByIds(ids));
    }
}
