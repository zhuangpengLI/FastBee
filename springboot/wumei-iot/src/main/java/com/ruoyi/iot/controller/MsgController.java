package com.ruoyi.iot.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.otherDomain.Msg;
import com.ruoyi.system.otherService.IMsgService;

/**
 * 消息通知Controller
 * 
 * @author renjiayue
 * @date 2022-10-06
 */
@RestController
@RequestMapping("/iot/iotMsg")
public class MsgController extends BaseController
{
    @Autowired
    private IMsgService msgService;

    /**
     * 查询消息通知列表
     */
    @PreAuthorize("@ss.hasPermi('iot:iotMsg:list')")
    @GetMapping("/list")
    public TableDataInfo list(Msg msg)
    {
        startPage();
        List<Msg> list = msgService.selectMsgList(msg);
        return getDataTable(list);
    }

    /**
     * 导出消息通知列表
     */
    @PreAuthorize("@ss.hasPermi('iot:iotMsg:export')")
    @Log(title = "消息通知", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Msg msg)
    {
        List<Msg> list = msgService.selectMsgList(msg);
        ExcelUtil<Msg> util = new ExcelUtil<Msg>(Msg.class);
        util.exportExcel(response, list, "消息通知数据");
    }

    /**
     * 获取消息通知详细信息
     */
    @PreAuthorize("@ss.hasPermi('iot:iotMsg:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(msgService.selectMsgById(id));
    }

    /**
     * 新增消息通知
     */
    @PreAuthorize("@ss.hasPermi('iot:iotMsg:add')")
    @Log(title = "消息通知", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Msg msg)
    {
        return toAjax(msgService.insertMsg(msg));
    }

    /**
     * 修改消息通知
     */
    @PreAuthorize("@ss.hasPermi('iot:iotMsg:edit')")
    @Log(title = "消息通知", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Msg msg)
    {
        return toAjax(msgService.updateMsg(msg));
    }

    /**
     * 删除消息通知
     */
    @PreAuthorize("@ss.hasPermi('iot:iotMsg:remove')")
    @Log(title = "消息通知", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(msgService.deleteMsgByIds(ids));
    }
}
