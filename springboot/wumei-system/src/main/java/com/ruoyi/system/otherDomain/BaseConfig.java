package com.ruoyi.system.otherDomain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 系统参数对象 iot_base_config
 * 
 * @author renjiayue
 * @date 2022-09-09
 */
public class BaseConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 单账号最多配置家庭数 */
    @Excel(name = "单账号最多配置家庭数")
    private Long userCreateRoomMax;

    /** 用户设备同时登录最大数 */
    @Excel(name = "用户设备同时登录最大数")
    private Long userLoginDeviceMax;

    /** 客服手机号 */
    @Excel(name = "客服手机号")
    private String customerServicePhone;

    /** 用户接受短信最大数量 */
    @Excel(name = "用户接受短信最大数量")
    private Long userReceiveSmsMax;

    /** 用户设置场景最大数 */
    @Excel(name = "用户设置场景最大数")
    private Long userSceneMax;
    
    /** 家庭成员最大数量 */
    @Excel(name = "家庭成员最大数量")
    private Long familUserMax;

    public Long getFamilUserMax() {
		return familUserMax;
	}

	public void setFamilUserMax(Long familUserMax) {
		this.familUserMax = familUserMax;
	}

	public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setUserCreateRoomMax(Long userCreateRoomMax) 
    {
        this.userCreateRoomMax = userCreateRoomMax;
    }

    public Long getUserCreateRoomMax() 
    {
        return userCreateRoomMax;
    }
    public void setUserLoginDeviceMax(Long userLoginDeviceMax) 
    {
        this.userLoginDeviceMax = userLoginDeviceMax;
    }

    public Long getUserLoginDeviceMax() 
    {
        return userLoginDeviceMax;
    }
    public void setCustomerServicePhone(String customerServicePhone) 
    {
        this.customerServicePhone = customerServicePhone;
    }

    public String getCustomerServicePhone() 
    {
        return customerServicePhone;
    }
    public void setUserReceiveSmsMax(Long userReceiveSmsMax) 
    {
        this.userReceiveSmsMax = userReceiveSmsMax;
    }

    public Long getUserReceiveSmsMax() 
    {
        return userReceiveSmsMax;
    }
    public void setUserSceneMax(Long userSceneMax) 
    {
        this.userSceneMax = userSceneMax;
    }

    public Long getUserSceneMax() 
    {
        return userSceneMax;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userCreateRoomMax", getUserCreateRoomMax())
            .append("userLoginDeviceMax", getUserLoginDeviceMax())
            .append("customerServicePhone", getCustomerServicePhone())
            .append("userReceiveSmsMax", getUserReceiveSmsMax())
            .append("userSceneMax", getUserSceneMax())
            .append("familUserMax", getFamilUserMax())
            .toString();
    }
}
