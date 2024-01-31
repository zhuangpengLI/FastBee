package com.ruoyi.iot.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 设备升级对象 iot_device_ota_update
 * 
 * @author renjiayue
 * @date 2022-09-27
 */
public class DeviceOtaUpdate extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 设备id */
    @Excel(name = "设备id")
    private Long deviceId;

    /** 设备名称 */
    @Excel(name = "设备名称")
    private String deviceName;

    /** 家庭id */
    @Excel(name = "家庭id")
    private Long familyId;

    /** 家庭名称 */
    @Excel(name = "家庭名称")
    private String familyName;

    /** 固件路径 */
    @Excel(name = "固件路径")
    private String firmwarePath;

    /** 升级版本 */
    @Excel(name = "升级版本")
    private String otaVersion;

    /** 升级状态 0升级中 1升级成功 2升级失败 */
    @Excel(name = "升级状态 0升级中 1升级成功 2升级失败")
    private Integer status;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setDeviceId(Long deviceId) 
    {
        this.deviceId = deviceId;
    }

    public Long getDeviceId() 
    {
        return deviceId;
    }
    public void setDeviceName(String deviceName) 
    {
        this.deviceName = deviceName;
    }

    public String getDeviceName() 
    {
        return deviceName;
    }
    public void setFamilyId(Long familyId) 
    {
        this.familyId = familyId;
    }

    public Long getFamilyId() 
    {
        return familyId;
    }
    public void setFamilyName(String familyName) 
    {
        this.familyName = familyName;
    }

    public String getFamilyName() 
    {
        return familyName;
    }
    public void setFirmwarePath(String firmwarePath) 
    {
        this.firmwarePath = firmwarePath;
    }

    public String getFirmwarePath() 
    {
        return firmwarePath;
    }
    public void setOtaVersion(String otaVersion) 
    {
        this.otaVersion = otaVersion;
    }

    public String getOtaVersion() 
    {
        return otaVersion;
    }
    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("deviceId", getDeviceId())
            .append("deviceName", getDeviceName())
            .append("familyId", getFamilyId())
            .append("familyName", getFamilyName())
            .append("firmwarePath", getFirmwarePath())
            .append("otaVersion", getOtaVersion())
            .append("status", getStatus())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .toString();
    }
}
