package com.ruoyi.system.otherService;

import java.util.List;

import com.ruoyi.system.otherDomain.BaseConfig;

/**
 * 系统参数Service接口
 * 
 * @author renjiayue
 * @date 2022-09-09
 */
public interface IBaseConfigService 
{
	/**
     * 通过缓存获取智能家居基础配置
     * @return
     */
    public BaseConfig selectOneBaseConfigByCache();
	 /**
     * 不通过id查询,只存在一条数据
     * @return
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
     * 批量删除系统参数
     * 
     * @param ids 需要删除的系统参数主键集合
     * @return 结果
     */
    public int deleteBaseConfigByIds(Long[] ids);

    /**
     * 删除系统参数信息
     * 
     * @param id 系统参数主键
     * @return 结果
     */
    public int deleteBaseConfigById(Long id);
}
