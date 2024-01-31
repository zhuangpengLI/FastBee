package com.ruoyi.iot.service;

import java.util.List;
import com.ruoyi.iot.domain.DeviceOtaUpdate;

/**
 * 设备升级Service接口
 * 
 * @author renjiayue
 * @date 2022-09-27
 */
public interface IDeviceOtaUpdateService 
{
    /**
     * 查询设备升级
     * 
     * @param id 设备升级主键
     * @return 设备升级
     */
    public DeviceOtaUpdate selectDeviceOtaUpdateById(Long id);

    /**
     * 查询设备升级列表
     * 
     * @param deviceOtaUpdate 设备升级
     * @return 设备升级集合
     */
    public List<DeviceOtaUpdate> selectDeviceOtaUpdateList(DeviceOtaUpdate deviceOtaUpdate);

    /**
     * 新增设备升级
     * 
     * @param deviceOtaUpdate 设备升级
     * @return 结果
     */
    public int insertDeviceOtaUpdate(DeviceOtaUpdate deviceOtaUpdate);

    /**
     * 修改设备升级
     * 
     * @param deviceOtaUpdate 设备升级
     * @return 结果
     */
    public int updateDeviceOtaUpdate(DeviceOtaUpdate deviceOtaUpdate);

    /**
     * 批量删除设备升级
     * 
     * @param ids 需要删除的设备升级主键集合
     * @return 结果
     */
    public int deleteDeviceOtaUpdateByIds(Long[] ids);

    /**
     * 删除设备升级信息
     * 
     * @param id 设备升级主键
     * @return 结果
     */
    public int deleteDeviceOtaUpdateById(Long id);
}
