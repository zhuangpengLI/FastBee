package com.ruoyi.iot.mapper;

import java.util.List;
import com.ruoyi.iot.domain.Dbox;
import com.ruoyi.iot.domain.DboxSwitch;

/**
 * 配电箱配置Mapper接口
 * 
 * @author renjiayue
 * @date 2022-10-08
 */
public interface DboxMapper 
{
    /**
     * 查询配电箱配置
     * 
     * @param id 配电箱配置主键
     * @return 配电箱配置
     */
    public Dbox selectDboxById(Long id);

    /**
     * 查询配电箱配置列表
     * 
     * @param dbox 配电箱配置
     * @return 配电箱配置集合
     */
    public List<Dbox> selectDboxList(Dbox dbox);

    /**
     * 新增配电箱配置
     * 
     * @param dbox 配电箱配置
     * @return 结果
     */
    public int insertDbox(Dbox dbox);

    /**
     * 修改配电箱配置
     * 
     * @param dbox 配电箱配置
     * @return 结果
     */
    public int updateDbox(Dbox dbox);

    /**
     * 删除配电箱配置
     * 
     * @param id 配电箱配置主键
     * @return 结果
     */
    public int deleteDboxById(Long id);

    /**
     * 批量删除配电箱配置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDboxByIds(Long[] ids);

    /**
     * 批量删除配电箱空开素材
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDboxSwitchByDboxIds(Long[] ids);
    
    /**
     * 批量新增配电箱空开素材
     * 
     * @param dboxSwitchList 配电箱空开素材列表
     * @return 结果
     */
    public int batchDboxSwitch(List<DboxSwitch> dboxSwitchList);
    

    /**
     * 通过配电箱配置主键删除配电箱空开素材信息
     * 
     * @param id 配电箱配置ID
     * @return 结果
     */
    public int deleteDboxSwitchByDboxId(Long id);
}
