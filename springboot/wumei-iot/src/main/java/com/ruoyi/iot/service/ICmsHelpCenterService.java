package com.ruoyi.iot.service;

import java.util.List;
import com.ruoyi.iot.domain.CmsHelpCenter;

/**
 * 帮助中心内容Service接口
 * 
 * @author renjiayue
 * @date 2022-09-09
 */
public interface ICmsHelpCenterService 
{
    /**
     * 查询帮助中心内容
     * 
     * @param id 帮助中心内容主键
     * @return 帮助中心内容
     */
    public CmsHelpCenter selectCmsHelpCenterById(Long id);

    /**
     * 查询帮助中心内容列表
     * 
     * @param cmsHelpCenter 帮助中心内容
     * @return 帮助中心内容集合
     */
    public List<CmsHelpCenter> selectCmsHelpCenterList(CmsHelpCenter cmsHelpCenter);

    /**
     * 新增帮助中心内容
     * 
     * @param cmsHelpCenter 帮助中心内容
     * @return 结果
     */
    public int insertCmsHelpCenter(CmsHelpCenter cmsHelpCenter);

    /**
     * 修改帮助中心内容
     * 
     * @param cmsHelpCenter 帮助中心内容
     * @return 结果
     */
    public int updateCmsHelpCenter(CmsHelpCenter cmsHelpCenter);

    /**
     * 批量删除帮助中心内容
     * 
     * @param ids 需要删除的帮助中心内容主键集合
     * @return 结果
     */
    public int deleteCmsHelpCenterByIds(Long[] ids);

    /**
     * 删除帮助中心内容信息
     * 
     * @param id 帮助中心内容主键
     * @return 结果
     */
    public int deleteCmsHelpCenterById(Long id);
}
