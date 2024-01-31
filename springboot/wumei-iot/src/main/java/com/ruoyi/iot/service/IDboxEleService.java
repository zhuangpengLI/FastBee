package com.ruoyi.iot.service;

import java.util.List;
import com.ruoyi.iot.domain.DboxEle;

/**
 * 网关电量统计Service接口
 * 
 * @author renjiayue
 * @date 2023-08-02
 */
public interface IDboxEleService 
{
    /**
     * 查询网关电量统计
     * 
     * @param id 网关电量统计主键
     * @return 网关电量统计
     */
    public DboxEle selectDboxEleById(Long id);

    /**
     * 查询网关电量统计列表
     * 
     * @param dboxEle 网关电量统计
     * @return 网关电量统计集合
     */
    public List<DboxEle> selectDboxEleList(DboxEle dboxEle);

    /**
     * 新增网关电量统计
     * 
     * @param dboxEle 网关电量统计
     * @return 结果
     */
    public int insertDboxEle(DboxEle dboxEle);

    /**
     * 修改网关电量统计
     * 
     * @param dboxEle 网关电量统计
     * @return 结果
     */
    public int updateDboxEle(DboxEle dboxEle);

    /**
     * 批量删除网关电量统计
     * 
     * @param ids 需要删除的网关电量统计主键集合
     * @return 结果
     */
    public int deleteDboxEleByIds(Long[] ids);

    /**
     * 删除网关电量统计信息
     * 
     * @param id 网关电量统计主键
     * @return 结果
     */
    public int deleteDboxEleById(Long id);
}
