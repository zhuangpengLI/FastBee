package com.ruoyi.common.utils;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;

public class SmsUtils {

	public final static String successCode = "Ok";
	//短信号码，验证码，有效时间(分钟)
	public static int sendMSLoginFunc(String phoneNum, String mesCode, String mesTime) {
		String code = "";
		try {

			Credential cred = new Credential("id",
					"key");

			HttpProfile httpProfile = new HttpProfile();
			httpProfile.setReqMethod("POST");

			httpProfile.setConnTimeout(60);

			httpProfile.setEndpoint("sms.tencentcloudapi.com");

			ClientProfile clientProfile = new ClientProfile();

			clientProfile.setSignMethod("HmacSHA256");
			clientProfile.setHttpProfile(httpProfile);

			SmsClient client = new SmsClient(cred, "ap-nanjing", clientProfile);

			SendSmsRequest req = new SendSmsRequest();

			// 短信服务的APPId
			String sdkAppId = "123";
			req.setSmsSdkAppId(sdkAppId);

			// 签名的内容 即公司名称或APP名称
			String signName = "签名";
			req.setSignName(signName);

			String senderid = "";
			req.setSenderId(senderid);

			String sessionContext = "xxx";
			req.setSessionContext(sessionContext);

			String extendCode = "";
			req.setExtendCode(extendCode);

			// 短信模板的id
			String templateId = "123";
			req.setTemplateId(templateId);

			String addPhoneNum = "+86" + phoneNum;

			String[] phoneNumberSet = { addPhoneNum };
			req.setPhoneNumberSet(phoneNumberSet);

			// 短信内容的2个参数， 验证码和有效时间(分钟)
			String[] templateParamSet = { mesCode, mesTime };

			req.setTemplateParamSet(templateParamSet);

			SendSmsResponse res = client.SendSms(req);

			// 打印回响的数据，里面有是否发送成功，这里暂时不校验
			System.out.println(SendSmsResponse.toJsonString(res));
			
			code = res.getSendStatusSet()[0].getCode();
//	           System.out.println(res
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return successCode.equals(code)?1:0;
	}
	
	public static void main(String[] args) {
		sendAlertMsg("15135082222", "家庭网关", "设备离线");
	}
	/**
	 * 发送设备报警信息
	 * @param phoneNum
	 * @param deviceName
	 * @param alertInfo
	 * @return
	 */
	public static int sendAlertMsg(String phone,String deviceName,String alertInfo) {
		 String templateId = "123";
		 String [] params = new String[] {deviceName,alertInfo} ;
		 return sendTempSMS(phone, templateId, params);
		
	}
	public static int sendTempSMS(String phoneNum, String templateId,String... param) {
		String code = "";
		try {

			Credential cred = new Credential("id",
					"key");

			HttpProfile httpProfile = new HttpProfile();
			httpProfile.setReqMethod("POST");

			httpProfile.setConnTimeout(60);

			httpProfile.setEndpoint("sms.tencentcloudapi.com");

			ClientProfile clientProfile = new ClientProfile();

			clientProfile.setSignMethod("HmacSHA256");
			clientProfile.setHttpProfile(httpProfile);

			SmsClient client = new SmsClient(cred, "ap-nanjing", clientProfile);

			SendSmsRequest req = new SendSmsRequest();

			// 短信服务的APPId
			String sdkAppId = "123";
			req.setSmsSdkAppId(sdkAppId);

			// 签名的内容 即公司名称或APP名称
			String signName = "签名";
			req.setSignName(signName);

			String senderid = "";
			req.setSenderId(senderid);

			String sessionContext = "xxx";
			req.setSessionContext(sessionContext);

			String extendCode = "";
			req.setExtendCode(extendCode);

			// 短信模板的id
			req.setTemplateId(templateId);

			String addPhoneNum = "+86" + phoneNum;

			String[] phoneNumberSet = { addPhoneNum };
			req.setPhoneNumberSet(phoneNumberSet);

			// 短信内容的参数
			String[] templateParamSet = param;

			req.setTemplateParamSet(templateParamSet);

			SendSmsResponse res = client.SendSms(req);

			// 打印回响的数据，里面有是否发送成功，这里暂时不校验
			System.out.println(SendSmsResponse.toJsonString(res));
			
			code = res.getSendStatusSet()[0].getCode();
//	           System.out.println(res
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return successCode.equals(code)?1:0;
	}
}
