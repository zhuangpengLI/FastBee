package com.ruoyi.iot.service;

import java.util.List;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.iot.domain.MsgOperMsg;
import com.ruoyi.iot.mobile.model.FamilyShareStatRespDto;
import com.ruoyi.iot.mobile.model.UpdateMsgOperMsg;
import com.ruoyi.iot.mobile.respModel.DeviceShareInfoRespDto;

/**
 * 需操作消息列表Service接口
 * 
 * @author renjiayue
 * @date 2022-09-18
 */
public interface IMsgOperMsgService 
{
    /**
     * 查询需操作消息列表
     * 
     * @param id 需操作消息列表主键
     * @return 需操作消息列表
     */
    public MsgOperMsg selectMsgOperMsgById(Long id);

    /**
     * 查询需操作消息列表列表
     * 
     * @param msgOperMsg 需操作消息列表
     * @return 需操作消息列表集合
     */
    public List<MsgOperMsg> selectMsgOperMsgList(MsgOperMsg msgOperMsg);
    /**
     * 查询分享列表
     * 
     * @param msgOperMsg 需操作消息列表
     * @return 需操作消息列表集合
     */
    public List<MsgOperMsg> selectShareOperMsgList(MsgOperMsg msgOperMsg);
    
    /**
     * 查询分享列表   包含详细接收人信息
     * 
     * @param msgOperMsg 需操作消息列表
     * @return 需操作消息列表集合
     */
    public List<DeviceShareInfoRespDto> selectShareMsgWithRecDetailList(MsgOperMsg msgOperMsg);
    /**
     * 家庭分享统计列表
     * 
     * @return 需操作消息列表集合
     */
    public List<FamilyShareStatRespDto> selectFamilyShareStatList(Long sendUserId, Long familyId);
    /**
     * 设备分享统计列表
     * 
     * @return 需操作消息列表集合
     */
    public List<FamilyShareStatRespDto> selectDeviceShareStatList(Long sendUserId, Long familyId, Long deviceId);

    /**
     * 新增需操作消息列表
     * 
     * @param msgOperMsg 需操作消息列表
     * @return 结果
     */
    public int insertMsgOperMsg(MsgOperMsg msgOperMsg);

    /**
     * 修改需操作消息列表
     * 
     * @param msgOperMsg 需操作消息列表
     * @return 结果
     */
    public int updateMsgOperMsg(MsgOperMsg msgOperMsg);

    /**
     * 批量删除需操作消息列表
     * 
     * @param ids 需要删除的需操作消息列表主键集合
     * @return 结果
     */
    public int deleteMsgOperMsgByIds(Long[] ids);

    /**
     * 删除需操作消息列表信息
     * 
     * @param id 需操作消息列表主键
     * @return 结果
     */
    public int deleteMsgOperMsgById(Long id);
    
    /**
     * 审核需操作消息
     * 
     * @param msgOperMsg 需操作消息列表
     * @return 结果
     */
    public AjaxResult operateOperMsg(UpdateMsgOperMsg msg,Long userId);
}
