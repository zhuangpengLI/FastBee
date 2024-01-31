package com.ruoyi.iot.mobile.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 房间对象 iot_room
 * 
 * @author renjiayue
 * @date 2022-09-07
 */
@ApiModel
public class AddOrUpdateOrDelRoomReq implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 房间ID */
    @ApiModelProperty("id")
    private Long roomId;

    /** 家庭ID */
    @ApiModelProperty("家庭ID")
    @NotNull(message="家庭信息不能为空")
    private Long familyId;

    /** 房间名称 */
    @ApiModelProperty("房间名称")
    @NotNull(message="房间名称不能为空")
    private String name;

    /** 排序 */
    @ApiModelProperty("排序")
    private Long roomOrder;

    public void setRoomId(Long roomId) 
    {
        this.roomId = roomId;
    }

    public Long getRoomId() 
    {
        return roomId;
    }
    public void setFamilyId(Long familyId) 
    {
        this.familyId = familyId;
    }

    public Long getFamilyId() 
    {
        return familyId;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setRoomOrder(Long roomOrder) 
    {
        this.roomOrder = roomOrder;
    }

    public Long getRoomOrder() 
    {
        return roomOrder;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("roomId", getRoomId())
            .append("familyId", getFamilyId())
            .append("name", getName())
            .append("roomOrder", getRoomOrder())
            .toString();
    }
}
