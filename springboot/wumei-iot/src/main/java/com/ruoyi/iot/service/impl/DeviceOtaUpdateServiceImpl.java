package com.ruoyi.iot.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.iot.mapper.DeviceOtaUpdateMapper;
import com.ruoyi.iot.domain.DeviceOtaUpdate;
import com.ruoyi.iot.service.IDeviceOtaUpdateService;

/**
 * 设备升级Service业务层处理
 * 
 * @author renjiayue
 * @date 2022-09-27
 */
@Service
public class DeviceOtaUpdateServiceImpl implements IDeviceOtaUpdateService 
{
	
    @Autowired
    private DeviceOtaUpdateMapper deviceOtaUpdateMapper;

    /**
     * 查询设备升级
     * 
     * @param id 设备升级主键
     * @return 设备升级
     */
    @Override
    public DeviceOtaUpdate selectDeviceOtaUpdateById(Long id)
    {
        return deviceOtaUpdateMapper.selectDeviceOtaUpdateById(id);
    }

    /**
     * 查询设备升级列表
     * 
     * @param deviceOtaUpdate 设备升级
     * @return 设备升级
     */
    @Override
    public List<DeviceOtaUpdate> selectDeviceOtaUpdateList(DeviceOtaUpdate deviceOtaUpdate)
    {
        return deviceOtaUpdateMapper.selectDeviceOtaUpdateList(deviceOtaUpdate);
    }

    /**
     * 新增设备升级
     * 
     * @param deviceOtaUpdate 设备升级
     * @return 结果
     */
    @Override
    public int insertDeviceOtaUpdate(DeviceOtaUpdate deviceOtaUpdate)
    {
        deviceOtaUpdate.setCreateTime(DateUtils.getNowDate());
        return deviceOtaUpdateMapper.insertDeviceOtaUpdate(deviceOtaUpdate);
    }

    /**
     * 修改设备升级
     * 
     * @param deviceOtaUpdate 设备升级
     * @return 结果
     */
    @Override
    public int updateDeviceOtaUpdate(DeviceOtaUpdate deviceOtaUpdate)
    {
        return deviceOtaUpdateMapper.updateDeviceOtaUpdate(deviceOtaUpdate);
    }

    /**
     * 批量删除设备升级
     * 
     * @param ids 需要删除的设备升级主键
     * @return 结果
     */
    @Override
    public int deleteDeviceOtaUpdateByIds(Long[] ids)
    {
        return deviceOtaUpdateMapper.deleteDeviceOtaUpdateByIds(ids);
    }

    /**
     * 删除设备升级信息
     * 
     * @param id 设备升级主键
     * @return 结果
     */
    @Override
    public int deleteDeviceOtaUpdateById(Long id)
    {
        return deviceOtaUpdateMapper.deleteDeviceOtaUpdateById(id);
    }
}
