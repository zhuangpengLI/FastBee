package com.ruoyi.iot.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 场景执行记录对象 iot_scene_record
 * 
 * @author renjiayue
 * @date 2022-10-09
 */
public class SceneRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 场景id */
    @Excel(name = "场景id")
    private Long sceneId;

    /** 执行方式 1手动执行 2定时执行 */
    @Excel(name = "执行方式 1手动执行 2定时执行")
    private Integer runType;

    /** 任务id */
    @Excel(name = "任务id")
    private Long jobId;

    /** 任务名称 */
    @Excel(name = "任务名称")
    private String jobName;

    /** 执行人id */
    @Excel(name = "执行人id")
    private Long userId;

    /** 执行设备数 */
    @Excel(name = "执行设备数")
    private Integer deviceCount;

    /** 执行成功数 */
    @Excel(name = "执行成功数")
    private Integer sucDeviceCount;
    
    //-----  查询结果字段 
    
    /** 用户昵称 */
    @Excel(name = "用户名称")
    private String nickName;
    
    /** 手机号码 */
    @Excel(name = "手机号码")
    private String phonenumber;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setSceneId(Long sceneId) 
    {
        this.sceneId = sceneId;
    }

    public Long getSceneId() 
    {
        return sceneId;
    }
    public void setRunType(Integer runType) 
    {
        this.runType = runType;
    }

    public Integer getRunType() 
    {
        return runType;
    }
    public void setJobId(Long jobId) 
    {
        this.jobId = jobId;
    }

    public Long getJobId() 
    {
        return jobId;
    }
    public void setJobName(String jobName) 
    {
        this.jobName = jobName;
    }

    public String getJobName() 
    {
        return jobName;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setDeviceCount(Integer deviceCount) 
    {
        this.deviceCount = deviceCount;
    }

    public Integer getDeviceCount() 
    {
        return deviceCount;
    }
    public void setSucDeviceCount(Integer sucDeviceCount) 
    {
        this.sucDeviceCount = sucDeviceCount;
    }

    public Integer getSucDeviceCount() 
    {
        return sucDeviceCount;
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
            .append("sceneId", getSceneId())
            .append("runType", getRunType())
            .append("jobId", getJobId())
            .append("jobName", getJobName())
            .append("userId", getUserId())
            .append("deviceCount", getDeviceCount())
            .append("sucDeviceCount", getSucDeviceCount())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
