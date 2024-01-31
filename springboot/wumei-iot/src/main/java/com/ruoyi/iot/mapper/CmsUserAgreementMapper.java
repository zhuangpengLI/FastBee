package com.ruoyi.iot.mapper;

import java.util.List;
import com.ruoyi.iot.domain.CmsUserAgreement;

/**
 * 用户协议Mapper接口
 * 
 * @author renjiayue
 * @date 2022-09-09
 */
public interface CmsUserAgreementMapper 
{
    /**
     * 查询用户协议
     * 
     * @param id 用户协议主键
     * @return 用户协议
     */
    public CmsUserAgreement selectCmsUserAgreementById(Long id);
    /**
     * 查询用户协议
     * 
     * @param type  协议类型 01用户协议 02隐私政策 03注销协议 04关于我们
     * @return 用户协议
     */
    public CmsUserAgreement selectCmsUserAgreementByType(String type);

    /**
     * 查询用户协议列表
     * 
     * @param cmsUserAgreement 用户协议
     * @return 用户协议集合
     */
    public List<CmsUserAgreement> selectCmsUserAgreementList(CmsUserAgreement cmsUserAgreement);

    /**
     * 新增用户协议
     * 
     * @param cmsUserAgreement 用户协议
     * @return 结果
     */
    public int insertCmsUserAgreement(CmsUserAgreement cmsUserAgreement);

    /**
     * 修改用户协议
     * 
     * @param cmsUserAgreement 用户协议
     * @return 结果
     */
    public int updateCmsUserAgreement(CmsUserAgreement cmsUserAgreement);

    /**
     * 删除用户协议
     * 
     * @param id 用户协议主键
     * @return 结果
     */
    public int deleteCmsUserAgreementById(Long id);

    /**
     * 批量删除用户协议
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCmsUserAgreementByIds(Long[] ids);
}
