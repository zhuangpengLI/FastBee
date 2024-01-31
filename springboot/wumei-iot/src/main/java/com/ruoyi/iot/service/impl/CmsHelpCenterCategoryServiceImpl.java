package com.ruoyi.iot.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.iot.mapper.CmsHelpCenterCategoryMapper;
import com.ruoyi.iot.domain.CmsHelpCenterCategory;
import com.ruoyi.iot.service.ICmsHelpCenterCategoryService;

/**
 * 帮助分类Service业务层处理
 * 
 * @author renjiayue
 * @date 2022-09-09
 */
@Service
public class CmsHelpCenterCategoryServiceImpl implements ICmsHelpCenterCategoryService 
{
    @Autowired
    private CmsHelpCenterCategoryMapper cmsHelpCenterCategoryMapper;

    /**
     * 查询帮助分类
     * 
     * @param categoryId 帮助分类主键
     * @return 帮助分类
     */
    @Override
    public CmsHelpCenterCategory selectCmsHelpCenterCategoryByCategoryId(Long categoryId)
    {
        return cmsHelpCenterCategoryMapper.selectCmsHelpCenterCategoryByCategoryId(categoryId);
    }

    /**
     * 查询帮助分类列表
     * 
     * @param cmsHelpCenterCategory 帮助分类
     * @return 帮助分类
     */
    @Override
    public List<CmsHelpCenterCategory> selectCmsHelpCenterCategoryList(CmsHelpCenterCategory cmsHelpCenterCategory)
    {
        return cmsHelpCenterCategoryMapper.selectCmsHelpCenterCategoryList(cmsHelpCenterCategory);
    }

    /**
     * 新增帮助分类
     * 
     * @param cmsHelpCenterCategory 帮助分类
     * @return 结果
     */
    @Override
    public int insertCmsHelpCenterCategory(CmsHelpCenterCategory cmsHelpCenterCategory)
    {
        cmsHelpCenterCategory.setCreateTime(DateUtils.getNowDate());
        return cmsHelpCenterCategoryMapper.insertCmsHelpCenterCategory(cmsHelpCenterCategory);
    }

    /**
     * 修改帮助分类
     * 
     * @param cmsHelpCenterCategory 帮助分类
     * @return 结果
     */
    @Override
    public int updateCmsHelpCenterCategory(CmsHelpCenterCategory cmsHelpCenterCategory)
    {
        cmsHelpCenterCategory.setUpdateTime(DateUtils.getNowDate());
        return cmsHelpCenterCategoryMapper.updateCmsHelpCenterCategory(cmsHelpCenterCategory);
    }

    /**
     * 批量删除帮助分类
     * 
     * @param categoryIds 需要删除的帮助分类主键
     * @return 结果
     */
    @Override
    public int deleteCmsHelpCenterCategoryByCategoryIds(Long[] categoryIds)
    {
        return cmsHelpCenterCategoryMapper.deleteCmsHelpCenterCategoryByCategoryIds(categoryIds);
    }

    /**
     * 删除帮助分类信息
     * 
     * @param categoryId 帮助分类主键
     * @return 结果
     */
    @Override
    public int deleteCmsHelpCenterCategoryByCategoryId(Long categoryId)
    {
        return cmsHelpCenterCategoryMapper.deleteCmsHelpCenterCategoryByCategoryId(categoryId);
    }
}
