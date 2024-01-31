package com.ruoyi.iot.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.ruoyi.iot.mapper.DeviceMapper;
import com.ruoyi.iot.mapper.MsgNoticeSettingMapper;
import com.ruoyi.iot.mobile.respModel.DeviceBriefRespInfo;
import com.ruoyi.iot.mobile.service.IFamilyDeviceService;
import com.ruoyi.iot.mobile.service.impl.FamilyDeviceServiceImpl;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.iot.domain.MsgNoticeSetting;
import com.ruoyi.iot.service.IFamilyService;
import com.ruoyi.iot.service.IMsgNoticeSettingService;
import com.ruoyi.system.otherDto.MsgSettingEnum;

/**
 * 消息设置Service业务层处理
 * 
 * @author renjiayue
 * @date 2022-10-21
 */
@Service
public class MsgNoticeSettingServiceImpl implements IMsgNoticeSettingService 
{
    private static final Logger log = LoggerFactory.getLogger(MsgNoticeSettingServiceImpl.class);

    @Autowired
    private MsgNoticeSettingMapper msgNoticeSettingMapper;
    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private IFamilyDeviceService familyDeviceService;
    @Autowired
    private IFamilyService familyService;
    

    /**
     * 查询消息设置
     * 
     * @param id 消息设置主键
     * @return 消息设置
     */
    @Override
    public MsgNoticeSetting selectMsgNoticeSettingById(Long id)
    {
        return msgNoticeSettingMapper.selectMsgNoticeSettingById(id);
    }

    /**
     * 查询消息设置列表
     * 
     * @param msgNoticeSetting 消息设置
     * @return 消息设置
     */
    @Override
    public List<MsgNoticeSetting> selectMsgNoticeSettingList(MsgNoticeSetting msgNoticeSetting)
    {
        return msgNoticeSettingMapper.selectMsgNoticeSettingList(msgNoticeSetting);
    }

    /**
     * 新增消息设置
     * 
     * @param msgNoticeSetting 消息设置
     * @return 结果
     */
    @Override
    public int insertMsgNoticeSetting(MsgNoticeSetting msgNoticeSetting)
    {
        return msgNoticeSettingMapper.insertMsgNoticeSetting(msgNoticeSetting);
    }

    /**
     * 修改消息设置
     * 
     * @param msgNoticeSetting 消息设置
     * @return 结果
     */
    @Override
    public int updateMsgNoticeSetting(MsgNoticeSetting msgNoticeSetting)
    {
        return msgNoticeSettingMapper.updateMsgNoticeSetting(msgNoticeSetting);
    }
    @Override
    public AjaxResult updateMsgNoticeSetting(MsgSettingEnum settingEnum,String busId
    		,Integer isDisabled,Long userId) {
    	if(!MsgSettingEnum.DEV_DEVICE.equals(settingEnum)
    			&& !MsgSettingEnum.DEV_FAMILY.equals(settingEnum)) {
    		//非设备和家庭的 没有业务id
    		busId = null;
    	}
    	MsgNoticeSetting msgNoticeSetting = new MsgNoticeSetting();
    	msgNoticeSetting.setIdentifier(settingEnum.getIdentifier());
    	msgNoticeSetting.setBusId(busId);
    	msgNoticeSetting.setUserId(userId);
		List<MsgNoticeSetting> list = msgNoticeSettingMapper.selectMsgNoticeSettingList(msgNoticeSetting );
		if(list.size()>1) {
			return AjaxResult.error("查询出多条数据,数据有误");
		}else if(list.size()==1) {
			//更新
			MsgNoticeSetting setting = list.get(0);
			setting.setIsDisabled(isDisabled);
			msgNoticeSettingMapper.updateMsgNoticeSetting(setting);
			return AjaxResult.success();
		}else {
			//新增
			insertCascadeMsgNoticeSetting(settingEnum, busId, isDisabled, userId);
			return AjaxResult.success();
		}
    }
    @Override
    public AjaxResult getOneNoticeSetting(MsgSettingEnum settingEnum,String busId
    		,Long userId) {
    	if(!MsgSettingEnum.DEV_DEVICE.equals(settingEnum)
    			&& !MsgSettingEnum.DEV_FAMILY.equals(settingEnum)) {
    		//非设备和家庭的 没有业务id
    		busId = null;
    	}
    	MsgNoticeSetting msgNoticeSetting = new MsgNoticeSetting();
    	msgNoticeSetting.setIdentifier(settingEnum.getIdentifier());
    	msgNoticeSetting.setBusId(busId);
    	msgNoticeSetting.setUserId(userId);
    	List<MsgNoticeSetting> list = msgNoticeSettingMapper.selectMsgNoticeSettingList(msgNoticeSetting );
    	if(list.size()>1) {
    		return AjaxResult.error("查询出多条数据,数据有误");
    	}else if(list.size()==1) {
    		//查询出结果
    		MsgNoticeSetting setting = list.get(0);
    		return AjaxResult.success(setting);
    	}else {
    		//模拟一个未禁用的结果
    		MsgNoticeSetting query = new MsgNoticeSetting();
        	query.setIdentifier(settingEnum.getIdentifier());
        	query.setSettingName(settingEnum.getIdentifierName());
        	query.setBusId(busId);
        	query.setUserId(userId);
        	query.setIsDisabled(0);
    		return AjaxResult.success(query);
    	}
    }
    
    
    /**
     * 
     * @param settingEnum
     * @param busId
     * @param isDisabled
     * @param userId
     * @return 新增记录id
     */
    private Long insertCascadeMsgNoticeSetting(MsgSettingEnum settingEnum,String busId
    		,Integer isDisabled,Long userId) {
    	MsgSettingEnum parent = settingEnum.getParent();
    	String parentBusId = null; //
    	Long parentId = null; //
    	if(MsgSettingEnum.DEV_DEVICE.equals(settingEnum)) {
    		//如果当前是设备通知 则busId为设备id
    		Long deviceId = Long.valueOf(busId);
    		boolean perm = familyDeviceService.isCommonPerm(deviceId, userId);
    		if(!perm) {
    			throw new RuntimeException("权限不足");
    		}
    		//parentBusId 则为家庭id  如果是空 则报错 理论上不应该报错
    		DeviceBriefRespInfo info = deviceMapper.selectBriefDeviceByDeviceId(deviceId);
    		parentBusId = info.getBelongFamilyId().toString();
    	}else {
    		if(MsgSettingEnum.DEV_FAMILY.equals(settingEnum)) {
    			//如果当前是家庭通知 则busId为家庭id
        		Long familyId = Long.valueOf(busId);
        		boolean perm = familyService.isCommonPerm(familyId,userId);
        		if(!perm) {
        			throw new RuntimeException("权限不足");
        		}
    		}
    		//其他情况 parentBusId均为空
    		parentBusId = null;
    	}
    	if(parent!=null) {
    		Integer defaultIsDisabled = 0;//默认是启用
    		parentId = insertCascadeMsgNoticeSetting(parent, parentBusId, defaultIsDisabled, userId);
    	}
    	//先查询,查询没有再新增
    	MsgNoticeSetting query = new MsgNoticeSetting();
    	query.setIdentifier(settingEnum.getIdentifier());
    	query.setBusId(busId);
    	query.setUserId(userId);
    	query.setParentId(parentId);
    	log.info("当前查询消息通知对象:{}",JSONObject.toJSON(query));
		List<MsgNoticeSetting> list = msgNoticeSettingMapper.selectMsgNoticeSettingList(query );
		if(list.size()>1) {
			throw new RuntimeException("设置消息通知数据有误");
		}else if(list.isEmpty()) {
			//新增
			MsgNoticeSetting setting = new MsgNoticeSetting();
			setting.setIdentifier(settingEnum.getIdentifier());
			setting.setBusId(busId);
			setting.setUserId(userId);
			setting.setIsDisabled(isDisabled);
			setting.setParentId(parentId);
			setting.setSettingName(settingEnum.getIdentifierName());
			msgNoticeSettingMapper.insertMsgNoticeSetting(setting);
	    	return setting.getId();
		}else {
			//直接返回
			return list.get(0).getId();
		}
    	
    }
    
    @Override
    public boolean isSendNotice(String identifier,String busId
    		,Long userId) {
    	MsgSettingEnum settingEnum = MsgSettingEnum.selectSettingByIdentifier(identifier);
    	if(!MsgSettingEnum.DEV_DEVICE.getIdentifier().equals(identifier)
    			&& !MsgSettingEnum.DEV_FAMILY.getIdentifier().equals(identifier)) {
    		//非设备和家庭的 没有业务id
    		busId = null;
    	}
    	MsgNoticeSetting msgNoticeSetting = new MsgNoticeSetting();
    	msgNoticeSetting.setIdentifier(identifier);
    	msgNoticeSetting.setBusId(busId);
    	msgNoticeSetting.setUserId(userId);
    	log.info("查询是否进行通知,参数为:{}",JSONObject.toJSON(msgNoticeSetting));
    	List<MsgNoticeSetting> list = msgNoticeSettingMapper.selectMsgNoticeSettingList(msgNoticeSetting );
    	if(list.size()>1) {
    		throw new RuntimeException("查询出多条数据,数据有误");
    	}else if(list.size()==1) {
    		//查询出结果
    		MsgNoticeSetting setting = list.get(0);
    		return selectIsDisabled(setting)==0;
    	}else {
    		insertCascadeMsgNoticeSetting(settingEnum, busId, 0, userId);//自动创建一个启用的数据 
    		List<MsgNoticeSetting> list2 = msgNoticeSettingMapper.selectMsgNoticeSettingList(msgNoticeSetting );
    		if(list2.size()>1) {
        		throw new RuntimeException("查询出多条数据,数据有误");
        	}else if(list2.size()==1) {
        		//查询出结果
        		MsgNoticeSetting setting = list2.get(0);
        		return selectIsDisabled(setting)==0;
        	}else {
        		//新增完还没有数据,则不进行通知
        		return false;
        	}
    	}
    }
    
    /**
     * 自底向上查询,只要有一个禁用 则不发送通知
     * @param setting
     * @return
     */
    private int selectIsDisabled(MsgNoticeSetting setting) {
    	int isDis = setting.getIsDisabled().intValue();
    	if(1==isDis) {
    		return 1;
    	}
    	Long parentId = setting.getParentId();
    	if(parentId==null) {
    		//查找到顶层,仍然可推送 则推送
    		return isDis;
    	}
    	MsgNoticeSetting pSetting = msgNoticeSettingMapper.selectMsgNoticeSettingById(parentId);
    	return selectIsDisabled(pSetting);
    	
    }

    /**
     * 批量删除消息设置
     * 
     * @param ids 需要删除的消息设置主键
     * @return 结果
     */
    @Override
    public int deleteMsgNoticeSettingByIds(Long[] ids)
    {
        return msgNoticeSettingMapper.deleteMsgNoticeSettingByIds(ids);
    }

    /**
     * 删除消息设置信息
     * 
     * @param id 消息设置主键
     * @return 结果
     */
    @Override
    public int deleteMsgNoticeSettingById(Long id)
    {
        return msgNoticeSettingMapper.deleteMsgNoticeSettingById(id);
    }
}
