package com.ruoyi.iot.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 网关电量统计对象 iot_dbox_ele
 * 
 * @author renjiayue
 * @date 2023-08-02
 */
public class DboxEle extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 设备ID */
    @Excel(name = "设备ID")
    private Long deviceId;

    /** 耗电量 */
    @Excel(name = "耗电量")
    private BigDecimal eleValue;

    /** 耗电日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "耗电日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date eleTime;

    /** 年 */
    @Excel(name = "年")
    private Long year;

    /** 月 */
    @Excel(name = "月")
    private Long month;

    /** 日 */
    @Excel(name = "日")
    private Long day;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setDeviceId(Long deviceId) 
    {
        this.deviceId = deviceId;
    }

    public Long getDeviceId() 
    {
        return deviceId;
    }
    public void setEleValue(BigDecimal eleValue) 
    {
        this.eleValue = eleValue;
    }

    public BigDecimal getEleValue() 
    {
        return eleValue;
    }
    public void setEleTime(Date eleTime) 
    {
        this.eleTime = eleTime;
    }

    public Date getEleTime() 
    {
        return eleTime;
    }
    public void setYear(Long year) 
    {
        this.year = year;
    }

    public Long getYear() 
    {
        return year;
    }
    public void setMonth(Long month) 
    {
        this.month = month;
    }

    public Long getMonth() 
    {
        return month;
    }
    public void setDay(Long day) 
    {
        this.day = day;
    }

    public Long getDay() 
    {
        return day;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("deviceId", getDeviceId())
            .append("eleValue", getEleValue())
            .append("eleTime", getEleTime())
            .append("year", getYear())
            .append("month", getMonth())
            .append("day", getDay())
            .toString();
    }
}
