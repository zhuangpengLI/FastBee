package com.ruoyi.iot.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.iot.mapper.SceneRecordMapper;
import com.ruoyi.iot.domain.SceneRecord;
import com.ruoyi.iot.service.ISceneRecordService;

/**
 * 场景执行记录Service业务层处理
 * 
 * @author renjiayue
 * @date 2022-10-09
 */
@Service
public class SceneRecordServiceImpl implements ISceneRecordService 
{
    @Autowired
    private SceneRecordMapper sceneRecordMapper;

    /**
     * 查询场景执行记录
     * 
     * @param id 场景执行记录主键
     * @return 场景执行记录
     */
    @Override
    public SceneRecord selectSceneRecordById(Long id)
    {
        return sceneRecordMapper.selectSceneRecordById(id);
    }

    /**
     * 查询场景执行记录列表
     * 
     * @param sceneRecord 场景执行记录
     * @return 场景执行记录
     */
    @Override
    public List<SceneRecord> selectSceneRecordList(SceneRecord sceneRecord)
    {
        return sceneRecordMapper.selectSceneRecordList(sceneRecord);
    }

    /**
     * 新增场景执行记录
     * 
     * @param sceneRecord 场景执行记录
     * @return 结果
     */
    @Override
    public int insertSceneRecord(SceneRecord sceneRecord)
    {
        sceneRecord.setCreateTime(DateUtils.getNowDate());
        return sceneRecordMapper.insertSceneRecord(sceneRecord);
    }

    /**
     * 修改场景执行记录
     * 
     * @param sceneRecord 场景执行记录
     * @return 结果
     */
    @Override
    public int updateSceneRecord(SceneRecord sceneRecord)
    {
        sceneRecord.setUpdateTime(DateUtils.getNowDate());
        return sceneRecordMapper.updateSceneRecord(sceneRecord);
    }

    /**
     * 批量删除场景执行记录
     * 
     * @param ids 需要删除的场景执行记录主键
     * @return 结果
     */
    @Override
    public int deleteSceneRecordByIds(Long[] ids)
    {
        return sceneRecordMapper.deleteSceneRecordByIds(ids);
    }

    /**
     * 删除场景执行记录信息
     * 
     * @param id 场景执行记录主键
     * @return 结果
     */
    @Override
    public int deleteSceneRecordById(Long id)
    {
        return sceneRecordMapper.deleteSceneRecordById(id);
    }
}
