package com.ruoyi.iot.service;

import java.util.List;
import com.ruoyi.iot.domain.Dbox;

/**
 * 配电箱配置Service接口
 * 
 * @author renjiayue
 * @date 2022-10-08
 */
public interface IDboxService 
{
    /**
     * 查询配电箱配置
     * 
     * @param id 配电箱配置主键
     * @return 配电箱配置
     */
    public Dbox selectDboxById(Long id);
    
    /**
     * 查询配电箱配置
     * 
     * @param id 配电箱配置主键
     * @return 配电箱配置
     */
    public Dbox selectDboxByType(String dboxType);

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
     * 批量删除配电箱配置
     * 
     * @param ids 需要删除的配电箱配置主键集合
     * @return 结果
     */
    public int deleteDboxByIds(Long[] ids);

    /**
     * 删除配电箱配置信息
     * 
     * @param id 配电箱配置主键
     * @return 结果
     */
    public int deleteDboxById(Long id);
}
