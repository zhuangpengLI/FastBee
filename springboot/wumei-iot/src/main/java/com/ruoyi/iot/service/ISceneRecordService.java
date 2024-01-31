package com.ruoyi.iot.service;

import java.util.List;
import com.ruoyi.iot.domain.SceneRecord;

/**
 * 场景执行记录Service接口
 * 
 * @author renjiayue
 * @date 2022-10-09
 */
public interface ISceneRecordService 
{
    /**
     * 查询场景执行记录
     * 
     * @param id 场景执行记录主键
     * @return 场景执行记录
     */
    public SceneRecord selectSceneRecordById(Long id);

    /**
     * 查询场景执行记录列表
     * 
     * @param sceneRecord 场景执行记录
     * @return 场景执行记录集合
     */
    public List<SceneRecord> selectSceneRecordList(SceneRecord sceneRecord);

    /**
     * 新增场景执行记录
     * 
     * @param sceneRecord 场景执行记录
     * @return 结果
     */
    public int insertSceneRecord(SceneRecord sceneRecord);

    /**
     * 修改场景执行记录
     * 
     * @param sceneRecord 场景执行记录
     * @return 结果
     */
    public int updateSceneRecord(SceneRecord sceneRecord);

    /**
     * 批量删除场景执行记录
     * 
     * @param ids 需要删除的场景执行记录主键集合
     * @return 结果
     */
    public int deleteSceneRecordByIds(Long[] ids);

    /**
     * 删除场景执行记录信息
     * 
     * @param id 场景执行记录主键
     * @return 结果
     */
    public int deleteSceneRecordById(Long id);
}
