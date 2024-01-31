package com.ruoyi.iot.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 场景设备对象 iot_scene_device
 * 
 * @author kerwincui
 * @date 2022-09-21
 */
public class SceneDevice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 场景id */
    @Excel(name = "场景id")
    private Long sceneId;

    /** 设备ID */
    @Excel(name = "设备ID")
    private Long deviceId;

    /** 设备编号 */
    @Excel(name = "设备编号")
    private String serialNumber;

    /** 设备名称 */
    @Excel(name = "设备名称")
    private String deviceName;

    /** 产品ID */
    @Excel(name = "产品ID")
    private Long productId;

    /** 产品名称 */
    @Excel(name = "产品名称")
    private String productName;

    /** 执行的动作集合 */
    @Excel(name = "执行的动作集合")
    private String actions;
    
    /** 执行的动作集合 */
    @Excel(name = "执行命令中文描述")
    private String actionsName;
    
    //设备型号
    private String deviceModelType;
    
    /** 设备图片地址 */
    private String imgUrl;
    
    //所属房间id
    private Long belongRoomId;
    //所属房间名称
    private String belongRoomName;

    public String getActionsName() {
		return actionsName;
	}

	public void setActionsName(String actionsName) {
		this.actionsName = actionsName;
	}

	public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setSceneId(Long sceneId) 
    {
        this.sceneId = sceneId;
    }

    public Long getSceneId() 
    {
        return sceneId;
    }
    public void setDeviceId(Long deviceId) 
    {
        this.deviceId = deviceId;
    }

    public Long getDeviceId() 
    {
        return deviceId;
    }
    public void setSerialNumber(String serialNumber) 
    {
        this.serialNumber = serialNumber;
    }

    public String getSerialNumber() 
    {
        return serialNumber;
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
    public void setActions(String actions) 
    {
        this.actions = actions;
    }

    public String getActions() 
    {
        return actions;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("sceneId", getSceneId())
            .append("deviceId", getDeviceId())
            .append("serialNumber", getSerialNumber())
            .append("deviceName", getDeviceName())
            .append("productId", getProductId())
            .append("productName", getProductName())
            .append("actions", getActions())
            .toString();
    }

	public String getDeviceModelType() {
		return deviceModelType;
	}

	public void setDeviceModelType(String deviceModelType) {
		this.deviceModelType = deviceModelType;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Long getBelongRoomId() {
		return belongRoomId;
	}

	public void setBelongRoomId(Long belongRoomId) {
		this.belongRoomId = belongRoomId;
	}

	public String getBelongRoomName() {
		return belongRoomName;
	}

	public void setBelongRoomName(String belongRoomName) {
		this.belongRoomName = belongRoomName;
	}
}
