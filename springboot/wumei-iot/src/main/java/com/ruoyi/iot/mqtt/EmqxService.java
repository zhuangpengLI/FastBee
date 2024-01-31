package com.ruoyi.iot.mqtt;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.AjaxResult;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.iot.config.MqAjaxResult;
import com.ruoyi.iot.config.MyWebSocketHandler;
import com.ruoyi.iot.domain.AlertLog;
import com.ruoyi.iot.domain.Device;
import com.ruoyi.iot.domain.DeviceLog;
import com.ruoyi.iot.domain.Family;
import com.ruoyi.iot.domain.FamilyUserRela;
import com.ruoyi.iot.dto.BoardAirSwitch;
import com.ruoyi.iot.mobile.constant.IotConstant;
import com.ruoyi.iot.mobile.constant.IotOperMsgConstant;
import com.ruoyi.iot.mobile.respModel.DeviceBriefRespInfo;
import com.ruoyi.iot.mobile.service.IFamilyDeviceService;
import com.ruoyi.iot.mobile.transferModel.MessageAndResponseTransfer;
import com.ruoyi.iot.model.NtpModel;
import com.ruoyi.iot.model.ThingsModels.IdentityAndName;
import com.ruoyi.iot.model.ThingsModels.ThingsModelValueItem;
import com.ruoyi.iot.model.ThingsModels.ThingsModelValueRemarkItem;
import com.ruoyi.iot.model.ThingsModels.ThingsModelValuesInput;
import com.ruoyi.iot.service.IAlertLogService;
import com.ruoyi.iot.service.IDeviceOtaUpdateService;
import com.ruoyi.iot.service.IDeviceService;
import com.ruoyi.iot.service.IFamilyService;
import com.ruoyi.iot.service.impl.ThingsModelServiceImpl;
import com.ruoyi.iot.tdengine.service.ILogService;
import com.ruoyi.system.MsgTypeConstant;
import com.ruoyi.system.otherDomain.Msg;
import com.ruoyi.system.otherDto.MsgSettingEnum;
import com.ruoyi.system.otherService.IMsgService;

@Service
public class EmqxService {
    private static final Logger logger = LoggerFactory.getLogger(EmqxService.class);

    @Autowired
    private EmqxClient emqxClient;

    @Autowired
    private IDeviceService deviceService;

    @Autowired
    private ILogService logService;
    @Autowired
    private MessageAndResponseTransfer transfer;
    @Autowired
    private IFamilyDeviceService familyDeviceService;
    @Autowired
    private IFamilyService familyService;
    @Autowired
    private IAlertLogService alertLogService;
    @Autowired
    private IMsgService msgService;
    @Autowired
    private ThingsModelServiceImpl thingsModelService;
    @Autowired
    private IDeviceOtaUpdateService deviceOtaUpdateService;
//    @Autowired
//    private IDboxSwitchService dboxSwitchService;

    /**
     * 订阅的wumeismart主题
     */
    private static final String prefix = "/+/+/";
    String sInfoTopic = prefix + "info/post";
    String sNtpTopic = prefix + "ntp/post";
    String sPropertyTopic = prefix + "property/post";
    String sFunctionTopic = prefix + "function/post";
    String sEventTopic = prefix + "event/post";
    String sShadowPropertyTopic = prefix + "property-offline/post";
    String sShadowFunctionTopic = prefix + "function-offline/post";
    
    /**
     * 订阅的znjj主题
     */
    private static final String ZNJJ_SUFFIX = "+";
    private static final String ZNJJ_GATEWAY_PREFIX = "/dbox/up/";
    private static final String ZNJJ_APP_PREFIX = "/app/down/";
    private static final String ZNJJ_GATEWAY_TOPIC = ZNJJ_GATEWAY_PREFIX + ZNJJ_SUFFIX;
    private static final String ZNJJ_APP_TOPIC = ZNJJ_APP_PREFIX + ZNJJ_SUFFIX;

    /**
     * 发布的wumeismart主题
     */
    String pStatusTopic = "/status/post";
    String pInfoTopic = "/info/get";
    String pNtpTopic = "/ntp/get";
    String pPropertyTopic = "/property/get";
    String pFunctionTopic = "/function/get";
    
    public static final Map<String,Boolean> deviceMsgMap = new HashMap<String,Boolean>();
    public static final String MSG_WUMEI_TYPE = "wumei";
    //当前系统
    public static final String MSG_ZNJJ_TYPE = "znjj";
    
    @PostConstruct
    public void initDeviceMsgType() {
    	//初始化厂商Map 看 订阅哪些消息和处理那些消息
    	deviceMsgMap.put(MSG_WUMEI_TYPE, false);
    	deviceMsgMap.put(MSG_ZNJJ_TYPE, true);
//    	deviceMsgMap.put("aaa", false);
    	//暂时只能开启一个 否则消息混乱无法处理
    	boolean onlyTure = false;
    	for(String key:deviceMsgMap.keySet()) {
    		if(onlyTure && deviceMsgMap.get(key)) {
    			// 超过一个类型订阅则退出系统  除非确保消息不会混乱 需要自己调整逻辑   		
    			logger.error("大于一个厂商的设备订阅,数据会错乱,退出系统");
    			System.exit(0);
    		}else {
    			if(!onlyTure) {
    				onlyTure = deviceMsgMap.get(key);
    			}
    		}
    	}
    }

    public void subscribe(MqttAsyncClient client) throws MqttException {
    	//订阅wumei消息
    	if(deviceMsgMap.get(MSG_WUMEI_TYPE)) {
    		//订阅设备信息
	        client.subscribe(sInfoTopic, 1);
	        // 订阅时钟同步
	        client.subscribe(sNtpTopic, 1);
	        // 订阅设备属性
	        client.subscribe(sPropertyTopic, 1);
	        // 订阅设备功能
	        client.subscribe(sFunctionTopic, 1);
	        // 订阅设备事件
	        client.subscribe(sEventTopic, 1);
	        // 订阅属性（影子模式）
	        client.subscribe(sShadowPropertyTopic, 1);
	        // 订阅功能（影子模式）
	        client.subscribe(sShadowFunctionTopic, 1);
	        
	        logger.info("mqtt订阅了  wumei   设备信息和物模型主题");
    	}
        
    	//订阅znjj消息
    	if(deviceMsgMap.get(MSG_ZNJJ_TYPE)) {
    		client.subscribe(ZNJJ_GATEWAY_TOPIC, 1);
    		//不用订阅app操作 topic 均由服务器端代理发送 app不直连mq服务器
    		logger.info("mqtt订阅了   znjj    网关消息和app消息主题");
    	}
    }

    /**
     * 消息回调方法
     * @param topic  主题
     * @param mqttMessage 消息体
     */
    @Async
    public void subscribeCallback(String topic, MqttMessage mqttMessage) throws InterruptedException {

        /**测试线程池使用*/
        logger.info("====>>>>线程名--{}",Thread.currentThread().getName());
        /**模拟耗时操作*/
        // Thread.sleep(1000);
        // subscribe后得到的消息会执行到这里面
        String message = new String(mqttMessage.getPayload());
        logger.info("接收消息主题 : " + topic);
        logger.info("接收消息Qos : " + mqttMessage.getQos());
        logger.info("接收消息内容 : " + message);

        if(deviceMsgMap.get(MSG_ZNJJ_TYPE)) {
        	processSmartMessage(topic, message);
        }
        //处理wumei消息
        if(deviceMsgMap.get(MSG_WUMEI_TYPE)) {
        	processWumeiMessage(topic, message);
        }
    }

	private void processSmartMessage(String topic, String message) {
		if(topic.startsWith(ZNJJ_GATEWAY_PREFIX)){
        	logger.info("接收到 znjj 网关  消息: ");
        	//TODO
        	processGatewayMsg(topic.substring(topic.lastIndexOf("/")+1),message);
//        	return;
        }else if(topic.startsWith(ZNJJ_APP_PREFIX)){
        	logger.info("接收到 znjj app  消息: ");
        	//TODO
//        	return;
        }else{
        	logger.info("接收到 其他订阅  消息: ");
        }
	}


    /**
     * 处理znjj 网关消息
     * @param gatewaySn
     * @param message
     */
    private void processGatewayMsg(String gatewaySn, String message) {
		// TODO Auto-generated method stub
    	logger.info("gatewaySn: {}",gatewaySn);
    	JSONObject msg  = JSONObject.parseObject(message);
    	String fun = msg.getString("fun");
    	String au = msg.getString("au");

		//订阅之前 或者 判断当前客户端之前 先判断解绑情况
		if(IotOperMsgConstant.APP_REQ_GW_FUN_JIEBANG.equals(fun)){
			logger.info("收到网关解绑信息");
			//TODO 解绑
			Device gateway = deviceService.selectDeviceBySerialNumber(gatewaySn);
			boolean isSuccess = false;
			if(gateway==null){
				//找不到网关代表解绑成功
				isSuccess = true;
			}else{
				try {
					AjaxResult a = familyDeviceService.unBindGateway(gateway.getDeviceId());
					logger.info("解绑返回结果:{}",a.get(AjaxResult.CODE_TAG));
					if(a.get(AjaxResult.CODE_TAG).equals(HttpStatus.SUCCESS)){
						isSuccess = true;
					}else{
						isSuccess = false;
					}
				} catch (SchedulerException e) {
					isSuccess = false;
				}
			}
			logger.info("当前最终结果:{}",isSuccess);
			if(isSuccess){
				//发布一个模拟app
				String topic = IotConstant.ZNJJ_APP_PREFIX +gatewaySn;
				JSONObject pushMessage = new JSONObject();
				pushMessage.put("au", transfer.getRandomAuInfo());//随机au  不处理相关消息
				pushMessage.put("fun",IotOperMsgConstant.APP_REQ_GW_FUN_JIEBANG);
				pushMessage.put("code",0);
				pushMessage.put("info","ok");
				emqxClient.publish(1, false, topic, pushMessage.toString());
			}
			return;
		}

    	if(MyWebSocketHandler.auList.get(au)==null) {
    		logger.info("非本客户端触发的消息,不用处理");
    		return;
    	}
    	if(au.equals(transfer.getAuInfo())) {
			//系统代理收到响应
    		Integer code = msg.getInteger("code");
			logger.info("系统代理请求收到响应 code:{}",code);
			 if(code==null) {
				 //code为空 说明 请求不需要code   理论上同步代理需要结果  都不会为空
				 code = 1;
			 }
			transfer.setOperFunResp(gatewaySn, fun,msg);
			logger.info("同步代理已返回结果,无需透传继续处理,需要发起同步代理的业务进行回调处理");
			return ;
		}
    	switch (fun) {
    	
	    	//获取到网关升级结果  代理模拟同步返回 走不到这里
//	    	case IotOperMsgConstant.GW_REQ_GW_FUN_OTADONE:
//	    	{
//	    		//网关状态请求 则返回网关状态   设备详细状态请求则返回设备状态 TODO
//				int code = msg.getIntValue("code");
//				Device device = deviceService.selectDeviceBySerialNumber(gatewaySn);
//				if(device!=null) {
//					DeviceOtaUpdate up = new DeviceOtaUpdate();
//					up.setDeviceId(device.getDeviceId());
//					up.setStatus(0);//处理中的数据
//					List<DeviceOtaUpdate> list = deviceOtaUpdateService.selectDeviceOtaUpdateList(up );
//					if(list.isEmpty()) {
//						logger.info("根据升级结果未查询到正在升级的记录信息");
//					}else {
//						list.forEach(a->{
//							if(code==1) {
//								//成功 
//								a.setStatus(1);//成功
//							}else {
//								//失败
//								a.setStatus(2);//失败
//							}
//							deviceOtaUpdateService.updateDeviceOtaUpdate(a);
//						});
//					}
//				}else {
//					logger.info("根据升级结果未查询到设备信息");
//				}
//	    		
//	    	}
//	    	break;
	    	//获取到网关事件简报结果
	    	case IotOperMsgConstant.GW_REQ_CS_FUN_EVENTBREF:
	    	{
	    		JSONArray devAmCt = msg.getJSONArray("devAmCt");
	    		//取数组第二个值  智能家居终端：int[0]=总报警数量 int[1]=CS未对报警确认的数量
	    		int devAmCt1 = devAmCt.getIntValue(1);
	    		JSONArray devFuCt = msg.getJSONArray("devFuCt");
	    		//取数组第二个值  智能家居终端：int[0]=总故障数量 int[1]=CS未对故障确认的数量
	    		int devFuCt1 = devFuCt.getIntValue(1);
	    		JSONArray kkAmCt = msg.getJSONArray("kkAmCt");
	    		//取数组第二个值  空开配电设备：int[0]=总报警数量 int[1]=CS未对报警确认的数量
	    		int kkAmCt1 = kkAmCt.getIntValue(1);
	    		JSONArray jcbFuCt = msg.getJSONArray("jcbFuCt");
	    		//取数组第二个值  配电设备的监测板：int[0]=总故障数量 int[1]=CS未对故障确认的数量
	    		int jcbFuCt1 = jcbFuCt.getIntValue(1);
	    		
	    		if((devAmCt1+devFuCt1+kkAmCt1+jcbFuCt1)>0) {
	    			//存在cs未确认的故障或报警  需要主动请求网关获取数据
	    			logger.info("存在cs未确认的故障或报警  需要主动请求网关获取数据:{},{},{},{}",devAmCt1,devFuCt1,kkAmCt1,jcbFuCt1);
	    			//发布一个模拟app  进入网关查找设备模式
	    	    	String topic = IotConstant.ZNJJ_APP_PREFIX +gatewaySn;
	    	    	JSONObject pushMessage = new JSONObject();
	    	    	pushMessage.put("au", au);
	    	    	pushMessage.put("fun",IotOperMsgConstant.CS_REQ_GW_FUN_CSREQEVENTS);
	    	    	emqxClient.publish(1, false, topic, pushMessage.toString());
	    		}
	    		
	    	}
	    	break;
	    	//获取到请求网关事件简报结果
	    	case IotOperMsgConstant.CS_REQ_GW_FUN_CSREQEVENTS:
	    	{
	    		Device gateway = deviceService.selectDeviceBySerialNumber(gatewaySn);
	    		Long familyId = gateway.getBelongFamilyId();
	    		JSONArray devEvents = msg.getJSONArray("devEvents");
	    		JSONArray kkEvents = msg.getJSONArray("kkEvents");
	    		JSONArray jcbEvents = msg.getJSONArray("jcbEvents");
	    		JSONArray devCfm = new JSONArray();
	    		JSONArray kkCfm = new JSONArray();
	    		JSONArray jcbCfm = new JSONArray();
	    		if(devEvents!=null) {
	    			processDevEvents(familyId, devEvents,gatewaySn);
	    			devEvents.forEach(dev->{
	    				JSONObject o = (JSONObject)dev;
	    				devCfm.add(o.getString("sn"));
	    			});
	    		}
	    		if(kkEvents!=null) {
	    			processKkEvents(familyId, kkEvents,gateway);
	    			kkEvents.forEach(dev->{
	    				JSONObject o = (JSONObject)dev;
	    				kkCfm.add(o.getIntValue("id"));
	    			});
	    		}
	    		if(jcbEvents!=null) {
	    			processJcbEvents(familyId, jcbEvents,gateway);
	    			jcbEvents.forEach(dev->{
	    				JSONObject o = (JSONObject)dev;
	    				jcbCfm.add(o.getIntValue("adr"));
	    			});
	    		}
	    		//向网关发送确认事件
    			logger.info("向网关发送确认事件");
    			//发布一个模拟app
    	    	String topic = IotConstant.ZNJJ_APP_PREFIX +gatewaySn;
    	    	JSONObject pushMessage = new JSONObject();
    	    	pushMessage.put("au", transfer.getRandomAuInfo());//随机au  不处理相关消息
    	    	pushMessage.put("fun",IotOperMsgConstant.CS_REQ_GW_FUN_CSCFM);
    	    	pushMessage.put("devCfm",devCfm);
    	    	pushMessage.put("kkCfm",kkCfm);
    	    	pushMessage.put("jcbCfm",jcbCfm);
    	    	emqxClient.publish(1, false, topic, pushMessage.toString());
	    		
	    		
	    	}
	    	//获取到网关推送事件
	    	case IotOperMsgConstant.GW_REQ_CS_FUN_EVENTPOP:
	    	{
	    		Device gateway = deviceService.selectDeviceBySerialNumber(gatewaySn);
	    		Long familyId = gateway.getBelongFamilyId();
	    		JSONArray devEvents = msg.getJSONArray("devEvents");
	    		JSONArray kkEvents = msg.getJSONArray("kkEvents");
	    		JSONArray jcbEvents = msg.getJSONArray("jcbEvents");
	    		JSONArray devCfm = new JSONArray();
	    		JSONArray kkCfm = new JSONArray();
	    		JSONArray jcbCfm = new JSONArray();
	    		if(devEvents!=null) {
	    			processDevEvents(familyId, devEvents,gatewaySn);
	    			devEvents.forEach(dev->{
	    				JSONObject o = (JSONObject)dev;
	    				devCfm.add(o.getString("sn"));
	    			});
	    		}
	    		if(kkEvents!=null) {
	    			processKkEvents(familyId, kkEvents,gateway);
	    			kkEvents.forEach(dev->{
	    				JSONObject o = (JSONObject)dev;
	    				kkCfm.add(o.getIntValue("id"));
	    			});
	    		}
	    		if(jcbEvents!=null) {
	    			processJcbEvents(familyId, jcbEvents,gateway);
	    			jcbEvents.forEach(dev->{
	    				JSONObject o = (JSONObject)dev;
	    				jcbCfm.add(o.getIntValue("adr"));
	    			});
	    		}
	    		//向网关发送确认事件
    			logger.info("向网关发送确认事件");
    			//发布一个模拟app
    	    	String topic = IotConstant.ZNJJ_APP_PREFIX +gatewaySn;
    	    	JSONObject pushMessage = new JSONObject();
    	    	pushMessage.put("au", transfer.getRandomAuInfo());//随机au  不处理相关消息
    	    	pushMessage.put("fun",IotOperMsgConstant.CS_REQ_GW_FUN_CSCFM);
    	    	pushMessage.put("devCfm",devCfm);
    	    	pushMessage.put("kkCfm",kkCfm);
    	    	pushMessage.put("jcbCfm",jcbCfm);
    	    	emqxClient.publish(1, false, topic, pushMessage.toString());
	    		
	    	}
	    	break;
				//获取到所有设备结果
			case IotOperMsgConstant.APP_REQ_GW_FUN_GETDEVSRSCT:
			{
//				TODO
			}
			break;
			//获取到所有设备结果
			case IotOperMsgConstant.APP_REQ_GW_FUN_GETHSV:
			{
//				获取网关软硬件版本
				logger.info("网关软硬件版本,au:{}",au);
				int code = msg.getIntValue("code");
			}
			break;
			//获取到所有设备结果
			case IotOperMsgConstant.APP_REQ_GW_FUN_GETDEVSRT:
			{
				//网关状态请求 则返回网关状态   设备详细状态请求则返回设备状态 TODO
				int code = msg.getIntValue("code");
				logger.info("获取到所有设备结果2,当前au为:{},code为:{},fun:{}",au,code,IotOperMsgConstant.APP_REQ_GW_FUN_GETDEVSRT);
				if(code ==1 ) {
					JSONArray jsonArray = msg.getJSONArray("devs");
					//设备状态更新
					List<DeviceBriefRespInfo> deviceList = new ArrayList<DeviceBriefRespInfo>();
					for(Object o:jsonArray) {
						JSONObject deviceInfo = (JSONObject)o;
						String sn = deviceInfo.getString("sn");
						try {
							//保证幂等性
							familyDeviceService.insertDevice(sn, null, gatewaySn);
						} catch (Exception e) {
							//失败 代表别的服务器添加成功
							e.printStackTrace();
						}
						DeviceBriefRespInfo device = updateThingsModel(deviceInfo);
						deviceList.add(device);
					}
					logger.info("mq消息返回前 mq size{},func:{}",MyWebSocketHandler.mq.size(),fun);
					MyWebSocketHandler.mq.add(MqAjaxResult.success(deviceList, fun, au, gatewaySn));
					logger.info("mq消息返回后 mq size{},func:{}",MyWebSocketHandler.mq.size(),fun);
					// 查询完设备状态后
				}else {
					logger.info("mq消息返回前 mq size{},func:{}",MyWebSocketHandler.mq.size(),fun);
					MyWebSocketHandler.mq.add(MqAjaxResult.error("查询失败", fun, au, gatewaySn));
					logger.info("mq消息返回后 mq size{},func:{}",MyWebSocketHandler.mq.size(),fun);

				}
			}
			break;
			
			//获取到设备修改结果  网关到 --  设备
		case IotOperMsgConstant.GW_REQ_APP_FUN_CHANGEDEVS:
			{
//				int code = msg.getIntValue("code");
//				logger.info("获取到设备修改结果,当前au为:{},code为:{}",au,code);
//				if(code ==1 ) {
					JSONArray jsonArray = msg.getJSONArray("devs");
					//设备状态更新
//					List<Device> deviceList = new ArrayList<Device>();
					for(Object o:jsonArray) {
						JSONObject deviceInfo = (JSONObject)o;
						
						//另起线程处理对象
						JSONObject parse = (JSONObject) JSONObject.parse(deviceInfo.toJSONString());
						
						int line = deviceInfo.getIntValue("line");
						deviceInfo.put("status",(line==1?3:4));//3在线 4离线
						String sn = deviceInfo.getString("sn");
						String deviceModelType = sn.substring(0, 4);
						deviceInfo.put("serialNumber",sn);
						convertValue(deviceModelType, deviceInfo);
//						deviceInfo.put("value", jsonArray);
						new Thread(()->{
							//更新模型太慢 需要先返回结果 再更新数据库
							DeviceBriefRespInfo device = updateThingsModel(parse);
						}).start();;
//						deviceList.add(device);
					}
					logger.info("mq消息返回前 mq size{},func:{}",MyWebSocketHandler.mq.size(),fun);
					MyWebSocketHandler.mq.add(MqAjaxResult.success(jsonArray, fun, au, gatewaySn));
					logger.info("mq消息返回后 mq size{},func:{}",MyWebSocketHandler.mq.size(),fun);

				// 查询完设备状态后
//				}
			}
			break;
			
			//获取到设备修改结果  网关到 --  设备
		case IotOperMsgConstant.GW_REQ_APP_FUN_CHANGEDEVS2:
		{
//			TODO
		}
		break;
		
			//获取到进入添加设备模式结果
		case IotOperMsgConstant.APP_REQ_GW_FUN_ENTERADDDEVENV:
			{
				int code = msg.getIntValue("code");
				logger.info("获取到添加设备模式结果,当前au为:{},code为:{}",au,code);
				if(code==1) {
					logger.info("mq消息返回前 mq size{},func:{}",MyWebSocketHandler.mq.size(),fun);
					MyWebSocketHandler.mq.add(MqAjaxResult.success(null, fun, au, gatewaySn));
					logger.info("mq消息返回后 mq size{},func:{}",MyWebSocketHandler.mq.size(),fun);

					logger.info("开启添加设备模式成功,gwsn:{}",gatewaySn);
				}else {
					logger.info("mq消息返回前 mq size{},func:{}",MyWebSocketHandler.mq.size(),fun);
					MyWebSocketHandler.mq.add(MqAjaxResult.error("操作失败", fun, au, gatewaySn));
					logger.info("mq消息返回后 mq size{},func:{}",MyWebSocketHandler.mq.size(),fun);

					logger.error("开启添加设备模式失败,gwsn:{}",gatewaySn);
				}
			}
			break;
			//获取到退出添加设备模式结果
		case IotOperMsgConstant.APP_REQ_GW_FUN_EXITADDDEVENV:
			{
				int code = msg.getIntValue("code");
				logger.info("获取到退出设备模式结果,当前au为:{},code为:{}",au,code);
				if(code==1) {
					logger.info("mq消息返回前 mq size{},func:{}",MyWebSocketHandler.mq.size(),fun);
					MyWebSocketHandler.mq.add(MqAjaxResult.success(null, fun, au, gatewaySn));
					logger.info("mq消息返回后 mq size{},func:{}",MyWebSocketHandler.mq.size(),fun);

					logger.info("退出添加设备模式成功,gwsn:{}",gatewaySn);
				}else {
					logger.info("mq消息返回前 mq size{},func:{}",MyWebSocketHandler.mq.size(),fun);
					MyWebSocketHandler.mq.add(MqAjaxResult.error("操作失败", fun, au, gatewaySn));
					logger.info("mq消息返回后 mq size{},func:{}",MyWebSocketHandler.mq.size(),fun);

					logger.error("退出添加设备模式失败,gwsn:{}",gatewaySn);
				}
			}
			break;
		//获取到查找到设备结果
		case IotOperMsgConstant.GW_REQ_APP_FUN_FINDDEV:
			{
//				int code = msg.getIntValue("code");
				//没有code 收到消息代表查找到
				JSONObject dev = msg.getJSONObject("dev");
				String sn = dev.getString("sn");
				logger.info("获取到查找到设备结果,当前au为:{},sn为:{}",au,sn);
				//成功处理
				//au为dbox 每个业务服务器都会处理
				DeviceBriefRespInfo device = null;
				device = familyDeviceService.selectBriefDeviceBySerialNumber(sn);
				Integer isNew = 1;
				if(device!=null) {
					isNew = 0;
				}
				int insertDevice = 0;
				try {
					insertDevice = familyDeviceService.insertDevice(sn, null, gatewaySn);
				} catch (Exception e) {
					//失败 代表别的服务器添加成功
					e.printStackTrace();
				}
				if(insertDevice>0) {
					//添加成功后更新产品物模型
					device = updateThingsModel(dev);
				}else {
					//添加失败的直接查询即可
					device = familyDeviceService.selectBriefDeviceBySerialNumber(sn);
				}
				if(device!=null) {
					device.setIsNew(isNew);
				}
				//无论哪个业务服务器处理成功 都需要给所有
				logger.info("mq消息返回前 mq size{},func:{}",MyWebSocketHandler.mq.size(),fun);
				MyWebSocketHandler.mq.add(MqAjaxResult.success(device, fun, au, gatewaySn));
				logger.info("mq消息返回后 mq size{},func:{}",MyWebSocketHandler.mq.size(),fun);

			}
			break;
			
			//获取到删除查找到设备结果
		case IotOperMsgConstant.APP_REQ_GW_FUN_DELDEV:
		{
			int code = msg.getIntValue("code");
			String sn = msg.getString("sn");
			logger.info("获取到删除设备结果,当前au为:{},code为:{},sn为:{}",au,code,sn);
			if(code==1) {
				//成功处理
				try {
					familyDeviceService.deleteDeviceActualBySn(sn);
					logger.info("mq消息返回前 mq size{},func:{}",MyWebSocketHandler.mq.size(),fun);
					MyWebSocketHandler.mq.add(MqAjaxResult.success(null, fun, au, gatewaySn));
					logger.info("mq消息返回后 mq size{},func:{}",MyWebSocketHandler.mq.size(),fun);

				} catch (SchedulerException e) {
					logger.info("mq消息返回前 mq size{},func:{}",MyWebSocketHandler.mq.size(),fun);
					MyWebSocketHandler.mq.add(MqAjaxResult.error("设备删除失败", fun, au, gatewaySn));
					logger.info("mq消息返回后 mq size{},func:{}",MyWebSocketHandler.mq.size(),fun);

					logger.error("设备删除失败,设备sn码:{}",sn);
					e.printStackTrace();
				}
			}else {
				logger.info("mq消息返回前 mq size{},func:{}",MyWebSocketHandler.mq.size(),fun);
				MyWebSocketHandler.mq.add(MqAjaxResult.error("网关解绑设备失败,请联系平台管理员", fun, au, gatewaySn));
				logger.info("mq消息返回后 mq size{},func:{}",MyWebSocketHandler.mq.size(),fun);

			}
		}
		break;
		//获取到查询设备参数结果
		case IotOperMsgConstant.COMMON_PARMAS_GET:
		{
			int code = msg.getIntValue("code");
			String sn = msg.getString("sn");
			logger.info("获取查询设备参数结果,当前au为:{},code为:{},sn为:{}",au,code,sn);
			if(code==1) {
				//成功处理
				logger.info("mq消息返回前 mq size{},func:{}",MyWebSocketHandler.mq.size(),fun);
				MyWebSocketHandler.mq.add(MqAjaxResult.success(msg, fun, au, gatewaySn));
				logger.info("mq消息返回后 mq size{},func:{}",MyWebSocketHandler.mq.size(),fun);

			}else {
				logger.info("mq消息返回前 mq size{},func:{}",MyWebSocketHandler.mq.size(),fun);
				MyWebSocketHandler.mq.add(MqAjaxResult.error("查询失败", fun, au, gatewaySn));
				logger.info("mq消息返回后 mq size{},func:{}",MyWebSocketHandler.mq.size(),fun);

			}
		}
		break;
		//获取到查询设备参数结果
		case IotOperMsgConstant.COMMON_PARMAS_SET:
		{
			int code = msg.getIntValue("code");
			String sn = msg.getString("sn");
			logger.info("获取设置设备参数结果,当前au为:{},code为:{},sn为:{}",au,code,sn);
			if(code==1) {
				//成功处理
				logger.info("mq消息返回前 mq size{},func:{}",MyWebSocketHandler.mq.size(),fun);
				MyWebSocketHandler.mq.add(MqAjaxResult.success(null, fun, au, gatewaySn));
				logger.info("mq消息返回后 mq size{},func:{}",MyWebSocketHandler.mq.size(),fun);

			}else {
				logger.info("mq消息返回前 mq size{},func:{}",MyWebSocketHandler.mq.size(),fun);
				MyWebSocketHandler.mq.add(MqAjaxResult.error("设置失败", fun, au, gatewaySn));
				logger.info("mq消息返回后 mq size{},func:{}",MyWebSocketHandler.mq.size(),fun);

			}
		}
		break;
		//获取到开关调用结果
		case IotOperMsgConstant.APP_REQ_GW_FUN_ONOFFF:
		{
			int code = msg.getIntValue("code");
			String sn = msg.getString("sn");
			logger.info("获取到开关/插座类设备结果,当前au为:{},code为:{},sn为:{}",au,code,sn);
			if(code==1) {
				DeviceBriefRespInfo device = updateThingsModel(msg);
				logger.info("mq消息返回前 mq size{},func:{}",MyWebSocketHandler.mq.size(),fun);
				MyWebSocketHandler.mq.add(MqAjaxResult.success(device, fun, au, gatewaySn));
				logger.info("mq消息返回后 mq size{},func:{}",MyWebSocketHandler.mq.size(),fun);

			}else {
				logger.info("mq消息返回前 mq size{},func:{}",MyWebSocketHandler.mq.size(),fun);
				MyWebSocketHandler.mq.add(MqAjaxResult.error(null, fun, au, gatewaySn));
				logger.info("mq消息返回后 mq size{},func:{}",MyWebSocketHandler.mq.size(),fun);

			}
		}
		break;
		//获取到开关调用结果
		case IotOperMsgConstant.APP_REQ_GW_FUN_RSTYGRQ:
		{
			int code = msg.getIntValue("code");
			String sn = msg.getString("sn");
			logger.info("获取到报警器类设备复位结果,当前au为:{},code为:{},sn为:{}",au,code,sn);
			if(code==1) {
//				DeviceBriefRespInfo device = updateThingsModel(msg);
				DeviceBriefRespInfo device = familyDeviceService.selectBriefDeviceBySerialNumber(sn);
				//暂不更新模型 消息中无alarm字段
				logger.info("mq消息返回前 mq size{},func:{}",MyWebSocketHandler.mq.size(),fun);
 				MyWebSocketHandler.mq.add(MqAjaxResult.success(device, fun, au, gatewaySn));
				logger.info("mq消息返回后 mq size{},func:{}",MyWebSocketHandler.mq.size(),fun);

			}else {
				logger.info("mq消息返回前 mq size{},func:{}",MyWebSocketHandler.mq.size(),fun);
				MyWebSocketHandler.mq.add(MqAjaxResult.error(null, fun, au, gatewaySn));
				logger.info("mq消息返回后 mq size{},func:{}",MyWebSocketHandler.mq.size(),fun);

			}
		}
		break;
		//获取到窗帘操作结果
		case IotOperMsgConstant.APP_REQ_GW_FUN_CLCTR:
		{
			int code = msg.getIntValue("code");
			String sn = msg.getString("sn");
			logger.info("获取到窗帘设备结果,当前au为:{},code为:{},sn为:{}",au,code,sn);
			if(code==1) {
				DeviceBriefRespInfo device = updateThingsModel(msg);
				logger.info("mq消息返回前 mq size{},func:{}",MyWebSocketHandler.mq.size(),fun);
				MyWebSocketHandler.mq.add(MqAjaxResult.success(device, fun, au, gatewaySn));
				logger.info("mq消息返回后 mq size{},func:{}",MyWebSocketHandler.mq.size(),fun);

			}else {
				logger.info("mq消息返回前 mq size{},func:{}",MyWebSocketHandler.mq.size(),fun);
				MyWebSocketHandler.mq.add(MqAjaxResult.error(null, fun, au, gatewaySn));
				logger.info("mq消息返回后 mq size{},func:{}",MyWebSocketHandler.mq.size(),fun);

			}
		}
		break;

		default:
			break;
		}
	}

    /**
     * 处理设备事件
     * @param familyId
     * @param devEvents
     */
	private void processDevEvents(Long familyId, JSONArray devEvents,String gatewaySn) {
		devEvents.forEach(o->{
			JSONObject json = (JSONObject)(o);
			Integer fav = json.getInteger("fav");
			Long fat = json.getLong("fat");
			Integer amv = json.getInteger("amv");
			Long amt = json.getLong("amt");
			String sn = json.getString("sn");
			Device device = deviceService.selectDeviceBySerialNumber(sn);
			if(device==null) {
				logger.warn("设备未添加到服务器,无需进行通知,sn:{}",sn);
				return;
			}
			Long userId = null;
			Long roomId = null;
			Long deviceId = null;
			String familyName = "";
			if(familyId!=null) {
				Family family = familyService.selectFamilyOnlyByFamilyId(familyId);
				userId = family.getBelongUserId();
				familyName = family.getName();
			}
			
			if(fav!=null && 0!=fav) {
				AlertLog alertLog = new AlertLog();
				alertLog.setAlertName(1==fav?"通讯故障":"其他");
				alertLog.setAlertLevel(99L);
				alertLog.setStatus(2L);//待处理
				alertLog.setAlertCat(1);
				alertLog.setFamilyId(familyId);
				alertLog.setUserId(userId);//userid保存为创建者的userId
				alertLog.setAlertTime(new Date(fat*1000));
				if(device!=null) {
					alertLog.setProductId(device.getProductId());
					alertLog.setProductName(device.getProductName());
					alertLog.setDeviceId(device.getDeviceId());
					alertLog.setDeviceName(device.getDeviceName());
					
					roomId = device.getBelongRoomId();
					deviceId = device.getDeviceId();
				}
				AlertLog queryLog = new AlertLog();
				queryLog.setAlertLevel(alertLog.getAlertLevel());
				queryLog.setDeviceId(alertLog.getDeviceId());
				queryLog.setAlertTime(alertLog.getAlertTime());
				List<AlertLog> old = new ArrayList<>();
				//==1代表时间不对  不用查询 直接报警
				if(fat.intValue()!=1) {
					old = alertLogService.selectAlertLogList(queryLog );
				}
				logger.info("设备当前故障时间:{},报警新增记录isNull:{}",fat.intValue(),old.size());
				if(old.isEmpty()) {
					boolean isPush = true;
					boolean isSms = false;
					boolean isCall = false;
					if(familyId!=null) {
						String msgContent = "";
						if(StringUtils.isBlank(alertLog.getDeviceName())) {
							msgContent+=sn;
						}else {
							msgContent+=alertLog.getDeviceName();
						}
						if(StringUtils.isBlank(alertLog.getAlertName())) {
							msgContent+=(device.getDeviceName()+"故障");
						}else {
							msgContent+=alertLog.getAlertName();
						}
						List<FamilyUserRela> list = familyService.selectUserListByFamilyIdAndUserId(familyId,null);
						//通知到所有家庭用户
						for (int i = 0; i < list.size(); i++) {
							FamilyUserRela familyUserRela = list.get(i);
							Long uId = familyUserRela.getUserId();
							String phone = familyUserRela.getPhonenumber();
							Map<String,String> smsParam = new HashMap<String,String>();
							msgService.sendMsg(new Msg(device.getDeviceName()+"故障",MsgTypeConstant.MSG_TYPE_FAMILY_MSG,msgContent,null,
									familyId,roomId,deviceId,uId,null), isPush, isSms, isCall,MsgSettingEnum.DEV_DEVICE.getIdentifier(),
									device.getDeviceId()+"", smsParam);
						}
					}
					alertLogService.insertAlertLog(alertLog, sn, null, 3, null);
				}else {
					logger.warn("当前告警可能已经在别的服务器新增过了");
				}
			}
			boolean isSms = false;
			String alertFun = IotOperMsgConstant.GW_DEV_FUN_EVENTS_ALERT;
			JSONObject alerInfo= new JSONObject();
			if(amv!=null && 0!=amv) {
				String binaryString = Integer.toBinaryString(amv);
				String modelType = sn.substring(0, 4);
				alerInfo.put("deviceModelType",modelType);
				alerInfo.put("serialNumber",sn);
				StringBuilder alertName = new StringBuilder();
				if("2001".equals(modelType) || "2002".equals(modelType) ) {
					String amvByte = StringUtils.leftPad(binaryString, 8, '0');
					logger.info("插座类报警byte值:{} ",amvByte);
					char[] charArray = amvByte.toCharArray();
					if(charArray[2]=='1') {
						alertName.append("漏电报警 ");
					}
					if(charArray[3]=='1') {
						alertName.append("漏电预警 ");
					}
					if(charArray[4]=='1') {
						alertName.append("温升报警 ");
					}
					if(charArray[5]=='1') {
						alertName.append("漏电预警 ");
					}
					if(charArray[6]=='1' && charArray[7]=='1') {
						alertName.append("合闸通电异常 ");
					}
					if(charArray[6]=='1' && charArray[7]=='0') {
						alertName.append("分闸断电异常 ");
					}
	//				if(charArray[7]=='1') {
	//					alertName.append("分闸断电 ");
	//				}
					alerInfo.put("pow",amv);
					isSms = true;//插座需要短信报警
					logger.info("mq消息返回前 mq size{},func:{}",MyWebSocketHandler.mq.size(),alertFun);
					MyWebSocketHandler.mq.add(MqAjaxResult.success(alerInfo, alertFun, "dbox", gatewaySn));
					logger.info("mq消息返回后 mq size{},func:{}",MyWebSocketHandler.mq.size(),alertFun);

				}else if("4001".equals(modelType) || "4002".equals(modelType) ) {
					int alarm = json.getIntValue("alarm");
					alerInfo.put("alarm",alarm);
					logger.info("mq消息返回前 mq size{},func:{}",MyWebSocketHandler.mq.size(),alertFun);
					MyWebSocketHandler.mq.add(MqAjaxResult.success(alerInfo, alertFun, "dbox", gatewaySn));
					logger.info("mq消息返回后 mq size{},func:{}",MyWebSocketHandler.mq.size(),alertFun);

					if(alarm==1) {
						alertName.append("报警 ");
					}
				}
				AlertLog alertLog = new AlertLog();
				alertLog.setAlertName(alertName.toString());
				alertLog.setAlertLevel(98L);
				alertLog.setStatus(2L);//待处理
				alertLog.setAlertCat(1);
				alertLog.setFamilyId(familyId);
				alertLog.setUserId(userId);//userid保存为创建者的userId
				alertLog.setAlertTime(new Date(amt*1000));
				if(device!=null) {
					alertLog.setProductId(device.getProductId());
					alertLog.setProductName(device.getProductName());
					alertLog.setDeviceId(device.getDeviceId());
					alertLog.setDeviceName(device.getDeviceName());
					
					roomId = device.getBelongRoomId();
					deviceId = device.getDeviceId();
				}
				AlertLog queryLog = new AlertLog();
				queryLog.setAlertLevel(alertLog.getAlertLevel());
				queryLog.setDeviceId(alertLog.getDeviceId());
				queryLog.setAlertTime(alertLog.getAlertTime());
				List<AlertLog> old = new ArrayList<>();
				//==1代表时间不对  不用查询 直接报警
				if(amt.intValue()!=1) {
					old = alertLogService.selectAlertLogList(queryLog );
				}
				logger.info("设备当前报警时间:{},报警新增记录isNull:{}",amt.intValue(),old.size());
				if(old.isEmpty()) {
					boolean isPush = true;
					
					boolean isCall = false;
					if(familyId!=null) {
						String msgContent = "";
						if(StringUtils.isBlank(alertLog.getDeviceName())) {
							msgContent+=sn;
						}else {
							msgContent+=alertLog.getDeviceName();
						}
						if(StringUtils.isBlank(alertLog.getAlertName())) {
							msgContent+=(device.getDeviceName()+"报警");
						}else {
							msgContent+=alertLog.getAlertName();
						}
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
							msgService.sendMsg(new Msg(device.getDeviceName()+"报警",MsgTypeConstant.MSG_TYPE_FAMILY_MSG,msgContent,null,
									familyId,roomId,deviceId,uId,null), isPush, isSms, isCall,MsgSettingEnum.DEV_DEVICE.getIdentifier(),
									device.getDeviceId()+"", smsParam);
						}
					}
					alertLogService.insertAlertLog(alertLog, sn, null, 3, null);
				}else {
					logger.warn("当前告警可能已经在别的服务器新增过了");
				}
			}
		});
	}
	/**
	 * 处理检测板事件
	 * @param familyId
	 * @param jcbEvents
	 */
	private void processJcbEvents(Long familyId, JSONArray jcbEvents,Device gateway) {
		if(jcbEvents.size()>0) {
			String alertFun = IotOperMsgConstant.GW_JCB_FUN_EVENTS;
			logger.info("mq消息返回前 mq size{},func:{}",MyWebSocketHandler.mq.size(),alertFun);
			MyWebSocketHandler.mq.add(MqAjaxResult.success(jcbEvents, alertFun, "dbox", gateway.getSerialNumber()));
			logger.info("mq消息返回后 mq size{},func:{}",MyWebSocketHandler.mq.size(),alertFun);
		}
		jcbEvents.forEach(o->{
			JSONObject json = (JSONObject)(o);
			Integer fav = json.getInteger("fav");
			Integer line = json.getInteger("line");
			Long fat = json.getLong("fat");
			Long userId = null;
			Long roomId = null;
			Long deviceId = null;
			String familyName = "";
			if(familyId!=null) {
				Family family = familyService.selectFamilyOnlyByFamilyId(familyId);
				userId = family.getBelongUserId();
				familyName = family.getName();
			}
			
			if(fav!=null && 0!=fav) {
				AlertLog alertLog = new AlertLog();
				alertLog.setAlertName(1==fav?"通讯故障":2==fav?"温度传感器故障":"其他");
				alertLog.setAlertLevel(99L);
				alertLog.setStatus(2L);//待处理
				alertLog.setAlertCat(1);
				alertLog.setFamilyId(familyId);
				alertLog.setUserId(userId);//userid保存为创建者的userId
				alertLog.setAlertTime(new Date(fat*1000));
				if(gateway!=null) {
					alertLog.setProductId(gateway.getProductId());
					alertLog.setProductName(gateway.getProductName());
					alertLog.setDeviceId(gateway.getDeviceId());
					alertLog.setDeviceName(gateway.getDeviceName());
					
					roomId = gateway.getBelongRoomId();
					deviceId = gateway.getDeviceId();
				}
				AlertLog queryLog = new AlertLog();
				queryLog.setAlertLevel(alertLog.getAlertLevel());
				queryLog.setDeviceId(alertLog.getDeviceId());
				queryLog.setAlertTime(alertLog.getAlertTime());
				List<AlertLog> old = new ArrayList<>();
				//==1代表时间不对  不用查询 直接报警
				if(fat.intValue()!=1) {
					old = alertLogService.selectAlertLogList(queryLog );
				}
				logger.info("检测板jcb当前报警时间:{},报警新增记录isNull:{}",fat.intValue(),old.size());
				if(old.isEmpty()) {
					boolean isPush = true;
					boolean isSms = true;
					boolean isCall = false;
					if(familyId!=null) {
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
							msgService.sendMsg(new Msg("网关检测板故障",MsgTypeConstant.MSG_TYPE_FAMILY_MSG,alertLog.getDeviceName()+alertLog.getAlertName(),null,
									familyId,roomId,deviceId,uId,null), isPush, isSms, isCall,MsgSettingEnum.DEV_DEVICE.getIdentifier(),
									gateway.getDeviceId()+"", smsParam);
						}
							
					}
					alertLogService.insertAlertLog(alertLog, gateway.getSerialNumber(), null, 3, null);
				}else {
					logger.warn("当前告警可能已经在别的服务器新增过了");
				}
			}
		});
	}
	/**
	 * 处理空开事件
	 * @param familyId
	 * @param kkEvents
	 */
	private void processKkEvents(Long familyId, JSONArray kkEvents,Device gateway) {
		kkEvents.forEach(o->{
			JSONObject json = (JSONObject)(o);
			Integer index = json.getInteger("id");
			Integer amv = json.getInteger("amv");
			Long amt = json.getLong("amt");
			Long userId = null;
			Long roomId = null;
			Long deviceId = null;
			String familyName = "";
			if(familyId!=null) {
				Family family = familyService.selectFamilyOnlyByFamilyId(familyId);
				userId = family.getBelongUserId();
				familyName = family.getName();
			}
			if(amv!=null && 0!=amv) {
				String binaryString = Integer.toBinaryString(amv);
				String amvByte = StringUtils.leftPad(binaryString, 8, '0');
				logger.info("空开报警byte值:{} ",amvByte);
				StringBuilder alertName = new StringBuilder();
				char[] charArray = amvByte.toCharArray();
				alertName.append("空开");
				Long boardAirSwitchStatus = -1L;
				if(charArray[5]=='1') {
					if(boardAirSwitchStatus<0) {
						boardAirSwitchStatus = 4L;
					}
					alertName.append("温度过高 ");
				}
				if(charArray[6]=='1') {
					if(boardAirSwitchStatus<0) {
						boardAirSwitchStatus = 2L;
					}
					alertName.append("失压(上端无电) ");
				}
				if(charArray[7]=='1') {
					if(boardAirSwitchStatus<0) {
						boardAirSwitchStatus = 1L;
					}
					alertName.append("跳闸 ");
				}
				if(boardAirSwitchStatus<0) {
					boardAirSwitchStatus = 0L;
				}
				//高温>失压>跳闸>合闸 //此处不考虑离线
				BoardAirSwitch boardAirSwitch = new BoardAirSwitch();
				boardAirSwitch.setIndex(index);
				boardAirSwitch.setSwitchStatus(boardAirSwitchStatus);
				//空开事件推送
				logger.info("mq消息返回前 mq size{},func:{}",MyWebSocketHandler.mq.size(),IotOperMsgConstant.GW_KK_FUN_EVENTS);
		    	MyWebSocketHandler.mq.add(MqAjaxResult.success(boardAirSwitch, IotOperMsgConstant.GW_KK_FUN_EVENTS, "dbox", gateway.getSerialNumber()));
				logger.info("mq消息返回后 mq size{},func:{}",MyWebSocketHandler.mq.size(),IotOperMsgConstant.GW_KK_FUN_EVENTS);
				AlertLog alertLog = new AlertLog();
				alertLog.setAlertName(alertName.toString());
				alertLog.setAlertLevel(98L);
				alertLog.setStatus(2L);//待处理
				alertLog.setAlertCat(1);
				alertLog.setFamilyId(familyId);
				alertLog.setUserId(userId);//userid保存为创建者的userId
				alertLog.setAlertTime(new Date(amt*1000));
				if(gateway!=null) {
					alertLog.setProductId(gateway.getProductId());
					alertLog.setProductName(gateway.getProductName());
					alertLog.setDeviceId(gateway.getDeviceId());
					alertLog.setDeviceName(gateway.getDeviceName());
					
					roomId = gateway.getBelongRoomId();
					deviceId = gateway.getDeviceId();
				}
				AlertLog queryLog = new AlertLog();
				queryLog.setAlertLevel(alertLog.getAlertLevel());
				queryLog.setDeviceId(alertLog.getDeviceId());
				queryLog.setAlertTime(alertLog.getAlertTime());
				List<AlertLog> old = new ArrayList<>();
				//==1代表时间不对  不用查询 直接报警
				if(amt.intValue()!=1) {
					old = alertLogService.selectAlertLogList(queryLog );
					logger.info("空开kk当前报警时间:{},报警新增记录isNull:{}",amt.intValue(),old.size());
				}
				if(old.isEmpty()) {
					boolean isPush = true;
					boolean isSms = true;
					boolean isCall = false;
					if(familyId!=null) {
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
							msgService.sendMsg(new Msg("网关空开报警",MsgTypeConstant.MSG_TYPE_FAMILY_MSG,alertLog.getDeviceName()+alertLog.getAlertName(),null,
									familyId,roomId,deviceId,uId,null), isPush, isSms, isCall,MsgSettingEnum.DEV_DEVICE.getIdentifier(),
									gateway.getDeviceId()+"", smsParam);
						}
					}
					alertLogService.insertAlertLog(alertLog, gateway.getSerialNumber(), null, 3, null);
				}else {
					logger.warn("当前告警可能已经在别的服务器新增过了");
				}
			}
		});
	}
    
    private JSONObject convertValue(String deviceModelType,JSONObject deviceInfo) {
    	//模型转换
    	JSONObject valueObject = new JSONObject();
    	if("1001".equals(deviceModelType) || "1002".equals(deviceModelType) ) {
    		if(deviceInfo.getInteger("s1")!=null) {
    			String value = StringUtils.joinWith(",", deviceInfo.getIntValue("s1"));
    			String id = "onOffStatus";
    			valueObject.put(id, value);
    		}
    		
    	}else if("1011".equals(deviceModelType) || "1012".equals(deviceModelType) ) {
    		if(deviceInfo.getInteger("s1")!=null) {
    			String value = StringUtils.joinWith(",", deviceInfo.getIntValue("s1"));
    			String id = "onOffStatus";
    			valueObject.put(id, value);
    		}
    		
    	}else if("1003".equals(deviceModelType)) {
    		if(deviceInfo.getInteger("s1")!=null) {
    			String value = StringUtils.joinWith(",", deviceInfo.getIntValue("s1"),deviceInfo.getIntValue("s2"));
    			String id = "onOffStatus";
    			valueObject.put(id, value);
    		}
    		
    	}else if("1013".equals(deviceModelType)) {
    		if(deviceInfo.getInteger("s1")!=null) {
    			String value = StringUtils.joinWith(",", deviceInfo.getIntValue("s1"),deviceInfo.getIntValue("s2"));
    			String id = "onOffStatus";
    			valueObject.put(id, value);
    		}
    		
    	}else if("1004".equals(deviceModelType)) {
    		if(deviceInfo.getInteger("s1")!=null) {
    			String value = StringUtils.joinWith(",", deviceInfo.getIntValue("s1"),deviceInfo.getIntValue("s2"),deviceInfo.getIntValue("s3"));
    			String id = "onOffStatus";
    			valueObject.put(id, value);
    		}
    		
    	}else if("1014".equals(deviceModelType)) {
    		if(deviceInfo.getInteger("s1")!=null) {
    			String value = StringUtils.joinWith(",", deviceInfo.getIntValue("s1"),deviceInfo.getIntValue("s2"),deviceInfo.getIntValue("s3"));
    			String id = "onOffStatus";
    			valueObject.put(id, value);
    		}
    		
    	}else if("2001".equals(deviceModelType) || "2002".equals(deviceModelType) ) {
    		if(deviceInfo.getInteger("pow")!=null) {
    			String value = deviceInfo.getIntValue("pow")+"";
    			String id = "pow";
    			valueObject.put(id, value);
    		}
    	}else {
    		//TODO q其他设备暂不处理
    		logger.warn("设备变更推送app 待转换数据给前台 deviceModelType:{}",deviceModelType);
    	}
    	deviceInfo.put("value", valueObject);
    	return deviceInfo;
    }
    
    private DeviceBriefRespInfo updateThingsModel(JSONObject deviceInfo) {
    	//设备信息
		String sn = deviceInfo.getString("sn");
		logger.info("更新设备物模型,sn码为:{}",sn);
		Device device = deviceService.selectDeviceBySerialNumber(sn);
		if(device!=null) {
			String deviceModelType = sn.substring(0, 4);
			Integer line = deviceInfo.getInteger("line");
			logger.info("设备型号为:{}",deviceModelType);
			logger.info("设备sn为:{},line:{}",sn,line);
			if(line!=null) {
				//不为空再更新状态
				device.setStatus(1==line?3:4);//3在线 4离线
			}
			List<ThingsModelValueItem> thingsModelValues = JSONObject.parseArray(device.getThingsModelValue(), ThingsModelValueItem.class);
			List<ThingsModelValueItem> thingsModelLastedValue = getThingsModelLastedValue(thingsModelValues,deviceInfo,deviceModelType);
			device.setThingsModelValue(JSONObject.toJSONString(thingsModelLastedValue));
			familyDeviceService.updateDeviceStatusAndThingsModelById(device);
			logger.info("设备物模型 更新为最新值 sn:{}",sn);
			//全部设备状态 物模型 更新
			DeviceBriefRespInfo deviceBrief = familyDeviceService.selectBriefDeviceByDeviceId(device.getDeviceId());
			return deviceBrief;
		}else {
			//有可能添加网关的时候调用 查询设备所有信息接口  这个时候没有设备,不需要更新
			return null;
		}
    }
    
    private List<ThingsModelValueItem> getThingsModelLastedValue(List<ThingsModelValueItem> oldModel,JSONObject deviceInfo,String deviceModelType){
    	List<ThingsModelValueItem> list = oldModel;
    	Map<String,ThingsModelValueItem> lastedMap = new HashMap<String,ThingsModelValueItem>();
    	//模型转换
    	if("1001".equals(deviceModelType) || "1011".equals(deviceModelType)  || "1002".equals(deviceModelType)  || "1012".equals(deviceModelType) ) {
    		if(deviceInfo.getInteger("s1")!=null) {
    			String value = StringUtils.joinWith(",", deviceInfo.getIntValue("s1"));
    			String id = "onOffStatus";
    			ThingsModelValueItem item = new ThingsModelValueItem();
    			item.setId(id);
    			item.setValue(value);
    			lastedMap.put(id, item);
    		}
    		
    		if(deviceInfo.getInteger("line")!=null) {
    			String value2 = deviceInfo.getIntValue("line")+"";
    			String id2 = "line";
    			ThingsModelValueItem item2 = new ThingsModelValueItem();
    			item2.setId(id2);
    			item2.setValue(value2);
    			lastedMap.put(id2, item2);
    		}
    	}else if("1003".equals(deviceModelType) || "1013".equals(deviceModelType)) {
    		if(deviceInfo.getInteger("s1")!=null) {
    			String value = StringUtils.joinWith(",", deviceInfo.getIntValue("s1"),deviceInfo.getIntValue("s2"));
    			String id = "onOffStatus";
    			ThingsModelValueItem item = new ThingsModelValueItem();
    			item.setId(id);
    			item.setValue(value);
    			lastedMap.put(id, item);
    		}
    		
    		if(deviceInfo.getInteger("line")!=null) {
    			String value2 = deviceInfo.getIntValue("line")+"";
    			String id2 = "line";
    			ThingsModelValueItem item2 = new ThingsModelValueItem();
    			item2.setId(id2);
    			item2.setValue(value2);
    			lastedMap.put(id2, item2);
    		}
    	}else if("1004".equals(deviceModelType) || "1014".equals(deviceModelType)) {
    		if(deviceInfo.getInteger("s1")!=null) {
    			String value = StringUtils.joinWith(",", deviceInfo.getIntValue("s1"),deviceInfo.getIntValue("s2"),deviceInfo.getIntValue("s3"));
    			String id = "onOffStatus";
    			ThingsModelValueItem item = new ThingsModelValueItem();
    			item.setId(id);
    			item.setValue(value);
    			lastedMap.put(id, item);
    		}
    		
    		if(deviceInfo.getInteger("line")!=null) {
    			String value2 = deviceInfo.getIntValue("line")+"";
    			String id2 = "line";
    			ThingsModelValueItem item2 = new ThingsModelValueItem();
    			item2.setId(id2);
    			item2.setValue(value2);
    			lastedMap.put(id2, item2);
    		}
    	}else if("2001".equals(deviceModelType) || "2002".equals(deviceModelType) ) {
    		if(deviceInfo.getInteger("pow")!=null) {
    			String value = deviceInfo.getIntValue("pow")+"";
    			String id = "pow";
    			ThingsModelValueItem item = new ThingsModelValueItem();
    			item.setId(id);
    			item.setValue(value);
    			lastedMap.put(id, item);
    		}
    		if(deviceInfo.getInteger("line")!=null) {
    			String value2 = deviceInfo.getIntValue("line")+"";
    			String id2 = "line";
    			ThingsModelValueItem item2 = new ThingsModelValueItem();
    			item2.setId(id2);
    			item2.setValue(value2);
    			lastedMap.put(id2, item2);
    		}
    		if(deviceInfo.getInteger("sta")!=null) {
    			String value3 = deviceInfo.getIntValue("sta")+"";
    			String id3 = "sta";
    			ThingsModelValueItem item3 = new ThingsModelValueItem();
    			item3.setId(id3);
    			item3.setValue(value3);
    			lastedMap.put(id3, item3);
    		}
    	}else if("3001".equals(deviceModelType) || "3011".equals(deviceModelType)  ) {
    		//TODO
    		if(deviceInfo.getInteger("line")!=null) {
    			String value2 = deviceInfo.getIntValue("line")+"";
    			String id2 = "line";
    			ThingsModelValueItem item2 = new ThingsModelValueItem();
    			item2.setId(id2);
    			item2.setValue(value2);
    			lastedMap.put(id2, item2);
    		}
    	}else if("3002".equals(deviceModelType) || "3012".equals(deviceModelType)  ) {
    		//TODO
    		if(deviceInfo.getInteger("line")!=null) {
    			String value2 = deviceInfo.getIntValue("line")+"";
    			String id2 = "line";
    			ThingsModelValueItem item2 = new ThingsModelValueItem();
    			item2.setId(id2);
    			item2.setValue(value2);
    			lastedMap.put(id2, item2);
    		}
    	}else if("4001".equals(deviceModelType) ) {
    		if(deviceInfo.getInteger("line")!=null) {
    			String value2 = deviceInfo.getIntValue("line")+"";
    			String id2 = "line";
    			ThingsModelValueItem item2 = new ThingsModelValueItem();
    			item2.setId(id2);
    			item2.setValue(value2);
    			lastedMap.put(id2, item2);
    		}
    		if(deviceInfo.getInteger("alarm")!=null) {
    			String value2 = deviceInfo.getIntValue("alarm")+"";
    			String id2 = "alarm";
    			ThingsModelValueItem item2 = new ThingsModelValueItem();
    			item2.setId(id2);
    			item2.setValue(value2);
    			lastedMap.put(id2, item2);
    		}
    		if(deviceInfo.getInteger("back1")!=null) {
    			String value2 = deviceInfo.getIntValue("back1")+"";
    			String id2 = "back1";
    			ThingsModelValueItem item2 = new ThingsModelValueItem();
    			item2.setId(id2);
    			item2.setValue(value2);
    			lastedMap.put(id2, item2);
    		}
    	}else if("4002".equals(deviceModelType) ) {
    		if(deviceInfo.getInteger("line")!=null) {
    			String value2 = deviceInfo.getIntValue("line")+"";
    			String id2 = "line";
    			ThingsModelValueItem item2 = new ThingsModelValueItem();
    			item2.setId(id2);
    			item2.setValue(value2);
    			lastedMap.put(id2, item2);
    		}
    		if(deviceInfo.getInteger("alarm")!=null) {
    			String value2 = deviceInfo.getIntValue("alarm")+"";
    			String id2 = "alarm";
    			ThingsModelValueItem item2 = new ThingsModelValueItem();
    			item2.setId(id2);
    			item2.setValue(value2);
    			lastedMap.put(id2, item2);
    		}
    		if(deviceInfo.getInteger("back1")!=null) {
    			String value2 = deviceInfo.getIntValue("back1")+"";
    			String id2 = "back1";
    			ThingsModelValueItem item2 = new ThingsModelValueItem();
    			item2.setId(id2);
    			item2.setValue(value2);
    			lastedMap.put(id2, item2);
    		}
    	}
    	
    	//实际模型赋值
    	list.forEach(t->{
    		String id = t.getId();
    		if(lastedMap.containsKey(id)) {
    			ThingsModelValueItem item = lastedMap.get(id);
    			t.setValue(item.getValue());
    			//其他值===
    		}
    	});
    	return list;
    }
    
    private void processDevEvents() {
    	
    }
    
    /**
     * 获取默认物模型值
     *
     * @param productId
     * @return
     */
    private List<ThingsModelValueItem> getThingsModelDefaultValue(Long productId) {
        // 获取物模型,设置默认值
        String thingsModels = thingsModelService.getCacheThingsModelByProductId(productId);
        JSONObject thingsModelObject = JSONObject.parseObject(thingsModels);
        JSONArray properties = thingsModelObject.getJSONArray("properties");
        JSONArray functions = thingsModelObject.getJSONArray("functions");
        List<ThingsModelValueItem> valueList = properties.toJavaList(ThingsModelValueItem.class);
        valueList.addAll(functions.toJavaList(ThingsModelValueItem.class));
        valueList.forEach(x -> {
            x.setValue("");
            x.setShadow("");
        });
        return valueList;
    }

    
    /**
     * 处理wumei消息
     * @param topic
     * @param message
     */
	private void processWumeiMessage(String topic, String message) {
		String[] topicItem = topic.substring(1).split("/");
        Long productId = Long.valueOf(topicItem[0]);
        String deviceNum = topicItem[1];
        String name = topicItem[2];
        switch (name) {
            case "info":
                reportDevice(productId, deviceNum, message);
                break;
            case "ntp":
                publishNtp(productId, deviceNum, message);
                break;
            case "property":
                reportProperty(productId, deviceNum, message, false);
                break;
            case "function":
                reportFunction(productId, deviceNum, message, false);
                break;
            case "event":
                reportEvent(productId, deviceNum, message);
                break;
            case "property-offline":
                reportProperty(productId, deviceNum, message, true);
                break;
            case "function-offline":
                reportFunction(productId, deviceNum, message, true);
                break;
        }
	}
    
	/**
     * 上报设备信息
     */
    private void reportDevice(Long productId, String deviceNum, String message) {
        try {
            // 设备实体
            Device deviceEntity=deviceService.selectDeviceBySerialNumber(deviceNum);
            // 上报设备信息
            Device device = JSON.parseObject(message, Device.class);
            device.setProductId(productId);
            device.setSerialNumber(deviceNum);
            deviceService.reportDevice(device,deviceEntity);
            // 发布设备状态
            publishStatus(productId, deviceNum, 3, deviceEntity.getIsShadow(),device.getRssi());
        } catch (Exception e) {
            logger.error("接收设备信息，解析数据时异常 message={}", e.getMessage());
        }
    }

    /**
     * 上报属性
     *
     * @param message
     */
    private void reportProperty(Long productId, String deviceNum, String message, boolean isShadow) {
        try {
            List<ThingsModelValueRemarkItem> thingsModelValueRemarkItems = JSON.parseArray(message, ThingsModelValueRemarkItem.class);
            ThingsModelValuesInput input = new ThingsModelValuesInput();
            input.setProductId(productId);
            input.setDeviceNumber(deviceNum);
            input.setThingsModelValueRemarkItem(thingsModelValueRemarkItems);
            deviceService.reportDeviceThingsModelValue(input, 1, isShadow);
        } catch (Exception e) {
            logger.error("接收属性数据，解析数据时异常 message={}", e.getMessage());
        }
    }

    /**
     * 上报功能
     *
     * @param message
     */
    private void reportFunction(Long productId, String deviceNum, String message, boolean isShadow) {
        try {
            List<ThingsModelValueRemarkItem> thingsModelValueRemarkItems = JSON.parseArray(message, ThingsModelValueRemarkItem.class);
            ThingsModelValuesInput input = new ThingsModelValuesInput();
            input.setProductId(productId);
            input.setDeviceNumber(deviceNum);
            input.setThingsModelValueRemarkItem(thingsModelValueRemarkItems);
            deviceService.reportDeviceThingsModelValue(input, 2, isShadow);
        } catch (Exception e) {
            logger.error("接收功能，解析数据时异常 message={}", e.getMessage());
        }
    }

    /**
     * 上报事件
     *
     * @param message
     */
    private void reportEvent(Long productId, String deviceNum, String message) {
        try {
            List<ThingsModelValueRemarkItem> thingsModelValueRemarkItems = JSON.parseArray(message, ThingsModelValueRemarkItem.class);
            Device device = deviceService.selectDeviceBySerialNumber(deviceNum);
            for (int i = 0; i < thingsModelValueRemarkItems.size(); i++) {
                // 添加到设备日志
                DeviceLog deviceLog = new DeviceLog();
                deviceLog.setDeviceId(device.getDeviceId());
                deviceLog.setDeviceName(device.getDeviceName());
                deviceLog.setLogValue(thingsModelValueRemarkItems.get(i).getValue());
                deviceLog.setRemark(thingsModelValueRemarkItems.get(i).getRemark());
                deviceLog.setSerialNumber(device.getSerialNumber());
                deviceLog.setIdentity(thingsModelValueRemarkItems.get(i).getId());
                deviceLog.setLogType(3);
                deviceLog.setIsMonitor(0);
                deviceLog.setUserId(device.getUserId());
                deviceLog.setUserName(device.getUserName());
                deviceLog.setTenantId(device.getTenantId());
                deviceLog.setTenantName(device.getTenantName());
                deviceLog.setCreateTime(DateUtils.getNowDate());
                // 1=影子模式，2=在线模式，3=其他
                deviceLog.setMode(2);
                logService.saveDeviceLog(deviceLog);
            }
        } catch (Exception e) {
            logger.error("接收事件，解析数据时异常 message={}", e.getMessage());
        }
    }


    /**
     * 1.发布设备状态
     */
    public void publishStatus(Long productId, String deviceNum, int deviceStatus, int isShadow,int rssi) {
        String message = "{\"status\":" + deviceStatus + ",\"isShadow\":" + isShadow + ",\"rssi\":" + rssi + "}";
        emqxClient.publish(1, false, "/" + productId + "/" + deviceNum + pStatusTopic, message);
    }

    /**
     * 2.发布设备信息
     */
    public void publishInfo(Long productId, String deviceNum) {
        emqxClient.publish(1, false, "/" + productId + "/" + deviceNum + pInfoTopic, "");
    }

    /**
     * 3.发布时钟同步信息
     *
     * @param message
     */
    private void publishNtp(Long productId, String deviceNum, String message) {
        NtpModel ntpModel = JSON.parseObject(message, NtpModel.class);
        ntpModel.setServerRecvTime(System.currentTimeMillis());
        ntpModel.setServerSendTime(System.currentTimeMillis());
        emqxClient.publish(1, false, "/" + productId + "/" + deviceNum + pNtpTopic, JSON.toJSONString(ntpModel));
    }

    /**
     * 4.发布属性
     */
    public void publishProperty(Long productId, String deviceNum, List<IdentityAndName> thingsList) {
        if (thingsList == null) {
            emqxClient.publish(1, true, "/" + productId + "/" + deviceNum + pPropertyTopic, "");
        } else {
            emqxClient.publish(1, true, "/" + productId + "/" + deviceNum + pPropertyTopic, JSON.toJSONString(thingsList));
        }
    }

    /**
     * 5.发布功能
     */
    public void publishFunction(Long productId, String deviceNum, List<IdentityAndName> thingsList) {
        if (thingsList == null) {
            emqxClient.publish(1, true, "/" + productId + "/" + deviceNum + pFunctionTopic, "");
        } else {
            emqxClient.publish(1, true, "/" + productId + "/" + deviceNum + pFunctionTopic, JSON.toJSONString(thingsList));
        }

    }
    /**
     * 设备数据同步
     *
     * @param deviceNumber 设备编号
     * @return 设备
     */
    public Device deviceSynchronization(String deviceNumber) {
        Device device=deviceService.selectDeviceBySerialNumber(deviceNumber);
        // 1-未激活，2-禁用，3-在线，4-离线
        if(device.getStatus()==3){
            device.setStatus(4);
            deviceService.updateDeviceStatus(device);
            // 发布设备信息
            publishInfo(device.getProductId(),device.getSerialNumber());
        }
        return device;
    }
    
}
