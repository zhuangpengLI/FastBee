package com.ruoyi.iot.mobile.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.iot.domain.MsgOperMsg;
import com.ruoyi.iot.mobile.model.FamilyShareStatRespDto;
import com.ruoyi.iot.mobile.model.UpdateMsgOperMsg;
import com.ruoyi.iot.service.IMsgOperMsgService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * 需操作消息列表Controller
 * 
 * @author renjiayue
 * @date 2022-09-18
 */
@Api(tags="a移动端----分享相关接口")
@RestController
@RequestMapping("/mobile/share")
public class MobileShareController extends BaseController
{
    @Autowired
    private IMsgOperMsgService msgOperMsgService;

    /**
     * 查询需操作消息列表列表
     */
    @ApiOperation("接受分享列表查询 ---  (操作还是使用审核消息的接口)")
    @ApiImplicitParam(name="status",value="状态 00 未处理 01已同意 02已拒绝 98已失效(别的请求已同意) 99已过期")
    @GetMapping("/receiveList")
    public TableDataInfo list(@RequestParam(value = "status",required = false) String status)
    {
        startPage();
        MsgOperMsg msgOperMsg = new MsgOperMsg();
        msgOperMsg.setReceiveUserId(getUserId());
        msgOperMsg.setIsOper(1);//需操作消息
        msgOperMsg.setStatus(status);
		List<MsgOperMsg> list = msgOperMsgService.selectShareOperMsgList(msgOperMsg );
        return getDataTable(list);
    }
    
    
    @ApiOperation("家庭分享统计列表")
    @GetMapping("/familyShareStatList")
    public TableDataInfo familyShareStatList()
    {
        startPage();
		List<FamilyShareStatRespDto> list = msgOperMsgService.selectFamilyShareStatList( getUserId(),null);
        return getDataTable(list);
    }
    
    
    @ApiOperation("设备分享统计列表")
    @GetMapping("/familyDeviceShareStatList")
    public TableDataInfo familyDeviceShareStatList(@RequestParam(value = "familyId",required = false) Long familyId)
    {
    	startPage();
    	List<FamilyShareStatRespDto> list = msgOperMsgService.selectDeviceShareStatList( getUserId(),familyId,null);
    	return getDataTable(list);
    }
}
