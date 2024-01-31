package com.ruoyi.iot.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.iot.mapper.DboxSwitchMapper;
import com.ruoyi.iot.domain.DboxSwitch;
import com.ruoyi.iot.service.IDboxSwitchService;

/**
 * 配电箱空开素材Service业务层处理
 * 
 * @author renjiayue
 * @date 2022-10-08
 */
@Service
public class DboxSwitchServiceImpl implements IDboxSwitchService 
{
    @Autowired
    private DboxSwitchMapper dboxSwitchMapper;

    /**
     * 查询配电箱空开素材
     * 
     * @param id 配电箱空开素材主键
     * @return 配电箱空开素材
     */
    @Override
    public DboxSwitch selectDboxSwitchById(Long id)
    {
        return dboxSwitchMapper.selectDboxSwitchById(id);
    }

    /**
     * 查询配电箱空开素材列表
     * 
     * @param dboxSwitch 配电箱空开素材
     * @return 配电箱空开素材
     */
    @Override
    public List<DboxSwitch> selectDboxSwitchList(DboxSwitch dboxSwitch)
    {
        return dboxSwitchMapper.selectDboxSwitchList(dboxSwitch);
    }

    /**
     * 新增配电箱空开素材
     * 
     * @param dboxSwitch 配电箱空开素材
     * @return 结果
     */
    @Override
    public int insertDboxSwitch(DboxSwitch dboxSwitch)
    {
        dboxSwitch.setCreateTime(DateUtils.getNowDate());
        return dboxSwitchMapper.insertDboxSwitch(dboxSwitch);
    }

    /**
     * 修改配电箱空开素材
     * 
     * @param dboxSwitch 配电箱空开素材
     * @return 结果
     */
    @Override
    public int updateDboxSwitch(DboxSwitch dboxSwitch)
    {
        dboxSwitch.setUpdateTime(DateUtils.getNowDate());
        return dboxSwitchMapper.updateDboxSwitch(dboxSwitch);
    }

    /**
     * 批量删除配电箱空开素材
     * 
     * @param ids 需要删除的配电箱空开素材主键
     * @return 结果
     */
    @Override
    public int deleteDboxSwitchByIds(Long[] ids)
    {
        return dboxSwitchMapper.deleteDboxSwitchByIds(ids);
    }

    /**
     * 删除配电箱空开素材信息
     * 
     * @param id 配电箱空开素材主键
     * @return 结果
     */
    @Override
    public int deleteDboxSwitchById(Long id)
    {
        return dboxSwitchMapper.deleteDboxSwitchById(id);
    }
}
