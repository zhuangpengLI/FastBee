package com.ruoyi.iot.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.iot.mapper.CmsFeedbackMapper;
import com.ruoyi.iot.domain.CmsFeedback;
import com.ruoyi.iot.service.ICmsFeedbackService;

/**
 * 意见反馈Service业务层处理
 * 
 * @author renjiayue
 * @date 2022-09-07
 */
@Service
public class CmsFeedbackServiceImpl implements ICmsFeedbackService 
{
    @Autowired
    private CmsFeedbackMapper cmsFeedbackMapper;

    /**
     * 查询意见反馈
     * 
     * @param id 意见反馈主键
     * @return 意见反馈
     */
    @Override
    public CmsFeedback selectCmsFeedbackById(Long id)
    {
        return cmsFeedbackMapper.selectCmsFeedbackById(id);
    }

    /**
     * 查询意见反馈列表
     * 
     * @param cmsFeedback 意见反馈
     * @return 意见反馈
     */
    @Override
    public List<CmsFeedback> selectCmsFeedbackList(CmsFeedback cmsFeedback)
    {
        return cmsFeedbackMapper.selectCmsFeedbackList(cmsFeedback);
    }

    /**
     * 新增意见反馈
     * 
     * @param cmsFeedback 意见反馈
     * @return 结果
     */
    @Override
    public int insertCmsFeedback(CmsFeedback cmsFeedback)
    {
        return cmsFeedbackMapper.insertCmsFeedback(cmsFeedback);
    }

    /**
     * 修改意见反馈
     * 
     * @param cmsFeedback 意见反馈
     * @return 结果
     */
    @Override
    public int updateCmsFeedback(CmsFeedback cmsFeedback)
    {
        cmsFeedback.setUpdateTime(DateUtils.getNowDate());
        return cmsFeedbackMapper.updateCmsFeedback(cmsFeedback);
    }

    /**
     * 批量删除意见反馈
     * 
     * @param ids 需要删除的意见反馈主键
     * @return 结果
     */
    @Override
    public int deleteCmsFeedbackByIds(Long[] ids)
    {
        return cmsFeedbackMapper.deleteCmsFeedbackByIds(ids);
    }

    /**
     * 删除意见反馈信息
     * 
     * @param id 意见反馈主键
     * @return 结果
     */
    @Override
    public int deleteCmsFeedbackById(Long id)
    {
        return cmsFeedbackMapper.deleteCmsFeedbackById(id);
    }
}
