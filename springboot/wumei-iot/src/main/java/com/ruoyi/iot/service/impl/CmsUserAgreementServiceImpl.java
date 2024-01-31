package com.ruoyi.iot.service.impl;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import com.ruoyi.common.utils.DateUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.iot.mapper.CmsUserAgreementMapper;
import com.ruoyi.iot.domain.CmsUserAgreement;
import com.ruoyi.iot.service.ICmsUserAgreementService;

/**
 * 用户协议Service业务层处理
 * 
 * @author renjiayue
 * @date 2022-09-09
 */
@Service
public class CmsUserAgreementServiceImpl implements ICmsUserAgreementService 
{
    @Autowired
    private CmsUserAgreementMapper cmsUserAgreementMapper;

    
    /**
     * 项目启动时，初始化参数到缓存
     */
    @PostConstruct
    public void init()
    {
    	Stream<String> types = Stream.of("01","02","03","04");
    	types.forEach(type->{
    		CmsUserAgreement dto = cmsUserAgreementMapper.selectCmsUserAgreementByType(type);
    		if(dto==null){
    			dto = new CmsUserAgreement();
    			dto.setType(type);
    			dto.setDetail("协议初始化");
//    			LocalDateTime now = LocalDateTime.now();
    			Date nowDate = DateUtils.getNowDate();
    			dto.setCreateTime(nowDate);
    			dto.setUpdateTime(nowDate);
    			cmsUserAgreementMapper.insertCmsUserAgreement(dto);
    		}
    	});
    }
    /**
     * 查询用户协议
     * 
     * @param id 用户协议主键
     * @return 用户协议
     */
    @Override
    public CmsUserAgreement selectCmsUserAgreementById(Long id)
    {
        return cmsUserAgreementMapper.selectCmsUserAgreementById(id);
    }
    /**
     * 查询用户协议
     * 
     * @param type  协议类型 01用户协议 02隐私政策 03注销协议 04关于我们
     * @return 用户协议
     */
    @Override
    public CmsUserAgreement selectCmsUserAgreementByType(String type)
    {
    	return cmsUserAgreementMapper.selectCmsUserAgreementByType(type);
    }

    /**
     * 查询用户协议列表
     * 
     * @param cmsUserAgreement 用户协议
     * @return 用户协议
     */
    @Override
    public List<CmsUserAgreement> selectCmsUserAgreementList(CmsUserAgreement cmsUserAgreement)
    {
        return cmsUserAgreementMapper.selectCmsUserAgreementList(cmsUserAgreement);
    }

    /**
     * 新增用户协议
     * 
     * @param cmsUserAgreement 用户协议
     * @return 结果
     */
    @Override
    public int insertCmsUserAgreement(CmsUserAgreement cmsUserAgreement)
    {
        cmsUserAgreement.setCreateTime(DateUtils.getNowDate());
        return cmsUserAgreementMapper.insertCmsUserAgreement(cmsUserAgreement);
    }

    /**
     * 修改用户协议
     * 
     * @param cmsUserAgreement 用户协议
     * @return 结果
     */
    @Override
    public int updateCmsUserAgreement(CmsUserAgreement cmsUserAgreement)
    {
        cmsUserAgreement.setUpdateTime(DateUtils.getNowDate());
        return cmsUserAgreementMapper.updateCmsUserAgreement(cmsUserAgreement);
    }

    /**
     * 批量删除用户协议
     * 
     * @param ids 需要删除的用户协议主键
     * @return 结果
     */
    @Override
    public int deleteCmsUserAgreementByIds(Long[] ids)
    {
        return cmsUserAgreementMapper.deleteCmsUserAgreementByIds(ids);
    }

    /**
     * 删除用户协议信息
     * 
     * @param id 用户协议主键
     * @return 结果
     */
    @Override
    public int deleteCmsUserAgreementById(Long id)
    {
        return cmsUserAgreementMapper.deleteCmsUserAgreementById(id);
    }
}
