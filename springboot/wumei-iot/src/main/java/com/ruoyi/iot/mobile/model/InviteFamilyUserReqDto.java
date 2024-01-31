package com.ruoyi.iot.mobile.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 家庭管理对象 iot_family
 * 
 * @author renjiayue
 * @date 2022-09-07
 */
@ApiModel("邀请家庭成员(分享家庭)")
public class InviteFamilyUserReqDto implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** id */
    @ApiModelProperty("家庭id")
    @NotNull(message = "家庭信息不能为空")
    private Long familyId;

    /** 手机号码 */
    @ApiModelProperty("手机号码")
    @NotNull(message = "手机号码不能为空")
    private String phonenumber;
    
    /** 角色  1普通用户 2管理员 */
    @ApiModelProperty("角色  1普通用户 2管理员")
    private String familyUserRole;

    public void setFamilyId(Long familyId) 
    {
        this.familyId = familyId;
    }

    public Long getFamilyId() 
    {
        return familyId;
    }

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getFamilyUserRole() {
		return familyUserRole;
	}

	public void setFamilyUserRole(String familyUserRole) {
		this.familyUserRole = familyUserRole;
	}
    

}
