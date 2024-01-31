package com.ruoyi.iot.service;

import java.util.List;

import org.quartz.SchedulerException;

import com.ruoyi.iot.domain.Scene;
import com.ruoyi.iot.domain.SceneDevice;

/**
 * 场景联动Service接口
 * 
 * @author kerwincui
 * @date 2022-09-21
 */
public interface ISceneService 
{
    /**
     * 查询场景联动
     * 
     * @param sceneId 场景联动主键
     * @return 场景联动
     */
    public Scene selectSceneBySceneId(Long sceneId);
    /**
     * 查询场景联动
     * 
     * @param sceneId 场景联动主键
     * @return 场景联动
     */
    public void runSceneBySceneId(Long sceneId,Integer runType,Long userId,Long jobId,String jobName);
    /**
     * 查询场景联动设备
     * 
     * @param sceneId 场景联动主键
     * @return 场景联动
     */
    public List<SceneDevice>  selectDeviceBySceneId(Long sceneId);

    /**
     * 查询场景联动列表
     * 
     * @param scene 场景联动
     * @return 场景联动集合
     */
    public List<Scene> selectSceneList(Scene scene);

    /**
     * 新增场景联动
     * 
     * @param scene 场景联动
     * @return 结果
     */
    public int insertScene(Scene scene);

    /**
     * 修改场景联动
     * 
     * @param scene 场景联动
     * @return 结果
     */
    public int updateSceneOnly(Scene scene);
    /**
     * 修改场景联动
     * 
     * @param scene 场景联动
     * @return 结果
     */
    public int updateScene(Scene scene);

    /**
     * 批量删除场景联动
     * 
     * @param sceneIds 需要删除的场景联动主键集合
     * @return 结果
     * @throws SchedulerException 
     */
    public int deleteSceneBySceneIds(Long[] sceneIds) throws SchedulerException;

    /**
     * 查询场景的设备统计 
     * @param sceneId
     * @return
     */
    public int countDeviceBySceneId(Long sceneId);
    /**
     * 删除场景联动信息
     * 
     * @param sceneId 场景联动主键
     * @return 结果
     * @throws SchedulerException 
     */
    public int deleteSceneBySceneId(Long sceneId) throws SchedulerException;
    /**
     * 删除场景联动信息 通过家庭id
     * 
     * @param familyId 家庭id
     * @return 结果
     * @throws SchedulerException 
     */
    public int deleteSceneByFamilyId(Long familyId) throws SchedulerException;
}
