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
public class BindFamilyGatewaySnReqDto implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** id */
    @ApiModelProperty("id")
    @NotNull(message="家庭id不能为空")
    private Long familyId;

    /** 扫描网关加入家庭权限是否开启 0否1是 */
    @ApiModelProperty("扫描网关加入家庭权限是否开启 0否1是")
    @NotNull(message="网关权限标识不能为空")
    private Integer isEnableJoinAuth;

    /** 网关sn码 */
    @ApiModelProperty("网关sn码")
    @NotNull(message="网关sn码不能为空")
    private String gatewaySn;

    public void setFamilyId(Long familyId) 
    {
        this.familyId = familyId;
    }

    public Long getFamilyId() 
    {
        return familyId;
    }
    public void setIsEnableJoinAuth(Integer isEnableJoinAuth) 
    {
        this.isEnableJoinAuth = isEnableJoinAuth;
    }

    public Integer getIsEnableJoinAuth() 
    {
        return isEnableJoinAuth;
    }
    public void setGatewaySn(String gatewaySn) 
    {
        this.gatewaySn = gatewaySn;
    }

    public String getGatewaySn() 
    {
        return gatewaySn;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("familyId", getFamilyId())
            .append("isEnableJoinAuth", getIsEnableJoinAuth())
            .append("gatewaySn", getGatewaySn())
            .toString();
    }
}
