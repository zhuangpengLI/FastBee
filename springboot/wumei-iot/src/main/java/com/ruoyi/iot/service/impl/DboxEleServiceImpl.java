package com.ruoyi.iot.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.iot.mapper.DboxEleMapper;
import com.ruoyi.iot.domain.DboxEle;
import com.ruoyi.iot.service.IDboxEleService;

/**
 * 网关电量统计Service业务层处理
 * 
 * @author renjiayue
 * @date 2023-08-02
 */
@Service
public class DboxEleServiceImpl implements IDboxEleService 
{
    @Autowired
    private DboxEleMapper dboxEleMapper;

    /**
     * 查询网关电量统计
     * 
     * @param id 网关电量统计主键
     * @return 网关电量统计
     */
    @Override
    public DboxEle selectDboxEleById(Long id)
    {
        return dboxEleMapper.selectDboxEleById(id);
    }

    /**
     * 查询网关电量统计列表
     * 
     * @param dboxEle 网关电量统计
     * @return 网关电量统计
     */
    @Override
    public List<DboxEle> selectDboxEleList(DboxEle dboxEle)
    {
        return dboxEleMapper.selectDboxEleList(dboxEle);
    }

    /**
     * 新增网关电量统计
     * 
     * @param dboxEle 网关电量统计
     * @return 结果
     */
    @Override
    public int insertDboxEle(DboxEle dboxEle)
    {
        return dboxEleMapper.insertDboxEle(dboxEle);
    }

    /**
     * 修改网关电量统计
     * 
     * @param dboxEle 网关电量统计
     * @return 结果
     */
    @Override
    public int updateDboxEle(DboxEle dboxEle)
    {
        return dboxEleMapper.updateDboxEle(dboxEle);
    }

    /**
     * 批量删除网关电量统计
     * 
     * @param ids 需要删除的网关电量统计主键
     * @return 结果
     */
    @Override
    public int deleteDboxEleByIds(Long[] ids)
    {
        return dboxEleMapper.deleteDboxEleByIds(ids);
    }

    /**
     * 删除网关电量统计信息
     * 
     * @param id 网关电量统计主键
     * @return 结果
     */
    @Override
    public int deleteDboxEleById(Long id)
    {
        return dboxEleMapper.deleteDboxEleById(id);
    }
}
