package com.ruoyi.iot.mobile.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 家庭管理对象 iot_family
 * 
 * @author renjiayue
 * @date 2022-09-07
 */
@ApiModel
public class AddOrUpdateFamilyReqDto implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** id */
    @ApiModelProperty("id")
    private Long familyId;

    /** 家庭名称 */
    @ApiModelProperty("家庭名称")
    @NotNull(message="家庭名称不能为空")
    private String name;

    /** 头像url */
    @ApiModelProperty("头像url")
    @NotNull(message="家庭头像url不能为空")
    private String avatarUrl;

    /** 位置 */
    @ApiModelProperty("位置")
    @NotNull(message="家庭位置不能为空")
    private String position;

    /** 扫描网关加入家庭权限是否开启 0否1是 */
    @ApiModelProperty("扫描网关加入家庭权限是否开启 0否1是")
    private Integer isEnableJoinAuth;

    public void setFamilyId(Long familyId) 
    {
        this.familyId = familyId;
    }

    public Long getFamilyId() 
    {
        return familyId;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setAvatarUrl(String avatarUrl) 
    {
        this.avatarUrl = avatarUrl;
    }

    public String getAvatarUrl() 
    {
        return avatarUrl;
    }
    public void setPosition(String position) 
    {
        this.position = position;
    }

    public String getPosition() 
    {
        return position;
    }
    public void setIsEnableJoinAuth(Integer isEnableJoinAuth) 
    {
        this.isEnableJoinAuth = isEnableJoinAuth;
    }

    public Integer getIsEnableJoinAuth() 
    {
        return isEnableJoinAuth;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("familyId", getFamilyId())
            .append("name", getName())
            .append("avatarUrl", getAvatarUrl())
            .append("position", getPosition())
            .append("isEnableJoinAuth", getIsEnableJoinAuth())
            .toString();
    }
}
