package com.ruoyi.iot.service;

import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.iot.domain.MsgNoticeSetting;
import com.ruoyi.system.otherDto.MsgSettingEnum;

/**
 * 消息设置Service接口
 * 
 * @author renjiayue
 * @date 2022-10-21
 */
public interface IMsgNoticeSettingService 
{
    /**
     * 查询消息设置
     * 
     * @param id 消息设置主键
     * @return 消息设置
     */
    public MsgNoticeSetting selectMsgNoticeSettingById(Long id);

    /**
     * 查询消息设置列表
     * 
     * @param msgNoticeSetting 消息设置
     * @return 消息设置集合
     */
    public List<MsgNoticeSetting> selectMsgNoticeSettingList(MsgNoticeSetting msgNoticeSetting);

    /**
     * 新增消息设置
     * 
     * @param msgNoticeSetting 消息设置
     * @return 结果
     */
    public int insertMsgNoticeSetting(MsgNoticeSetting msgNoticeSetting);

    /**
     * 修改消息设置
     * 
     * @param msgNoticeSetting 消息设置
     * @return 结果
     */
    public int updateMsgNoticeSetting(MsgNoticeSetting msgNoticeSetting);
    /**
     * 修改消息设置
     * 
     * @param msgNoticeSetting 消息设置
     * @return 结果
     */
    public AjaxResult updateMsgNoticeSetting(MsgSettingEnum settingEnum,String busId
    		,Integer isDisabled,Long userId);
    
    /**
     * 获取唯一消息设置结果
     * @param settingEnum
     * @param busId
     * @param userId
     * @return
     */
    public AjaxResult getOneNoticeSetting(MsgSettingEnum settingEnum,String busId
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
     * 批量删除消息设置
     * 
     * @param ids 需要删除的消息设置主键集合
     * @return 结果
     */
    public int deleteMsgNoticeSettingByIds(Long[] ids);

    /**
     * 删除消息设置信息
     * 
     * @param id 消息设置主键
     * @return 结果
     */
    public int deleteMsgNoticeSettingById(Long id);
}
