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
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.iot.domain.MsgSmsNoticeSetting;
import com.ruoyi.iot.service.IMsgSmsNoticeSettingService;

/**
 * 短信通知设置Controller
 * 
 * @author renjiayue
 * @date 2022-11-08
 */
@RestController
@RequestMapping("/iot/smsSetting")
public class MsgSmsNoticeSettingController extends BaseController
{
    @Autowired
    private IMsgSmsNoticeSettingService msgSmsNoticeSettingService;

    /**
     * 查询短信通知设置列表
     */
    @PreAuthorize("@ss.hasPermi('iot:smsSetting:list')")
    @GetMapping("/list")
    public AjaxResult list(MsgSmsNoticeSetting msgSmsNoticeSetting)
    {
        List<MsgSmsNoticeSetting> list = msgSmsNoticeSettingService.selectMsgSmsNoticeSettingList(msgSmsNoticeSetting);
        return AjaxResult.success(list);
    }

    /**
     * 导出短信通知设置列表
     */
    @PreAuthorize("@ss.hasPermi('iot:smsSetting:export')")
    @Log(title = "短信通知设置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MsgSmsNoticeSetting msgSmsNoticeSetting)
    {
        List<MsgSmsNoticeSetting> list = msgSmsNoticeSettingService.selectMsgSmsNoticeSettingList(msgSmsNoticeSetting);
        ExcelUtil<MsgSmsNoticeSetting> util = new ExcelUtil<MsgSmsNoticeSetting>(MsgSmsNoticeSetting.class);
        util.exportExcel(response, list, "短信通知设置数据");
    }

    /**
     * 获取短信通知设置详细信息
     */
    @PreAuthorize("@ss.hasPermi('iot:smsSetting:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(msgSmsNoticeSettingService.selectMsgSmsNoticeSettingById(id));
    }

    /**
     * 新增短信通知设置
     */
    @PreAuthorize("@ss.hasPermi('iot:smsSetting:add')")
    @Log(title = "短信通知设置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MsgSmsNoticeSetting msgSmsNoticeSetting)
    {
        return toAjax(msgSmsNoticeSettingService.insertMsgSmsNoticeSetting(msgSmsNoticeSetting));
    }

    /**
     * 修改短信通知设置
     */
    @PreAuthorize("@ss.hasPermi('iot:smsSetting:edit')")
    @Log(title = "短信通知设置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MsgSmsNoticeSetting msgSmsNoticeSetting)
    {
        return toAjax(msgSmsNoticeSettingService.updateMsgSmsNoticeSetting(msgSmsNoticeSetting));
    }

    /**
     * 删除短信通知设置
     */
    @PreAuthorize("@ss.hasPermi('iot:smsSetting:remove')")
    @Log(title = "短信通知设置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(msgSmsNoticeSettingService.deleteMsgSmsNoticeSettingByIds(ids));
    }
}
