package com.ruoyi.iot.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 升级管理对象 iot_apk_version
 * 
 * @author renjiayue
 * @date 2022-09-09
 */
public class ApkVersion extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long apkId;

    /** apk类型1客户端 */
    @Excel(name = "apk类型1客户端")
    private String apkType;

    /** app平台 1 IOS  2 Android */
    @Excel(name = "app平台 1 IOS  2 Android")
    private String platform;

    /** apk下载链接 */
    @Excel(name = "apk下载链接")
    private String apkUrl;

    /** 版本号 */
    @Excel(name = "版本号")
    private Long versionCode;

    /** 版本名称 */
    @Excel(name = "版本名称")
    private String versionName;

    /** 更新内容 */
    @Excel(name = "更新内容")
    private String updateContent;

    /** 是否强制更新 */
    @Excel(name = "是否强制更新")
    private String isForce;

    public void setApkId(Long apkId) 
    {
        this.apkId = apkId;
    }

    public Long getApkId() 
    {
        return apkId;
    }
    public void setApkType(String apkType) 
    {
        this.apkType = apkType;
    }

    public String getApkType() 
    {
        return apkType;
    }
    public void setPlatform(String platform) 
    {
        this.platform = platform;
    }

    public String getPlatform() 
    {
        return platform;
    }
    public void setApkUrl(String apkUrl) 
    {
        this.apkUrl = apkUrl;
    }

    public String getApkUrl() 
    {
        return apkUrl;
    }
    public void setVersionCode(Long versionCode) 
    {
        this.versionCode = versionCode;
    }

    public Long getVersionCode() 
    {
        return versionCode;
    }
    public void setVersionName(String versionName) 
    {
        this.versionName = versionName;
    }

    public String getVersionName() 
    {
        return versionName;
    }
    public void setUpdateContent(String updateContent) 
    {
        this.updateContent = updateContent;
    }

    public String getUpdateContent() 
    {
        return updateContent;
    }
    public void setIsForce(String isForce) 
    {
        this.isForce = isForce;
    }

    public String getIsForce() 
    {
        return isForce;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("apkId", getApkId())
            .append("apkType", getApkType())
            .append("platform", getPlatform())
            .append("apkUrl", getApkUrl())
            .append("versionCode", getVersionCode())
            .append("versionName", getVersionName())
            .append("updateContent", getUpdateContent())
            .append("isForce", getIsForce())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .toString();
    }
}
