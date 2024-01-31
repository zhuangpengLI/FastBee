package com.ruoyi.iot.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.iot.mapper.ApkVersionMapper;
import com.ruoyi.iot.domain.ApkVersion;
import com.ruoyi.iot.service.IApkVersionService;

/**
 * 升级管理Service业务层处理
 * 
 * @author renjiayue
 * @date 2022-09-09
 */
@Service
public class ApkVersionServiceImpl implements IApkVersionService 
{
    @Autowired
    private ApkVersionMapper apkVersionMapper;

    /**
     * 查询升级管理
     * 
     * @param apkId 升级管理主键
     * @return 升级管理
     */
    @Override
    public ApkVersion selectApkVersionByApkId(Long apkId)
    {
        return apkVersionMapper.selectApkVersionByApkId(apkId);
    }

    /**
     * 查询升级管理列表
     * 
     * @param apkVersion 升级管理
     * @return 升级管理
     */
    @Override
    public List<ApkVersion> selectApkVersionList(ApkVersion apkVersion)
    {
        return apkVersionMapper.selectApkVersionList(apkVersion);
    }

    /**
     * 新增升级管理
     * 
     * @param apkVersion 升级管理
     * @return 结果
     */
    @Override
    public int insertApkVersion(ApkVersion apkVersion)
    {
        apkVersion.setCreateTime(DateUtils.getNowDate());
        return apkVersionMapper.insertApkVersion(apkVersion);
    }

    /**
     * 修改升级管理
     * 
     * @param apkVersion 升级管理
     * @return 结果
     */
    @Override
    public int updateApkVersion(ApkVersion apkVersion)
    {
        apkVersion.setUpdateTime(DateUtils.getNowDate());
        return apkVersionMapper.updateApkVersion(apkVersion);
    }

    /**
     * 批量删除升级管理
     * 
     * @param apkIds 需要删除的升级管理主键
     * @return 结果
     */
    @Override
    public int deleteApkVersionByApkIds(Long[] apkIds)
    {
        return apkVersionMapper.deleteApkVersionByApkIds(apkIds);
    }

    /**
     * 删除升级管理信息
     * 
     * @param apkId 升级管理主键
     * @return 结果
     */
    @Override
    public int deleteApkVersionByApkId(Long apkId)
    {
        return apkVersionMapper.deleteApkVersionByApkId(apkId);
    }
}
