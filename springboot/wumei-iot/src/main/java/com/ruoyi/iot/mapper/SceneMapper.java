package com.ruoyi.iot.mapper;

import java.util.List;
import com.ruoyi.iot.domain.Scene;
import com.ruoyi.iot.domain.SceneDevice;

/**
 * 场景联动Mapper接口
 * 
 * @author kerwincui
 * @date 2022-09-21
 */
public interface SceneMapper 
{
    /**
     * 查询场景联动
     * 
     * @param sceneId 场景联动主键
     * @return 场景联动
     */
    public Scene selectSceneBySceneId(Long sceneId);
    /**
     * 仅查询场景联动
     * 
     * @param sceneId 场景联动主键
     * @return 场景联动
     */
    public Scene selectOnlySceneBySceneId(Long sceneId);
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
     * 查询场景联动列表
     * 
     * @param scene 场景联动
     * @return 场景联动集合
     */
    public int countSceneByFamilyId(Long familyId);

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
    public int updateScene(Scene scene);

    /**
     * 删除场景联动
     * 
     * @param sceneId 场景联动主键
     * @return 结果
     */
    public int deleteSceneBySceneId(Long sceneId);

    /**
     * 批量删除场景联动
     * 
     * @param sceneIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSceneBySceneIds(Long[] sceneIds);

    /**
     * 批量删除场景设备
     * 
     * @param sceneIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSceneDeviceBySceneIds(Long[] sceneIds);
    
    /**
     * 批量新增场景设备
     * 
     * @param sceneDeviceList 场景设备列表
     * @return 结果
     */
    public int batchSceneDevice(List<SceneDevice> sceneDeviceList);
    

    /**
     * 通过场景联动主键删除场景设备信息
     * 
     * @param sceneId 场景联动ID
     * @return 结果
     */
    public int countDeviceBySceneId(Long sceneId);
    /**
     * 通过场景联动主键删除场景设备信息
     * 
     * @param sceneId 场景联动ID
     * @return 结果
     */
    public int deleteSceneDeviceBySceneId(Long sceneId);
    
    /**
     * 通过设备id除场景设备信息(删除此设备在所有场景下的信息)
     * 
     * @param deviceId 设备id
     * @return 结果
     */
    public int deleteSceneDeviceByDeviceId(Long deviceId);
}
