package com.ruoyi.iot.mobile.model;

import java.io.Serializable;

/**
 * 分享统计列表
 * 
 * @author renjiayue
 * @date 2022-09-18
 */
public class FamilyShareStatRespDto implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 家庭id */
    private Long familyId;

    /** 家庭名称 */
    private String familyName;
    
    /**
     * 家庭头像
     */
    private String familyAvatarUrl;

    /** 设备id */
    private Long deviceId;

    /** 设备名称 */
    private String deviceName;
    
    /** 设备图片地址 */
    private String deviceImgUrl;
    
  //设备型号  例如
    //1001 1002单键开关  			1003双键开关 		1004三键开关 		1005三键+温湿度 
    //1011 1012单键语音开关 		1013双键语音开关	1014三键语音开关	1015三键+温湿度语音开关
    //2001 5孔10A			 		2002 3孔16A
    //3001 单轨窗帘 				3002 双轨窗帘
    //3011 单轨语音窗帘 				3012 双轨语音窗帘
    //4001 烟感报警器 				4002燃气报警器
    private String deviceModelType;
    /**
     * 分享成员数
     */
    private Integer shareUserCount;


    public void setFamilyId(Long familyId) 
    {
        this.familyId = familyId;
    }

    public Long getFamilyId() 
    {
        return familyId;
    }
    public void setFamilyName(String familyName) 
    {
        this.familyName = familyName;
    }

    public String getFamilyName() 
    {
        return familyName;
    }
    public void setDeviceId(Long deviceId) 
    {
        this.deviceId = deviceId;
    }

    public Long getDeviceId() 
    {
        return deviceId;
    }
    public void setDeviceName(String deviceName) 
    {
        this.deviceName = deviceName;
    }

    public String getDeviceName() 
    {
        return deviceName;
    }

	public Integer getShareUserCount() {
		return shareUserCount;
	}

	public void setShareUserCount(Integer shareUserCount) {
		this.shareUserCount = shareUserCount;
	}

	public String getFamilyAvatarUrl() {
		return familyAvatarUrl;
	}

	public void setFamilyAvatarUrl(String familyAvatarUrl) {
		this.familyAvatarUrl = familyAvatarUrl;
	}

	public String getDeviceImgUrl() {
		return deviceImgUrl;
	}

	public void setDeviceImgUrl(String deviceImgUrl) {
		this.deviceImgUrl = deviceImgUrl;
	}

	public String getDeviceModelType() {
		return deviceModelType;
	}

	public void setDeviceModelType(String deviceModelType) {
		this.deviceModelType = deviceModelType;
	}

}
