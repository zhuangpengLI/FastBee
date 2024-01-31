package com.ruoyi.system.otherDomain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 消息通知对象 iot_msg
 * 
 * @author renjiayue
 * @date 2022-10-06
 */
public class Msg extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 消息标题 */
    @Excel(name = "消息标题")
    private String msgTitle;

    /** 消息类型（1通知(系统消息) 2公告(系统消息) 50其他系统消息 99家庭消息） */
    @Excel(name = "消息类型", readConverterExp = "消息类型（1通知(系统消息) 2公告(系统消息) 50其他系统消息 99家庭消息）")
    private String msgType;

    /** 消息内容 */
    @Excel(name = "消息内容")
    private String msgContent;

    /** 消息图片 */
    @Excel(name = "消息图片")
    private String imgUrl;

    /** 家庭id */
    @Excel(name = "家庭id")
    private Long familyId;

    /** 房间id */
    @Excel(name = "房间id")
    private Long roomId;

    /** 设备id */
    @Excel(name = "设备id")
    private Long deviceId;
    
    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;
    
    /** 系统通知id (通知和公告) */
    @Excel(name = "系统通知id (通知和公告)")
    private Long noticeId;

    /** 是否已读 0否 1是 */
    @Excel(name = "是否已读 0否 1是")
    private Integer isRead;
    
    private String familyName;
    
    public Msg() {
		super();
	}

	public Msg(String msgTitle, String msgType, String msgContent, String imgUrl, Long familyId, Long roomId,
			Long deviceId, Long userId, Long noticeId) {
		super();
		this.msgTitle = msgTitle;
		this.msgType = msgType;
		this.msgContent = msgContent;
		this.imgUrl = imgUrl;
		this.familyId = familyId;
		this.roomId = roomId;
		this.deviceId = deviceId;
		this.userId = userId;
		this.noticeId = noticeId;
		this.isRead = 0;
	}

	public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setMsgTitle(String msgTitle) 
    {
        this.msgTitle = msgTitle;
    }

    public String getMsgTitle() 
    {
        return msgTitle;
    }
    public void setMsgType(String msgType) 
    {
        this.msgType = msgType;
    }

    public String getMsgType() 
    {
        return msgType;
    }
    public void setMsgContent(String msgContent) 
    {
        this.msgContent = msgContent;
    }

    public String getMsgContent() 
    {
        return msgContent;
    }
    public void setImgUrl(String imgUrl) 
    {
        this.imgUrl = imgUrl;
    }

    public String getImgUrl() 
    {
        return imgUrl;
    }
    public void setFamilyId(Long familyId) 
    {
        this.familyId = familyId;
    }

    public Long getFamilyId() 
    {
        return familyId;
    }
    public void setRoomId(Long roomId) 
    {
        this.roomId = roomId;
    }

    public Long getRoomId() 
    {
        return roomId;
    }
    public void setDeviceId(Long deviceId) 
    {
        this.deviceId = deviceId;
    }

    public Long getDeviceId() 
    {
        return deviceId;
    }
    public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(Long noticeId) {
		this.noticeId = noticeId;
	}

	public void setIsRead(Integer isRead) 
    {
        this.isRead = isRead;
    }

    public Integer getIsRead() 
    {
        return isRead;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("msgTitle", getMsgTitle())
            .append("msgType", getMsgType())
            .append("msgContent", getMsgContent())
            .append("imgUrl", getImgUrl())
            .append("familyId", getFamilyId())
            .append("roomId", getRoomId())
            .append("deviceId", getDeviceId())
            .append("userId", getUserId())
            .append("isRead", getIsRead())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}
}
