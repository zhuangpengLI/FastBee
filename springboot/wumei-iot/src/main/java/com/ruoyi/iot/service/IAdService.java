package com.ruoyi.iot.service;

import java.util.List;
import com.ruoyi.iot.domain.Ad;

/**
 * 广告Service接口
 * 
 * @author renjiayue
 * @date 2022-09-03
 */
public interface IAdService 
{
    /**
     * 查询广告
     * 
     * @param id 广告主键
     * @return 广告
     */
    public Ad selectAdById(Long id);

    /**
     * 查询广告列表
     * 
     * @param ad 广告
     * @return 广告集合
     */
    public List<Ad> selectAdList(Ad ad);

    /**
     * 新增广告
     * 
     * @param ad 广告
     * @return 结果
     */
    public int insertAd(Ad ad);

    /**
     * 修改广告
     * 
     * @param ad 广告
     * @return 结果
     */
    public int updateAd(Ad ad);

    /**
     * 批量删除广告
     * 
     * @param ids 需要删除的广告主键集合
     * @return 结果
     */
    public int deleteAdByIds(Long[] ids);

    /**
     * 删除广告信息
     * 
     * @param id 广告主键
     * @return 结果
     */
    public int deleteAdById(Long id);
}
