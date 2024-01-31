package com.ruoyi.iot.mapper;

import java.util.List;
import com.ruoyi.iot.domain.Ad;

/**
 * 广告Mapper接口
 * 
 * @author renjiayue
 * @date 2022-09-03
 */
public interface AdMapper 
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
     * 删除广告
     * 
     * @param id 广告主键
     * @return 结果
     */
    public int deleteAdById(Long id);

    /**
     * 批量删除广告
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAdByIds(Long[] ids);
}
