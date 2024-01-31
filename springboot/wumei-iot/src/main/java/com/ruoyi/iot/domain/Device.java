package com.ruoyi.iot.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 设备对象 iot_device
 * 
 * @author kerwincui
 * @date 2021-12-16
 */
public class Device extends BaseEntity
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
    
    /** 设备类型（1-直连设备、2-网关子设备、3-网关设备） */
    @Excel(name = "设备类型", readConverterExp = "1=直连设备、2=网关子设备、3=网关设备")
    private Integer deviceType;
    
  //设备型号  例如
    //1001 1002单键开关  			1003双键开关 		1004三键开关 		1005三键+温湿度 
    //1011 1012单键语音开关 		1013双键语音开关	1014三键语音开关	1015三键+温湿度语音开关
    //2001 5孔10A			 		2002 3孔16A
    //3001 单轨窗帘 				3002 双轨窗帘
    //3011 单轨语音窗帘 				3012 双轨语音窗帘
    //4001 烟感报警器 				4002燃气报警器
    private String deviceModelType;

	/** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 用户昵称 */
    @Excel(name = "用户昵称")
    private String userName;

    /** 租户ID */
    @Excel(name = "租户ID")
    private Long tenantId;

    /** 租户名称 */
    @Excel(name = "租户名称")
    private String tenantName;

    /** 设备编号 */
    @Excel(name = "设备编号")
    private String serialNumber;

    /** 固件版本 */
    @Excel(name = "固件版本")
    private BigDecimal firmwareVersion;

    /** 设备状态（1-未激活，2-禁用，3-在线，4-离线） */
    @Excel(name = "设备状态")
    private Integer status;

    /** wifi信号强度（信号极好4格[-55— 0]，信号好3格[-70— -55]，信号一般2格[-85— -70]，信号差1格[-100— -85]） */
    @Excel(name = "wifi信号强度")
    private Integer rssi;

    /** 设备影子 */
    private Integer isShadow;

    /** 设备所在地址 */
    @Excel(name = "设备所在地址")
    private String networkAddress;

    /** 设备入网IP */
    @Excel(name = "设备入网IP")
    private String networkIp;

    /** 设备经度 */
    @Excel(name = "设备经度")
    private BigDecimal longitude;

    /** 设备纬度 */
    @Excel(name = "设备纬度")
    private BigDecimal latitude;

    /** 激活时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "激活时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date activeTime;

    @Excel(name = "物模型")
    private String thingsModelValue;

    /** 图片地址 */
    private String imgUrl;

    /** 是否自定义位置 **/
    private Integer locationWay;

    /** 设备摘要 **/
    private String summary;
    
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

    /** 分组ID，用于分组查询 **/
    private Long groupId;

    /** 是否设备所有者，用于查询 **/
    private Integer isOwner;
    
    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public Integer getLocationWay() {
        return locationWay;
    }

    public void setLocationWay(Integer locationWay) {
        this.locationWay = locationWay;
    }

    public Integer getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(Integer isOwner) {
        this.isOwner = isOwner;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getIsShadow() {
        return isShadow;
    }

    public void setIsShadow(Integer isShadow) {
        this.isShadow = isShadow;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
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
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setUserName(String userName) 
    {
        this.userName = userName;
    }

    public String getUserName() 
    {
        return userName;
    }
    public void setTenantId(Long tenantId) 
    {
        this.tenantId = tenantId;
    }

    public Long getTenantId() 
    {
        return tenantId;
    }
    public void setTenantName(String tenantName) 
    {
        this.tenantName = tenantName;
    }

    public String getTenantName() 
    {
        return tenantName;
    }
    public void setSerialNumber(String serialNumber) 
    {
        this.serialNumber = serialNumber;
    }

    public String getSerialNumber() 
    {
        return serialNumber;
    }
    public void setFirmwareVersion(BigDecimal firmwareVersion) 
    {
        this.firmwareVersion = firmwareVersion;
    }

    public BigDecimal getFirmwareVersion() 
    {
        return firmwareVersion;
    }
    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }
    public void setRssi(Integer rssi) 
    {
        this.rssi = rssi;
    }

    public Integer getRssi() 
    {
        return rssi;
    }
    public void setThingsModelValue(String thingsModelValue)
    {
        this.thingsModelValue = thingsModelValue;
    }

    public String getThingsModelValue()
    {
        return thingsModelValue;
    }
    public void setNetworkAddress(String networkAddress) 
    {
        this.networkAddress = networkAddress;
    }

    public String getNetworkAddress() 
    {
        return networkAddress;
    }
    public void setNetworkIp(String networkIp) 
    {
        this.networkIp = networkIp;
    }

    public String getNetworkIp() 
    {
        return networkIp;
    }
    public void setLongitude(BigDecimal longitude)
    {
        this.longitude = longitude;
    }

    public BigDecimal getLongitude()
    {
        return longitude;
    }
    public void setLatitude(BigDecimal latitude)
    {
        this.latitude = latitude;
    }

    public BigDecimal getLatitude()
    {
        return latitude;
    }
    public void setActiveTime(Date activeTime) 
    {
        this.activeTime = activeTime;
    }

    public Date getActiveTime() 
    {
        return activeTime;
    }

    public Integer getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(Integer deviceType) {
		this.deviceType = deviceType;
	}
	

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

	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("deviceId", getDeviceId())
            .append("deviceName", getDeviceName())
            .append("productId", getProductId())
            .append("productName", getProductName())
            .append("userId", getUserId())
            .append("userName", getUserName())
            .append("tenantId", getTenantId())
            .append("tenantName", getTenantName())
            .append("serialNumber", getSerialNumber())
            .append("firmwareVersion", getFirmwareVersion())
            .append("status", getStatus())
            .append("rssi", getRssi())
            .append("networkAddress", getNetworkAddress())
            .append("networkIp", getNetworkIp())
            .append("longitude", getLongitude())
            .append("latitude", getLatitude())
            .append("activeTime", getActiveTime())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }

	public String getDeviceModelType() {
		return deviceModelType;
	}

	public void setDeviceModelType(String deviceModelType) {
		this.deviceModelType = deviceModelType;
	}

	public String getBelongRoomName() {
		return belongRoomName;
	}

	public void setBelongRoomName(String belongRoomName) {
		this.belongRoomName = belongRoomName;
	}
}
