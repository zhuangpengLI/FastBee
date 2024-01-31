package com.ruoyi.iot.mobile.model;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 设备定时任务
 * 
 * @author kerwincui
 */
@ApiModel("更新定时状态")
public class UpdateDeviceJobStatusReqDto
{

    /** 任务ID */
    @ApiModelProperty("任务序号")
    @NotNull
    private Long jobId;

    /** 任务状态（0正常 1暂停） */
    @ApiModelProperty("任务状态 0=正常,1=暂停")
    @NotNull
    private String status;

    public Long getJobId()
    {
        return jobId;
    }

    public void setJobId(Long jobId)
    {
        this.jobId = jobId;
    }


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


}
