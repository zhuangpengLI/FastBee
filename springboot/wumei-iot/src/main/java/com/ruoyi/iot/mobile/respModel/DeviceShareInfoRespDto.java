package com.ruoyi.iot.mobile.respModel;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;

/**
 * 需操作消息列表对象 msg_oper_msg
 * 
 * @author renjiayue
 * @date 2022-09-18
 */
public class DeviceShareInfoRespDto   implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 发送方用户id */
    @Excel(name = "发送方用户id")
    private Long sendUserId;

    /** 发送方用户名称 */
    @Excel(name = "发送方用户名称")
    private String sendUserName;

    /** 接收方用户id */
    @Excel(name = "接收方用户id")
    private Long receiveUserId;

    /** 接收方用户名称 */
    @Excel(name = "接收方用户名称")
    private String receiveUserName;
    
    /**
     * 接收方手机号
     */
    private String receiveUserPhone;
    /**
     * 接收方头像
     */
    private String receiveUserAvatar;

    /** 消息类型 01家庭分享 02设备分享 03家庭申请 */
    @Excel(name = "消息类型 01家庭分享 02设备分享 03家庭申请")
    private String msgType;

    /** 是否需要操作 0否(比如申请不需要审核的情况) 1是 */
    @Excel(name = "是否需要操作 0否(比如申请不需要审核的情况) 1是")
    private Integer isOper;

    /** 消息类型中文描述 比如:分享了 共享 申请加入等 */
    @Excel(name = "消息类型中文描述 比如:分享了 共享 申请加入等")
    private String msgTypeName;

    /** 家庭id */
    @Excel(name = "家庭id")
    private Long familyId;

    /** 家庭名称 */
    @Excel(name = "家庭名称")
    private String familyName;

    /** 设备id */
    @Excel(name = "设备id")
    private Long deviceId;

    /** 设备名称 */
    @Excel(name = "设备名称")
    private String deviceName;

    /** 00 未处理 01已同意 02已拒绝 98已失效(别的请求已同意) 99已过期 */
    @Excel(name = "00 未处理 01已同意 02已拒绝 98已失效(别的请求已同意) 99已过期")
    private String status;

    /** 创建时间(分享/申请时间) */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间(分享/申请时间)", width = 30, dateFormat = "yyyy-MM-dd")
    private Date addTime;

    /** 消息处理时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "消息处理时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date operTime;

    /** 消息过期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "消息过期时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date expireTime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setSendUserId(Long sendUserId) 
    {
        this.sendUserId = sendUserId;
    }

    public Long getSendUserId() 
    {
        return sendUserId;
    }
    public void setSendUserName(String sendUserName) 
    {
        this.sendUserName = sendUserName;
    }

    public String getSendUserName() 
    {
        return sendUserName;
    }
    public void setReceiveUserId(Long receiveUserId) 
    {
        this.receiveUserId = receiveUserId;
    }

    public Long getReceiveUserId() 
    {
        return receiveUserId;
    }
    public void setReceiveUserName(String receiveUserName) 
    {
        this.receiveUserName = receiveUserName;
    }

    public String getReceiveUserName() 
    {
        return receiveUserName;
    }
    public void setMsgType(String msgType) 
    {
        this.msgType = msgType;
    }

    public String getMsgType() 
    {
        return msgType;
    }
    public void setIsOper(Integer isOper) 
    {
        this.isOper = isOper;
    }

    public Integer getIsOper() 
    {
        return isOper;
    }
    public void setMsgTypeName(String msgTypeName) 
    {
        this.msgTypeName = msgTypeName;
    }

    public String getMsgTypeName() 
    {
        return msgTypeName;
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
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setAddTime(Date addTime) 
    {
        this.addTime = addTime;
    }

    public Date getAddTime() 
    {
        return addTime;
    }
    public void setOperTime(Date operTime) 
    {
        this.operTime = operTime;
    }

    public Date getOperTime() 
    {
        return operTime;
    }
    public void setExpireTime(Date expireTime) 
    {
        this.expireTime = expireTime;
    }

    public Date getExpireTime() 
    {
        return expireTime;
    }
    

    public String getReceiveUserPhone() {
		return receiveUserPhone;
	}

	public void setReceiveUserPhone(String receiveUserPhone) {
		this.receiveUserPhone = receiveUserPhone;
	}

	public String getReceiveUserAvatar() {
		return receiveUserAvatar;
	}

	public void setReceiveUserAvatar(String receiveUserAvatar) {
		this.receiveUserAvatar = receiveUserAvatar;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("sendUserId", getSendUserId())
            .append("sendUserName", getSendUserName())
            .append("receiveUserId", getReceiveUserId())
            .append("receiveUserName", getReceiveUserName())
            .append("msgType", getMsgType())
            .append("isOper", getIsOper())
            .append("msgTypeName", getMsgTypeName())
            .append("familyId", getFamilyId())
            .append("familyName", getFamilyName())
            .append("deviceId", getDeviceId())
            .append("deviceName", getDeviceName())
            .append("status", getStatus())
            .append("addTime", getAddTime())
            .append("operTime", getOperTime())
            .append("expireTime", getExpireTime())
            .toString();
    }
}
