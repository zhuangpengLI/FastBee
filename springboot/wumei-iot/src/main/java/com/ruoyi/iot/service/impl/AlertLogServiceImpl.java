package com.ruoyi.iot.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.iot.domain.AlertLog;
import com.ruoyi.iot.domain.DeviceLog;
import com.ruoyi.iot.dto.AlertLogAndFamilyWithUser;
import com.ruoyi.iot.mapper.AlertLogMapper;
import com.ruoyi.iot.service.IAlertLogService;
import com.ruoyi.iot.service.IDeviceLogService;
import com.ruoyi.iot.tdengine.service.ILogService;

/**
 * 设备告警Service业务层处理
 * 
 * @author kerwincui
 * @date 2022-01-13
 */
@Service
public class AlertLogServiceImpl implements IAlertLogService 
{
    @Autowired
    private AlertLogMapper alertLogMapper;
    @Autowired
    private IDeviceLogService deviceLogService;
    @Autowired
    private ILogService logService;

    /**
     * 查询设备告警
     * 
     * @param alertLogId 设备告警主键
     * @return 设备告警
     */
    @Override
    public AlertLog selectAlertLogByAlertLogId(Long alertLogId)
    {
        return alertLogMapper.selectAlertLogByAlertLogId(alertLogId);
    }

    /**
     * 查询设备告警列表
     * 
     * @param alertLog 设备告警
     * @return 设备告警
     */
    @Override
    public List<AlertLog> selectAlertLogList(AlertLog alertLog)
    {
        return alertLogMapper.selectAlertLogList(alertLog);
    }
    @Override
    public List<AlertLog> selectAlertLogWithInfoList(AlertLogAndFamilyWithUser alertLog)
    {
    	return alertLogMapper.selectAlertLogWithInfoList(alertLog);
    }

    /**
     * 新增设备告警
     * 
     * @param alertLog 设备告警
     * @return 结果
     */
    @Transactional
    @Override
    public int insertAlertLog(AlertLog alertLog, String sn, String identity, Integer logType, String logValue)
    {
        alertLog.setCreateTime(DateUtils.getNowDate());
        int i = alertLogMapper.insertAlertLog(alertLog);
        if(i>0) {
        	// 添加到设备日志
            DeviceLog deviceLog = new DeviceLog();
            deviceLog.setDeviceId(alertLog.getDeviceId());
            deviceLog.setDeviceName(alertLog.getDeviceName());
            deviceLog.setLogValue(logValue);
            deviceLog.setLogType(logType);
//            deviceLog.setLogType(3);//事件上报
            deviceLog.setLogText(alertLog.getAlertName());
//            deviceLog.setRemark();
            //TODO sn码未保存
            deviceLog.setSerialNumber(sn);
            deviceLog.setIdentity(identity);
            deviceLog.setIsMonitor(0);
            deviceLog.setUserId(alertLog.getUserId());
            deviceLog.setCreateTime(DateUtils.getNowDate());
            // 1=影子模式，2=在线模式，3=其他
            deviceLog.setMode(2);
            logService.saveDeviceLog(deviceLog);
        }
        return i;
    }

    /**
     * 修改设备告警
     * 
     * @param alertLog 设备告警
     * @return 结果
     */
    @Override
    public int updateAlertLog(AlertLog alertLog)
    {
        alertLog.setUpdateTime(DateUtils.getNowDate());
        return alertLogMapper.updateAlertLog(alertLog);
    }

    /**
     * 批量删除设备告警
     * 
     * @param alertLogIds 需要删除的设备告警主键
     * @return 结果
     */
    @Override
    public int deleteAlertLogByAlertLogIds(Long[] alertLogIds)
    {
        return alertLogMapper.deleteAlertLogByAlertLogIds(alertLogIds);
    }

    /**
     * 删除设备告警信息
     * 
     * @param alertLogId 设备告警主键
     * @return 结果
     */
    @Override
    public int deleteAlertLogByAlertLogId(Long alertLogId)
    {
        return alertLogMapper.deleteAlertLogByAlertLogId(alertLogId);
    }
}
