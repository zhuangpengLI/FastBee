package com.ruoyi.iot.mobile.respModel;

import com.ruoyi.common.annotation.Excel;

public class DeviceNoticeSetting extends DeviceBriefRespInfo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** 是否禁用通知 0否 1是 */
    @Excel(name = "是否禁用通知 0否 1是")
    private Integer isDisabled;
    
    private Long settingUserId;
    
    //通知标识符
    private String identifier;

	public Integer getIsDisabled() {
		return isDisabled;
	}

	public void setIsDisabled(Integer isDisabled) {
		this.isDisabled = isDisabled;
	}

	public Long getSettingUserId() {
		return settingUserId;
	}

	public void setSettingUserId(Long settingUserId) {
		this.settingUserId = settingUserId;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
}
