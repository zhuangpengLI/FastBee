package com.ruoyi.iot.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.system.otherDomain.Msg;


/**
 * 消息通知Mapper接口
 * 
 * @author renjiayue
 * @date 2022-10-06
 */
public interface MsgMapper 
{
    /**
     * 查询消息通知
     * 
     * @param id 消息通知主键
     * @return 消息通知
     */
    public Msg selectMsgById(Long id);

    /**
     * 查询消息通知列表
     * 
     * @param msg 消息通知
     * @return 消息通知集合
     */
    public List<Msg> selectMsgList(Msg msg);
    
    /**
     * 查询系统消息通知列表
     * 
     * @param msg 消息通知
     * @return 消息通知集合
     */
    public List<Msg> selectSystemMsgList(Msg msg);

    /**
     * 新增消息通知
     * 
     * @param msg 消息通知
     * @return 结果
     */
    public int insertMsg(Msg msg);

    /**
     * 修改消息通知
     * 
     * @param msg 消息通知
     * @return 结果
     */
    public int updateMsg(Msg msg);
    
    /**
     * 更新消息已读
     * 
     * @param id 消息通知主键
     * @return 消息通知
     */
    public int updateMsgRead(Long id);
    
    /**
     * 更新消息已读
     * 
     * @param id 消息通知主键
     * @return 消息通知
     */
    public int updateMsgReadByType(@Param("userId")Long userId,@Param("types")String[] types);

    /**
     * 删除消息通知
     * 
     * @param id 消息通知主键
     * @return 结果
     */
    public int deleteMsgById(Long id);
    
    /**
     * 删除消息通知
     * 
     * @param id 消息通知主键
     * @return 结果
     */
    public int deleteMsgByNoticeId(@Param("msgType")String msgType,@Param("noticeId")Long noticeId);
    
    /**
     * 删除消息通知
     * 
     * @param id 消息通知主键
     * @return 结果
     */
    public int deleteMsg(@Param("userId")Long userId,@Param("familyId")Long familyId,@Param("deviceId")Long deviceId);

    /**
     * 批量删除消息通知
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMsgByIds(Long[] ids);
}
