package com.ruoyi.iot.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.iot.mapper.BaseConfigMapper;
import com.ruoyi.system.otherDomain.BaseConfig;
import com.ruoyi.system.otherService.IBaseConfigService;

/**
 * 系统参数Service业务层处理
 * 
 * @author renjiayue
 * @date 2022-09-09
 */
@Service
public class BaseConfigServiceImpl implements IBaseConfigService 
{
    @Autowired
    private BaseConfigMapper baseConfigMapper;
    
    @Autowired
    private RedisCache redisCache;

    
    /**
     * 项目启动时，初始化参数到缓存
     */
    @PostConstruct
    public void init()
    {
    	BaseConfig baseConfig = baseConfigMapper.selectOneBaseConfig();
    	if(baseConfig==null){
    		baseConfig = new BaseConfig();
    		baseConfig.setUserCreateRoomMax(5L);
    		baseConfig.setUserLoginDeviceMax(3L);
    		baseConfig.setCustomerServicePhone(null);
    		baseConfig.setUserReceiveSmsMax(3L);
    		baseConfig.setUserSceneMax(3L);
    		baseConfig.setFamilUserMax(6L);//家庭最大用户数
    		baseConfigMapper.insertBaseConfig(baseConfig);
    	}
    	//TODO 清除服务器缓存后删除本条
    	redisCache.deleteObject(getCacheKey());
    	//集群数据处理  若已缓存不再处理,否则进行缓存
    	BaseConfig cache = selectOneBaseConfigByCache();
    	if(cache==null){
    		baseConfig = baseConfigMapper.selectOneBaseConfig();
    		setCache(baseConfig);
    	}
    }
    /**
     * 设置cache key
     * 
     * @param configKey 参数键
     * @return 缓存键key
     */
    private String getCacheKey()
    {
        return Constants.SYS_BASE_CONFIG_KEY;
    }
    /**
     * 设置基础配置缓存(新增或更新后缓存)
     * @param baseConfig
     */
    private void setCache(BaseConfig baseConfig){
    	redisCache.setCacheObject(getCacheKey(), baseConfig);
    }
    
    /**
     * 通过缓存获取智能家居基础配置
     * @return
     */
    @Override
    public BaseConfig selectOneBaseConfigByCache(){
    	BaseConfig cacheObject = redisCache.getCacheObject(getCacheKey());
    	return cacheObject;
    }
    
    /**
     * 不通过id查询,只存在一条数据
     * @return
     */
    @Override
    public BaseConfig selectOneBaseConfig(){
    	return baseConfigMapper.selectOneBaseConfig();
    }
    /**
     * 查询系统参数
     * 
     * @param id 系统参数主键
     * @return 系统参数
     */
    @Override
    public BaseConfig selectBaseConfigById(Long id)
    {
        return baseConfigMapper.selectBaseConfigById(id);
    }

    /**
     * 查询系统参数列表
     * 
     * @param baseConfig 系统参数
     * @return 系统参数
     */
    @Override
    public List<BaseConfig> selectBaseConfigList(BaseConfig baseConfig)
    {
        return baseConfigMapper.selectBaseConfigList(baseConfig);
    }

    /**
     * 新增系统参数
     * 
     * @param baseConfig 系统参数
     * @return 结果
     */
    @Override
    public int insertBaseConfig(BaseConfig baseConfig)
    {
        return baseConfigMapper.insertBaseConfig(baseConfig);
    }

    /**
     * 修改系统参数
     * 
     * @param baseConfig 系统参数
     * @return 结果
     */
    @Override
    public int updateBaseConfig(BaseConfig baseConfig)
    {
    	int updateBaseConfig = baseConfigMapper.updateBaseConfig(baseConfig);
    	baseConfig = baseConfigMapper.selectOneBaseConfig();
    	setCache(baseConfig);
        return updateBaseConfig;
    }

    /**
     * 批量删除系统参数
     * 
     * @param ids 需要删除的系统参数主键
     * @return 结果
     */
    @Override
    public int deleteBaseConfigByIds(Long[] ids)
    {
        return baseConfigMapper.deleteBaseConfigByIds(ids);
    }

    /**
     * 删除系统参数信息
     * 
     * @param id 系统参数主键
     * @return 结果
     */
    @Override
    public int deleteBaseConfigById(Long id)
    {
        return baseConfigMapper.deleteBaseConfigById(id);
    }
}
