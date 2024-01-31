package com.ruoyi.iot.mapper;

import java.util.List;

import com.ruoyi.system.otherDomain.BaseConfig;

/**
 * 系统参数Mapper接口
 * 
 * @author renjiayue
 * @date 2022-09-09
 */
public interface BaseConfigMapper 
{
	/**
	 * 查询系统参数
	 * 
	 * @return 系统参数
	 */
	public BaseConfig selectOneBaseConfig();
    /**
     * 查询系统参数
     * 
     * @param id 系统参数主键
     * @return 系统参数
     */
    public BaseConfig selectBaseConfigById(Long id);

    /**
     * 查询系统参数列表
     * 
     * @param baseConfig 系统参数
     * @return 系统参数集合
     */
    public List<BaseConfig> selectBaseConfigList(BaseConfig baseConfig);

    /**
     * 新增系统参数
     * 
     * @param baseConfig 系统参数
     * @return 结果
     */
    public int insertBaseConfig(BaseConfig baseConfig);

    /**
     * 修改系统参数
     * 
     * @param baseConfig 系统参数
     * @return 结果
     */
    public int updateBaseConfig(BaseConfig baseConfig);

    /**
     * 删除系统参数
     * 
     * @param id 系统参数主键
     * @return 结果
     */
    public int deleteBaseConfigById(Long id);

    /**
     * 批量删除系统参数
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBaseConfigByIds(Long[] ids);
}
