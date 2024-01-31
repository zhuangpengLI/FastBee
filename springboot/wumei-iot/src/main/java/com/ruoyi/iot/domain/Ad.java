package com.ruoyi.iot.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 广告对象 iot_ad
 * 
 * @author renjiayue
 * @date 2022-09-03
 */
public class Ad extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 广告标题 */
    @Excel(name = "广告标题")
    private String name;

    /** 所广告的商品页面或者活动页面链接地址 */
    @Excel(name = "所广告的商品页面或者活动页面链接地址")
    private String link;

    /** 广告宣传图片 */
    @Excel(name = "广告宣传图片")
    private String url;

    /** 广告位置：1是启动页面 2是首页 3是我的页面 */
    @Excel(name = "广告位置：1是启动页面 2是首页 3是我的页面")
    private Integer position;

    /** 广告内容 */
    @Excel(name = "广告内容")
    private String content;

    /** 是否启动 */
    @Excel(name = "是否启动")
    private Integer enabled;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date addTime;

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
    public void setLink(String link) 
    {
        this.link = link;
    }

    public String getLink() 
    {
        return link;
    }
    public void setUrl(String url) 
    {
        this.url = url;
    }

    public String getUrl() 
    {
        return url;
    }
    public void setPosition(Integer position) 
    {
        this.position = position;
    }

    public Integer getPosition() 
    {
        return position;
    }
    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }
    public void setEnabled(Integer enabled) 
    {
        this.enabled = enabled;
    }

    public Integer getEnabled() 
    {
        return enabled;
    }
    public void setAddTime(Date addTime) 
    {
        this.addTime = addTime;
    }

    public Date getAddTime() 
    {
        return addTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("link", getLink())
            .append("url", getUrl())
            .append("position", getPosition())
            .append("content", getContent())
            .append("enabled", getEnabled())
            .append("addTime", getAddTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
