package com.ruoyi.iot.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.TreeEntity;

/**
 * 短信通知设置对象 msg_sms_notice_setting
 * 
 * @author renjiayue
 * @date 2022-11-08
 */
public class MsgSmsNoticeSetting extends TreeEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;

    /** DEV_TOTAL(设备通知) DEV_FAMILY(设备通知-家庭) DEV_DEVICE(设备通知-家庭-设备) */
    @Excel(name = "DEV_TOTAL(设备通知) DEV_FAMILY(设备通知-家庭) DEV_DEVICE(设备通知-家庭-设备)")
    private String identifier;

    /** 通知设置名称 */
    @Excel(name = "通知设置名称")
    private String settingName;

    /** 标识符对应业务id 如家庭id 设备id */
    @Excel(name = "标识符对应业务id 如家庭id 设备id")
    private String busId;

    /** 是否禁用通知 0否 1是 */
    @Excel(name = "是否禁用通知 0否 1是")
    private Integer isDisabled;

    /** 1短信条数限制 2短信有效期限制 */
    @Excel(name = "1短信条数限制 2短信有效期限制")
    private Integer smsLimitType;

    /** 限制短信条数 */
    @Excel(name = "限制短信条数")
    private Integer smsLimitCount;

    /** 短信已发送条数 */
    @Excel(name = "短信已发送条数")
    private Integer smsSendedCount;

    /** 限制短信有效截止日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "限制短信有效截止日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date smsLimitDate;

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
    public void setIdentifier(String identifier) 
    {
        this.identifier = identifier;
    }

    public String getIdentifier() 
    {
        return identifier;
    }
    public void setSettingName(String settingName) 
    {
        this.settingName = settingName;
    }

    public String getSettingName() 
    {
        return settingName;
    }
    public void setBusId(String busId) 
    {
        this.busId = busId;
    }

    public String getBusId() 
    {
        return busId;
    }
    public void setIsDisabled(Integer isDisabled) 
    {
        this.isDisabled = isDisabled;
    }

    public Integer getIsDisabled() 
    {
        return isDisabled;
    }
    public void setSmsLimitType(Integer smsLimitType) 
    {
        this.smsLimitType = smsLimitType;
    }

    public Integer getSmsLimitType() 
    {
        return smsLimitType;
    }
    public void setSmsLimitCount(Integer smsLimitCount) 
    {
        this.smsLimitCount = smsLimitCount;
    }

    public Integer getSmsLimitCount() 
    {
        return smsLimitCount;
    }
    public void setSmsSendedCount(Integer smsSendedCount) 
    {
        this.smsSendedCount = smsSendedCount;
    }

    public Integer getSmsSendedCount() 
    {
        return smsSendedCount;
    }
    public void setSmsLimitDate(Date smsLimitDate) 
    {
        this.smsLimitDate = smsLimitDate;
    }

    public Date getSmsLimitDate() 
    {
        return smsLimitDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("identifier", getIdentifier())
            .append("settingName", getSettingName())
            .append("busId", getBusId())
            .append("parentId", getParentId())
            .append("isDisabled", getIsDisabled())
            .append("smsLimitType", getSmsLimitType())
            .append("smsLimitCount", getSmsLimitCount())
            .append("smsSendedCount", getSmsSendedCount())
            .append("smsLimitDate", getSmsLimitDate())
            .toString();
    }
}
