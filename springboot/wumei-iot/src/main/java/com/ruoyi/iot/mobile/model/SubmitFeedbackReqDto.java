package com.ruoyi.iot.mobile.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.ruoyi.common.annotation.Excel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 意见反馈对象 cms_feedback
 * 
 * @author renjiayue
 * @date 2022-09-07
 */
@ApiModel
public class SubmitFeedbackReqDto implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 反馈类型 */
    @ApiModelProperty("反馈类型  1投诉 2建议 3其他")
    @NotNull
    private String feedType;

    /** 来源 1 IOS  2 Android */
    @ApiModelProperty("来源 1 IOS  2 Android ")
    @NotNull
    private String source;

    /** 反馈标题 */
    @ApiModelProperty("反馈标题")
    private String title;

    /** 反馈内容 */
    @ApiModelProperty("反馈内容")
    @NotNull
    private String content;

    /** 图片地址列表，采用JSON数组格式 */
    @ApiModelProperty("图片地址列表，采用JSON数组格式,空则为[]")
    @NotNull
    private String picUrls;

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
    public void setPicUrls(String picUrls) 
    {
        this.picUrls = picUrls;
    }

    public String getPicUrls() 
    {
        return picUrls;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("feedType", getFeedType())
            .append("source", getSource())
            .append("title", getTitle())
            .append("content", getContent())
            .append("picUrls", getPicUrls())
            .toString();
    }
}
