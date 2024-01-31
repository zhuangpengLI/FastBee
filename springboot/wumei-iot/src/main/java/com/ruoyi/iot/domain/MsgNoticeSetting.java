package com.ruoyi.iot.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.TreeEntity;

/**
 * 消息设置对象 msg_notice_setting
 * 
 * @author renjiayue
 * @date 2022-10-21
 */
public class MsgNoticeSetting extends TreeEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;

    /** 标识符 SYS_TOTAL(系统通知) SYS_FAMILY_SHARE(家庭共享) SYS_DEVICE_SHARE(设备共享) SYS_OFFICAL(官方消息) DEV_TOTAL(设备通知) DEV_FAMILY(设备通知-家庭) DEV_DEVICE(设备通知-家庭-设备) */
    @Excel(name = "标识符 SYS_TOTAL(系统通知) SYS_FAMILY_SHARE(家庭共享) SYS_DEVICE_SHARE(设备共享) SYS_OFFICAL(官方消息) DEV_TOTAL(设备通知) DEV_FAMILY(设备通知-家庭) DEV_DEVICE(设备通知-家庭-设备)")
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
            .toString();
    }
}
