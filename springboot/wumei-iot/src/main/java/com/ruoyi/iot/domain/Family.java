package com.ruoyi.iot.domain;

import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 家庭管理对象 iot_family
 * 
 * @author renjiayue
 * @date 2022-09-07
 */
public class Family extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long familyId;

    /** 家庭名称 */
    @Excel(name = "家庭名称")
    private String name;

    /** 头像url */
    @Excel(name = "头像url")
    private String avatarUrl;

    /** 位置 */
    @Excel(name = "位置")
    private String position;

    /** 扫描网关加入家庭权限是否开启 0否1是 */
    private Integer isEnableJoinAuth;

    /** 是否绑定网关 0否1是 */
    @Excel(name = "是否绑定网关 0否1是")
    private Integer isBind;

    /** 网关sn码 */
    private String gatewaySn;

    /** 所属用户id */
    @Excel(name = "所属用户id")
    private Long belongUserId;

    /** 创建用户id */
    private Long createUserId;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 房间信息 */
    private List<Room> roomList;
    
    /** 家庭用户关联信息 */
    private List<FamilyUserRela> familyUserRelaList;

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
    public void setAvatarUrl(String avatarUrl) 
    {
        this.avatarUrl = avatarUrl;
    }

    public String getAvatarUrl() 
    {
        return avatarUrl;
    }
    public void setPosition(String position) 
    {
        this.position = position;
    }

    public String getPosition() 
    {
        return position;
    }
    public void setIsEnableJoinAuth(Integer isEnableJoinAuth) 
    {
        this.isEnableJoinAuth = isEnableJoinAuth;
    }

    public Integer getIsEnableJoinAuth() 
    {
        return isEnableJoinAuth;
    }
    public void setIsBind(Integer isBind) 
    {
        this.isBind = isBind;
    }

    public Integer getIsBind() 
    {
        return isBind;
    }
    public void setGatewaySn(String gatewaySn) 
    {
        this.gatewaySn = gatewaySn;
    }

    public String getGatewaySn() 
    {
        return gatewaySn;
    }
    public void setBelongUserId(Long belongUserId) 
    {
        this.belongUserId = belongUserId;
    }

    public Long getBelongUserId() 
    {
        return belongUserId;
    }
    public void setCreateUserId(Long createUserId) 
    {
        this.createUserId = createUserId;
    }

    public Long getCreateUserId() 
    {
        return createUserId;
    }
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    public List<Room> getRoomList()
    {
        return roomList;
    }

    public void setRoomList(List<Room> roomList)
    {
        this.roomList = roomList;
    }
    
    public List<FamilyUserRela> getFamilyUserRelaList()
    {
        return familyUserRelaList;
    }

    public void setFamilyUserRelaList(List<FamilyUserRela> familyUserRelaList)
    {
        this.familyUserRelaList = familyUserRelaList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("familyId", getFamilyId())
            .append("name", getName())
            .append("avatarUrl", getAvatarUrl())
            .append("position", getPosition())
            .append("isEnableJoinAuth", getIsEnableJoinAuth())
            .append("isBind", getIsBind())
            .append("gatewaySn", getGatewaySn())
            .append("belongUserId", getBelongUserId())
            .append("createUserId", getCreateUserId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("delFlag", getDelFlag())
            .append("roomList", getRoomList())
            .append("familyUserRelaList", getFamilyUserRelaList())
            .toString();
    }
}
