package com.ruoyi.iot.model;

import com.ruoyi.iot.domain.Scene;

/**
 * 场景联动对象 iot_scene
 * 
 * @author kerwincui
 * @date 2022-09-21
 */
public class AdminSceneRespDto extends Scene
{
    private static final long serialVersionUID = 1L;

    /** 场景设备数量 */
    private Integer sceneDeviceCount;
    
    /**
     * 场景所属家庭名称
     */
    private String familyName;

	public Integer getSceneDeviceCount() {
		return sceneDeviceCount;
	}

	public void setSceneDeviceCount(Integer sceneDeviceCount) {
		this.sceneDeviceCount = sceneDeviceCount;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

   
}
