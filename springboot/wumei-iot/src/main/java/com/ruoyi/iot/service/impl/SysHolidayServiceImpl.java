package com.ruoyi.iot.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.iot.mapper.SysHolidayMapper;
import com.ruoyi.iot.domain.SysHoliday;
import com.ruoyi.iot.service.ISysHolidayService;

/**
 * 系统节假日Service业务层处理
 * 
 * @author renjiayue
 * @date 2022-11-14
 */
@Service
public class SysHolidayServiceImpl implements ISysHolidayService 
{
    @Autowired
    private SysHolidayMapper sysHolidayMapper;

    /**
     * 查询系统节假日
     * 
     * @param id 系统节假日主键
     * @return 系统节假日
     */
    @Override
    public SysHoliday selectSysHolidayById(Long id)
    {
        return sysHolidayMapper.selectSysHolidayById(id);
    }

    /**
     * 查询系统节假日列表
     * 
     * @param sysHoliday 系统节假日
     * @return 系统节假日
     */
    @Override
    public List<SysHoliday> selectSysHolidayList(SysHoliday sysHoliday)
    {
        return sysHolidayMapper.selectSysHolidayList(sysHoliday);
    }

    /**
     * 新增系统节假日
     * 
     * @param sysHoliday 系统节假日
     * @return 结果
     */
    @Override
    public int insertSysHoliday(SysHoliday sysHoliday)
    {
        sysHoliday.setCreateTime(DateUtils.getNowDate());
        return sysHolidayMapper.insertSysHoliday(sysHoliday);
    }

    /**
     * 修改系统节假日
     * 
     * @param sysHoliday 系统节假日
     * @return 结果
     */
    @Override
    public int updateSysHoliday(SysHoliday sysHoliday)
    {
        sysHoliday.setUpdateTime(DateUtils.getNowDate());
        return sysHolidayMapper.updateSysHoliday(sysHoliday);
    }

    /**
     * 批量删除系统节假日
     * 
     * @param ids 需要删除的系统节假日主键
     * @return 结果
     */
    @Override
    public int deleteSysHolidayByIds(Long[] ids)
    {
        return sysHolidayMapper.deleteSysHolidayByIds(ids);
    }

    /**
     * 删除系统节假日信息
     * 
     * @param id 系统节假日主键
     * @return 结果
     */
    @Override
    public int deleteSysHolidayById(Long id)
    {
        return sysHolidayMapper.deleteSysHolidayById(id);
    }
}
