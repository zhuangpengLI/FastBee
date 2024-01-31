package com.ruoyi.iot.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 房间对象 iot_room
 * 
 * @author renjiayue
 * @date 2022-09-07
 */
public class Room extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 房间ID */
    private Long roomId;

    /** 家庭ID */
    @Excel(name = "家庭ID")
    private Long familyId;

    /** 房间名称 */
    @Excel(name = "房间名称")
    private String name;

    /** 排序 */
    @Excel(name = "排序")
    private Long roomOrder;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

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
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("roomId", getRoomId())
            .append("familyId", getFamilyId())
            .append("name", getName())
            .append("roomOrder", getRoomOrder())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
