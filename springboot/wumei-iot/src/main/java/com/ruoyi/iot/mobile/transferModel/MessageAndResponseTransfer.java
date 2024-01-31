package com.ruoyi.iot.mobile.transferModel;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.iot.mqtt.EmqxClient;

@Component
public class MessageAndResponseTransfer {
	
	//系统代理客户端(手机端)发起的请求前缀
	//这些请求需要响应到客户端
//	public static final String SYS_PROXY_PREFIX = "SYSTEM-PROXY-";
	public static final String SYS_PROXY_PREFIX = "SP";
	@Autowired
    private EmqxClient emqxClient;
	private static final Logger logger = LoggerFactory.getLogger(MessageAndResponseTransfer.class);

	
	//只处理操作结果通讯
	private Map<String,CompletableFuture<JSONObject>> operFun  = new ConcurrentHashMap<String,CompletableFuture<JSONObject>>() ;

	/**
	 * 获取当前设备
	 * @return
	 */
	public String getAuInfo() {
		return SYS_PROXY_PREFIX+emqxClient.getShortClientId();
	}
	public String getRandomAuInfo() {
		String a = "qwertyuiopasdfghjklzxcvbnm1234567890";
		char[] charArray = a.toCharArray();
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<11;i++) {
			sb.append(charArray[(int)(Math.random()*(a.length()))]);
		}
		return sb.toString();
	}
	public static void main(String[] args) {
		String a = "qwertyuiopasdfghjklzxcvbnm1234567890";
		char[] charArray = a.toCharArray();
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<11;i++) {
			sb.append(charArray[(int)(Math.random()*(a.length()))]);
		}
		System.out.println(sb.toString());
	}
	//createOperFunReq
	public void createOperFunReq(String gatwaySn,String funName) {
		String name = gatwaySn+"-"+funName;
		name = name.toUpperCase();//TODO 方法调整完之后需要删除
		while(operFun.get(name)!=null) {
			//有同样的请求 需要等待
			logger.info("{} 有重复消息需要等待~~~~~~~~~",name);
		}
		CompletableFuture<JSONObject> completableFuture = new CompletableFuture<JSONObject>();
		operFun.put(name, completableFuture);
	}
	
	//设置响应值
	public void setOperFunResp(String gatwaySn,String funName,JSONObject msg) {
		String name = gatwaySn+"-"+funName;
		name = name.toUpperCase();//TODO 方法调整完之后需要删除
		CompletableFuture<JSONObject> future = operFun.get(name);
		if(future!=null) {
			future.complete(msg);
		}
	}
	
	/**
	 * 如果不消费 会变成消息积压  所以create之后一定要get
	 * @param gatwaySn
	 * @param funName
	 * @return
	 */
	//获取操作结果响应
	public Boolean getOperFunResp(String gatwaySn,String funName) {
		return getOperFunResp(gatwaySn, funName, 10);
	}
	
	/**
	 * 如果不消费 会变成消息积压  所以create之后一定要get
	 * @param gatwaySn
	 * @param funName
	 * @param timeout 超时秒数
	 * @return
	 */
	public Boolean getOperFunResp(String gatwaySn,String funName,long timeout) {
		String name = gatwaySn+"-"+funName;
		name = name.toUpperCase();//TODO 方法调整完之后需要删除
		CompletableFuture<JSONObject> completableFuture = operFun.get(name);
		Boolean result = null;
		try {
			//10s响应
			result  = false;
			JSONObject msg = completableFuture.get(timeout, TimeUnit.SECONDS);
			if(msg!=null) {
				Integer code = msg.getInteger("code");
//				logger.info("系统代理请求收到响应 code:{}",code);
				 if(code==null) {
					 //code为空 说明 请求不需要code   理论上同步代理需要结果  都不会为空
					 code = 1;
				 }
				 if(1==code.intValue()) {
					 result = true;
				 }
			}
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//获取后 不乱成功失败 均需要清除数据
		clearOperFun(name);
		logger.info("获取值成功后,代理客户端列表请求数据量为:"+operFun.size());
		return result==null?false:result;
	}
	
	/**
	 * 如果不消费 会变成消息积压  所以create之后一定要get
	 * @param gatwaySn
	 * @param funName
	 * @return
	 */
	//获取操作结果响应
	public JSONObject getContentOperFunResp(String gatwaySn,String funName) {
		return getContentOperFunResp(gatwaySn, funName, 10);
	}
	/**
	 * 如果不消费 会变成消息积压  所以create之后一定要get
	 * @param gatwaySn
	 * @param funName
	 * @param timeout 超时秒数
	 * @return
	 */
	public JSONObject getContentOperFunResp(String gatwaySn,String funName,long timeout) {
		String name = gatwaySn+"-"+funName;
		name = name.toUpperCase();//TODO 方法调整完之后需要删除
		CompletableFuture<JSONObject> completableFuture = operFun.get(name);
		JSONObject msg = null;
		try {
			msg = completableFuture.get(timeout, TimeUnit.SECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//获取后 不乱成功失败 均需要清除数据
		clearOperFun(name);
		logger.info("获取值成功后,代理客户端列表请求数据量为:"+operFun.size());
		return msg;
	}
	
	private void clearOperFun(String name) {
		operFun.remove(name);
	}
}
