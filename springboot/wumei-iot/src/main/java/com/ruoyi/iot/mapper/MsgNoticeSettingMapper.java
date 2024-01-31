package com.ruoyi.iot.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.iot.domain.MsgNoticeSetting;

/**
 * 消息设置Mapper接口
 * 
 * @author renjiayue
 * @date 2022-10-21
 */
public interface MsgNoticeSettingMapper 
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
     * 删除消息设置
     * 
     * @param id 消息设置主键
     * @return 结果
     */
    public int deleteMsgNoticeSettingById(Long id);
    
    /**
     * 删除子设置 通过父级id
     * @param id
     * @return
     */
    public int deleteMsgNoticeSettingByParentId(Long parentId);
    /**
     * 删除消息设置
     * 
     * @param id 消息设置主键
     * @return 结果
     */
    public int deleteMsgNoticeSetting(@Param("userId")Long userId,@Param("identifier")String identifier,@Param("busId")String busId);

    /**
     * 批量删除消息设置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMsgNoticeSettingByIds(Long[] ids);
}
