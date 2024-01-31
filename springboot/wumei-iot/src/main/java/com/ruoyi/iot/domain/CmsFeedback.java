package com.ruoyi.iot.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 意见反馈对象 cms_feedback
 * 
 * @author renjiayue
 * @date 2022-09-07
 */
public class CmsFeedback extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 用户表的用户ID */
    private Long userId;

    /** 用户名称 */
    @Excel(name = "用户名称")
    private String username;
    
    /** 用户昵称 */
    @Excel(name = "用户昵称")
    private String nickName;

    /** 手机号 */
    @Excel(name = "手机号")
    private String mobile;

    /** 反馈类型  1投诉 2建议 3其他 */
    private String feedType;

    /** 来源 1 IOS  2 Android */
    @Excel(name = "来源 1 IOS  2 Android")
    private String source;

    /** 反馈标题 */
    private String title;

    /** 反馈内容 */
    @Excel(name = "反馈内容")
    private String content;

    /** 状态 0未回复 1已回复 */
    private Long status;

    /** 是否含有图片 */
    private Integer hasPicture;

    /** 图片地址列表，采用JSON数组格式 */
    @Excel(name = "图片地址列表，采用JSON数组格式")
    private String picUrls;

    /** 回复内容 */
    @Excel(name = "回复内容")
    private String reply;

    /** 回复时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "回复时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date replyTime;

    /** 创建时间(反馈时间) */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间(反馈时间)", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date addTime;

    /** 逻辑删除 */
    private Integer deleted;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setUsername(String username) 
    {
        this.username = username;
    }

    public String getUsername() 
    {
        return username;
    }
    public void setMobile(String mobile) 
    {
        this.mobile = mobile;
    }

    public String getMobile() 
    {
        return mobile;
    }
    public void setFeedType(String feedType) 
    {
        this.feedType = feedType;
    }

    public String getFeedType() 
    {
        return feedType;
    }
    public void setSource(String source) 
    {
        this.source = source;
    }

    public String getSource() 
    {
        return source;
    }
    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }
    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }
    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }
    public void setHasPicture(Integer hasPicture) 
    {
        this.hasPicture = hasPicture;
    }

    public Integer getHasPicture() 
    {
        return hasPicture;
    }
    public void setPicUrls(String picUrls) 
    {
        this.picUrls = picUrls;
    }

    public String getPicUrls() 
    {
        return picUrls;
    }
    public void setReply(String reply) 
    {
        this.reply = reply;
    }

    public String getReply() 
    {
        return reply;
    }
    public void setReplyTime(Date replyTime) 
    {
        this.replyTime = replyTime;
    }

    public Date getReplyTime() 
    {
        return replyTime;
    }
    public void setAddTime(Date addTime) 
    {
        this.addTime = addTime;
    }

    public Date getAddTime() 
    {
        return addTime;
    }
    public void setDeleted(Integer deleted) 
    {
        this.deleted = deleted;
    }

    public Integer getDeleted() 
    {
        return deleted;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("username", getUsername())
            .append("mobile", getMobile())
            .append("feedType", getFeedType())
            .append("source", getSource())
            .append("title", getTitle())
            .append("content", getContent())
            .append("status", getStatus())
            .append("hasPicture", getHasPicture())
            .append("picUrls", getPicUrls())
            .append("reply", getReply())
            .append("replyTime", getReplyTime())
            .append("addTime", getAddTime())
            .append("updateTime", getUpdateTime())
            .append("deleted", getDeleted())
            .toString();
    }

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
}
