package com.ruoyi.iot.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.iot.domain.MsgNoticeSetting;
import com.ruoyi.iot.domain.MsgSmsNoticeSetting;
import com.ruoyi.iot.mapper.MsgSmsNoticeSettingMapper;
import com.ruoyi.iot.mobile.respModel.DeviceBriefRespInfo;
import com.ruoyi.iot.service.IMsgSmsNoticeSettingService;
import com.ruoyi.system.otherDto.SmsSettingEnum;

/**
 * 短信通知设置Service业务层处理
 * 
 * @author renjiayue
 * @date 2022-11-08
 */
@Service
public class MsgSmsNoticeSettingServiceImpl implements IMsgSmsNoticeSettingService 
{
    private static final Logger log = LoggerFactory.getLogger(MsgSmsNoticeSettingServiceImpl.class);
    @Autowired
    private MsgSmsNoticeSettingMapper msgSmsNoticeSettingMapper;

    /**
     * 查询短信通知设置
     * 
     * @param id 短信通知设置主键
     * @return 短信通知设置
     */
    @Override
    public MsgSmsNoticeSetting selectMsgSmsNoticeSettingById(Long id)
    {
        return msgSmsNoticeSettingMapper.selectMsgSmsNoticeSettingById(id);
    }

    /**
     * 查询短信通知设置列表
     * 
     * @param msgSmsNoticeSetting 短信通知设置
     * @return 短信通知设置
     */
    @Override
    public List<MsgSmsNoticeSetting> selectMsgSmsNoticeSettingList(MsgSmsNoticeSetting msgSmsNoticeSetting)
    {
        return msgSmsNoticeSettingMapper.selectMsgSmsNoticeSettingList(msgSmsNoticeSetting);
    }

    /**
     * 新增短信通知设置
     * 
     * @param msgSmsNoticeSetting 短信通知设置
     * @return 结果
     */
    @Override
    public int insertMsgSmsNoticeSetting(MsgSmsNoticeSetting msgSmsNoticeSetting)
    {
        return msgSmsNoticeSettingMapper.insertMsgSmsNoticeSetting(msgSmsNoticeSetting);
    }

    /**
     * 修改短信通知设置
     * 
     * @param msgSmsNoticeSetting 短信通知设置
     * @return 结果
     */
    @Override
    public int updateMsgSmsNoticeSetting(MsgSmsNoticeSetting msgSmsNoticeSetting)
    {
        return msgSmsNoticeSettingMapper.updateMsgSmsNoticeSetting(msgSmsNoticeSetting);
    }

    //TODO 需要做定时任务来处理 每年免费短信额度及年费到期处理
    @Override
    public AjaxResult updateMsgNoticeSetting(SmsSettingEnum settingEnum,String busId
    		,Integer isDisabled,Long userId) {
//    	if(!SmsSettingEnum.DEV_DEVICE.equals(settingEnum)
//    			&& !SmsSettingEnum.DEV_FAMILY.equals(settingEnum)) {
//    		//非设备和家庭的 没有业务id
    		busId = null;
//    	}
    		MsgSmsNoticeSetting msgNoticeSetting = new MsgSmsNoticeSetting();
    	msgNoticeSetting.setIdentifier(settingEnum.getIdentifier());
    	msgNoticeSetting.setBusId(busId);
    	msgNoticeSetting.setUserId(userId);
		List<MsgSmsNoticeSetting> list = msgSmsNoticeSettingMapper.selectMsgSmsNoticeSettingList(msgNoticeSetting );
		if(list.size()>1) {
			return AjaxResult.error("查询出多条数据,数据有误");
		}else if(list.size()==1) {
			//更新
			MsgSmsNoticeSetting setting = list.get(0);
			if(isDisabled==0) {
				//如果要启用,则需要校验短信条数以及短信到期时间
				if(setting.getSmsLimitType().intValue()==1) {
	    			int smsSendedCount = setting.getSmsSendedCount();
	    			Integer smsLimitCount = setting.getSmsLimitCount();
	    			if(smsSendedCount>=smsLimitCount) {
	    				return AjaxResult.error("启用失败,请联系物业或厂家开通短信服务");
	    			}
	    		}else if(setting.getSmsLimitType().intValue()==2) {
	    			Date smsLimitDate = setting.getSmsLimitDate();
	    			if(smsLimitDate==null || smsLimitDate.before(new Date())) {
	    				//如果到期了  本年度还有免费短信额度 则应该启用成功
	    				int smsSendedCount = setting.getSmsSendedCount();
		    			Integer smsLimitCount = setting.getSmsLimitCount();
		    			if(smsSendedCount<smsLimitCount) {
		    				setting.setSmsLimitType(1);//调整为免费短信条数 ,继续启用
		    			}else {
		    				return AjaxResult.error("启用失败,请联系物业或厂家开通短信服务");
		    			}
	    			}
	    		}else {
	    			return AjaxResult.error("启用失败,请联系售后支持");
	    		}
			}
			setting.setIsDisabled(isDisabled);
			msgSmsNoticeSettingMapper.updateMsgSmsNoticeSetting(setting);
			return AjaxResult.success();
		}else {
			//新增
			insertCascadeMsgNoticeSetting(settingEnum, busId, isDisabled, userId);
			return AjaxResult.success();
		}
    }
    @Override
    public AjaxResult getOneNoticeSetting(SmsSettingEnum settingEnum,String busId
    		,Long userId) {
//    	if(!SmsSettingEnum.DEV_DEVICE.equals(settingEnum)
//    			&& !SmsSettingEnum.DEV_FAMILY.equals(settingEnum)) {
//    		//非设备和家庭的 没有业务id
    		busId = null;
//    	}
    		MsgSmsNoticeSetting msgNoticeSetting = new MsgSmsNoticeSetting();
    	msgNoticeSetting.setIdentifier(settingEnum.getIdentifier());
    	msgNoticeSetting.setBusId(busId);
    	msgNoticeSetting.setUserId(userId);
    	List<MsgSmsNoticeSetting> list = msgSmsNoticeSettingMapper.selectMsgSmsNoticeSettingList(msgNoticeSetting );
    	if(list.size()>1) {
    		return AjaxResult.error("查询出多条数据,数据有误");
    	}else if(list.size()==1) {
    		//查询出结果
    		MsgSmsNoticeSetting setting = list.get(0);
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
    private Long insertCascadeMsgNoticeSetting(SmsSettingEnum settingEnum,String busId
    		,Integer isDisabled,Long userId) {
    	SmsSettingEnum parent = settingEnum.getParent();
    	String parentBusId = null; //
    	Long parentId = null; //
//    	if(SmsSettingEnum.DEV_DEVICE.equals(settingEnum)) {
//    		//如果当前是设备通知 则busId为设备id
//    		Long deviceId = Long.valueOf(busId);
//    		boolean perm = familyDeviceService.isCommonPerm(deviceId, userId);
//    		if(!perm) {
//    			throw new RuntimeException("权限不足");
//    		}
//    		//parentBusId 则为家庭id  如果是空 则报错 理论上不应该报错
//    		DeviceBriefRespInfo info = deviceMapper.selectBriefDeviceByDeviceId(deviceId);
//    		parentBusId = info.getBelongFamilyId().toString();
//    	}else {
//    		if(SmsSettingEnum.DEV_FAMILY.equals(settingEnum)) {
//    			//如果当前是家庭通知 则busId为家庭id
//        		Long familyId = Long.valueOf(busId);
//        		boolean perm = familyService.isCommonPerm(familyId,userId);
//        		if(!perm) {
//        			throw new RuntimeException("权限不足");
//        		}
//    		}
//    		//其他情况 parentBusId均为空
//    		parentBusId = null;
//    	}
    	if(parent!=null) {
    		Integer defaultIsDisabled = 0;//默认是启用
    		parentId = insertCascadeMsgNoticeSetting(parent, parentBusId, defaultIsDisabled, userId);
    	}
    	//先查询,查询没有再新增
    	MsgSmsNoticeSetting query = new MsgSmsNoticeSetting();
    	query.setIdentifier(settingEnum.getIdentifier());
    	query.setBusId(busId);
    	query.setUserId(userId);
    	query.setParentId(parentId);
		List<MsgSmsNoticeSetting> list = msgSmsNoticeSettingMapper.selectMsgSmsNoticeSettingList(query );
		if(list.size()>1) {
			throw new RuntimeException("设置短信消息通知数据有误");
		}else if(list.isEmpty()) {
			//新增
			MsgSmsNoticeSetting setting = new MsgSmsNoticeSetting();
			setting.setIdentifier(settingEnum.getIdentifier());
			setting.setBusId(busId);
			setting.setUserId(userId);
			setting.setIsDisabled(isDisabled);
			setting.setParentId(parentId);
			setting.setSettingName(settingEnum.getIdentifierName());
			setting.setSmsLimitType(1);
			setting.setSmsLimitCount(100);
			setting.setSmsSendedCount(0);
			msgSmsNoticeSettingMapper.insertMsgSmsNoticeSetting(setting);
	    	return setting.getId();
		}else {
			//直接返回
			return list.get(0).getId();
		}
    	
    }
    
    @Override
    public boolean isSendNotice(String identifier,String busId
    		,Long userId) {
    	SmsSettingEnum settingEnum = SmsSettingEnum.selectSettingByIdentifier(identifier);
//    	if(!SmsSettingEnum.DEV_DEVICE.getIdentifier().equals(identifier)
//    			&& !SmsSettingEnum.DEV_FAMILY.getIdentifier().equals(identifier)) {
    		//非设备和家庭的 没有业务id
    		busId = null;
//    	}
    		MsgSmsNoticeSetting msgNoticeSetting = new MsgSmsNoticeSetting();
    	msgNoticeSetting.setIdentifier(identifier);
    	msgNoticeSetting.setBusId(busId);
    	msgNoticeSetting.setUserId(userId);
    	log.info("查询是否进行duanxin通知,参数为:{}",JSONObject.toJSON(msgNoticeSetting));
    	List<MsgSmsNoticeSetting> list = msgSmsNoticeSettingMapper.selectMsgSmsNoticeSettingList(msgNoticeSetting );
    	if(list.size()>1) {
    		log.error("查询出多条数据,数据有误111111111111");
    		throw new RuntimeException("查询出多条数据,数据有误");
    	}else if(list.size()==1) {
    		//查询出结果
    		MsgSmsNoticeSetting setting = list.get(0);
    		return selectIsDisabled(setting)==0;
    	}else {
    		insertCascadeMsgNoticeSetting(settingEnum, busId, 0, userId);//自动创建一个启用的数据 
    		List<MsgSmsNoticeSetting> list2 = msgSmsNoticeSettingMapper.selectMsgSmsNoticeSettingList(msgNoticeSetting );
    		if(list2.size()>1) {
    			log.error("查询出多条数据,数据有误22222222222");
        		throw new RuntimeException("查询出多条数据,数据有误");
        	}else if(list2.size()==1) {
        		//查询出结果
        		MsgSmsNoticeSetting setting = list2.get(0);
        		return selectIsDisabled(setting)==0;
        	}else {
        		log.warn("新增完还没有数据,则不进行通知");
        		//新增完还没有数据,则不进行通知
        		return false;
        	}
    	}
    }
    @Override
    public boolean sendSmsSucProcess(String identifier,String busId
    		,Long userId) {
    	log.info("开始更新短信限制值,userId:{}",userId);
    	SmsSettingEnum settingEnum = SmsSettingEnum.selectSettingByIdentifier(identifier);
//    	if(!SmsSettingEnum.DEV_DEVICE.getIdentifier().equals(identifier)
//    			&& !SmsSettingEnum.DEV_FAMILY.getIdentifier().equals(identifier)) {
    	//非设备和家庭的 没有业务id
    	busId = null;
//    	}
    	MsgSmsNoticeSetting msgNoticeSetting = new MsgSmsNoticeSetting();
    	msgNoticeSetting.setIdentifier(identifier);
    	msgNoticeSetting.setBusId(busId);
    	msgNoticeSetting.setUserId(userId);
    	log.info("当前短信配置查询参数:{}",JSONObject.toJSON(msgNoticeSetting));
    	List<MsgSmsNoticeSetting> list = msgSmsNoticeSettingMapper.selectMsgSmsNoticeSettingList(msgNoticeSetting );
    	if(list.size()>1) {
    		throw new RuntimeException("短信配置查询出多条数据,数据有误");
    	}else if(list.size()==1) {
    		//查询出结果
    		MsgSmsNoticeSetting setting = list.get(0);
    		if(setting.getSmsLimitType().intValue()==1) {
    			int smsSendedCount = setting.getSmsSendedCount()+1;
    			Integer smsLimitCount = setting.getSmsLimitCount();
    			setting.setSmsSendedCount(smsSendedCount);
    			if(smsSendedCount>=smsLimitCount) {
    				setting.setIsDisabled(1);//超过限制条数 禁用
    			}
    			msgSmsNoticeSettingMapper.updateMsgSmsNoticeSetting(setting);
    		}else if(setting.getSmsLimitType().intValue()==2) {
    			//会多发一条
    			//超过有效期直接禁用
    			Date smsLimitDate = setting.getSmsLimitDate();
    			if(smsLimitDate==null || smsLimitDate.before(new Date())) {
    				setting.setIsDisabled(1);//超过限制日期 禁用
    				msgSmsNoticeSettingMapper.updateMsgSmsNoticeSetting(setting);
    			}
    		}else {
    			throw new RuntimeException("不支持的类型,"+setting.getSmsLimitType());
    		}
    		return true;
    	}else {
    		throw new RuntimeException("短信配置未查询到数据,数据有误");
    	}
    }
    
    /**
     * 自底向上查询,只要有一个禁用 则不发送通知
     * @param setting
     * @return
     */
    private int selectIsDisabled(MsgSmsNoticeSetting setting) {
    	int isDis = setting.getIsDisabled().intValue();
    	if(1==isDis) {
    		return 1;
    	}
    	Long parentId = setting.getParentId();
    	if(parentId==null) {
    		//查找到顶层,仍然可推送 则推送
    		return isDis;
    	}
    	MsgSmsNoticeSetting pSetting = msgSmsNoticeSettingMapper.selectMsgSmsNoticeSettingById(parentId);
    	return selectIsDisabled(pSetting);
    	
    }
    
    /**
     * 批量删除短信通知设置
     * 
     * @param ids 需要删除的短信通知设置主键
     * @return 结果
     */
    @Override
    public int deleteMsgSmsNoticeSettingByIds(Long[] ids)
    {
        return msgSmsNoticeSettingMapper.deleteMsgSmsNoticeSettingByIds(ids);
    }

    /**
     * 删除短信通知设置信息
     * 
     * @param id 短信通知设置主键
     * @return 结果
     */
    @Override
    public int deleteMsgSmsNoticeSettingById(Long id)
    {
        return msgSmsNoticeSettingMapper.deleteMsgSmsNoticeSettingById(id);
    }
}
