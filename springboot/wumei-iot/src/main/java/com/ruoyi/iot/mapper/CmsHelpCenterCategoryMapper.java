package com.ruoyi.iot.mapper;

import java.util.List;
import com.ruoyi.iot.domain.CmsHelpCenterCategory;

/**
 * 帮助分类Mapper接口
 * 
 * @author renjiayue
 * @date 2022-09-09
 */
public interface CmsHelpCenterCategoryMapper 
{
    /**
     * 查询帮助分类
     * 
     * @param categoryId 帮助分类主键
     * @return 帮助分类
     */
    public CmsHelpCenterCategory selectCmsHelpCenterCategoryByCategoryId(Long categoryId);

    /**
     * 查询帮助分类列表
     * 
     * @param cmsHelpCenterCategory 帮助分类
     * @return 帮助分类集合
     */
    public List<CmsHelpCenterCategory> selectCmsHelpCenterCategoryList(CmsHelpCenterCategory cmsHelpCenterCategory);

    /**
     * 新增帮助分类
     * 
     * @param cmsHelpCenterCategory 帮助分类
     * @return 结果
     */
    public int insertCmsHelpCenterCategory(CmsHelpCenterCategory cmsHelpCenterCategory);

    /**
     * 修改帮助分类
     * 
     * @param cmsHelpCenterCategory 帮助分类
     * @return 结果
     */
    public int updateCmsHelpCenterCategory(CmsHelpCenterCategory cmsHelpCenterCategory);

    /**
     * 删除帮助分类
     * 
     * @param categoryId 帮助分类主键
     * @return 结果
     */
    public int deleteCmsHelpCenterCategoryByCategoryId(Long categoryId);

    /**
     * 批量删除帮助分类
     * 
     * @param categoryIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCmsHelpCenterCategoryByCategoryIds(Long[] categoryIds);
}
