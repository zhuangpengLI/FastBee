package com.ruoyi.iot.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.iot.domain.MsgNoticeSetting;
import com.ruoyi.iot.service.IMsgNoticeSettingService;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * 消息设置Controller
 * 
 * @author renjiayue
 * @date 2022-10-21
 */
@RestController
@RequestMapping("/iot/msgSetting")
public class MsgNoticeSettingController extends BaseController
{
    @Autowired
    private IMsgNoticeSettingService msgNoticeSettingService;

    /**
     * 查询消息设置列表
     */
    @PreAuthorize("@ss.hasPermi('iot:msgSetting:list')")
    @GetMapping("/list")
    public AjaxResult list(MsgNoticeSetting msgNoticeSetting)
    {
        List<MsgNoticeSetting> list = msgNoticeSettingService.selectMsgNoticeSettingList(msgNoticeSetting);
        return AjaxResult.success(list);
    }

    /**
     * 导出消息设置列表
     */
    @PreAuthorize("@ss.hasPermi('iot:msgSetting:export')")
    @Log(title = "消息设置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MsgNoticeSetting msgNoticeSetting)
    {
        List<MsgNoticeSetting> list = msgNoticeSettingService.selectMsgNoticeSettingList(msgNoticeSetting);
        ExcelUtil<MsgNoticeSetting> util = new ExcelUtil<MsgNoticeSetting>(MsgNoticeSetting.class);
        util.exportExcel(response, list, "消息设置数据");
    }

    /**
     * 获取消息设置详细信息
     */
    @PreAuthorize("@ss.hasPermi('iot:msgSetting:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(msgNoticeSettingService.selectMsgNoticeSettingById(id));
    }

    /**
     * 新增消息设置
     */
    @PreAuthorize("@ss.hasPermi('iot:msgSetting:add')")
    @Log(title = "消息设置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MsgNoticeSetting msgNoticeSetting)
    {
        return toAjax(msgNoticeSettingService.insertMsgNoticeSetting(msgNoticeSetting));
    }

    /**
     * 修改消息设置
     */
    @PreAuthorize("@ss.hasPermi('iot:msgSetting:edit')")
    @Log(title = "消息设置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MsgNoticeSetting msgNoticeSetting)
    {
        return toAjax(msgNoticeSettingService.updateMsgNoticeSetting(msgNoticeSetting));
    }

    /**
     * 删除消息设置
     */
    @PreAuthorize("@ss.hasPermi('iot:msgSetting:remove')")
    @Log(title = "消息设置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(msgNoticeSettingService.deleteMsgNoticeSettingByIds(ids));
    }
}
