package com.ruoyi.iot.mobile.model;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.quartz.util.CronUtils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 设备定时任务
 * 
 * @author kerwincui
 */
@ApiModel("添加定时任务")
public class AddDeviceJobReqDto
{
    private static final long serialVersionUID = 1L;

    /** 任务名称 */
    @ApiModelProperty("任务名称")
    @NotNull
    private String jobName;

    /** 设备id */
    @ApiModelProperty("设备id")
    private Long deviceId;

    /** 执行动作 */
    @ApiModelProperty("执行动作")
    private String actions;

    /** cron执行表达式 */
    @ApiModelProperty("执行表达式")
    @NotNull
    private String cronExpression;

    /** 定时类型（1=设备定时，2=设备告警，3=场景联动） */
    @ApiModelProperty("定时类型 1==设备定时，2=设备告警，3=场景联动")
    @NotNull
    private Integer jobType;
    
    /**
     * 1 纯cron表达式 2法定节假日 3法定工作日
     */
    @ApiModelProperty("定时时间类型 1 纯cron表达式 2法定节假日 3法定工作日")
    @NotNull
    private Integer jobTimeType;

    /** 场景联动ID */
    @ApiModelProperty("场景联动ID")
    private Long sceneId;

    /** 告警ID */
    @ApiModelProperty("告警ID")
    private Long alertId;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getJobType() {
        return jobType;
    }

    public void setJobType(Integer jobType) {
        this.jobType = jobType;
    }

    public Long getSceneId() {
        return sceneId;
    }

    public void setSceneId(Long sceneId) {
        this.sceneId = sceneId;
    }

    public Long getAlertId() {
        return alertId;
    }

    public void setAlertId(Long alertId) {
        this.alertId = alertId;
    }

    public String getJobName()
    {
        return jobName;
    }

    public void setJobName(String jobName)
    {
        this.jobName = jobName;
    }


    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

    @NotBlank(message = "Cron执行表达式不能为空")
    @Size(min = 0, max = 255, message = "Cron执行表达式不能超过255个字符")
    public String getCronExpression()
    {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression)
    {
        this.cronExpression = cronExpression;
    }
    
    public Integer getJobTimeType() {
		return jobTimeType;
	}

	public void setJobTimeType(Integer jobTimeType) {
		this.jobTimeType = jobTimeType;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getNextValidTime()
    {
        if (StringUtils.isNotEmpty(cronExpression))
        {
            return CronUtils.getNextExecution(cronExpression);
        }
        return null;
    }
}
