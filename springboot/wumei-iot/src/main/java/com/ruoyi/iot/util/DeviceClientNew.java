package com.ruoyi.iot.util;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.ServiceHelper;
import cn.jiguang.common.connection.HttpProxy;
import cn.jiguang.common.connection.NativeHttpClient;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jiguang.common.resp.BaseResult;
import cn.jiguang.common.resp.ResponseWrapper;
import cn.jpush.api.device.AliasDeviceListResult;
import cn.jpush.api.device.DeviceClient;

public class DeviceClientNew extends DeviceClient {

	private final NativeHttpClient _httpClient;
    private String hostName;
    private String devicesPath;
    private String tagsPath;
    private String aliasesPath;
    
	public DeviceClientNew(String masterSecret, String appKey) {
		this(masterSecret, appKey, null, ClientConfig.getInstance());
		// TODO Auto-generated constructor stub
	}
	
	public DeviceClientNew(String masterSecret, String appKey, HttpProxy proxy, ClientConfig conf) {
		super(masterSecret, appKey);
        ServiceHelper.checkBasic(appKey, masterSecret);

        hostName = (String) conf.get(ClientConfig.DEVICE_HOST_NAME);
        devicesPath = (String) conf.get(ClientConfig.DEVICES_PATH);
        tagsPath = (String) conf.get(ClientConfig.TAGS_PATH);
        aliasesPath = (String) conf.get(ClientConfig.ALIASES_PATH);

        String authCode = ServiceHelper.getBasicAuthorization(appKey, masterSecret);
        _httpClient = new NativeHttpClient(authCode, proxy, conf);
    }
	
	/**
	 * 
	 * @param alias
	 * @param platform
	 * @param new_format false不支持该返回结构
	 * @return
	 * @throws APIConnectionException
	 * @throws APIRequestException
	 */
	 public AliasDeviceListResultNew getAliasDeviceList(String alias, String platform,Boolean new_format) throws APIConnectionException, APIRequestException {
	        String url = hostName + aliasesPath + "/" + alias;
	        if (null != platform) {
	        	url += "?platform=" + platform; 
	        }
	        if(new_format!=null) {
	        	url += "?new_format=" + new_format; 
	        }
	        
	        ResponseWrapper response = _httpClient.sendGet(url);
	        
	        return BaseResult.fromResponse(response, AliasDeviceListResultNew.class);
	    }
	

}
