package com.ruoyi.iot.mapper;

import java.util.List;

import com.ruoyi.iot.domain.MsgSmsNoticeSetting;


/**
 * 短信通知设置Mapper接口
 * 
 * @author renjiayue
 * @date 2022-11-08
 */
public interface MsgSmsNoticeSettingMapper 
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
     * 删除短信通知设置
     * 
     * @param id 短信通知设置主键
     * @return 结果
     */
    public int deleteMsgSmsNoticeSettingById(Long id);

    /**
     * 批量删除短信通知设置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMsgSmsNoticeSettingByIds(Long[] ids);
}
