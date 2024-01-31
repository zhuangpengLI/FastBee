package com.ruoyi.iot.service;

import java.util.List;
import com.ruoyi.iot.domain.DboxSwitch;

/**
 * 配电箱空开素材Service接口
 * 
 * @author renjiayue
 * @date 2022-10-08
 */
public interface IDboxSwitchService 
{
    /**
     * 查询配电箱空开素材
     * 
     * @param id 配电箱空开素材主键
     * @return 配电箱空开素材
     */
    public DboxSwitch selectDboxSwitchById(Long id);

    /**
     * 查询配电箱空开素材列表
     * 
     * @param dboxSwitch 配电箱空开素材
     * @return 配电箱空开素材集合
     */
    public List<DboxSwitch> selectDboxSwitchList(DboxSwitch dboxSwitch);

    /**
     * 新增配电箱空开素材
     * 
     * @param dboxSwitch 配电箱空开素材
     * @return 结果
     */
    public int insertDboxSwitch(DboxSwitch dboxSwitch);

    /**
     * 修改配电箱空开素材
     * 
     * @param dboxSwitch 配电箱空开素材
     * @return 结果
     */
    public int updateDboxSwitch(DboxSwitch dboxSwitch);

    /**
     * 批量删除配电箱空开素材
     * 
     * @param ids 需要删除的配电箱空开素材主键集合
     * @return 结果
     */
    public int deleteDboxSwitchByIds(Long[] ids);

    /**
     * 删除配电箱空开素材信息
     * 
     * @param id 配电箱空开素材主键
     * @return 结果
     */
    public int deleteDboxSwitchById(Long id);
}
