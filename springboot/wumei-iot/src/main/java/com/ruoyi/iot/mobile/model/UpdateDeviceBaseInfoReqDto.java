package com.ruoyi.iot.mobile.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 设备基本信息iot_device
 * 
 * @author renjiayue
 */
@ApiModel("更新设备基本信息dto 除设备id和sn码之外不能全为空")
public class UpdateDeviceBaseInfoReqDto  implements Serializable
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("设备id")
    @NotNull
    private Long deviceId;

    @ApiModelProperty("设备名称")
    private String deviceName;

    /** 设备编号 */
    @ApiModelProperty("设备编号 SN码  不能修改只用来校验设备信息")
    private String serialNumber;

    //房间id
    @ApiModelProperty("房间id")
    private Long belongRoomId;
    //是否为常用 0否 1是
    @ApiModelProperty("是否为常用 0否 1是")
    private Integer isFamilyUsual;

    
	public Long getBelongRoomId() {
		return belongRoomId;
	}

	public void setBelongRoomId(Long belongRoomId) {
		this.belongRoomId = belongRoomId;
	}

	public Integer getIsFamilyUsual() {
		return isFamilyUsual;
	}

	public void setIsFamilyUsual(Integer isFamilyUsual) {
		this.isFamilyUsual = isFamilyUsual;
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
    public void setSerialNumber(String serialNumber) 
    {
        this.serialNumber = serialNumber;
    }

    public String getSerialNumber() 
    {
        return serialNumber;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("deviceId", getDeviceId())
            .append("deviceName", getDeviceName())
            .append("serialNumber", getSerialNumber())
            .toString();
    }
 }
