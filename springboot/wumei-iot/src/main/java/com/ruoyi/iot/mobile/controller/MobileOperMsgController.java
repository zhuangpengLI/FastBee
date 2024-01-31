package com.ruoyi.iot.mobile.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.iot.domain.MsgOperMsg;
import com.ruoyi.iot.mobile.model.UpdateMsgOperMsg;
import com.ruoyi.iot.service.IMsgOperMsgService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 需操作消息列表Controller
 * 
 * @author renjiayue
 * @date 2022-09-18
 */
@Api(tags="aa移动端----需操作消息相关接口")
@RestController
@RequestMapping("/mobile/operMsg")
public class MobileOperMsgController extends BaseController
{
    @Autowired
    private IMsgOperMsgService msgOperMsgService;

    /**
     * 查询需操作消息列表列表
     */
    @ApiOperation("用户需操作消息列表查询")
    @GetMapping("/list")
    public TableDataInfo list()
    {
        startPage();
        MsgOperMsg msgOperMsg = new MsgOperMsg();
        msgOperMsg.setReceiveUserId(getUserId());
        msgOperMsg.setIsOper(1);//需操作消息
		List<MsgOperMsg> list = msgOperMsgService.selectMsgOperMsgList(msgOperMsg );
        return getDataTable(list);
    }
    @ApiOperation("用户未操作消息统计")
    @GetMapping("/countNoProcess")
    public AjaxResult countNoProcess()
    {
    	MsgOperMsg msgOperMsg = new MsgOperMsg();
    	msgOperMsg.setReceiveUserId(getUserId());
    	msgOperMsg.setIsOper(1);//需操作消息
    	msgOperMsg.setStatus("00");
    	List<MsgOperMsg> list = msgOperMsgService.selectMsgOperMsgList(msgOperMsg );
    	return AjaxResult.success(list.size());
    }


    /**
     * 修改需操作消息列表
     */
    @ApiOperation("用户操作消息")
    @PutMapping
    public AjaxResult edit(@RequestBody UpdateMsgOperMsg msgOperMsg)
    {
        return msgOperMsgService.operateOperMsg(msgOperMsg,getUserId());
    }
}
