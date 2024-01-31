package com.ruoyi.iot.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 配电箱空开素材对象 iot_dbox_switch
 * 
 * @author renjiayue
 * @date 2022-10-08
 */
public class DboxSwitch extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 素材名称 */
    @Excel(name = "素材名称")
    private String name;

    /** 空开类型 */
    @Excel(name = "空开类型")
    private String switchType;

    /** 空开状态 0=正常(默认合闸)， 1=跳闸 分闸   2=欠压（上无电，合闸）  4=高温（合闸） */
    @Excel(name = "空开状态 0=正常(默认合闸)， 1=跳闸 分闸   2=欠压", readConverterExp = "上=无电，合闸")
    private Long switchStatus;

    /** 空开状态图 */
    @Excel(name = "空开状态图")
    private String imgUrl;

    /** 配电箱型号 */
    @Excel(name = "配电箱型号")
    private String dboxType;

    /** 配电箱id */
    @Excel(name = "配电箱id")
    private Long dboxId;

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
    public void setSwitchType(String switchType) 
    {
        this.switchType = switchType;
    }

    public String getSwitchType() 
    {
        return switchType;
    }
    public void setSwitchStatus(Long switchStatus) 
    {
        this.switchStatus = switchStatus;
    }

    public Long getSwitchStatus() 
    {
        return switchStatus;
    }
    public void setImgUrl(String imgUrl) 
    {
        this.imgUrl = imgUrl;
    }

    public String getImgUrl() 
    {
        return imgUrl;
    }
    public void setDboxType(String dboxType) 
    {
        this.dboxType = dboxType;
    }

    public String getDboxType() 
    {
        return dboxType;
    }
    public void setDboxId(Long dboxId) 
    {
        this.dboxId = dboxId;
    }

    public Long getDboxId() 
    {
        return dboxId;
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
            .append("switchType", getSwitchType())
            .append("switchStatus", getSwitchStatus())
            .append("imgUrl", getImgUrl())
            .append("dboxType", getDboxType())
            .append("dboxId", getDboxId())
            .append("enable", getEnable())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
