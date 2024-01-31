package com.ruoyi.iot.dto;

import com.ruoyi.iot.domain.AlertLog;

public class AlertLogAndFamilyWithUser extends AlertLog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** id */
    private Long familyId;

    /** 家庭名称 */
    private String familyName;

    /** 家庭位置 */
    private String familyPosition;
    
    /** 用户ID */
    private Long userId;

    /** 用户昵称 */
    private String nickName;

    /** 手机号码 */
    private String phonenumber;

	public Long getFamilyId() {
		return familyId;
	}

	public void setFamilyId(Long familyId) {
		this.familyId = familyId;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getFamilyPosition() {
		return familyPosition;
	}

	public void setFamilyPosition(String familyPosition) {
		this.familyPosition = familyPosition;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}
    
    
}
