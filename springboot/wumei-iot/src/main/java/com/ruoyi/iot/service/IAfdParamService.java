package com.ruoyi.iot.service;

import java.util.List;
import com.ruoyi.iot.domain.AfdParam;

/**
 * 语音维护Service接口
 * 
 * @author renjiayue
 * @date 2022-12-25
 */
public interface IAfdParamService 
{
    /**
     * 查询语音维护
     * 
     * @param id 语音维护主键
     * @return 语音维护
     */
    public AfdParam selectAfdParamById(Long id);

    /**
     * 查询语音维护列表
     * 
     * @param afdParam 语音维护
     * @return 语音维护集合
     */
    public List<AfdParam> selectAfdParamList(AfdParam afdParam);
    /**
     * 前端查询语音维护列表
     * 
     * @param afdParam 语音维护
     * @return 语音维护集合
     */
    public List<AfdParam> selectAfdParamList2(AfdParam afdParam);

    /**
     * 新增语音维护
     * 
     * @param afdParam 语音维护
     * @return 结果
     */
    public int insertAfdParam(AfdParam afdParam);

    /**
     * 修改语音维护
     * 
     * @param afdParam 语音维护
     * @return 结果
     */
    public int updateAfdParam(AfdParam afdParam);

    /**
     * 批量删除语音维护
     * 
     * @param ids 需要删除的语音维护主键集合
     * @return 结果
     */
    public int deleteAfdParamByIds(Long[] ids);

    /**
     * 删除语音维护信息
     * 
     * @param id 语音维护主键
     * @return 结果
     */
    public int deleteAfdParamById(Long id);
}
