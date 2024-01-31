package com.ruoyi.iot.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 家庭用户关联对象 iot_family_user_rela
 * 
 * @author renjiayue
 * @date 2022-09-07
 */
public class FamilyUserRela extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 家庭id */
    private Long familyId;

    /** 用户id */
    private Long userId;

    /** 角色  1普通用户 2管理员 */
    private String familyUserRole;
    
    //--------------用户信息 begin
    /** 用户昵称 */
    private String nickName;

    /** 手机号码 */
    private String phonenumber;
    /** 用户头像url */
    private String userAvatarUrl;
    
    /**
     * 用户身份
     * 派生属性
     * 1普通用户 3创建者4归属者
     */
    private String userIdentity;
    //--------------用户信息 end
    
    //---------------------家庭信息begin
    /** 家庭名称 */
    private String familyName;

    /** 家庭头像url */
    private String familyAvatarUrl;

    /** 家庭位置 */
    private String familyPosition;

    /** 家庭扫描网关加入家庭权限是否开启 0否1是 */
    private Integer familyIsEnableJoinAuth;

    /** 家庭是否绑定网关 0否1是 */
    private Integer familyIsBind;

    /** 家庭网关sn码 */
    private String familyGatewaySn;
    
  //---------------------家庭信息end
    
  //---------------------家庭统计信息begin
  //房间数量
  	private Integer countRoom;
  	//	成员数量
  	private Integer countUser;
  	//设备数量
  	private Integer countDevice;
  	
    //---------------------家庭统计信息end

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setFamilyId(Long familyId) 
    {
        this.familyId = familyId;
    }

    public Long getFamilyId() 
    {
        return familyId;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setFamilyUserRole(String familyUserRole) 
    {
        this.familyUserRole = familyUserRole;
    }

    public String getFamilyUserRole() 
    {
        return familyUserRole;
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

	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("familyId", getFamilyId())
            .append("userId", getUserId())
            .append("familyUserRole", getFamilyUserRole())
            .append("createTime", getCreateTime())
            .append("nickName", getNickName())
            .append("phonenumber", getPhonenumber())
            .toString();
    }

	public String getUserIdentity() {
		return userIdentity;
	}

	public void setUserIdentity(String userIdentity) {
		this.userIdentity = userIdentity;
	}

	public String getfamilyName() {
		return familyName;
	}

	public void setfamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getfamilyAvatarUrl() {
		return familyAvatarUrl;
	}

	public void setfamilyAvatarUrl(String familyAvatarUrl) {
		this.familyAvatarUrl = familyAvatarUrl;
	}

	public String getfamilyPosition() {
		return familyPosition;
	}

	public void setfamilyPosition(String familyPosition) {
		this.familyPosition = familyPosition;
	}

	public Integer getfamilyIsEnableJoinAuth() {
		return familyIsEnableJoinAuth;
	}

	public void setfamilyIsEnableJoinAuth(Integer familyIsEnableJoinAuth) {
		this.familyIsEnableJoinAuth = familyIsEnableJoinAuth;
	}

	public Integer getfamilyIsBind() {
		return familyIsBind;
	}

	public void setfamilyIsBind(Integer familyIsBind) {
		this.familyIsBind = familyIsBind;
	}

	public String getfamilyGatewaySn() {
		return familyGatewaySn;
	}

	public void setfamilyGatewaySn(String familyGatewaySn) {
		this.familyGatewaySn = familyGatewaySn;
	}
	public Integer getCountRoom() {
  		return countRoom;
  	}
  	public void setCountRoom(Integer countRoom) {
  		this.countRoom = countRoom;
  	}
  	public Integer getCountUser() {
  		return countUser;
  	}
  	public void setCountUser(Integer countUser) {
  		this.countUser = countUser;
  	}
  	public Integer getCountDevice() {
  		return countDevice;
  	}
  	public void setCountDevice(Integer countDevice) {
  		this.countDevice = countDevice;
  	}

	public String getUserAvatarUrl() {
		return userAvatarUrl;
	}

	public void setUserAvatarUrl(String userAvatarUrl) {
		this.userAvatarUrl = userAvatarUrl;
	}
	
}