package com.ruoyi.iot.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.iot.mapper.CmsHelpCenterMapper;
import com.ruoyi.iot.domain.CmsHelpCenter;
import com.ruoyi.iot.service.ICmsHelpCenterService;

/**
 * 帮助中心内容Service业务层处理
 * 
 * @author renjiayue
 * @date 2022-09-09
 */
@Service
public class CmsHelpCenterServiceImpl implements ICmsHelpCenterService 
{
    @Autowired
    private CmsHelpCenterMapper cmsHelpCenterMapper;

    /**
     * 查询帮助中心内容
     * 
     * @param id 帮助中心内容主键
     * @return 帮助中心内容
     */
    @Override
    public CmsHelpCenter selectCmsHelpCenterById(Long id)
    {
        return cmsHelpCenterMapper.selectCmsHelpCenterById(id);
    }

    /**
     * 查询帮助中心内容列表
     * 
     * @param cmsHelpCenter 帮助中心内容
     * @return 帮助中心内容
     */
    @Override
    public List<CmsHelpCenter> selectCmsHelpCenterList(CmsHelpCenter cmsHelpCenter)
    {
        return cmsHelpCenterMapper.selectCmsHelpCenterList(cmsHelpCenter);
    }

    /**
     * 新增帮助中心内容
     * 
     * @param cmsHelpCenter 帮助中心内容
     * @return 结果
     */
    @Override
    public int insertCmsHelpCenter(CmsHelpCenter cmsHelpCenter)
    {
        cmsHelpCenter.setCreateTime(DateUtils.getNowDate());
        return cmsHelpCenterMapper.insertCmsHelpCenter(cmsHelpCenter);
    }

    /**
     * 修改帮助中心内容
     * 
     * @param cmsHelpCenter 帮助中心内容
     * @return 结果
     */
    @Override
    public int updateCmsHelpCenter(CmsHelpCenter cmsHelpCenter)
    {
        cmsHelpCenter.setUpdateTime(DateUtils.getNowDate());
        return cmsHelpCenterMapper.updateCmsHelpCenter(cmsHelpCenter);
    }

    /**
     * 批量删除帮助中心内容
     * 
     * @param ids 需要删除的帮助中心内容主键
     * @return 结果
     */
    @Override
    public int deleteCmsHelpCenterByIds(Long[] ids)
    {
        return cmsHelpCenterMapper.deleteCmsHelpCenterByIds(ids);
    }

    /**
     * 删除帮助中心内容信息
     * 
     * @param id 帮助中心内容主键
     * @return 结果
     */
    @Override
    public int deleteCmsHelpCenterById(Long id)
    {
        return cmsHelpCenterMapper.deleteCmsHelpCenterById(id);
    }
}
