package com.ruoyi.iot.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.iot.mobile.constant.IotOperMsgConstant;
import com.ruoyi.iot.mobile.transferModel.MessageAndResponseTransfer;
import com.ruoyi.iot.service.impl.AuthRequestFactoryImpl;

@Component
public class MyWebSocketHandler extends TextWebSocketHandler{
    private static final Logger log = LoggerFactory.getLogger(MyWebSocketHandler.class);
	//网关 -- 关联的session
	public static Map<String, List<WebSocketSession>> clients = new ConcurrentHashMap<String, List<WebSocketSession>>();
	
	//mq消息
	public static ArrayBlockingQueue<MqAjaxResult> mq = new ArrayBlockingQueue<MqAjaxResult>(10000,true);
	
	//au列表
	public static Map<String, Object> auList = new ConcurrentHashMap<String, Object>();

	//aulist 公共对象 
	public static Object o = new Object();
	@Autowired
	private MessageAndResponseTransfer messageAndResponseTransfer;
	@Autowired
	private TokenService tokenService;
	
	@PostConstruct
	private void init() {
		auList.put("dbox", o);//网关消息一定要接受
		auList.put(messageAndResponseTransfer.getAuInfo(), o);//同步请求代理
		new Thread(()->{
			log.info("websocket 初始化,消息线程启动中....");
			while(true) {
				try {
					log.info("mq take before size:{}",mq.size());
					MqAjaxResult take = mq.take();
					log.info("mq take after size:{}",mq.size());
					String takeAu = take.getAu();
					String fun = take.getFun();
					log.info("开始推送消息给socket au 消息内容为:{}",IotOperMsgConstant.APP_REQ_GW_FUN_GETDEVSRT.equals(fun)?fun:JSONObject.toJSON(take));
					String gatewaySn = take.getGatewaySn();
					List<WebSocketSession> list = clients.get(gatewaySn);
					if(list!=null) {
						list.forEach(session->{
							try {
								log.info("-----------------推送消息给au:{},sessionId:{}",session.getAttributes().get("au"),session.getId());
								//暂时调整为不管谁请求都发送消息
//								Object au = session.getAttributes().get("au");
//								if(au!=null && ("dbox".equals(takeAu) || au.toString().equals(takeAu))) {
//									//谁请求 给谁发送
										sendSocketMsg(session,new TextMessage(JSONObject.toJSON(take).toString()));
//								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								log.error("消息发送失败,au:{},sessionId:{}",takeAu,session==null?null:session.getId());
								e.printStackTrace();
							}
						});
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					log.error("InterruptedException 在take时候被抛出");
					log.info( "InterruptedException 在take时候被抛出");
					e.printStackTrace();
				}
			}
		}).start();
		
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		String query = session.getUri().getQuery();
		log.info(query);
		if(StringUtils.isBlank(query)) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("code",500);
			jsonObject.put("msg","用户信息校验失败");
			sendSocketMsg(session,new TextMessage(jsonObject.toString()));
			session.close();
		}else {
			String[] split = query.split("=");
			if(split.length==2 && "token".equals(split[0])) {
				LoginUser loginUser = tokenService.getLoginUserByToken(split[1]);
				if(loginUser==null) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("code",500);
					jsonObject.put("msg","用户信息校验失败");
					sendSocketMsg(session,new TextMessage(jsonObject.toString()));
					session.close();
				}else {
					log.info("链接成功");
					super.afterConnectionEstablished(session);
				}
			}else {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code",500);
				jsonObject.put("msg","用户信息校验失败");
				sendSocketMsg(session,new TextMessage(jsonObject.toString()));
				session.close();
			}
		}
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		try {
			log.info("11111111111111 开始处理socket链接关闭数据,连接信息 sessionId:{}",session.getId());
			Map<String, Object> sessionAttributes = session.getAttributes();
			log.info("11111111111111 sessionAttributes:{}", sessionAttributes ==null?null:JSONObject.toJSON(sessionAttributes));
			String gatewaySn = sessionAttributes.get("gatewaySn").toString();
			log.info("11111111111111 gatewaySn:{}",gatewaySn);
			List<WebSocketSession> list = clients.get(gatewaySn);
			Object sessionAu = sessionAttributes.get("au");
			log.info("11111111111111 sessionAu:{}",sessionAu);
//			for(WebSocketSession socket:list) {
//				if(sessionAu.equals(socket.getAttributes().get("au"))) {
//					list.remove(socket);
//					break;
//				}
//			}
			log.info("11111111111111 移除au列表中此数据");
			auList.remove(sessionAu);
			if(!CollectionUtils.isEmpty(list)){
				log.info("list 不为空");
				//调整为倒序移除全部该au数据
				for(int i=list.size();i>0;i--) {
					WebSocketSession socket = list.get(i-1);
					if(sessionAu.equals(socket.getAttributes().get("au"))) {
						list.remove(socket);
					}
				}

				if(list.isEmpty()) {
					//如果连接列表空了,则移除改网关订阅列表
					log.info("11111111111111 连接列表空了,则移除改网关订阅列表,gatewaySn{}",gatewaySn);
					clients.remove(gatewaySn);
				}
			}else {
				log.info("list 为空");
			}
		} catch (Exception e) {
			log.error("11111111111111 连接断开 清除session失败");
			System.out.println("11111111111111 连接断开 清除session失败");
			e.printStackTrace();
		}
		super.afterConnectionClosed(session, status);
	}
	
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		try {
			log.info("22222222222222222222222222 开始处理socket session异常问题数据,连接信息 sessionId:{}",session.getId());
			Map<String, Object> sessionAttributes = session.getAttributes();
			log.info("22222222222222222222222222 sessionAttributes:{}", sessionAttributes ==null?null:JSONObject.toJSON(sessionAttributes));

			String gatewaySn = sessionAttributes.get("gatewaySn").toString();
			log.info("22222222222222222222222222 gatewaySn:{}",gatewaySn);
			List<WebSocketSession> list = clients.get(gatewaySn);
			Object sessionAu = sessionAttributes.get("au");
			log.info("22222222222222222222222222 sessionAu:{}",sessionAu);
//			for(WebSocketSession socket:list) {
//				if(sessionAu.equals(socket.getAttributes().get("au"))) {
//					list.remove(socket);
//					break;
//				}
//			}
			log.info("22222222222222222222222222 移除au列表中此数据");
			auList.remove(sessionAu);
			if(!CollectionUtils.isEmpty(list)){
				log.info("list 不为空");
				//调整为倒序移除全部该au数据
				for(int i=list.size();i>0;i--) {
					WebSocketSession socket = list.get(i-1);
					if(sessionAu.equals(socket.getAttributes().get("au"))) {
						list.remove(socket);
					}
				}

				if(list.isEmpty()) {
					//如果连接列表空了,则移除改网关订阅列表
					log.info("22222222222222222222222222 连接列表空了,则移除改网关订阅列表,gatewaySn{}",gatewaySn);
					clients.remove(gatewaySn);
				}
			}else {
				log.info("list 为空");
			}
		} catch (Exception e) {
			log.error("22222222222222222222222222 连接异常 清除session失败");
			System.out.println("22222222222222222222222222 连接异常 清除session失败");
			e.printStackTrace();
		}
//		super.afterConnectionClosed(session, status);
		// TODO Auto-generated method stub
		super.handleTransportError(session, exception);
	}
	
	public void sendDeviceOnlineStatus(String gatewaySn,Integer status){
		try {
			List<WebSocketSession> list = clients.get(gatewaySn);
			for(WebSocketSession socket:list) {
				sendSocketMsg(socket,new TextMessage("{\"gatewaySn\":\""+gatewaySn+"\",\"status\","+status+"}"));
			}
		} catch (Exception e) {
			log.error("移除au订阅的旧网关成功失败");
			System.out.println("移除au订阅的旧网关成功失败");
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		String gatewaySn = "111";
		Integer status = 3;
		System.out.println("{\"gatewaySn\":\""+gatewaySn+"\",\"status\","+status+"}");
	}
	
	/**
	 * 发送socket消息
	 * @param session
	 * @param message
	 */
	private void sendSocketMsg(WebSocketSession session, TextMessage message) {
		synchronized(session) {
			try {
				log.info("sessionid:{},sessionStatus:{}",session.getId(),session.isOpen());
				session .sendMessage(message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error("消息发送失败=============au:{},gatewaySn:{},sessionId:{}",session.getAttributes().get("au"),session.getAttributes().get("gatewaySn"),session.getId());
			}
		}
	}
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		
		log.info("收到新订阅消息 sessionId:{}",session.getId());
		System.out.println("msg:");
		System.out.println(message.getPayload());
		log.info("msg:{}",message.getPayload());
		String payload = message.getPayload();
		try {
			JSONObject o = (JSONObject) JSONObject.parse(payload);
			Map<String, Object> sessionAttributes = session.getAttributes();
			Object au = sessionAttributes.get("au");

			Object gatewaySn = sessionAttributes.get("gatewaySn");
			if(au==null && gatewaySn==null) {
//			System.out.println("未获取到订阅消息,不支持消息推送");
				String tempAu = o.getString("au");
				String tempGatewaySn = o.getString("gatewaySn");
				if(StringUtils.isAnyBlank(tempAu,tempGatewaySn)) {
					log.info("订阅消息有误");
					System.out.println("订阅消息有误");
					return;
				}else {
					sessionAttributes.put("au",tempAu);
					sessionAttributes.put("gatewaySn",tempGatewaySn);
					List<WebSocketSession> list = clients.get(tempGatewaySn);
					if(list==null) {
						list = new ArrayList<>();
					}
					list.add(session);
					clients.put(tempGatewaySn,list);
					log.info("tempAu放入auList:");
					auList.put(tempAu, o);
					log.info("发送订阅成功消息");
					sendSocketMsg(session,new TextMessage("true"));
					debugLog();
					return;
				}
			}else if(au!=null) {
				String tempAu = o.getString("au");
				String tempGatewaySn = o.getString("gatewaySn");
				if(StringUtils.isAnyBlank(tempAu,tempGatewaySn)) {
					log.error("\"修改订阅消息有误\"");
					System.out.println("修改订阅消息有误");
					return;
				}
				
				if(!tempAu.equals(au)) {
					log.error("\"修改订阅消息有误,不能修改\"");
					System.out.println("修改订阅消息有误,不能修改");
					return;
				}
				
				//清除旧网关信息
				try {
					String oldGatewaySn = sessionAttributes.get("gatewaySn").toString();
					log.info("旧网关sn:"+oldGatewaySn);
					System.out.println("旧网关sn:"+oldGatewaySn);
					List<WebSocketSession> list = clients.get(oldGatewaySn);
					if(!CollectionUtils.isEmpty(list)){
						log.info("旧网关客户端链接数量，长度：{}",list.size());
						for (int i = list.size() - 1; i >= 0; i--) {
							WebSocketSession socket = list.get(i);
							Map<String, Object> attributes = socket.getAttributes();
							Object au1 = attributes.get("au");
							log.info("当前移除过程循环中本次 socket 信息：{},au:{}",attributes,au1);
							if(sessionAttributes.get("au").equals(au1)) {
								list.remove(socket);
								System.out.println("移除au订阅的旧网关成功");
								log.info("移除au订阅的旧网关成功");
//								break;
							}
						}
//						for(WebSocketSession socket:list) {
//						}
					}else{
						log.info("旧网关客户端链接数量：{}",0);
					}
				} catch (Exception e) {
					log.error("移除au订阅的旧网关成功失败");
					System.out.println("移除au订阅的旧网关成功失败");
					e.printStackTrace();
				}
				sessionAttributes.put("au",tempAu);
				sessionAttributes.put("gatewaySn",tempGatewaySn);
				List<WebSocketSession> list = clients.get(tempGatewaySn);
				if(list==null) {
					list = new ArrayList<>();
				}
				list.add(session);
				log.info("新网关sn:"+tempGatewaySn);
				System.out.println("新网关sn:"+tempGatewaySn);
				clients.put(tempGatewaySn,list);
				log.info("tempAu放入auList:");
				auList.put(tempAu, o);
				log.info("发送订阅成功消息");
				sendSocketMsg(session,new TextMessage("true"));
				debugLog();
				return;
			}else {
				log.error("该socket已经订阅过消息,不支持修改");
				System.out.println("该socket已经订阅过消息,不支持修改");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("收到订阅消息处理异常",e);
			System.out.println("收到订阅消息处理异常");
			e.printStackTrace();
		}
		sendSocketMsg(session,new TextMessage("false"));
		
//		super.handleTextMessage(session, message);
	}
	
	private void debugLog() {
		//TODO 此日志调试结束后续移除
				log.info("订阅结束后 au列表为:{}",JSONObject.toJSON(auList));
				log.info("订阅结束后 au长度为:{}",auList.size());
				log.info("订阅结束后 clients长度为:{}",clients.size());
		clients.forEach((a,b)->{
			List<String> collect = b.stream().map(WebSocketSession::getId).collect(Collectors.toList());
			log.info("当前gatewaySn:{},当前session 列表长度为:{},列表id为:{}",a,collect.size(),JSONObject.toJSON(collect));
		});
//				log.info("订阅结束后 clients列表为:{}",JSONObject.toJSON(clients));
	}
	
}