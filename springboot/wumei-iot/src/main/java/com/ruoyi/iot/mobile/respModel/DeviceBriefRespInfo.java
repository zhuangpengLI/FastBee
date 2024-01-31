package com.ruoyi.iot.mobile.respModel;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.ruoyi.common.annotation.Excel;

/**
 * 设备简要对象iot_device
 * 
 * @author renjiayue
 */
public class DeviceBriefRespInfo  implements Serializable
{

    private static final long serialVersionUID = 1L;

    /** 产品分类ID */
    private Long deviceId;

    /** 产品分类名称 */
    @Excel(name = "设备名称")
    private String deviceName;

    /** 产品ID */
    @Excel(name = "产品ID")
    private Long productId;

    /** 产品名称 */
    @Excel(name = "产品名称")
    private String productName;

    /** 设备编号 */
    @Excel(name = "设备编号 SN码")
    private String serialNumber;

    /** 设备状态（1-未激活，2-禁用，3-在线，4-离线） */
    @Excel(name = "设备状态")
    private Integer status;

    @Excel(name = "物模型")
    private String thingsModelValue;
    
    /** 图片地址 */
    private String imgUrl;
    //家庭id
    private Long belongFamilyId;
    //房间id
    private Long belongRoomId;
    //房间名称
    private String belongRoomName;
    //是否为常用 0否 1是
    private Integer isFamilyUsual;
    //所属家庭网关的设备ID
    private Long familyGwDeviceId;
    
    //设备型号  例如
    //1001 1002单键开关  			1003双键开关 		1004三键开关 		1005三键+温湿度 
    //1011 1012单键语音开关 		1013双键语音开关	1014三键语音开关	1015三键+温湿度语音开关
    //2001 5孔10A			 		2002 3孔16A
    //3001 单轨窗帘 				3002 双轨窗帘
  //3011 单轨语音窗帘 				3012 双轨语音窗帘
    //4001 烟感报警器 				4002燃气报警器
    private String deviceModelType;
    
    //设备当前状态json
    //开关 s1 s2 s3
    //插座 pow
    //其他.....
    private String deviceCurStatusJson;
    
    /**
     * 是否是新设备  1是 0否  仅在添加设备时使用
     */
    private Integer isNew;

    
    public Long getBelongFamilyId() {
		return belongFamilyId;
	}

	public void setBelongFamilyId(Long belongFamilyId) {
		this.belongFamilyId = belongFamilyId;
	}

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

	public Long getFamilyGwDeviceId() {
		return familyGwDeviceId;
	}

	public void setFamilyGwDeviceId(Long familyGwDeviceId) {
		this.familyGwDeviceId = familyGwDeviceId;
	}

	public String getDeviceModelType() {
		return deviceModelType;
	}

	public void setDeviceModelType(String deviceModelType) {
		this.deviceModelType = deviceModelType;
	}

	public String getDeviceCurStatusJson() {
		return deviceCurStatusJson;
	}

	public void setDeviceCurStatusJson(String deviceCurStatusJson) {
		this.deviceCurStatusJson = deviceCurStatusJson;
	}

	public void setStatus(Integer status) {
		this.status = status;
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
    public void setProductId(Long productId) 
    {
        this.productId = productId;
    }

    public Long getProductId() 
    {
        return productId;
    }
    public void setProductName(String productName) 
    {
        this.productName = productName;
    }

    public String getProductName() 
    {
        return productName;
    }
    public void setSerialNumber(String serialNumber) 
    {
        this.serialNumber = serialNumber;
    }

    public String getSerialNumber() 
    {
        return serialNumber;
    }

    public Integer getStatus() 
    {
        return status;
    }
    public void setThingsModelValue(String thingsModelValue)
    {
        this.thingsModelValue = thingsModelValue;
    }

    public String getThingsModelValue()
    {
        return thingsModelValue;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("deviceId", getDeviceId())
            .append("deviceName", getDeviceName())
            .append("productId", getProductId())
            .append("productName", getProductName())
            .append("serialNumber", getSerialNumber())
            .append("status", getStatus())
            .toString();
    }

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getBelongRoomName() {
		return belongRoomName;
	}

	public void setBelongRoomName(String belongRoomName) {
		this.belongRoomName = belongRoomName;
	}

	public Integer getIsNew() {
		return isNew;
	}

	public void setIsNew(Integer isNew) {
		this.isNew = isNew;
	}
}
