package com.ruoyi.iot.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.iot.domain.MsgOperMsg;
import com.ruoyi.iot.mobile.model.FamilyShareStatRespDto;
import com.ruoyi.iot.mobile.respModel.DeviceShareInfoRespDto;

/**
 * 需操作消息列表Mapper接口
 * 
 * @author renjiayue
 * @date 2022-09-18
 */
public interface MsgOperMsgMapper 
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
     * 查询家庭分享列表统计
     * @param sendUserId
     * @param familyId
     * @return
     */
    public List<FamilyShareStatRespDto> selectFamilyShareStatList(@Param("sendUserId") Long sendUserId,@Param("familyId") Long familyId);
    /**
     * 查询设备分享列表统计
     * @param sendUserId
     * @param familyId
     * @return
     */
    public List<FamilyShareStatRespDto> selectDeviceShareStatList(@Param("sendUserId") Long sendUserId,@Param("familyId") Long familyId,@Param("deviceId") Long deviceId);
    /**
     * 查询需操作消息列表统计数
     * 
     * @param msgOperMsg 需操作消息列表
     * @return 需操作消息列表集合
     */
    public int countMsgOperMsgList(MsgOperMsg msgOperMsg);

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
     * 删除需操作消息列表
     * 
     * @param id 需操作消息列表主键
     * @return 结果
     */
    public int deleteMsgOperMsgById(Long id);
    /**
     * 删除需操作消息列表
     * 
     * @param id 需操作消息列表主键
     * @return 结果
     */
    public int deleteMsgOperMsg(@Param("receiveUserId")Long receiveUserId,@Param("msgType")String msgType,@Param("familyId")Long familyId,@Param("deviceId")Long deviceId);

    /**
     * 批量删除需操作消息列表
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMsgOperMsgByIds(Long[] ids);
    
    /**
     * 根据条件更新消息为失效   !!!!!!批量更新 谨慎操作 
     * 只有 00 未处理状态才可以更新
     * 
     * @param msgOperMsg 条件
     * @return 结果
     */
    public int updateOperMsgTo98(MsgOperMsg msgOperMsg);
}
