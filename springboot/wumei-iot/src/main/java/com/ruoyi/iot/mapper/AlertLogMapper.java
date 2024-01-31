package com.ruoyi.iot.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.ruoyi.iot.domain.AlertLog;
import com.ruoyi.iot.dto.AlertLogAndFamilyWithUser;

/**
 * 设备告警Mapper接口
 * 
 * @author kerwincui
 * @date 2022-01-13
 */
@Repository
public interface AlertLogMapper 
{
    /**
     * 查询设备告警
     * 
     * @param alertLogId 设备告警主键
     * @return 设备告警
     */
    public AlertLog selectAlertLogByAlertLogId(Long alertLogId);

    /**
     * 查询设备告警列表
     * 
     * @param alertLog 设备告警
     * @return 设备告警集合
     */
    public List<AlertLog> selectAlertLogList(AlertLog alertLog);
    
    /**
     * 查询设备告警列表
     * 
     * @param alertLog 设备告警
     * @return 设备告警集合
     */
    public List<AlertLog> selectAlertLogWithInfoList(AlertLogAndFamilyWithUser alertLog);

    /**
     * 新增设备告警
     * 
     * @param alertLog 设备告警
     * @return 结果
     */
    public int insertAlertLog(AlertLog alertLog);

    /**
     * 修改设备告警
     * 
     * @param alertLog 设备告警
     * @return 结果
     */
    public int updateAlertLog(AlertLog alertLog);

    /**
     * 删除设备告警
     * 
     * @param alertLogId 设备告警主键
     * @return 结果
     */
    public int deleteAlertLogByAlertLogId(Long alertLogId);
    /**
     * 删除设备告警
     * 
     * @param alertLogId 设备告警主键
     * @return 结果
     */
    public int deleteAlertLog(@Param("familyId") Long familyId,@Param("deviceId")Long deviceId);

    /**
     * 批量删除设备告警
     * 
     * @param alertLogIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAlertLogByAlertLogIds(Long[] alertLogIds);
}
