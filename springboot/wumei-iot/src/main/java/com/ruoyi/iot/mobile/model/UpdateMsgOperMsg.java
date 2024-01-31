package com.ruoyi.iot.mobile.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 需操作消息列表对象 msg_oper_msg
 * 
 * @author renjiayue
 * @date 2022-09-18
 */
@ApiModel
public class UpdateMsgOperMsg implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** id */
    @ApiModelProperty("可操作消息id")
    @NotNull
    private Long id;

    /** 00 未处理 01已同意 02已拒绝 98已失效(别的请求已同意) 99已过期 */
    @ApiModelProperty("01 同意 02拒绝")
    @NotNull
    private String status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


}
