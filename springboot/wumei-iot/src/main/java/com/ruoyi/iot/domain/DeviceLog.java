package com.ruoyi.iot.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Date;
import java.util.List;

/**
 * 设备日志对象 iot_device_log
 *
 * @author kerwincui
 * @date 2022-01-13
 */
public class DeviceLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "时间戳")
    private Date ts;
    /** 设备日志ID */
    private Long logId;

    /** 类型（1=属性上报，2=调用功能，3=事件上报，4=设备升级，5=设备上线，6=设备离线） */
    @Excel(name = "类型", readConverterExp = "类型（1=属性上报，2=调用功能，3=事件上报，4=设备升级，5=设备上线，6=设备离线）")
    private Integer logType;

    /** 日志值 */
    @Excel(name = "日志值")
    private String logValue;
    
    /**
     * 日志文本
     */
    private String logText;

    /** 设备ID */
    @Excel(name = "设备ID")
    private Long deviceId;

    /** 设备名称 */
    @Excel(name = "设备名称")
    private String deviceName;

    /** 设备编号 */
    @Excel(name = "设备编号")
    private String serialNumber;

    /** 标识符 */
    @Excel(name = "标识符")
    private String identity;

    /** 是否监测数据（1=是，0=否） */
    @Excel(name = "是否监测数据", readConverterExp = "1=是，0=否")
    private Integer isMonitor;

    /** 模式 */
    @Excel(name = "模式", readConverterExp = "1=影子模式，2=在线模式,3=其他")
    private Integer mode;

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

    /** 查询用的开始时间 */
    private String beginTime;

    /** 查询用的结束时间 */
    private String endTime;

    /** 查询的总数 */
    private int total;
    
    /** 用户昵称 查询结果 */
    private String nickName;

    /** 手机号码 查询结果 */
    private String phonenumber;
    
    //--查询
    private List<Long> familyIds;
    //-- 查询及返回结果
    private Long familyId;
    private Long roomId;
    //---返回结果
    private String familyName;
    private String roomName;
    
    /** 设备图片地址 */
    private String imgUrl;
    
    //设备型号  例如
    //1001 1002单键开关  			1003双键开关 		1004三键开关 		1005三键+温湿度 
    //1011 1012单键语音开关 		1013双键语音开关	1014三键语音开关	1015三键+温湿度语音开关
    //2001 5孔10A			 		2002 3孔16A
    //3001 单轨窗帘 				3002 双轨窗帘
    //3011 单轨语音窗帘 				3012 双轨语音窗帘
    //4001 烟感报警器 				4002燃气报警器
    private String deviceModelType;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void setLogId(Long logId)
    {
        this.logId = logId;
    }

    public Long getLogId() 
    {
        return logId;
    }

    public void setLogType(Integer logType) 
    {
        this.logType = logType;
    }

    public Integer getLogType() 
    {
        return logType;
    }
    public void setLogValue(String logValue) 
    {
        this.logValue = logValue;
    }

    public String getLogValue() 
    {
        return logValue;
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
    public void setIdentity(String identity) 
    {
        this.identity = identity;
    }

    public String getIdentity() 
    {
        return identity;
    }
    public void setIsMonitor(Integer isMonitor) 
    {
        this.isMonitor = isMonitor;
    }

    public Integer getIsMonitor() 
    {
        return isMonitor;
    }

	public List<Long> getFamilyIds() {
		return familyIds;
	}

	public void setFamilyIds(List<Long> familyIds) {
		this.familyIds = familyIds;
	}

	public Long getFamilyId() {
		return familyId;
	}

	public void setFamilyId(Long familyId) {
		this.familyId = familyId;
	}

	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("logId", getLogId())
            .append("logType", getLogType())
            .append("logValue", getLogValue())
            .append("deviceId", getDeviceId())
            .append("deviceName", getDeviceName())
            .append("identity", getIdentity())
            .append("createBy", getCreateBy())
            .append("isMonitor", getIsMonitor())
            .append("createTime", getCreateTime())
            .append("remark", getRemark())
            .toString();
    }

	public String getLogText() {
		return logText;
	}

	public void setLogText(String logText) {
		this.logText = logText;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getDeviceModelType() {
		return deviceModelType;
	}

	public void setDeviceModelType(String deviceModelType) {
		this.deviceModelType = deviceModelType;
	}
	
}
