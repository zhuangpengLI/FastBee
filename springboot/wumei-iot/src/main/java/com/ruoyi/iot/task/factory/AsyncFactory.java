package com.ruoyi.iot.task.factory;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.LogUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ip.AddressUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.iot.domain.AlertLog;
import com.ruoyi.iot.domain.Device;
import com.ruoyi.iot.domain.DeviceLog;
import com.ruoyi.iot.domain.Family;
import com.ruoyi.iot.domain.FamilyUserRela;
import com.ruoyi.iot.service.IFamilyService;
import com.ruoyi.system.MsgTypeConstant;
import com.ruoyi.system.domain.SysLogininfor;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.domain.SysOperLog;
import com.ruoyi.system.otherDomain.Msg;
import com.ruoyi.system.otherDto.MsgSettingEnum;
import com.ruoyi.system.otherDto.SysUserWithFamilyStat;
import com.ruoyi.system.otherService.IMsgService;
import com.ruoyi.system.service.ISysLogininforService;
import com.ruoyi.system.service.ISysNoticeService;
import com.ruoyi.system.service.ISysOperLogService;
import com.ruoyi.system.service.ISysUserService;

import eu.bitwalker.useragentutils.UserAgent;

/**
 * 异步工厂（产生任务用）
 * 
 * @author ruoyi
 */
public class AsyncFactory
{
    private static final Logger sys_user_logger = LoggerFactory.getLogger("sys-user");
    private static final Logger sys_info_logger = LoggerFactory.getLogger("sys-info");

    public static TimerTask deviceSendOfflineTimerMsg(final Device device,final String randomUUID)
    {
    	return new TimerTask()
    	{
    		@Override
    		public void run()
    		{
    			
    			sys_info_logger.info("网关离线发送通知和消息 网关sn:{}",device.getSerialNumber());
    			RedisCache redisCache = SpringUtils.getBean(RedisCache.class);
    			String send = redisCache.getCacheObject(Constants.GW_OFFLINE_NOTICE_KEY+device.getSerialNumber());
    			if(send==null) {
    				sys_info_logger.info("本次不发送  离线时间不超过15分钟");
    				return;
    			}else {
    				if(!randomUUID.equals(send)) {
    					//如果不相等,则说明已经上线过.本次离线时间不超过15分钟
    					sys_info_logger.info("本次不发送  本次离线时间不超过15分钟(有新的离线定时任务)");
        				return;
    				}
    				//发送消息 并删除该离线键
    				redisCache.deleteObject(Constants.GW_OFFLINE_NOTICE_KEY+device.getSerialNumber());
    			}
    			IFamilyService familyService = SpringUtils.getBean(IFamilyService.class);
//    			ISysNoticeService noticeService = SpringUtils.getBean(ISysNoticeService.class);
//    			ISysUserService userService = SpringUtils.getBean(ISysUserService.class);
    			IMsgService msgService = SpringUtils.getBean(IMsgService.class);
    			
    			// 添加到设备日志
    	    	DeviceLog deviceLog = new DeviceLog();
    	    	deviceLog.setDeviceId(device.getDeviceId());
    	    	deviceLog.setDeviceName(device.getDeviceName());
    	    	deviceLog.setSerialNumber(device.getSerialNumber());
    	    	deviceLog.setIsMonitor(0);
    	    	deviceLog.setUserId(device.getUserId());
    	    	deviceLog.setUserName(device.getUserName());
    	    	deviceLog.setTenantId(device.getTenantId());
    	    	deviceLog.setTenantName(device.getTenantName());
    	    	deviceLog.setCreateTime(DateUtils.getNowDate());
    	    	// 日志模式 1=影子模式，2=在线模式，3=其他
    	    	deviceLog.setMode(3);
    	    	
    	    	deviceLog.setLogValue("0");
        		deviceLog.setRemark("设备离线");
        		deviceLog.setIdentity("offline");
        		deviceLog.setLogType(6);
        		
    			//在 znjj 电气中离线需要报警
        		Long userId = null;
    			Long roomId = null;
    			Long deviceId = null;
    			Long familyId = device.getBelongFamilyId();
    			String familyName = "";
    			if(familyId!=null) {
    				Family family = familyService.selectFamilyOnlyByFamilyId(familyId);
    				userId = family.getBelongUserId();
    				familyName = family.getName();
    			}
        		AlertLog alertLog = new AlertLog();
    			alertLog.setAlertName(deviceLog.getRemark());//"设备离线"
    			alertLog.setAlertLevel(98L);
    			alertLog.setStatus(2L);//待处理
    			alertLog.setAlertCat(1);
    			alertLog.setFamilyId(device.getBelongFamilyId());
    			alertLog.setUserId(userId);//userid保存为创建者的userId
    			alertLog.setAlertTime(new Date());
    			alertLog.setProductId(device.getProductId());
    			alertLog.setProductName(device.getProductName());
    			alertLog.setDeviceId(device.getDeviceId());
    			alertLog.setDeviceName(device.getDeviceName());
    			
    			roomId = device.getBelongRoomId();
    			deviceId = device.getDeviceId();
    			boolean isPush = true;
    			boolean isSms = true;
    			boolean isCall = false;
    			if(familyId!=null) {
    				String msgContent = "";
    				msgContent+=alertLog.getDeviceName();
    				msgContent+=alertLog.getAlertName();
    				List<FamilyUserRela> list = familyService.selectUserListByFamilyIdAndUserId(familyId,null);
    				//通知到所有家庭用户
    				for (int i = 0; i < list.size(); i++) {
    					FamilyUserRela familyUserRela = list.get(i);
    					Long uId = familyUserRela.getUserId();
    					String phone = familyUserRela.getPhonenumber();
    					Map<String,String> smsParam = new HashMap<String,String>();
    					if(isSms) {
    						smsParam.put("smsType", "ALERT");
    						smsParam.put("phone", phone);
    						smsParam.put("deviceName", alertLog.getDeviceName()+"("+familyName+")");
    						smsParam.put("alertInfo", alertLog.getAlertName());
    					}
    					msgService.sendMsg(new Msg("设备离线",MsgTypeConstant.MSG_TYPE_FAMILY_MSG,msgContent,null,
    							familyId,roomId,deviceId,uId,null), isPush, isSms, isCall,MsgSettingEnum.DEV_DEVICE.getIdentifier(),
    							device.getDeviceId()+"", smsParam);
    				}
    			}
    		}
    	};
    }
}
