package com.ruoyi.iot.mobile.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class MobileLoginUserInput {
    /**
     * 验证码
     */
	@ApiModelProperty("验证码")
    private String code;

	@ApiModelProperty("手机号")
    private String phonenumber;
	
	@ApiModelProperty("极光推送registrationId 用于唯一推送")
	private String registrationId;

    public String getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}

	public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }
}
