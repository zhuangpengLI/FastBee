package com.ruoyi.iot.mapper;

import java.util.List;
import com.ruoyi.iot.domain.SysHoliday;

/**
 * 系统节假日Mapper接口
 * 
 * @author renjiayue
 * @date 2022-11-14
 */
public interface SysHolidayMapper 
{
    /**
     * 查询系统节假日
     * 
     * @param id 系统节假日主键
     * @return 系统节假日
     */
    public SysHoliday selectSysHolidayById(Long id);

    /**
     * 查询系统节假日列表
     * 
     * @param sysHoliday 系统节假日
     * @return 系统节假日集合
     */
    public List<SysHoliday> selectSysHolidayList(SysHoliday sysHoliday);

    /**
     * 新增系统节假日
     * 
     * @param sysHoliday 系统节假日
     * @return 结果
     */
    public int insertSysHoliday(SysHoliday sysHoliday);

    /**
     * 修改系统节假日
     * 
     * @param sysHoliday 系统节假日
     * @return 结果
     */
    public int updateSysHoliday(SysHoliday sysHoliday);

    /**
     * 删除系统节假日
     * 
     * @param id 系统节假日主键
     * @return 结果
     */
    public int deleteSysHolidayById(Long id);

    /**
     * 批量删除系统节假日
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysHolidayByIds(Long[] ids);
}
