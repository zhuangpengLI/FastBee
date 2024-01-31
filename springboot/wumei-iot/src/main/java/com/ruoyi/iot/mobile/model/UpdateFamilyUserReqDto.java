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
@ApiModel("更新家庭成员")
public class UpdateFamilyUserReqDto implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** id */
    @ApiModelProperty("家庭id")
    @NotNull(message = "家庭信息不能为空")
    private Long familyId;
    
    @ApiModelProperty("用户id")
    @NotNull(message = "用户信息不能为空")
    private Long userId;

    /** 角色  1普通用户 2管理员 */
    @ApiModelProperty("角色  1普通用户 2管理员")
    @NotNull(message = "角色家庭信息不能为空")
    private String familyUserRole;

    public void setFamilyId(Long familyId) 
    {
        this.familyId = familyId;
    }

    public Long getFamilyId() 
    {
        return familyId;
    }

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getFamilyUserRole() {
		return familyUserRole;
	}

	public void setFamilyUserRole(String familyUserRole) {
		this.familyUserRole = familyUserRole;
	}
    

}
