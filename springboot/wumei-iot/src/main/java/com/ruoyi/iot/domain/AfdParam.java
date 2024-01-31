package com.ruoyi.iot.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 语音维护对象 iot_afd_param
 * 
 * @author renjiayue
 * @date 2022-12-25
 */
public class AfdParam extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 语音词 */
    @Excel(name = "语音词")
    private String name;

    /** 语音版本 */
    @Excel(name = "语音版本")
    private Integer aver;

    /** 语音词类型 1唤醒词 2免唤醒动作词 3动作词 4场景词 */
    @Excel(name = "语音词类型 1唤醒词 2免唤醒动作词 3动作词 4场景词")
    private Integer wordType;

    /** 硬件语音序号 */
    @Excel(name = "硬件语音序号")
    private Integer aorder;

    /** 支持设备类型 1语音开关 2语音窗帘 */
    @Excel(name = "支持设备类型 1语音开关 2语音窗帘")
    private Integer deviceType;

    /** 是否启用 0否1是 */
    @Excel(name = "是否启用 0否1是")
    private Integer enable;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setAver(Integer aver) 
    {
        this.aver = aver;
    }

    public Integer getAver() 
    {
        return aver;
    }
    public void setWordType(Integer wordType) 
    {
        this.wordType = wordType;
    }

    public Integer getWordType() 
    {
        return wordType;
    }
    public void setAorder(Integer aorder) 
    {
        this.aorder = aorder;
    }

    public Integer getAorder() 
    {
        return aorder;
    }
    public void setDeviceType(Integer deviceType) 
    {
        this.deviceType = deviceType;
    }

    public Integer getDeviceType() 
    {
        return deviceType;
    }
    public void setEnable(Integer enable) 
    {
        this.enable = enable;
    }

    public Integer getEnable() 
    {
        return enable;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("aver", getAver())
            .append("wordType", getWordType())
            .append("aorder", getAorder())
            .append("deviceType", getDeviceType())
            .append("enable", getEnable())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
