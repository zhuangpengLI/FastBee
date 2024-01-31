package com.ruoyi.iot.mapper;

import java.util.List;
import com.ruoyi.iot.domain.CmsFeedback;

/**
 * 意见反馈Mapper接口
 * 
 * @author renjiayue
 * @date 2022-09-07
 */
public interface CmsFeedbackMapper 
{
    /**
     * 查询意见反馈
     * 
     * @param id 意见反馈主键
     * @return 意见反馈
     */
    public CmsFeedback selectCmsFeedbackById(Long id);

    /**
     * 查询意见反馈列表
     * 
     * @param cmsFeedback 意见反馈
     * @return 意见反馈集合
     */
    public List<CmsFeedback> selectCmsFeedbackList(CmsFeedback cmsFeedback);

    /**
     * 新增意见反馈
     * 
     * @param cmsFeedback 意见反馈
     * @return 结果
     */
    public int insertCmsFeedback(CmsFeedback cmsFeedback);

    /**
     * 修改意见反馈
     * 
     * @param cmsFeedback 意见反馈
     * @return 结果
     */
    public int updateCmsFeedback(CmsFeedback cmsFeedback);

    /**
     * 删除意见反馈
     * 
     * @param id 意见反馈主键
     * @return 结果
     */
    public int deleteCmsFeedbackById(Long id);

    /**
     * 批量删除意见反馈
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCmsFeedbackByIds(Long[] ids);
}
