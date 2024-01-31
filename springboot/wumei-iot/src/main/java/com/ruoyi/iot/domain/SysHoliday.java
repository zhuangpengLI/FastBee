package com.ruoyi.iot.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 系统节假日对象 sys_holiday
 * 
 * @author renjiayue
 * @date 2022-11-14
 */
public class SysHoliday extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 设置日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "设置日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date setDate;

    /** 1节假日 2工作日 */
    @Excel(name = "1节假日 2工作日")
    private Long dateType;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setSetDate(Date setDate) 
    {
        this.setDate = setDate;
    }

    public Date getSetDate() 
    {
        return setDate;
    }
    public void setDateType(Long dateType) 
    {
        this.dateType = dateType;
    }

    public Long getDateType() 
    {
        return dateType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("setDate", getSetDate())
            .append("dateType", getDateType())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
