package com.ruoyi.iot.mapper;

import java.util.List;
import com.ruoyi.iot.domain.ApkVersion;

/**
 * 升级管理Mapper接口
 * 
 * @author renjiayue
 * @date 2022-09-09
 */
public interface ApkVersionMapper 
{
    /**
     * 查询升级管理
     * 
     * @param apkId 升级管理主键
     * @return 升级管理
     */
    public ApkVersion selectApkVersionByApkId(Long apkId);

    /**
     * 查询升级管理列表
     * 
     * @param apkVersion 升级管理
     * @return 升级管理集合
     */
    public List<ApkVersion> selectApkVersionList(ApkVersion apkVersion);

    /**
     * 新增升级管理
     * 
     * @param apkVersion 升级管理
     * @return 结果
     */
    public int insertApkVersion(ApkVersion apkVersion);

    /**
     * 修改升级管理
     * 
     * @param apkVersion 升级管理
     * @return 结果
     */
    public int updateApkVersion(ApkVersion apkVersion);

    /**
     * 删除升级管理
     * 
     * @param apkId 升级管理主键
     * @return 结果
     */
    public int deleteApkVersionByApkId(Long apkId);

    /**
     * 批量删除升级管理
     * 
     * @param apkIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteApkVersionByApkIds(Long[] apkIds);
}
