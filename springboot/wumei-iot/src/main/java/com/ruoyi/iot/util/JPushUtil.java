package com.ruoyi.iot.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.iot.mobile.controller.MobileProfileController;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jiguang.common.resp.DefaultResult;
import cn.jpush.api.JPushClient;
import cn.jpush.api.device.AliasDeviceListResult;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

@Component
public class JPushUtil {
	
    private static final Logger log = LoggerFactory.getLogger(JPushUtil.class);

	@Value("${jpush.appKey}")
    private String appKey;

    @Value("${jpush.masterSecret}")
    private String masterSecret;
    
    private JPushClient jpushClient;
    
    private DeviceClientNew deviceClientNew;

	@Autowired
	public void setJpushClient() {
		this.jpushClient = new JPushClient(masterSecret, appKey);
	}
	@Autowired
	public void setDeviceClientNew() {
		this.deviceClientNew = new DeviceClientNew(masterSecret, appKey);
	}
	
	
	/**
	 * 查询需要清除的设备别名
	 * @param userId
	 * @param limitLogin
	 * @return
	 * @throws Exception
	 */
	public Set<String> queryNeedClearRegistrationIdsByAlias(Long userId,int limitLogin) throws Exception {
		AliasDeviceListResultNew aliasDeviceList = deviceClientNew.getAliasDeviceList(userId+"", null, true);
		List<RegInfo> data = aliasDeviceList.getData();
		if(data.size()<10){
			//小于10个不处理
			return new HashSet();
		}
		RegInfo[] array = data.toArray(new RegInfo[] {});
		Arrays.sort(array, (a,b)->{
			return a.getLast_online_date().compareTo(b.getLast_online_date());
		});
		Set<String> ret = new HashSet<String>(); 
		for(int i=0;i<data.size()-limitLogin;i++) {
			ret.add(array[i].getRegistration_id());
		}
		log.info("需要清除设备id数据:{}",JSONObject.toJSON(ret));
		return ret;
	}
	
//	public static void main(String[] args) throws APIConnectionException, APIRequestException {
//		DeviceClientNew deviceClientNew2 = new DeviceClientNew("1e22b94670096531d996c9dcf5", "462238485d0072af8cd4762cfe");
//		AliasDeviceListResultNew aliasDeviceList = deviceClientNew2.getAliasDeviceList(126+"", null, true);
//		System.out.println(JSONObject.toJSON(aliasDeviceList));
//		List<RegInfo> data = aliasDeviceList.getData();
//		System.out.println(data.size());
//		System.out.println(JSONObject.toJSON(data));
//		for(RegInfo a:data) {
//			System.out.println(JSONObject.toJSON(a));
//
//		}
//	}
	
	/**
	 * 清除registrationId 对应 的 用户id(别名)
	 * @param registrationId
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public boolean clearUserIdToRegistrationIdAlias(String registrationId) throws Exception {
		//清除原用户 别名及标签
		DefaultResult pushResult = jpushClient.updateDeviceTagAlias(registrationId, true,true);
		log.info("清除设备id原别名结果:{},设备id:{}",JSONObject.toJSON(pushResult),registrationId);
		return pushResult.isResultOK();
	}
	/**
	 * 清除registrationId 对应 的 用户id(别名)
	 * @param registrationId
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public boolean clearUserIdToRegistrationIdAlias(Long userId,Set<String> registrationIds) throws Exception {
		//清除原用户 别名及标签
		DefaultResult pushResult = jpushClient.removeDevicesFromAlias(userId+"", registrationIds);
		log.info("清除设备id原别名结果:{},设备id:{}",JSONObject.toJSON(pushResult),JSONObject.toJSON(registrationIds));
		return pushResult.isResultOK();
	}
	/**
	 * 更新registrationId 对应 的 用户id(别名)
	 * @param registrationId
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public boolean updateUserIdToRegistrationIdAlias(String registrationId,Long userId) throws Exception {
		//清除原用户 别名及标签
		DefaultResult pushResult = jpushClient.updateDeviceTagAlias(registrationId, true,true);
		log.info("清除设备id原别名结果:{},设备id:{},用户id{}",JSONObject.toJSON(pushResult),registrationId,userId);
		if(pushResult.isResultOK()) {
			//设置新用户id 为 RegistrationId别名
			boolean setUserIdToRegistrationIdAlias = setUserIdToRegistrationIdAlias(registrationId, userId);
			return setUserIdToRegistrationIdAlias;
		}
		return false;
	}
	
	/**
	 * 设置registrationId 对应 的 用户id(别名)
	 * @param registrationId
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public boolean setUserIdToRegistrationIdAlias(String registrationId,Long userId) throws Exception {
		DefaultResult pushResult = jpushClient.updateDeviceTagAlias(registrationId, userId+"", null, null);
		log.info("设置设备id新别名结果:{},设备id:{},用户id{}",JSONObject.toJSON(pushResult),registrationId,userId);
//		System.out.println(JSONObject.toJSON(pushResult));
		return pushResult.isResultOK();
	}
	
	/**
	 * 推送message到指定设备(不通知)
	 * @param registrationId
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public boolean sendMessageToRegistrationId(String registrationId,String message,Map<String,String> extras) throws Exception{		
		
		if(extras==null){
			extras = new HashMap<String,String>();
		}
		PushPayload pushPayload = PushPayload.newBuilder()
        .setPlatform(Platform.all())
        .setAudience(Audience.newBuilder()
                .addAudienceTarget(AudienceTarget.registrationId(registrationId))
                .build())
        .setMessage(Message.newBuilder()
                .setMsgContent(message)
                .addExtras(extras)
                .build())
        .build();
		PushResult pushResult = jpushClient.sendPush(pushPayload);
		return true;
	}
	
	/**
	 * 推送message到指定设备(不通知)
	 * @param registrationId
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public boolean sendMessageToMemberId(Long userId,String message,Map<String,String> extras) throws Exception{		
		
		if(extras==null){
			extras = new HashMap<String,String>();
		}
		PushPayload pushPayload = PushPayload.newBuilder()
				.setPlatform(Platform.all())
				.setAudience(Audience.newBuilder()
						.addAudienceTarget(AudienceTarget.alias(userId+""))
						.build())
				.setMessage(Message.newBuilder()
						.setMsgContent(message)
						.addExtras(extras)
						.build())
				.build();
		PushResult pushResult = jpushClient.sendPush(pushPayload);
		return true;
	}
	
	/**
	 * 推送通知以及消息
	 * @param registrationId
	 * @param alert
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public boolean sendAlertAndMessageToRegistrationId(String registrationId,String alert,String message,Map<String,String> extras) throws Exception{
		
		if(extras==null){
			extras = new HashMap<String,String>();
		}
		PushPayload pushPayload = PushPayload.newBuilder()
				.setPlatform(Platform.all())
				.setAudience(Audience.newBuilder()
						.addAudienceTarget(AudienceTarget.registrationId(registrationId))
						.build())
				.setNotification(Notification.newBuilder()
						.addPlatformNotification(AndroidNotification.newBuilder().setAlert(alert).addExtras(extras).build())
						.addPlatformNotification(IosNotification.newBuilder().setAlert(alert).addExtras(extras).build())
						.build())
				.setMessage(Message.newBuilder()
						.setMsgContent(message)
						.addExtras(extras)
						.build())
				.build();
		PushResult pushResult = jpushClient.sendPush(pushPayload);
		return true;
	}
	
	/**
	 * 推送通知以及消息
	 * @param registrationId
	 * @param alert
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public boolean sendAlertAndMessageToMemberId(Long userId,String alert,String message,Map<String,String> extras) throws Exception{
		
		if(extras==null){
			extras = new HashMap<String,String>();
		}
		PushPayload pushPayload = PushPayload.newBuilder()
				.setPlatform(Platform.all())
				.setAudience(Audience.newBuilder()
						.addAudienceTarget(AudienceTarget.alias(userId+""))
						.build())
				.setNotification(Notification.newBuilder()
						.addPlatformNotification(AndroidNotification.newBuilder().setAlert(alert).addExtras(extras).build())
						.addPlatformNotification(IosNotification.newBuilder().setAlert(alert).addExtras(extras).build())
						.build())
				.setMessage(Message.newBuilder()
						.setMsgContent(message)
						.addExtras(extras)
						.build())
				.build();
		PushResult pushResult = jpushClient.sendPush(pushPayload);
		return true;
	}
	
}
