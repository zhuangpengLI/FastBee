package com.ruoyi.iot.mobile.constant;

/**
 * 可操作的消息内容 
 * @author yue
 *
 */
public class IotOperMsgConstant {

	//网关主题
	public static final String ZNJJ_GATEWAY_PREFIX = "/dbox/up/";
	//app主题
	public static final String ZNJJ_APP_PREFIX = "/app/down/";
	
	
	//公共参数获取方法 -
	public static final String COMMON_PARMAS_GET= "GetNPras";
	//公共参数设置方法 -
	public static final String COMMON_PARMAS_SET= "SetNPras";
	//参数类型 -- 常规参数
	public static final String COMMON_PARMAS_ITEM_NORMAL= "normal";
	//参数类型 -- 语音参数
	public static final String COMMON_PARMAS_ITEM_AFD= "afd";
	
	//网关方法 -- 设置场景定时
	public static final String APP_REQ_GW_FUN_SETCJTABLE = "SetCJTable";
	//网关方法 -- 获取当前网关的软硬件版本信息
	public static final String APP_REQ_GW_FUN_GETHSV = "GetHSV";
	//网关方法 -- 查询所有设备 精简sn 数据压缩 接口
	public static final String APP_REQ_GW_FUN_GETDEVSARRAY = "GetDevsArray";
	//网关方法 -- 查询所有设备信息 精简sn接口
	public static final String APP_REQ_GW_FUN_GETDEVSRSCT = "GetDevsRSC";
	//网关方法 -- 查询所有设备信息
	public static final String APP_REQ_GW_FUN_GETDEVSRT = "GetDevsRT";
	//网关方法 -- 进入添加设备模式
	public static final String APP_REQ_GW_FUN_ENTERADDDEVENV= "EnterAddDevEnv";
	//网关方法 -- 退出添加设备模式
	public static final String APP_REQ_GW_FUN_EXITADDDEVENV= "ExitAddDevEnv";
	//网关方法 -- 查找到设备
	public static final String GW_REQ_APP_FUN_FINDDEV= "FindDev";
	//网关方法 -- 设备变更1
	public static final String GW_REQ_APP_FUN_CHANGEDEVS= "ChangeDevs";
	//网关方法 -- 设备变更
	public static final String GW_REQ_APP_FUN_CHANGEDEVS2= "ChangeDevs2";
	//网关方法 -- 删除设备
	public static final String APP_REQ_GW_FUN_DELDEV= "DelDev";

	//网关方法 -- 解绑家庭
	public static final String APP_REQ_GW_FUN_JIEBANG= "JieBang";

	//网关方法 -- 故障和报警事件上报
	public static final String GW_REQ_CS_FUN_EVENTPOP= "eventPop";
	//网关方法 -- 故障和报警事件简报
	public static final String GW_REQ_CS_FUN_EVENTBREF= "eventBref";
	//网关方法 -- 故障和报警事件请求
	public static final String CS_REQ_GW_FUN_CSREQEVENTS= "CSReqEvents";
	//网关方法 -- 故障和报警事件确认
	public static final String CS_REQ_GW_FUN_CSCFM= "CSCfm";
	
	//网关方法 -- 设备复位(可以复位已报警的设备)
	public static final String APP_OR_CS_REQ_GW_FUN_CSRST= "csRst";
	
	//网关方法 -- 网关升级请求
	public static final String CS_REQ_GW_FUN_ASKOTAUPD= "AskOTAUpd";
	//网关方法 -- 网关升级完成
	public static final String GW_REQ_GW_FUN_OTADONE= "OTADone";
	
	//网关方法 -- 获取月份中每天耗电量
	public static final String CS_REQ_GW_FUN_GETELEBYDAY= "GetEleByDay";
	//网关方法 -- 获取7*24耗电量
	public static final String CS_REQ_GW_FUN_GET724ELE= "Get724ele";
	//网关方法 -- 获取配电信息
	public static final String CS_REQ_GW_FUN_GETPOWDEV2= "GetPowDev2";
	
	
	//开关事件 (开关 插座)
	public static final String APP_REQ_GW_FUN_ONOFFF= "OnOff";
	
	//报警复位 (烟感 燃气)
	public static final String APP_REQ_GW_FUN_RSTYGRQ= "rstYGRQ";
	//窗帘事件
	public static final String APP_REQ_GW_FUN_CLCTR= "clctr";
	
	
	
	
	
	
	//自定义推送方法 -- 空开故障和报警事件请求
	public static final String GW_KK_FUN_EVENTS= "kkEvent";
	//自定义推送方法 -- 检测板故障和报警事件请求
	public static final String GW_JCB_FUN_EVENTS= "jcbEvent";
	//自定义推送方法 -- 设备报警事件请求
	public static final String GW_DEV_FUN_EVENTS_ALERT= "devAlert";
	
	/**
	 * 设备型号转换 
	 * @param deviceModelType 设备类型 1001  1002  1003 等 (16进制理解)
	 * @return 转换为10进制 int 值
	 */
	public static int tDeviceModelTypeTo10(String deviceModelType) {
		return Integer.valueOf(deviceModelType, 16);
	}
	/**
	 * 设备型号转换 
	 * @param deviceModelType16 设备类型 1001  1002  1003 等 (16进制理解)
	 * @return 转换为10进制 int 值
	 */
	public static String t16ToDeviceModelType(int deviceModelType16) {
		return Integer.toHexString(deviceModelType16);
	}
	
	
	public static void main(String[] args) {
		System.out.println(tDeviceModelTypeTo10("1001"));
		System.out.println(t16ToDeviceModelType(543));
		System.out.println(t16ToDeviceModelType(1022));
	}
	
}
