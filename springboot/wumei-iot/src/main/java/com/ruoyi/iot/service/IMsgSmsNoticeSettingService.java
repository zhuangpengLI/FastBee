package com.ruoyi.iot.service;

import java.util.List;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.iot.domain.MsgSmsNoticeSetting;
import com.ruoyi.system.otherDto.MsgSettingEnum;
import com.ruoyi.system.otherDto.SmsSettingEnum;

/**
 * 短信通知设置Service接口
 * 
 * @author renjiayue
 * @date 2022-11-08
 */
public interface IMsgSmsNoticeSettingService 
{
    /**
     * 查询短信通知设置
     * 
     * @param id 短信通知设置主键
     * @return 短信通知设置
     */
    public MsgSmsNoticeSetting selectMsgSmsNoticeSettingById(Long id);

    /**
     * 查询短信通知设置列表
     * 
     * @param msgSmsNoticeSetting 短信通知设置
     * @return 短信通知设置集合
     */
    public List<MsgSmsNoticeSetting> selectMsgSmsNoticeSettingList(MsgSmsNoticeSetting msgSmsNoticeSetting);

    /**
     * 新增短信通知设置
     * 
     * @param msgSmsNoticeSetting 短信通知设置
     * @return 结果
     */
    public int insertMsgSmsNoticeSetting(MsgSmsNoticeSetting msgSmsNoticeSetting);

    /**
     * 修改短信通知设置
     * 
     * @param msgSmsNoticeSetting 短信通知设置
     * @return 结果
     */
    public int updateMsgSmsNoticeSetting(MsgSmsNoticeSetting msgSmsNoticeSetting);
    
    /**
     * 修改消息设置
     * 
     * @param msgNoticeSetting 消息设置
     * @return 结果
     */
    public AjaxResult updateMsgNoticeSetting(SmsSettingEnum settingEnum,String busId
    		,Integer isDisabled,Long userId);
    
    /**
     * 获取唯一消息设置结果
     * @param settingEnum
     * @param busId
     * @param userId
     * @return
     */
    public AjaxResult getOneNoticeSetting(SmsSettingEnum settingEnum,String busId
    		,Long userId);
    /**
     * 获取唯一消息设置结果
     * @param settingEnum
     * @param busId
     * @param userId
     * @return
     */
    public boolean isSendNotice(String identifier,String busId
    		,Long userId);
    /**
     * 短信发送成功后 更新短信限制设置
     * @param settingEnum
     * @param busId
     * @param userId
     * @return
     */
    public boolean sendSmsSucProcess(String identifier,String busId
    		,Long userId);

    /**
     * 批量删除短信通知设置
     * 
     * @param ids 需要删除的短信通知设置主键集合
     * @return 结果
     */
    public int deleteMsgSmsNoticeSettingByIds(Long[] ids);

    /**
     * 删除短信通知设置信息
     * 
     * @param id 短信通知设置主键
     * @return 结果
     */
    public int deleteMsgSmsNoticeSettingById(Long id);
}
