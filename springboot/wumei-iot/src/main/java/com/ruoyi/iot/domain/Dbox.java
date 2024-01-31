package com.ruoyi.iot.domain;

import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 配电箱配置对象 iot_dbox
 * 
 * @author renjiayue
 * @date 2022-10-08
 */
public class Dbox extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 配电箱型号 */
    @Excel(name = "配电箱型号")
    private String dboxType;

    /** 配电箱背景图 */
    @Excel(name = "配电箱背景图")
    private String bgImgUrl;

    /** 背景线条图片 */
    @Excel(name = "背景线条图片")
    private String stringImgUrl;

    /** 配电箱名称 */
    @Excel(name = "配电箱名称")
    private String dboxName;

    /** 是否启用 0否1是 */
    @Excel(name = "是否启用 0否1是")
    private Integer enable;

    /** 配电箱空开素材信息 */
    private List<DboxSwitch> dboxSwitchList;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setDboxType(String dboxType) 
    {
        this.dboxType = dboxType;
    }

    public String getDboxType() 
    {
        return dboxType;
    }
    public void setBgImgUrl(String bgImgUrl) 
    {
        this.bgImgUrl = bgImgUrl;
    }

    public String getBgImgUrl() 
    {
        return bgImgUrl;
    }
    public void setStringImgUrl(String stringImgUrl) 
    {
        this.stringImgUrl = stringImgUrl;
    }

    public String getStringImgUrl() 
    {
        return stringImgUrl;
    }
    public void setDboxName(String dboxName) 
    {
        this.dboxName = dboxName;
    }

    public String getDboxName() 
    {
        return dboxName;
    }
    public void setEnable(Integer enable) 
    {
        this.enable = enable;
    }

    public Integer getEnable() 
    {
        return enable;
    }

    public List<DboxSwitch> getDboxSwitchList()
    {
        return dboxSwitchList;
    }

    public void setDboxSwitchList(List<DboxSwitch> dboxSwitchList)
    {
        this.dboxSwitchList = dboxSwitchList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("dboxType", getDboxType())
            .append("bgImgUrl", getBgImgUrl())
            .append("stringImgUrl", getStringImgUrl())
            .append("dboxName", getDboxName())
            .append("enable", getEnable())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("dboxSwitchList", getDboxSwitchList())
            .toString();
    }
}
