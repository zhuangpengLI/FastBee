package com.ruoyi.iot.domain;

import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 场景联动对象 iot_scene
 * 
 * @author kerwincui
 * @date 2022-09-21
 */
public class Scene extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 场景ID */
    private Long sceneId;

    /** 场景名称 */
    @Excel(name = "场景名称")
    private String sceneName;

    /** 场景图片 */
    @Excel(name = "场景图片")
    private String scenceImgUrl;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 家庭id */
    @Excel(name = "家庭id")
    private Long familyId;

    /** 用户名称 */
    @Excel(name = "用户名称")
    private String userName;

    /** 触发器 */
    @Excel(name = "触发器")
    private String triggers;

    /** 执行动作 */
    @Excel(name = "执行动作")
    private String actions;

    /** 触发类型 1定时触发 2触发器触发 3混合触发 */
    @Excel(name = "触发类型 1定时触发 2触发器触发 3混合触发")
    private Long triggerType;

    /** 是否启用 0否 1是 */
    @Excel(name = "是否启用 0否 1是")
    private Integer enabled;
    
    /** 是否启用 0否 1是 */
    @Excel(name = "是否启用定时场景 0否 1是")
    private Integer jobEnabled;
    /** 是否系统内置场景 0否1是 */
    private Integer isSys;
    /** 内置场景id */
    private Long sysSceneId;

    /** 场景设备信息 */
    private List<SceneDevice> sceneDeviceList;

    public void setSceneId(Long sceneId) 
    {
        this.sceneId = sceneId;
    }

    public Long getSceneId() 
    {
        return sceneId;
    }
    public void setSceneName(String sceneName) 
    {
        this.sceneName = sceneName;
    }

    public String getSceneName() 
    {
        return sceneName;
    }
    public void setScenceImgUrl(String scenceImgUrl) 
    {
        this.scenceImgUrl = scenceImgUrl;
    }

    public String getScenceImgUrl() 
    {
        return scenceImgUrl;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setFamilyId(Long familyId) 
    {
        this.familyId = familyId;
    }

    public Long getFamilyId() 
    {
        return familyId;
    }
    public void setUserName(String userName) 
    {
        this.userName = userName;
    }

    public String getUserName() 
    {
        return userName;
    }
    public void setTriggers(String triggers) 
    {
        this.triggers = triggers;
    }

    public String getTriggers() 
    {
        return triggers;
    }
    public void setActions(String actions) 
    {
        this.actions = actions;
    }

    public String getActions() 
    {
        return actions;
    }
    public void setTriggerType(Long triggerType) 
    {
        this.triggerType = triggerType;
    }

    public Long getTriggerType() 
    {
        return triggerType;
    }
    public void setEnabled(Integer enabled) 
    {
        this.enabled = enabled;
    }

    public Integer getEnabled() 
    {
        return enabled;
    }

    public List<SceneDevice> getSceneDeviceList()
    {
        return sceneDeviceList;
    }

    public void setSceneDeviceList(List<SceneDevice> sceneDeviceList)
    {
        this.sceneDeviceList = sceneDeviceList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("sceneId", getSceneId())
            .append("sceneName", getSceneName())
            .append("scenceImgUrl", getScenceImgUrl())
            .append("userId", getUserId())
            .append("familyId", getFamilyId())
            .append("userName", getUserName())
            .append("triggers", getTriggers())
            .append("actions", getActions())
            .append("triggerType", getTriggerType())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("enabled", getEnabled())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("sceneDeviceList", getSceneDeviceList())
            .toString();
    }

	public Integer getJobEnabled() {
		return jobEnabled;
	}

	public void setJobEnabled(Integer jobEnabled) {
		this.jobEnabled = jobEnabled;
	}

	public Integer getIsSys() {
		return isSys;
	}

	public void setIsSys(Integer isSys) {
		this.isSys = isSys;
	}

	public Long getSysSceneId() {
		return sysSceneId;
	}

	public void setSysSceneId(Long sysSceneId) {
		this.sysSceneId = sysSceneId;
	}
	
}
