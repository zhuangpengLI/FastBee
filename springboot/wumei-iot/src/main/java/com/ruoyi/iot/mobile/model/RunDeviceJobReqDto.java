package com.ruoyi.iot.mobile.model;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("执行定时任务")
public class RunDeviceJobReqDto
{

    /** 任务ID */
    @ApiModelProperty("任务序号")
    @NotNull
    private Long jobId;

    /** 任务组名 */
    @ApiModelProperty("任务组名")
    @NotNull
    private String jobGroup;

	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}
    
    
}
