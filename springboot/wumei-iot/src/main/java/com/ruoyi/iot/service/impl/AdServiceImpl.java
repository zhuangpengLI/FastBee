package com.ruoyi.iot.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.iot.mapper.AdMapper;
import com.ruoyi.iot.domain.Ad;
import com.ruoyi.iot.service.IAdService;

/**
 * 广告Service业务层处理
 * 
 * @author renjiayue
 * @date 2022-09-03
 */
@Service
public class AdServiceImpl implements IAdService 
{
    @Autowired
    private AdMapper adMapper;

    /**
     * 查询广告
     * 
     * @param id 广告主键
     * @return 广告
     */
    @Override
    public Ad selectAdById(Long id)
    {
        return adMapper.selectAdById(id);
    }

    /**
     * 查询广告列表
     * 
     * @param ad 广告
     * @return 广告
     */
    @Override
    public List<Ad> selectAdList(Ad ad)
    {
        return adMapper.selectAdList(ad);
    }

    /**
     * 新增广告
     * 
     * @param ad 广告
     * @return 结果
     */
    @Override
    public int insertAd(Ad ad)
    {
        return adMapper.insertAd(ad);
    }

    /**
     * 修改广告
     * 
     * @param ad 广告
     * @return 结果
     */
    @Override
    public int updateAd(Ad ad)
    {
        ad.setUpdateTime(DateUtils.getNowDate());
        return adMapper.updateAd(ad);
    }

    /**
     * 批量删除广告
     * 
     * @param ids 需要删除的广告主键
     * @return 结果
     */
    @Override
    public int deleteAdByIds(Long[] ids)
    {
        return adMapper.deleteAdByIds(ids);
    }

    /**
     * 删除广告信息
     * 
     * @param id 广告主键
     * @return 结果
     */
    @Override
    public int deleteAdById(Long id)
    {
        return adMapper.deleteAdById(id);
    }
}
