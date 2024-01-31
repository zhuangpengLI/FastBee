package com.ruoyi.iot.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.iot.mapper.AfdParamMapper;
import com.ruoyi.iot.domain.AfdParam;
import com.ruoyi.iot.service.IAfdParamService;

/**
 * 语音维护Service业务层处理
 * 
 * @author renjiayue
 * @date 2022-12-25
 */
@Service
public class AfdParamServiceImpl implements IAfdParamService 
{
    @Autowired
    private AfdParamMapper afdParamMapper;

    /**
     * 查询语音维护
     * 
     * @param id 语音维护主键
     * @return 语音维护
     */
    @Override
    public AfdParam selectAfdParamById(Long id)
    {
        return afdParamMapper.selectAfdParamById(id);
    }

    /**
     * 查询语音维护列表
     * 
     * @param afdParam 语音维护
     * @return 语音维护
     */
    @Override
    public List<AfdParam> selectAfdParamList(AfdParam afdParam)
    {
        return afdParamMapper.selectAfdParamList(afdParam);
    }
    @Override
    public List<AfdParam> selectAfdParamList2(AfdParam afdParam)
    {
    	return afdParamMapper.selectAfdParamList2(afdParam);
    }

    /**
     * 新增语音维护
     * 
     * @param afdParam 语音维护
     * @return 结果
     */
    @Override
    public int insertAfdParam(AfdParam afdParam)
    {
        afdParam.setCreateTime(DateUtils.getNowDate());
        return afdParamMapper.insertAfdParam(afdParam);
    }

    /**
     * 修改语音维护
     * 
     * @param afdParam 语音维护
     * @return 结果
     */
    @Override
    public int updateAfdParam(AfdParam afdParam)
    {
        afdParam.setUpdateTime(DateUtils.getNowDate());
        return afdParamMapper.updateAfdParam(afdParam);
    }

    /**
     * 批量删除语音维护
     * 
     * @param ids 需要删除的语音维护主键
     * @return 结果
     */
    @Override
    public int deleteAfdParamByIds(Long[] ids)
    {
        return afdParamMapper.deleteAfdParamByIds(ids);
    }

    /**
     * 删除语音维护信息
     * 
     * @param id 语音维护主键
     * @return 结果
     */
    @Override
    public int deleteAfdParamById(Long id)
    {
        return afdParamMapper.deleteAfdParamById(id);
    }
}
