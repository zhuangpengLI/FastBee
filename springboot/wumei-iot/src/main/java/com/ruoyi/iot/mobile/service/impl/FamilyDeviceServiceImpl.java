package com.ruoyi.iot.mobile.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.ruoyi.iot.domain.*;
import com.ruoyi.iot.service.*;
import org.apache.commons.lang3.StringUtils;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.iot.dto.BoardAirSwitch;
import com.ruoyi.iot.dto.DetectingBoard;
import com.ruoyi.iot.dto.PowerDistribution;
import com.ruoyi.iot.mapper.DeviceLogMapper;
import com.ruoyi.iot.mapper.DeviceMapper;
import com.ruoyi.iot.mapper.DeviceUserMapper;
import com.ruoyi.iot.mapper.FamilyMapper;
import com.ruoyi.iot.mobile.constant.IotConstant;
import com.ruoyi.iot.mobile.constant.IotOperMsgConstant;
import com.ruoyi.iot.mobile.model.InviteDeviceUserReqDto;
import com.ruoyi.iot.mobile.model.UpdateDeviceBaseInfoReqDto;
import com.ruoyi.iot.mobile.respModel.DeviceBriefRespInfo;
import com.ruoyi.iot.mobile.respModel.DeviceNoticeSetting;
import com.ruoyi.iot.mobile.service.IFamilyDeviceService;
import com.ruoyi.iot.mobile.transferModel.MessageAndResponseTransfer;
import com.ruoyi.iot.model.DeviceShortOutput;
import com.ruoyi.iot.model.ThingsModels.ThingsModelValueItem;
import com.ruoyi.iot.mqtt.EmqxClient;
import com.ruoyi.iot.mqtt.EmqxService;
import com.ruoyi.iot.service.impl.DeviceJobServiceImpl;
import com.ruoyi.iot.service.impl.ThingsModelServiceImpl;
import com.ruoyi.iot.tdengine.service.ILogService;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.service.ISysUserService;

/**
 * 设备Service业务层处理
 *
 * @author kerwincui
 * @date 2021-12-16
 */
@Service
public class FamilyDeviceServiceImpl implements IFamilyDeviceService {
    private static final Logger log = LoggerFactory.getLogger(FamilyDeviceServiceImpl.class);

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private DeviceUserMapper deviceUserMapper;

    @Autowired
    private ThingsModelServiceImpl thingsModelService;

    @Autowired
    private DeviceJobServiceImpl deviceJobService;

    @Autowired
    private DeviceLogMapper deviceLogMapper;

    @Autowired
    private IToolService toolService;

    @Autowired
    private IProductService productService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ILogService logService;

    @Autowired
    @Lazy
    private EmqxService emqxService;
    @Autowired
    private EmqxClient emqxClient;
    
    @Autowired
    private FamilyMapper familyMapper;
    @Autowired
    private MessageAndResponseTransfer transfer;
    
    @Autowired
    private IFamilyService familyService;
    
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private IFamilyDeviceService familyDeviceService;
    @Autowired
    private IDeviceService deviceService;
    @Autowired
    private IDeviceOtaUpdateService deviceOtaUpdateService;
    @Autowired
    private IDboxService dboxService;
    @Autowired
    private IDboxSwitchService dboxSwitchService;
	@Autowired
	private IDboxEleService dboxEleService;
    
    @Override
    public int countNotInRomm(Long familyId,Long userId) {
    	FamilyUserRela userRela = familyMapper.selectUserByFamilyIdAndUserId(familyId, userId);
    	if(userRela==null){
//    		/非家庭成员,
    		return 0;
    	}
    	return deviceMapper.countNotInRomm(familyId);
    }
    @Override
    public int countNotInRomm(Long familyId) {
    	if(familyId==null){
//    		/非家庭成员,
    		return 0;
    	}
    	return deviceMapper.countNotInRomm(familyId);
    }

	@Override
	public AjaxResult getGatewayStatus(Long familyId,Long userId) {
		Family family = familyMapper.selectFamilyOnlyByFamilyId(familyId);
		FamilyUserRela userRela = familyMapper.selectUserByFamilyIdAndUserId(familyId, userId);
    	if(userRela==null){
//    		/非家庭成员,
    		return AjaxResult.error("家庭信息有误");
    	}
    	Map<String, Object> hashMap = new HashMap<String,Object>();
    	Integer isBind = family.getIsBind();
    	hashMap.put("isBind", isBind);
    	hashMap.put("status", 0);
    	String gatewaySn = family.getGatewaySn();
    	//发布一个模拟app  获取所有设备请求 GetDevsRT 挂起请求
    	String topic = IotConstant.ZNJJ_APP_PREFIX +gatewaySn;
    	JSONObject pushMessage = new JSONObject();
    	pushMessage.put("au", transfer.getAuInfo());
    	pushMessage.put("ta", null);
    	pushMessage.put("fun",IotOperMsgConstant.APP_REQ_GW_FUN_GETHSV);
    	emqxClient.publish(1, false, topic, pushMessage.toString());
    	transfer.createOperFunReq(gatewaySn, IotOperMsgConstant.APP_REQ_GW_FUN_GETHSV);
    	Boolean operFunResp = transfer.getOperFunResp(gatewaySn, IotOperMsgConstant.APP_REQ_GW_FUN_GETHSV);
    	if(operFunResp) {
    		//网关在线
    		hashMap.put("status", 1);
    	}
    	//TODO 此处可以考虑直接查询设备表状态,网关需要使用webhook 回调 更新是否在线(是否连接mqtt服务器)
    	// 需要考虑如何接受 响应数据
    	//收到网关上报的GetDevsRT消息时 (代表响应)
    	return AjaxResult.success(hashMap);
	}
	@Override
	public AjaxResult getGatewayStatus(String gatewaySn) {
		Map<String, Object> hashMap = new HashMap<String,Object>();
		//发布一个模拟app  获取所有设备请求 GetDevsRT 挂起请求
		String topic = IotConstant.ZNJJ_APP_PREFIX +gatewaySn;
		JSONObject pushMessage = new JSONObject();
		pushMessage.put("au", transfer.getAuInfo());
		pushMessage.put("ta", null);
		pushMessage.put("fun",IotOperMsgConstant.APP_REQ_GW_FUN_GETHSV);
		emqxClient.publish(1, false, topic, pushMessage.toString());
		transfer.createOperFunReq(gatewaySn, IotOperMsgConstant.APP_REQ_GW_FUN_GETHSV);
		Boolean operFunResp = transfer.getOperFunResp(gatewaySn, IotOperMsgConstant.APP_REQ_GW_FUN_GETHSV);
		if(operFunResp) {
			//网关在线
			hashMap.put("status", 1);
		}else {
			hashMap.put("status", 0);
		}
		//TODO 此处可以考虑直接查询设备表状态,网关需要使用webhook 回调 更新是否在线(是否连接mqtt服务器)
		// 需要考虑如何接受 响应数据
		//收到网关上报的GetDevsRT消息时 (代表响应)
		return AjaxResult.success(hashMap);
	}
	@Override
	public DeviceBriefRespInfo selectBriefDeviceByDeviceId(Long deviceId) {
		return deviceMapper.selectBriefDeviceByDeviceId(deviceId);
	}
	@Override
	public DeviceBriefRespInfo selectBriefDeviceBySerialNumber(String serialNumber) {
		return deviceMapper.selectBriefDeviceBySerialNumber(serialNumber);
	}
	@Override
	public List<DeviceBriefRespInfo> selectRoomDeviceList(Long roomId,Long familyId, Long userId) {
		if(roomId!=null) {
			//房间id不为空则判断房间是否属于家庭
			Room room = familyMapper.selectRoomByRoomId(roomId);
			Long roomFamilyId = room.getFamilyId();
			if(!roomFamilyId.equals(familyId)) {
				//家庭信息错误,
				return new ArrayList<>();
			}
		}
//		Family family = familyMapper.selectFamilyOnlyByFamilyId(familyId);
		FamilyUserRela userRela = familyMapper.selectUserByFamilyIdAndUserId(familyId, userId);
    	if(userRela==null){
//    		/非家庭成员,
    		return new ArrayList<>();
    	}
    	List<DeviceBriefRespInfo> selectRoomDeviceList = deviceMapper.selectRoomDeviceList(roomId, familyId);
		return selectRoomDeviceList;
	}
	@Override
	public List<DeviceBriefRespInfo> selectFamilyNotInRoomDeviceList(Long familyId, Long userId) {
//		Family family = familyMapper.selectFamilyOnlyByFamilyId(familyId);
		FamilyUserRela userRela = familyMapper.selectUserByFamilyIdAndUserId(familyId, userId);
		if(userRela==null){
//    		/非家庭成员,
			return new ArrayList<>();
		}
		List<DeviceBriefRespInfo> selectRoomDeviceList = deviceMapper.selectFamilyNotInRoomDeviceList( familyId);
		return selectRoomDeviceList;
	}
	@Override
	public List<DeviceBriefRespInfo> selectFamilyUsualDeviceList(Long familyId, Long userId) {
//		Family family = familyMapper.selectFamilyOnlyByFamilyId(familyId);
		FamilyUserRela userRela = familyMapper.selectUserByFamilyIdAndUserId(familyId, userId);
		if(userRela==null){
//    		/非家庭成员,
			return new ArrayList<>();
		}
		List<DeviceBriefRespInfo> selectRoomDeviceList = deviceMapper.selectFamilyUsualDeviceList( familyId);
		return selectRoomDeviceList;
	}
	
	@Transactional
    @Override
    public AjaxResult inviteDeviceUser(InviteDeviceUserReqDto dto,Long loginUserId) {
    	Long deviceId = dto.getDeviceId();
    	Device device = deviceMapper.selectDeviceByDeviceId(deviceId);
    	if(device==null) {
    		return AjaxResult.error("设备信息有误");
    	}
    	Long familyId = device.getBelongFamilyId();
    	if(familyId==null) {
    		return AjaxResult.error("家庭信息不存在");
    	}
    	String phonenumber = dto.getPhonenumber();
    	SysUser info = sysUserMapper.checkPhoneUnique(phonenumber,"01");
    	if(info==null) {
    		return AjaxResult.error("手机号未注册,请通知对方下载APP注册后,再进行分享");
    	}
    	Long userId = info.getUserId();
//    	String familyUserRole = dto.getFamilyUserRole();
//    	familyUserRole = "1";//不管是谁邀请过来 都是普通用户
//    	 如果是拥有者有权限  需要校验家庭信息
    	Family family = familyMapper.selectFamilyOnlyByFamilyId(familyId);
    	if(family==null){
    		return AjaxResult.error("家庭信息不存在");
    	}
    	FamilyUserRela userRela = familyMapper.selectUserByFamilyIdAndUserId(familyId, userId);
		if(userRela!=null){
    		//用户已经属于家庭成员,无需重复邀请"
    		return AjaxResult.error("用户已经属于家庭成员,无需重复邀请");
    	}
    	FamilyUserRela loginUserRela = familyMapper.selectUserByFamilyIdAndUserId(familyId, loginUserId);
		if(loginUserRela==null){
    		//非家庭成员 不允许
    		return AjaxResult.error("权限不足");
    	}
		//权限校验  
		if(!"2".equals(loginUserRela.getFamilyUserRole())){
//			return AjaxResult.error("权限不足");
//			不是管理员 只能邀请普通成员
//			familyUserRole = "1";
			return AjaxResult.error("权限不足");
		}
//		userRela.setFamilyUserRole(familyUserRole);
//		理论上邀请过来都是普通成员比较合理 邀请完后 再主动调整为管理员  这样邀请信息不需要存 邀请角色 权限也比较合理
		// 生成邀请信息  消息通知 等
		//判断是否已经邀请过
		int count = familyService.countFamilyOrDeviceSendMsg(familyId,family.getName(),deviceId,device.getDeviceName(), userId,loginUserId);
		if(count>0) {
			return AjaxResult.error("已经邀请过,无需重复邀请");
		}
		//设备要求只能是普通成员
		familyService.shareFamilyOrDeviceSendMsg(familyId,family.getName(),deviceId,device.getDeviceName(), userId,loginUserId, "1");
		return AjaxResult.success("邀请成功");
    }
	
	
	@Override
	public AjaxResult startAddDevice(Long familyId,Long userId,String au) {
		Family family = familyMapper.selectFamilyOnlyByFamilyId(familyId);
		FamilyUserRela userRela = familyMapper.selectUserByFamilyIdAndUserId(familyId, userId);
    	if(userRela==null){
//    		/非家庭成员,
    		return AjaxResult.error("家庭信息有误");
    	}
    	if(!"2".equals(userRela.getFamilyUserRole())){
    		return AjaxResult.error("无权限,请联系管理员");
    	}
    	Integer isBind = family.getIsBind();
    	if(isBind.intValue() != 1) {
    		return AjaxResult.error("家庭未绑定网关,无法进入添加设备模式");
    	}
    	String gatewaySn = family.getGatewaySn();
    	//发布一个模拟app  进入网关查找设备模式
    	String topic = IotConstant.ZNJJ_APP_PREFIX +gatewaySn;
    	JSONObject pushMessage = new JSONObject();
    	pushMessage.put("au", au);
    	pushMessage.put("fun",IotOperMsgConstant.APP_REQ_GW_FUN_ENTERADDDEVENV);
    	emqxClient.publish(1, false, topic, pushMessage.toString());
    	// 异步接受到mq消息后 使用websocket 推送给客户端(所有订阅该网关的)
    	return AjaxResult.success("请求成功");
	}
	@Override
	public AjaxResult endAddDevice(Long familyId,Long userId,String au) {
		Family family = familyMapper.selectFamilyOnlyByFamilyId(familyId);
		FamilyUserRela userRela = familyMapper.selectUserByFamilyIdAndUserId(familyId, userId);
		if(userRela==null){
//    		/非家庭成员,
			return AjaxResult.error("家庭信息有误");
		}
		if(!"2".equals(userRela.getFamilyUserRole())){
			return AjaxResult.error("无权限,请联系管理员");
		}
		Integer isBind = family.getIsBind();
		if(isBind.intValue() != 1) {
			return AjaxResult.error("家庭未绑定网关,无法进入添加设备模式");
		}
		String gatewaySn = family.getGatewaySn();
		//发布一个模拟app  进入网关查找设备模式
		String topic = IotConstant.ZNJJ_APP_PREFIX +gatewaySn;
		JSONObject pushMessage = new JSONObject();
		pushMessage.put("au", au);
		pushMessage.put("fun",IotOperMsgConstant.APP_REQ_GW_FUN_EXITADDDEVENV);
		emqxClient.publish(1, false, topic, pushMessage.toString());
		// 异步接受到mq消息后 使用websocket 推送给客户端(所有订阅该网关的)
		return AjaxResult.success("请求成功");
	}
	
	/**
     * 添加设备
     *
     * @return 结果 1添加成功 <=0 添加失败  1000代表之前添加过 本次只做返回
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertDevice(String serialNumber, Long userId, String gatewaySn) {
        // 设备编号唯一检查
        Device old = deviceMapper.selectDeviceBySerialNumber(serialNumber);
        if (old != null) {
            Long belongFamilyId = old.getBelongFamilyId();
            if(belongFamilyId!=null) {
            	log.info("设备可能解绑 需要重新绑定到网关上");
            	Family family = familyService.selectFamilyOnlyByFamilyId(belongFamilyId);
            	if(!gatewaySn.equals(family.getGatewaySn())) {
            		log.error("设备之前已经绑定到家庭{},无法重新绑定新家庭",belongFamilyId);
            		return 0;
            	}else {
                	log.info("设备可能在其他端绑定到家庭,或者之前已经绑定过了~~~ 无需处理");
                	//需要返回对应的设备 不然页面看不到
                	return 1;
            	}
            }else {
            	//
            	log.info("设备可能解绑了家庭 需要重新绑定到网关上");
            	//网关/家庭信息
            	String deviceModelType = serialNumber.substring(0, 4);
            	Product product = productService.selectProductByDeviceModelType(deviceModelType);
            	if (product == null) {
            		log.error("自动更新网关设备家庭信息时，根据产品ID查找不到对应产品");
            		return 0;
            	}
            	Device gwDevice = deviceMapper.selectShortDeviceBySerialNumber(gatewaySn);
            	old.setDeviceName(product.getProductName());//设备名称还原默认
            	old.setThingsModelValue(JSONObject.toJSONString(getThingsModelDefaultValue(product.getProductId(),product)));//设备模型还原默认
            	if(gwDevice!=null) {
            		//设备所属家庭 属于网关所属家庭
            		old.setBelongFamilyId(gwDevice.getBelongFamilyId());
            		old.setIsFamilyUsual(1);//默认为常用设备
            	}
            	deviceMapper.updateDevice(old);
            	return 1;
            }
        }
        log.info("设备编号：" + serialNumber + "不存在，开始新增设备");
        //产品信息
//        Long productId
        String deviceModelType = serialNumber.substring(0, 4);
        Product product = productService.selectProductByDeviceModelType(deviceModelType);
        if (product == null) {
            log.error("自动添加设备时，根据产品ID查找不到对应产品,设备型号:{},设备sn:{}",deviceModelType,serialNumber);
            return 0;
        }
        Device device = new Device();
        device.setSerialNumber(serialNumber);
        //不存放user信息 获取不到
//        SysUser user = userService.selectUserById(userId);
//        device.setUserId(userId);
//        device.setUserName(user.getUserName());
        device.setFirmwareVersion(BigDecimal.valueOf(1.0));
        // 设备状态（1-未激活，2-禁用，3-在线，4-离线）
        device.setStatus(1);
        device.setActiveTime(DateUtils.getNowDate());
        device.setIsShadow(0);
        device.setRssi(0);
        // 1-自动定位，2-设备定位，3-自定义位置
        device.setLocationWay(1);
        device.setCreateTime(DateUtils.getNowDate());
        device.setThingsModelValue(JSONObject.toJSONString(getThingsModelDefaultValue(product.getProductId(),product)));
        // 随机位置
        device.setLongitude(BigDecimal.valueOf(116.23 - (Math.random() * 15)));
        device.setLatitude(BigDecimal.valueOf(39.54 - (Math.random() * 15)));
        device.setNetworkAddress("中国");
        device.setNetworkIp("127.0.0.1");
        // 设置租户
//        设备名称使用产品名称
//        int random = (int) (Math.random() * (90)) + 10;
//        device.setDeviceName(product.getProductName() + random);
        device.setDeviceName(product.getProductName());
        device.setTenantId(product.getTenantId());
        device.setTenantName(product.getTenantName());
        device.setImgUrl(product.getImgUrl());
        device.setProductId(product.getProductId());
        device.setProductName(product.getProductName());
        device.setDeviceType(product.getDeviceType());
        device.setDeviceModelType(product.getDeviceModelType());
        
        //网关/家庭信息
        Device gwDevice = deviceMapper.selectShortDeviceBySerialNumber(gatewaySn);
        if(gwDevice!=null) {
        	//设备所属家庭 属于网关所属家庭
        	device.setBelongFamilyId(gwDevice.getBelongFamilyId());
        	device.setIsFamilyUsual(1);//默认为常用设备
        }
        int insertDevice = deviceMapper.insertDevice(device);

        //设备用户表 在本系统中不使用  本系统使用 家庭(网关) 设备 关联  只要是家庭成员就可使用
        // 添加设备用户
//        DeviceUser deviceUser = new DeviceUser();
//        deviceUser.setUserId(userId);
//        deviceUser.setUserName(user.getUserName());
//        deviceUser.setPhonenumber(user.getPhonenumber());
//        deviceUser.setDeviceId(device.getDeviceId());
//        deviceUser.setDeviceName(device.getDeviceName());
//        deviceUser.setTenantId(product.getTenantId());
//        deviceUser.setTenantName(product.getTenantName());
//        deviceUser.setIsOwner(1);
//        deviceUserMapper.insertDeviceUser(deviceUser);
        return 1;
    }
    @Override
    public boolean isGatewayDevice(String serialNumber) {
    	return  Pattern.matches("^([a-zA-Z0-9]){20,24}$", serialNumber);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized int insertGatewayDevice(String serialNumber,Long familyId) {
    	if(StringUtils.isBlank(serialNumber) || serialNumber.length()!=20) {
    		log.error("网关信息有误:{}",serialNumber);
    		return 0;
    	}
    	serialNumber = serialNumber.toUpperCase();
    	// 设备编号唯一检查
    	int count = deviceMapper.selectDeviceCountBySerialNumber(serialNumber);
    	if (count != 0) {
    		log.error("设备编号：" + serialNumber + "已经存在了，新增设备失败");
    		return 0;
    	}
    	//产品信息
//        Long productId
    	Product product = productService.selectGwProductByDeviceModelType();
    	if (product == null) {
    		log.error("自动添加设备时，根据产品ID查找不到对应产品");
    		return 0;
    	}
    	Device device = new Device();
    	device.setSerialNumber(serialNumber);
    	//不存放user信息 获取不到
//        SysUser user = userService.selectUserById(userId);
//        device.setUserId(userId);
//        device.setUserName(user.getUserName());
    	device.setFirmwareVersion(BigDecimal.valueOf(1.0));
    	// 设备状态（1-未激活，2-禁用，3-在线，4-离线）
    	device.setStatus(3);
    	device.setActiveTime(DateUtils.getNowDate());
    	device.setIsShadow(0);
    	device.setRssi(0);
    	// 1-自动定位，2-设备定位，3-自定义位置
    	device.setLocationWay(1);
    	device.setCreateTime(DateUtils.getNowDate());
    	device.setThingsModelValue(JSONObject.toJSONString(getThingsModelDefaultValue(product.getProductId(),product)));
    	// 随机位置
    	device.setLongitude(BigDecimal.valueOf(116.23 - (Math.random() * 15)));
    	device.setLatitude(BigDecimal.valueOf(39.54 - (Math.random() * 15)));
    	device.setNetworkAddress("中国");
    	device.setNetworkIp("127.0.0.1");
    	// 设置租户
//        设备名称使用产品名称
    	device.setDeviceName(product.getProductName());
    	device.setTenantId(product.getTenantId());
    	device.setTenantName(product.getTenantName());
    	device.setImgUrl(product.getImgUrl());
    	device.setProductId(product.getProductId());
    	device.setProductName(product.getProductName());
    	device.setDeviceType(product.getDeviceType());
    	device.setDeviceModelType(product.getDeviceModelType());
    	
    	//网关/家庭信息
    	device.setBelongFamilyId(familyId);
    	int insertDevice = deviceMapper.insertDevice(device);
    	
    	return insertDevice;
    }

    /**
     * 获取物模型值
     *
     * @param productId
     * @return
     */
    private List<ThingsModelValueItem> getThingsModelDefaultValue(Long productId,Product product) {
    	
    	if(EmqxService.deviceMsgMap.get(EmqxService.MSG_ZNJJ_TYPE)) {
    		//如果是当前 znjj 则使用初始化 各个类型 数据方式    算了 不单独处理了
    		// 获取物模型,设置默认值
    		String deviceModelType = product.getDeviceModelType();
            String thingsModels = thingsModelService.getCacheThingsModelByProductId(productId);
            JSONObject thingsModelObject = JSONObject.parseObject(thingsModels);
            JSONArray properties = thingsModelObject.getJSONArray("properties");
            JSONArray functions = thingsModelObject.getJSONArray("functions");
            List<ThingsModelValueItem> valueList = properties.toJavaList(ThingsModelValueItem.class);
            valueList.addAll(functions.toJavaList(ThingsModelValueItem.class));
            valueList.forEach(x -> {
                x.setValue("");
                x.setShadow("");
                if("name".equals(x.getId())) {
                	if("1001".equals(deviceModelType) || "1011".equals(deviceModelType)  || "1002".equals(deviceModelType)  || "1012".equals(deviceModelType) ) {
                		x.setValue("");
                	}else if("1003".equals(deviceModelType) || "1013".equals(deviceModelType)) {
                		x.setValue(",");
                	}else if("1004".equals(deviceModelType) || "1014".equals(deviceModelType)) {
                		x.setValue(",,");
                	}
                }
                
            });
            return valueList;
    	}else {
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
       
    }
    
    @Override
    public AjaxResult updateDeviceBaseInfo(UpdateDeviceBaseInfoReqDto dto, Long userId) {
    	Device device = new Device();
    	Long deviceId = dto.getDeviceId();
    	boolean adminPerm = familyDeviceService.isAdminPerm(deviceId, userId);
    	if(!adminPerm) {
    		return AjaxResult.error("权限不足,请联系管理员");
    	}
    	device.setDeviceId(dto.getDeviceId());
    	if(dto.getIsFamilyUsual()!=null) {
    		Integer isFamilyUsual = dto.getIsFamilyUsual();
    		if(isFamilyUsual.intValue()!=0 && isFamilyUsual.intValue()!=1) {
    			isFamilyUsual = null;//错误值 不做更新
    		}
    		device.setIsFamilyUsual(isFamilyUsual);
    	}
    	device.setBelongRoomId(dto.getBelongRoomId());
    	device.setDeviceName(dto.getDeviceName());
        device.setUpdateTime(DateUtils.getNowDate());
        deviceMapper.updateDevice(device);
        return AjaxResult.success("修改成功", 1);
    }
    @Override
    public AjaxResult deleteDeviceReq(Long deviceId, Long userId,String au) {
    	boolean adminPerm = familyDeviceService.isAdminPerm(deviceId, userId);
    	if(!adminPerm) {
    		return AjaxResult.error("权限不足,请联系管理员");
    	}
    	return deleteDeviceReq(deviceId, au);
    	
    }
    @Override
    public AjaxResult deleteDeviceReq(Long deviceId,String au) {
    	Device device = deviceMapper.selectDeviceByDeviceId(deviceId);
    	Long familyId = device.getBelongFamilyId();
    	if(familyId==null) {
    		//删除后会更新家庭信息为空,网关信息为空
    		return AjaxResult.error("当前设备已删除,无需重复删除");
    	}
    	Family family = familyMapper.selectFamilyOnlyByFamilyId(familyId);
    	String gatewaySn = family.getGatewaySn();
    	//发布一个模拟app  进入网关查找设备模式
    	String topic = IotConstant.ZNJJ_APP_PREFIX +gatewaySn;
    	JSONObject pushMessage = new JSONObject();
    	pushMessage.put("au", au);
    	pushMessage.put("sn", device.getSerialNumber());
//		pushMessage.put("fid", 1999);无效  网关不返回 用不了
    	pushMessage.put("fun",IotOperMsgConstant.APP_REQ_GW_FUN_DELDEV);
    	emqxClient.publish(1, false, topic, pushMessage.toString());
    	// 异步接受到mq消息后 使用websocket 推送给客户端(所有订阅该网关的)
    	return AjaxResult.success("请求成功");
    	
    }
    @Override
    public int deleteDeviceReqSync(Long deviceId,String au) {
    	Device device = deviceMapper.selectDeviceByDeviceId(deviceId);
    	Long familyId = device.getBelongFamilyId();
    	if(familyId==null) {
    		//删除后会更新家庭信息为空,网关信息为空
    		log.info("当前设备已删除,无需重复删除");
    		return 1;
    	}
    	Family family = familyMapper.selectFamilyOnlyByFamilyId(familyId);
    	String gatewaySn = family.getGatewaySn();
    	//发布一个模拟app  进入网关查找设备模式
    	String topic = IotConstant.ZNJJ_APP_PREFIX +gatewaySn;
    	JSONObject pushMessage = new JSONObject();
    	pushMessage.put("au", au);
    	pushMessage.put("sn", device.getSerialNumber());
//		pushMessage.put("fid", 1999);无效  网关不返回 用不了
    	pushMessage.put("fun",IotOperMsgConstant.APP_REQ_GW_FUN_DELDEV);
    	emqxClient.publish(1, false, topic, pushMessage.toString());
    	transfer.createOperFunReq(gatewaySn, IotOperMsgConstant.APP_REQ_GW_FUN_DELDEV);
    	Boolean operFunResp = transfer.getOperFunResp(gatewaySn, IotOperMsgConstant.APP_REQ_GW_FUN_DELDEV);
    	log.info("删除网关设备同步请求结果:"+operFunResp);
    	return operFunResp?1:0;
    	
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteDeviceActual(Long deviceId) throws SchedulerException {
    	Device device = deviceMapper.selectDeviceByDeviceId(deviceId);
    	if(device==null) {
    		//删除后会更新家庭信息为空,网关信息为空   ---  调整为直接删除
    		log.warn("当前设备已删除,请确认.....设备id:{}",deviceId);
    	}else {
    		//清除设备表中的家庭信息  不用了 直接删除设备  省的麻烦
//    		deviceMapper.clearDeviceFamilyInfo(deviceId);
    		deviceService .deleteDeviceAllInfoByDeviceId(deviceId);
    	}
    	
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteCsDeviceNoGw(Long deviceId) throws SchedulerException {
    	Device device = deviceMapper.selectDeviceByDeviceId(deviceId);
    	if(device==null) {
    		//删除后会更新家庭信息为空,网关信息为空   ---  调整为直接删除
    		log.warn("当前设备已删除,请确认.....设备id:{}",deviceId);
    	}else {
    		//清除设备表中的家庭信息  不用了 直接删除设备  省的麻烦
    		deviceService .deleteCsDeviceAllInfoNoGwByDeviceId(deviceId);
    	}
    	
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteDeviceActualBySn(String sn) throws SchedulerException {
    	Device device = deviceMapper.selectDeviceBySerialNumber(sn);
    	if(device==null) {
    		//删除后会更新家庭信息为空,网关信息为空   ---  调整为直接删除
    		log.warn("当前设备已删除,请确认.....设备sn:{}",sn);
    	}else {
    		deleteDeviceActual(device.getDeviceId());
    	}
    	
    }
    @Deprecated
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteDeviceActualByFamilyId(Long familyId) throws SchedulerException {
    	//查询家庭所有设备  循环删除
    	List<DeviceBriefRespInfo> deviceList = deviceMapper.selectRoomDeviceList(null, familyId);
    	for(DeviceBriefRespInfo dev:deviceList) {
    		deleteDeviceActual(dev.getDeviceId());
    	}
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteCsDeviceNoGwByFamilyId(Long familyId) throws SchedulerException {
    	//查询家庭所有设备  循环删除
    	List<DeviceBriefRespInfo> deviceList = deviceMapper.selectRoomDeviceList(null, familyId);
    	for(DeviceBriefRespInfo dev:deviceList) {
    		deleteCsDeviceNoGw(dev.getDeviceId());
    	}
    }
    
    @Override
    public AjaxResult getAllDeviceGwInfo(Long familyId,String au,Long userId) {
    	Family family = familyMapper.selectFamilyOnlyByFamilyId(familyId);
    	boolean commonPerm = familyService.isCommonPerm(familyId, userId);
		if(!commonPerm){
			return AjaxResult.error("无权限");
		}
		Integer isBind = family.getIsBind();
		if(isBind.intValue() != 1) {
			return AjaxResult.error("家庭未绑定网关");
		}
		String gatewaySn = family.getGatewaySn();
		//发布一个模拟app  进入网关查找设备模式
		String topic = IotConstant.ZNJJ_APP_PREFIX +gatewaySn;
		JSONObject pushMessage = new JSONObject();
		pushMessage.put("au", au);
		pushMessage.put("fun",IotOperMsgConstant.APP_REQ_GW_FUN_GETDEVSRT);
		emqxClient.publish(1, false, topic, pushMessage.toString());
		// 异步接受到mq消息后 使用websocket 推送给客户端(所有订阅该网关的)
		return AjaxResult.success("请求成功");
    	
    }
    @Override
    public AjaxResult updateDeviceStatusAndThingsModelById(Device device) {
    	
    	Device updateDevice = new Device();
    	updateDevice.setDeviceId(device.getDeviceId());
    	updateDevice.setUpdateTime(DateUtils.getNowDate());
    	updateDevice.setStatus(device.getStatus());
//    	updateDevice.setThingsModelValue(JSONObject.toJSONString(getThingsModelDefaultValue(device.getProductId())));
    	updateDevice.setThingsModelValue(device.getThingsModelValue());
        deviceMapper.updateDevice(updateDevice);
        return AjaxResult.success("修改成功", 1);
    }
    /**
     * 获取物模型值
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
    
    @Override
    public AjaxResult updateDeviceModelName(Long deviceId, Long userId,String value) {
    	boolean adminPerm = familyDeviceService.isAdminPerm(deviceId, userId);
    	if(!adminPerm) {
    		return AjaxResult.error("权限不足,请联系管理员");
    	}
    	Device device = deviceMapper.selectDeviceByDeviceId(deviceId);
    	String deviceModelType = device.getDeviceModelType();
    	Long familyId = device.getBelongFamilyId();
    	Family family = familyMapper.selectFamilyOnlyByFamilyId(familyId);
    	String gatewaySn = family.getGatewaySn();
    	String param = "name";
    	ThingsModel thingsModel = thingsModelService.selectThingsModelByIdentifier(device.getProductId(), param);
    	Integer type2 = thingsModel.getType();
    	if(thingsModel==null || (1!=type2.intValue())) {
    		return AjaxResult.error("名称属性不存在");
    	}
    	Object typeValue = null;
    	String datatype = thingsModel.getDatatype();
    	if("integer".equals(datatype) || "bool".equals(datatype) ) {
    		typeValue = Integer.valueOf(value);
    	}else if("string".equals(datatype)) {
    		typeValue = value;
    	}else if("decimal".equals(datatype)) {
    		typeValue = new BigDecimal(value);
    	}else if("array".equals(datatype)) {
    		String specs = thingsModel.getSpecs();
    		JSONObject parseObject = JSONObject.parseObject(specs);
    		String subType = parseObject.getString("arrayType");
    		if("string".equals(subType) ) {
    			if(Stream.of("1001","1002","1003","1004","1005","1011","1012","1013","1014","1015").anyMatch(p->deviceModelType.equals(p))) {
    				List<ThingsModelValueItem> thingsModelValues = JSONObject.parseArray(device.getThingsModelValue(), ThingsModelValueItem.class);
    				for(ThingsModelValueItem item:thingsModelValues) {
    					if(param.equals(item.getId())) {
    						String itemValue = item.getValue();
    						if(StringUtils.isBlank(itemValue)) {
    							item.setValue(value);
    						}else {
    							String[] names = itemValue.split(",",-1);
    							String[] split = value.split(",",-1);
    							if(names.length!=split.length) {
    								return AjaxResult.success("数据格式有误");
    							}
    							for(int i=0;i<split.length;i++) {
    								String s = split[i];
    								if(StringUtils.isBlank(s)) {
    									//当前为空 ,则当前按键名称不修改
    								}else {
    									names[i] = s;
    								}
    							}
    							itemValue = StringUtils.join(names, ',');//最新名称
    							item.setValue(itemValue);
    						}
    						device.setThingsModelValue(JSONObject.toJSONString(thingsModelValues));
    						familyDeviceService.updateDeviceStatusAndThingsModelById(device);
    						break;
    					}
    				}
    			}else {
    				return AjaxResult.success("暂不支持该设备");
    			}
    		}
    	}else {
    		return AjaxResult.success("暂不支持该类型数据");
    	}
    	return AjaxResult.success("修改成功");
    	
    }
    @Override
    public AjaxResult operDeviceFun(Long deviceId, Long userId,String au,String param,String value,Long fid) {
    	boolean adminPerm = familyDeviceService.isCommonPerm(deviceId, userId);
    	if(!adminPerm) {
    		return AjaxResult.error("权限不足,请联系管理员");
    	}
    	return familyDeviceService.operDeviceFunNoPerm(deviceId, userId, au, param, value, fid);
    	
    }
    @Override
    public AjaxResult operDeviceFunNoPerm(Long deviceId, Long userId,String au,String param,String value,Long fid) {
    	Device device = deviceMapper.selectDeviceByDeviceId(deviceId);
    	String deviceModelType = device.getDeviceModelType();
    	Long familyId = device.getBelongFamilyId();
    	Family family = familyMapper.selectFamilyOnlyByFamilyId(familyId);
    	String gatewaySn = family.getGatewaySn();
    	//发布一个模拟app  进入网关查找设备模式
    	String topic = IotConstant.ZNJJ_APP_PREFIX +gatewaySn;
    	JSONObject pushMessage = new JSONObject();
    	pushMessage.put("au", au);
    	pushMessage.put("sn", device.getSerialNumber());
    	pushMessage.put("fid", fid);//有些接口有值 有些没有
    	pushMessage.put("fun",param);
    	ThingsModel thingsModel = thingsModelService.selectThingsModelByIdentifier(device.getProductId(), param);
    	if(thingsModel==null) {
    		return AjaxResult.error("暂不支持该功能");
    	}
    	Integer type2 = thingsModel.getType();
    	if(thingsModel==null || (type2!=2&&type2!=4)) {
    		return AjaxResult.error("参数有误");
    	}
    	Object typeValue = null;
    	String logText = "";//设备日志文本描述
    	String datatype = thingsModel.getDatatype();
    	if("integer".equals(datatype) || "bool".equals(datatype) ) {
    		if(Stream.of("2001","2002").anyMatch(p->deviceModelType.equals(p))) {
    			int pow = Integer.valueOf(value);
    			pushMessage.put("pow", pow);
    			logText = pow==1?"通电":pow==0?"断电":pow==2?"复位":"其他";
    		}else if(Stream.of("4001","4002").anyMatch(p->deviceModelType.equals(p))) {
//    			int pow = Integer.valueOf(value);
//    			pushMessage.put("pow", pow);
    			if(!IotOperMsgConstant.APP_REQ_GW_FUN_RSTYGRQ.equals(param)) {
					return AjaxResult.success("暂不支持该操作");
				}
    			logText = "复位报警";
    		}else{
    			return AjaxResult.error("暂不支持该设备");
    		}
    	}else if("string".equals(datatype)) {
    		typeValue = value;
    	}else if("decimal".equals(datatype)) {
    		typeValue = new BigDecimal(value);
    	}else if("array".equals(datatype)) {
    		String specs = thingsModel.getSpecs();
    		JSONObject parseObject = JSONObject.parseObject(specs);
    		String subType = parseObject.getString("arrayType");
    		if("int".equals(subType) || "bool".equals(subType) ) {
    			if(Stream.of("1001","1002","1003","1004","1005","1011","1012","1013","1014","1015").anyMatch(p->deviceModelType.equals(p))) {
    				if(!"OnOff".equals(param)) {
    					return AjaxResult.success("暂不支持该操作");
    				}
    				String[] split = value.split(",",-1);
    				for(int i=0;i<split.length;i++) {
    					String s = split[i];
    					if(StringUtils.isBlank(s)) {
    						//当前为空 ,则当前按键不操作
    						logText += "保持,";
    					}else {
    						int ope = Integer.valueOf(s);
    						pushMessage.put("s"+(i+1), ope);
    						logText += ope==1?"打开,":"关闭,";
    					}
    				}
    				logText = logText.substring(0,logText.length()-1);
    			}else if(Stream.of("3001","3002","3011","3012").anyMatch(p->deviceModelType.equals(p))) {
    				if(!"clctr".equals(param)) {
    					return AjaxResult.success("暂不支持该操作");
    				}
    				String[] split = value.split(",",-1);
    				for(int i=0;i<split.length;i++) {
    					String s = split[i];
    					if(StringUtils.isBlank(s)) {
    						//当前为空 ,则当前按键不操作
    						logText += "保持,";
    					}else {
    						int ope = Integer.valueOf(s);
    						pushMessage.put("ac"+(i+1), ope);
    						logText += ope==1?"拉开,":ope==2?"拉上,":ope==4?"暂停,":ope==3?"保持,":"其他,";
    					}
    				}
    				logText = logText.substring(0,logText.length()-1);
    			}else {
    				return AjaxResult.error("暂不支持该设备");
    			}
    		}
    	}else {
    		return AjaxResult.error("暂不支持该类型数据");
    	}
    	//    	pushMessage.put(param,typeValue );
    	familyDeviceService.saveLog(device, param, value, logText, 2, userId);//功能调用日志
    	emqxClient.publish(1, false, topic, pushMessage.toString());
    	// 异步接受到mq消息后 使用websocket 推送给客户端(所有订阅该网关的)
    	return AjaxResult.success("请求成功");
    	
    }
    
    @Override
    public void saveLog(Device device,String identity,String value,String logText,Integer logType,Long userId) {
    	// 添加到设备日志
        DeviceLog deviceLog = new DeviceLog();
        deviceLog.setDeviceId(device.getDeviceId());
        deviceLog.setDeviceName(device.getDeviceName());
        deviceLog.setLogValue(value);
        deviceLog.setLogText(logText);
//        deviceLog.setRemark();
        deviceLog.setSerialNumber(device.getSerialNumber());
        deviceLog.setIdentity(identity);
        deviceLog.setLogType(logType);
        deviceLog.setIsMonitor(0);
        deviceLog.setUserId(userId);
        deviceLog.setCreateTime(DateUtils.getNowDate());
        // 1=影子模式，2=在线模式，3=其他
        deviceLog.setMode(2);
        logService.saveDeviceLog(deviceLog);
    }
    @Override
    public AjaxResult setDeviceParamReq(Long deviceId, Long userId,String au,String type,String param,String value) {
    	boolean adminPerm = familyDeviceService.isCommonPerm(deviceId, userId);
    	if(!adminPerm) {
    		return AjaxResult.error("权限不足,请联系管理员");
    	}
    	Device device = deviceMapper.selectDeviceByDeviceId(deviceId);
    	String deviceModelType = device.getDeviceModelType();
    	Long familyId = device.getBelongFamilyId();
    	Family family = familyMapper.selectFamilyOnlyByFamilyId(familyId);
    	String gatewaySn = family.getGatewaySn();
    	//发布一个模拟app  进入网关查找设备模式
		String topic = IotConstant.ZNJJ_APP_PREFIX +gatewaySn;
		JSONObject pushMessage = new JSONObject();
		pushMessage.put("au", au);
		pushMessage.put("sn", device.getSerialNumber());
		pushMessage.put("item", type);
//		pushMessage.put("fid", 1999);无效  网关不返回 用不了
		ThingsModel thingsModel = thingsModelService.selectThingsModelByIdentifier(device.getProductId(), param);
		if(thingsModel==null) {
			return AjaxResult.success("参数有误");
		}
		Integer type2 = thingsModel.getType();
		if(thingsModel==null || (type2!=1&&type2!=4)) {
			return AjaxResult.success("参数有误");
		}
		Object typeValue = null;
		String datatype = thingsModel.getDatatype();
		if("integer".equals(datatype) || "bool".equals(datatype) ) {
			typeValue = Integer.valueOf(value);
		}else if("string".equals(datatype)) {
			typeValue = value;
		}else if("decimal".equals(datatype)) {
			typeValue = new BigDecimal(value);
		}else if("array".equals(datatype)) {
			String specs = thingsModel.getSpecs();
			JSONObject parseObject = JSONObject.parseObject(specs);
			String subType = parseObject.getString("arrayType");
			List typeArrayValue = new ArrayList<>();
			if("int".equals(subType) || "bool".equals(subType) ) {
				if(Stream.of("1011","1012","1013","1014","1015","3011","3012").anyMatch(p->deviceModelType.equals(p))) {
    				if(!"aorders".equals(param)) {
    					return AjaxResult.success("暂不支持该操作");
    				}
    				String[] split = value.split(",",-1);
    				for(int i=0;i<split.length;i++) {
    					String s = split[i];
    					if(StringUtils.isBlank(s)) {
    						
    					}else {
    						int ope = Integer.valueOf(s);
    						typeArrayValue.add(ope);
    					}
    				}
    			}else {
    				return AjaxResult.success("暂不支持该操作");
    			}
			}
			typeValue = typeArrayValue;
		}else {
			return AjaxResult.success("暂不支持该参数");
		}
//		1 TODO
		pushMessage.put("fun",IotOperMsgConstant.COMMON_PARMAS_SET);
		pushMessage.put(param,typeValue );
		emqxClient.publish(1, false, topic, pushMessage.toString());
		// 异步接受到mq消息后 使用websocket 推送给客户端(所有订阅该网关的)
		return AjaxResult.success("请求成功");
    	
    }
    @Override
    public AjaxResult getDeviceParamReq(Long deviceId, Long userId,String au,String type) {
    	boolean adminPerm = familyDeviceService.isCommonPerm(deviceId, userId);
    	if(!adminPerm) {
    		return AjaxResult.error("权限不足,请联系管理员");
    	}
    	Device device = deviceMapper.selectDeviceByDeviceId(deviceId);
    	Long familyId = device.getBelongFamilyId();
    	Family family = familyMapper.selectFamilyOnlyByFamilyId(familyId);
    	String gatewaySn = family.getGatewaySn();
    	//发布一个模拟app  进入网关查找设备模式
    	String topic = IotConstant.ZNJJ_APP_PREFIX +gatewaySn;
    	JSONObject pushMessage = new JSONObject();
    	pushMessage.put("au", au);
    	pushMessage.put("sn", device.getSerialNumber());
    	pushMessage.put("item", type);
//		pushMessage.put("fid", 1999);无效  网关不返回 用不了
    	//TODO 考虑一下是否 同步返回
    	pushMessage.put("fun",IotOperMsgConstant.COMMON_PARMAS_GET);
    	emqxClient.publish(1, false, topic, pushMessage.toString());
    	// 异步接受到mq消息后 使用websocket 推送给客户端(所有订阅该网关的)
    	return AjaxResult.success("请求成功");
    	
    }
    
    @Override
    public boolean isAdminPerm(Long deviceId, Long userId) {
    	Device oldDevice = deviceMapper.selectDeviceByDeviceId(deviceId);
    	if(oldDevice==null) {
    		log.error("设备信息不存在");
    		return false;
    	}
    	Long familyId = oldDevice.getBelongFamilyId();
    	if(familyId==null) {
    		log.error("设备家庭信息不存在");
    		return false;
    	}
    	FamilyUserRela loginUserRela = familyMapper.selectUserByFamilyIdAndUserId(familyId, userId);
		if(loginUserRela==null){
    		//非家庭成员 不允许
			log.error("非家庭成员");
    		return false;
    	}
		//权限校验  
		if(!"2".equals(loginUserRela.getFamilyUserRole())){
			log.info("普通家庭成员");
			return false;
		}else {
			log.info("管理员");
			return true;
		}
    }
    @Override
    public boolean isCommonPerm(Long deviceId, Long userId) {
    	Device oldDevice = deviceMapper.selectDeviceByDeviceId(deviceId);
    	if(oldDevice==null) {
    		log.error("设备信息不存在");
    		return false;
    	}
    	Long familyId = oldDevice.getBelongFamilyId();
    	if(familyId==null) {
    		log.error("设备家庭信息不存在");
    		return false;
    	}
    	FamilyUserRela loginUserRela = familyMapper.selectUserByFamilyIdAndUserId(familyId, userId);
    	if(loginUserRela==null){
    		//非家庭成员 不允许
    		log.error("非家庭成员");
    		return false;
    	}else {
    		log.info("家庭成员");
    		return true;
    		
    	}
    }
	@Override
	public AjaxResult upgradeGateway(Long deviceId, String firmwarePath,String version, String remark) {
		Device device = deviceMapper.selectDeviceByDeviceId(deviceId);
    	Long familyId = device.getBelongFamilyId();
    	Family family = familyMapper.selectFamilyOnlyByFamilyId(familyId);
    	String familyName = null;
    	if(family!=null) {
    		familyName = family.getName();
    	}
//    	String gatewaySn = family.getGatewaySn();
		String gatewaySn = device.getSerialNumber();
		DeviceOtaUpdate update = new DeviceOtaUpdate();
		update.setDeviceId(deviceId);
		update.setStatus(0);
		List<DeviceOtaUpdate> list = deviceOtaUpdateService.selectDeviceOtaUpdateList(update );
		if(!list.isEmpty()) {
			return AjaxResult.error("设备升级中,请耐心等待");
		}
    	//发布一个模拟app  进入网关查找设备模式
    	String topic = IotConstant.ZNJJ_APP_PREFIX +gatewaySn;
    	JSONObject pushMessage = new JSONObject();
    	pushMessage.put("au", transfer.getAuInfo());
//		pushMessage.put("fid", 1999);无效  网关不返回 用不了
		pushMessage.put("url", firmwarePath);
    	pushMessage.put("fun",IotOperMsgConstant.CS_REQ_GW_FUN_ASKOTAUPD);
    	emqxClient.publish(1, false, topic, pushMessage.toString());
    	
    	DeviceOtaUpdate ota = new DeviceOtaUpdate();
		ota.setDeviceId(deviceId);
		ota.setDeviceName(device.getDeviceName());
		ota.setFamilyId(familyId);
		ota.setFamilyName(familyName);
		ota.setStatus(0);//升级中
		ota.setFirmwarePath(firmwarePath);
		ota.setRemark(remark);
		ota.setOtaVersion(version);
		deviceOtaUpdateService.insertDeviceOtaUpdate(ota);
//		Task
		
    	new Thread(()->{
    		transfer.createOperFunReq(gatewaySn, IotOperMsgConstant.CS_REQ_GW_FUN_ASKOTAUPD);
    		transfer.createOperFunReq(gatewaySn, IotOperMsgConstant.GW_REQ_GW_FUN_OTADONE);//模拟一个完成的请求
    		//5分钟时间下载 固件
    		Boolean operFunResp = transfer.getOperFunResp(gatewaySn, IotOperMsgConstant.CS_REQ_GW_FUN_ASKOTAUPD,300);
    		if(operFunResp) {
    			Boolean operFunResp2 = transfer.getOperFunResp(gatewaySn, IotOperMsgConstant.GW_REQ_GW_FUN_OTADONE,300);
    			if(operFunResp2) {
    				//升级成功
    				ota.setStatus(1);//成功
    				deviceOtaUpdateService.updateDeviceOtaUpdate(ota);
        		}else {
        			//升级失败
        			ota.setStatus(2);//失败
        			deviceOtaUpdateService.updateDeviceOtaUpdate(ota);
        		}
    		}else {
    			//固件下载失败也需要获取done事件结果  否则数据有误
    			Boolean operFunResp2 = transfer.getOperFunResp(gatewaySn, IotOperMsgConstant.GW_REQ_GW_FUN_OTADONE,10);
    			//升级失败
    			ota.setStatus(2);//失败
    			deviceOtaUpdateService.updateDeviceOtaUpdate(ota);
    		}
    	}).start();
    	// 异步接受到mq消息后 使用websocket 推送给客户端(所有订阅该网关的)
    	return AjaxResult.success("设备升级中");
	}
	@Override
	public AjaxResult upgradeAllGateway(String firmwarePath,String version, String remark) {
		
    	Device device = new Device();
    	device.setDeviceType(3);//网关设备
		List<DeviceShortOutput> deviceList = deviceMapper.selectDeviceShortList2(device );
		deviceList.forEach(d->{
			log.info("升级设备:{}",d.getDeviceId());
			upgradeGateway(d.getDeviceId(), firmwarePath,version, remark);
		});
		return AjaxResult.success("设备升级中");
	}
	

	@Override
    public List<DeviceNoticeSetting> selectDeviceNoticeSettingList(DeviceNoticeSetting device){
		return deviceMapper.selectDeviceNoticeSettingList(device);
    }
	@Override
	public AjaxResult energyData(Long deviceId, Integer type, Integer unit) {
		DeviceBriefRespInfo info = deviceMapper.selectBriefDeviceByDeviceId(deviceId);
		String gatewaySn = info.getSerialNumber();
		List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
		BigDecimal oneThur = new BigDecimal(1000);
		LocalDate curDate = LocalDate.now();
		//TODO 有真实数据后需放开
//		curDate = curDate.minusMonths(1);
		int year = curDate.getYear();
		int curMonth = curDate.getMonthValue();
		int curDay = curDate.getDayOfMonth();
		int curHour = LocalDateTime.now().getHour();
		if(1==type) {
			String topic = IotConstant.ZNJJ_APP_PREFIX +gatewaySn;
			JSONObject pushMessage = new JSONObject();
			pushMessage.put("au", transfer.getAuInfo());
			pushMessage.put("fun",IotOperMsgConstant.CS_REQ_GW_FUN_GET724ELE);
			emqxClient.publish(1, false, topic, pushMessage.toString());
			transfer.createOperFunReq(gatewaySn, IotOperMsgConstant.CS_REQ_GW_FUN_GET724ELE);
			JSONObject operFunResp = transfer.getContentOperFunResp(gatewaySn, IotOperMsgConstant.CS_REQ_GW_FUN_GET724ELE);
			if(operFunResp==null) {
				log.error("获取能耗数据超时:{}",IotOperMsgConstant.CS_REQ_GW_FUN_GET724ELE);
				return AjaxResult.error("获取用电分析数据超时");
			}
			JSONArray eleValue = operFunResp.getJSONArray("eleValue");
			JSONArray eleTime = operFunResp.getJSONArray("eleTime");
			String nyr = (""+year).substring(2)+(curMonth>=10?(curMonth+""):("0"+curMonth))+(curDay>=10?(curDay+""):("0"+curDay));
			int start = new Integer(nyr+"00");
			int end = new Integer(nyr+"99");
			Map<String,Map<String, Object>> gwDataMap = new HashMap<String,Map<String,Object>>();//网关已有数据
			BigDecimal totalData = BigDecimal.ZERO;
			for(int i=0;i<eleTime.size();i++) {
				Integer date = (Integer) eleTime.get(i);
				Integer object = (Integer) eleValue.get(i);
				if(date>=start&&date<end) {
					Map<String, Object> map = new HashMap<String,Object>();
					String hour = (""+date).substring(6);
					map.put("date", hour+"时");
					BigDecimal a = 1==unit?new BigDecimal(object):new BigDecimal(object).divide(oneThur, 2, RoundingMode.HALF_UP);
					map.put("data",a);
					totalData  = totalData.add(a);
					gwDataMap.put(map.get("date").toString(), map);
				}
			}
			//24小时数据
			for(int i=0;i<24;i++) {
				String date = (i>=10?(i+""):("0"+i))+"时";
				if(i>=curHour) {
					//当日数据只展示到当前小时前一小时
					break;
				}
				if(gwDataMap.get(date)!=null) {
					list.add(gwDataMap.get(date));
				}else {
					Map<String, Object> map = new HashMap<String,Object>();
					map.put("date", date);
					BigDecimal a = BigDecimal.ZERO;
					map.put("data",a);
					list.add(map);
				}
			}
			Map<String, Object> returnMap = new HashMap<String,Object>();
			returnMap.put("list",list);
			returnMap.put("totalData",totalData);
			return AjaxResult.success(returnMap);
		}else if(2==type) {
			JSONArray dataArray = new JSONArray();
			int dayOfMonth = curDate.getDayOfMonth();
			if(dayOfMonth<=7) {
				//发布一个模拟app  进入网关查找设备模式
				int lastMonthYear = 0;
				int lastMonth = 0;
				if(curMonth==1) {
					//去年12月份
					lastMonthYear = year-1;
					lastMonth = 12;
				}else {
					lastMonthYear = year;
					lastMonth = curMonth-1;
				}
				//上月1号
				LocalDate lastMonthOne = LocalDate.of(lastMonthYear, lastMonth, 1);
				LocalDate with = lastMonthOne.with(TemporalAdjusters.lastDayOfMonth());
				int lastMonthTotalDays = with.getDayOfMonth();
				String topic = IotConstant.ZNJJ_APP_PREFIX +gatewaySn;
				JSONObject pushMessage = new JSONObject();
				pushMessage.put("au", transfer.getAuInfo());
				pushMessage.put("fun",IotOperMsgConstant.CS_REQ_GW_FUN_GETELEBYDAY);
				pushMessage.put("year", lastMonthYear);
				pushMessage.put("month", lastMonth);
				emqxClient.publish(1, false, topic, pushMessage.toString());
				transfer.createOperFunReq(gatewaySn, IotOperMsgConstant.CS_REQ_GW_FUN_GETELEBYDAY);
				JSONObject operFunResp = transfer.getContentOperFunResp(gatewaySn, IotOperMsgConstant.CS_REQ_GW_FUN_GETELEBYDAY);
				if(operFunResp==null) {
					log.error("获取能耗数据超时:{}",IotOperMsgConstant.CS_REQ_GW_FUN_GETELEBYDAY);
					return AjaxResult.error("获取用电分析数据超时");
				}
				JSONArray jsonArray = operFunResp.getJSONArray("eleValue");
				for(int i=0;i<lastMonthTotalDays;i++) {
					dataArray.add(jsonArray.get(i));
				}
				dayOfMonth = dayOfMonth+lastMonthTotalDays;
				try {
					Thread.sleep(500L);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//发布一个模拟app  进入网关查找设备模式
			String topic = IotConstant.ZNJJ_APP_PREFIX +gatewaySn;
			JSONObject pushMessage = new JSONObject();
			pushMessage.put("au", transfer.getAuInfo());
			pushMessage.put("fun",IotOperMsgConstant.CS_REQ_GW_FUN_GETELEBYDAY);
			pushMessage.put("year", curDate.getYear());
			pushMessage.put("month", curMonth);
			emqxClient.publish(1, false, topic, pushMessage.toString());
			transfer.createOperFunReq(gatewaySn, IotOperMsgConstant.CS_REQ_GW_FUN_GETELEBYDAY);
			JSONObject operFunResp = transfer.getContentOperFunResp(gatewaySn, IotOperMsgConstant.CS_REQ_GW_FUN_GETELEBYDAY);
			if(operFunResp==null) {
				log.error("获取能耗数据超时:{}",IotOperMsgConstant.CS_REQ_GW_FUN_GETELEBYDAY);
				return AjaxResult.error("获取用电分析数据超时");
			}
			JSONArray jsonArray = operFunResp.getJSONArray("eleValue");
			dataArray.addAll(jsonArray);
			DateTimeFormatter dateFor = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			BigDecimal totalData = BigDecimal.ZERO;
			for(int j=7;j>0;j--) {
				Integer object = (Integer) dataArray.get(dayOfMonth-j-1);
				Map<String, Object> map = new HashMap<String,Object>();
				map.put("date", curDate.minusDays(j).format(dateFor));
				BigDecimal a = 1==unit?new BigDecimal(object):new BigDecimal(object).divide(oneThur, 2, RoundingMode.HALF_UP);
				totalData  = totalData.add(a);
				map.put("data",a);
				list.add(map);
			}
			Map<String, Object> returnMap = new HashMap<String,Object>();
			returnMap.put("list",list);
			returnMap.put("totalData",totalData);
			return AjaxResult.success(returnMap);
		}else if(3==type) {
			BigDecimal totalData = BigDecimal.ZERO;
			for(int m = 12;m>=0;m--) {
				LocalDate queryDate = curDate.minusMonths(m);
				//发布一个模拟app  进入网关查找设备模式
				String topic = IotConstant.ZNJJ_APP_PREFIX +gatewaySn;
				JSONObject pushMessage = new JSONObject();
				pushMessage.put("au", transfer.getAuInfo());
				pushMessage.put("fun",IotOperMsgConstant.CS_REQ_GW_FUN_GETELEBYDAY);
				pushMessage.put("year", queryDate.getYear());
				pushMessage.put("month", queryDate.getMonthValue());
				emqxClient.publish(1, false, topic, pushMessage.toString());
				transfer.createOperFunReq(gatewaySn, IotOperMsgConstant.CS_REQ_GW_FUN_GETELEBYDAY);
				JSONObject operFunResp = transfer.getContentOperFunResp(gatewaySn, IotOperMsgConstant.CS_REQ_GW_FUN_GETELEBYDAY);
				if(operFunResp==null) {
					log.error("获取能耗数据超时:{}",IotOperMsgConstant.CS_REQ_GW_FUN_GETELEBYDAY);
					return AjaxResult.error("获取用电分析数据超时");
				}
				JSONArray jsonArray = operFunResp.getJSONArray("eleValue");
				DateTimeFormatter dateFor = DateTimeFormatter.ofPattern("yyyy年MM月");
				
				Map<String, Object> map = new HashMap<String,Object>();
				map.put("date", queryDate.format(dateFor));
				BigDecimal total = BigDecimal.ZERO;
				for(int i=0;i<jsonArray.size();i++) {
					Integer object = (Integer) jsonArray.get(i);
					BigDecimal a = 1==unit?new BigDecimal(object):new BigDecimal(object).divide(oneThur, 2, RoundingMode.HALF_UP);
					total = total.add(a);
				}
				totalData  = totalData.add(total);
				map.put("data",total);
				list.add(map);
				try {
					Thread.sleep(500L);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Map<String, Object> returnMap = new HashMap<String,Object>();
			returnMap.put("list",list);
			returnMap.put("totalData",totalData);
			return AjaxResult.success(returnMap);
		}else if(4==type) {
			String topic = IotConstant.ZNJJ_APP_PREFIX +gatewaySn;
			JSONObject pushMessage = new JSONObject();
			pushMessage.put("au", transfer.getAuInfo());
			pushMessage.put("fun",IotOperMsgConstant.CS_REQ_GW_FUN_GET724ELE);
			emqxClient.publish(1, false, topic, pushMessage.toString());
			transfer.createOperFunReq(gatewaySn, IotOperMsgConstant.CS_REQ_GW_FUN_GET724ELE);
			JSONObject operFunResp = transfer.getContentOperFunResp(gatewaySn, IotOperMsgConstant.CS_REQ_GW_FUN_GET724ELE);
			if(operFunResp==null) {
				log.error("获取能耗数据超时:{}",IotOperMsgConstant.CS_REQ_GW_FUN_GET724ELE);
				return AjaxResult.error("获取用电分析数据超时");
			}
			JSONArray eleValue = operFunResp.getJSONArray("eleValue");
			JSONArray eleTime = operFunResp.getJSONArray("eleTime");
			LocalDate before7 = curDate.minusDays(7);
			int before7year = before7.getYear();
			int before7curMonth = before7.getMonthValue();
			int before7curDay = before7.getDayOfMonth();
			String nyr = (""+year).substring(2)+(curMonth>=10?(curMonth+""):("0"+curMonth))+(curDay>=10?(curDay+""):("0"+curDay));
			String nyrbefore7 = (""+before7year).substring(2)+(before7curMonth>=10?(before7curMonth+""):("0"+before7curMonth))+(before7curDay>=10?(before7curDay+""):("0"+before7curDay));
			int start = new Integer(nyrbefore7+"00");
			int end = new Integer(nyr+"99");
			Map<String,Map<String, Object>> gwDataMap = new HashMap<String,Map<String,Object>>();//网关已有数据
			BigDecimal totalData = BigDecimal.ZERO;
			for(int i=0;i<eleTime.size();i++) {
				Integer date = (Integer) eleTime.get(i);
				Integer object = (Integer) eleValue.get(i);
				if(date>=start&&date<end) {
					Map<String, Object> map = new HashMap<String,Object>();
					String day = (""+date).substring(4,6)+"日";
					String dateS = day+(""+date).substring(6)+"时";
					map.put("date", dateS);
					BigDecimal a = 1==unit?new BigDecimal(object):new BigDecimal(object).divide(oneThur, 2, RoundingMode.HALF_UP);
					map.put("data",a);
					totalData  = totalData.add(a);
					gwDataMap.put(map.get("date").toString(), map);
				}
			}
			for(int j=6;j>=0;j--) {
				int dayOfMonth = curDate.minusDays(j).getDayOfMonth();
				String day = (dayOfMonth>=10?(dayOfMonth+""):("0"+dayOfMonth))+"日";
				//24小时数据
				for(int i=0;i<24;i++) {
					String date = day+(i>=10?(i+""):("0"+i))+"时";
					if(gwDataMap.get(date)!=null) {
						list.add(gwDataMap.get(date));
					}else {
						Map<String, Object> map = new HashMap<String,Object>();
						map.put("date", date);
						BigDecimal a = BigDecimal.ZERO;
						map.put("data",a);
						list.add(map);
					}
				}
			}
			Map<String, Object> returnMap = new HashMap<String,Object>();
			returnMap.put("list",list);
			returnMap.put("totalData",totalData);
			return AjaxResult.success(returnMap);
		}else if(5==type) {//最近30天
			boolean isMock = false;//模拟数据
			Random random = new Random();
			JSONArray dataArray = new JSONArray();
			int dayOfMonth = curDate.getDayOfMonth();
			if(dayOfMonth<=30) {
				//发布一个模拟app  进入网关查找设备模式
				int lastMonthYear = 0;
				int lastMonth = 0;
				if(curMonth==1) {
					//去年12月份
					lastMonthYear = year-1;
					lastMonth = 12;
				}else {
					lastMonthYear = year;
					lastMonth = curMonth-1;
				}
				//上月1号
				LocalDate lastMonthOne = LocalDate.of(lastMonthYear, lastMonth, 1);
				LocalDate with = lastMonthOne.with(TemporalAdjusters.lastDayOfMonth());
				int lastMonthTotalDays = with.getDayOfMonth();
				String topic = IotConstant.ZNJJ_APP_PREFIX +gatewaySn;
				JSONObject pushMessage = new JSONObject();
				pushMessage.put("au", transfer.getAuInfo());
				pushMessage.put("fun",IotOperMsgConstant.CS_REQ_GW_FUN_GETELEBYDAY);
				pushMessage.put("year", lastMonthYear);
				pushMessage.put("month", lastMonth);
				emqxClient.publish(1, false, topic, pushMessage.toString());
				transfer.createOperFunReq(gatewaySn, IotOperMsgConstant.CS_REQ_GW_FUN_GETELEBYDAY);
				JSONObject operFunResp = transfer.getContentOperFunResp(gatewaySn, IotOperMsgConstant.CS_REQ_GW_FUN_GETELEBYDAY);
				if(operFunResp==null) {
					log.error("获取能耗数据超时:{}",IotOperMsgConstant.CS_REQ_GW_FUN_GETELEBYDAY);
					return AjaxResult.error("获取用电分析数据超时");
				}
				JSONArray jsonArray = operFunResp.getJSONArray("eleValue");
				for(int i=0;i<lastMonthTotalDays;i++) {
					dataArray.add(jsonArray.get(i));
				}
				dayOfMonth = dayOfMonth+lastMonthTotalDays;
				try {
					Thread.sleep(500L);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//发布一个模拟app  进入网关查找设备模式
			String topic = IotConstant.ZNJJ_APP_PREFIX +gatewaySn;
			JSONObject pushMessage = new JSONObject();
			pushMessage.put("au", transfer.getAuInfo());
			pushMessage.put("fun",IotOperMsgConstant.CS_REQ_GW_FUN_GETELEBYDAY);
			pushMessage.put("year", curDate.getYear());
			pushMessage.put("month", curMonth);
			emqxClient.publish(1, false, topic, pushMessage.toString());
			transfer.createOperFunReq(gatewaySn, IotOperMsgConstant.CS_REQ_GW_FUN_GETELEBYDAY);
			JSONObject operFunResp = transfer.getContentOperFunResp(gatewaySn, IotOperMsgConstant.CS_REQ_GW_FUN_GETELEBYDAY);
			if(operFunResp==null) {
				log.error("获取能耗数据超时:{}",IotOperMsgConstant.CS_REQ_GW_FUN_GETELEBYDAY);
				return AjaxResult.error("获取用电分析数据超时");
			}
			JSONArray jsonArray = operFunResp.getJSONArray("eleValue");
			dataArray.addAll(jsonArray);
			DateTimeFormatter dateFor = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			BigDecimal totalData = BigDecimal.ZERO;
			for(int j=30;j>0;j--) {
				Integer object = (Integer) dataArray.get(dayOfMonth-j-1);
				Map<String, Object> map = new HashMap<String,Object>();
				map.put("date", curDate.minusDays(j).format(dateFor));
				BigDecimal a = 1==unit?new BigDecimal(object):new BigDecimal(object).divide(oneThur, 2, RoundingMode.HALF_UP);
				while(isMock) {
					double nextDouble = random.nextDouble();
					if(nextDouble>0.2 && nextDouble<0.8) {
						a = new BigDecimal(nextDouble*100).setScale(2,RoundingMode.HALF_EVEN);
						break;
					}
				}
				totalData  = totalData.add(a);
				map.put("data",a);
				list.add(map);
			}
			Map<String, Object> returnMap = new HashMap<String,Object>();
			returnMap.put("list",list);
			returnMap.put("totalData",totalData);
			return AjaxResult.success(returnMap);
		}
		return AjaxResult.error("暂不支持的类型");
	}
	@Override
	public AjaxResult energyDataByDay(Long deviceId, Integer year, Integer month, Integer day) {
		DeviceBriefRespInfo info = deviceMapper.selectBriefDeviceByDeviceId(deviceId);
		String gatewaySn = info.getSerialNumber();
		List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
		BigDecimal oneThur = new BigDecimal(1000);
		LocalDate curDate = LocalDate.now();
		if(year == null) {
			year = curDate.getYear();
		}
		if(month == null) {
			month = curDate.getMonthValue();
		}
		if(day == null) {
			day = curDate.getDayOfMonth();
		}
		LocalDate queryDate = LocalDate.of(year, month, day);
		if(queryDate.isAfter(curDate)) {
			return AjaxResult.error("暂不支持的日期");
		}
		if(queryDate.isBefore(curDate.minusDays(6))) {
			return AjaxResult.error("暂不支持的日期");
		}
		int curHour = 25;
		if(queryDate.compareTo(curDate)==0) {
			//当天再校验小时
			curHour = LocalDateTime.now().getHour();
		}
		int unit = 2;//kwh
		JSONArray dataArray = new JSONArray();
		int dayOfMonth = curDate.getDayOfMonth();
		//发布一个模拟app  进入网关查找设备模式
		String topic = IotConstant.ZNJJ_APP_PREFIX +gatewaySn;
		JSONObject pushMessage = new JSONObject();
		pushMessage.put("au", transfer.getAuInfo());
		pushMessage.put("fun",IotOperMsgConstant.CS_REQ_GW_FUN_GET724ELE);
		emqxClient.publish(1, false, topic, pushMessage.toString());
		transfer.createOperFunReq(gatewaySn, IotOperMsgConstant.CS_REQ_GW_FUN_GET724ELE);
		JSONObject operFunResp = transfer.getContentOperFunResp(gatewaySn, IotOperMsgConstant.CS_REQ_GW_FUN_GET724ELE);
		if(operFunResp==null) {
			log.error("获取能耗数据超时:{}",IotOperMsgConstant.CS_REQ_GW_FUN_GET724ELE);
			return AjaxResult.error("获取用电分析数据超时");
		}
		JSONArray eleValue = operFunResp.getJSONArray("eleValue");
		JSONArray eleTime = operFunResp.getJSONArray("eleTime");
		String nyr = (""+year).substring(2)+(month>=10?(month+""):("0"+month))+(day>=10?(day+""):("0"+day));
		int start = new Integer(nyr+"00");
		int end = new Integer(nyr+"99");
		Map<String,Map<String, Object>> gwDataMap = new HashMap<String,Map<String,Object>>();//网关已有数据
		BigDecimal totalData = BigDecimal.ZERO;
		for(int i=0;i<eleTime.size();i++) {
			Integer date = (Integer) eleTime.get(i);
			Integer object = (Integer) eleValue.get(i);
			if(date>=start&&date<end) {
				Map<String, Object> map = new HashMap<String,Object>();
				String hour = (""+date).substring(6);
				map.put("date", hour+"时");
				BigDecimal a = 1==unit?new BigDecimal(object):new BigDecimal(object).divide(oneThur, 2, RoundingMode.HALF_UP);
				map.put("data",a);
				totalData  = totalData.add(a);
				gwDataMap.put(map.get("date").toString(), map);
			}
		}
		//24小时数据
		for(int i=0;i<24;i++) {
			String date = (i>=10?(i+""):("0"+i))+"时";
			if(i>=curHour) {
				//当日数据只展示到当前小时前一小时
				break;
			}
			if(gwDataMap.get(date)!=null) {
				list.add(gwDataMap.get(date));
			}else {
				Map<String, Object> map = new HashMap<String,Object>();
				map.put("date", date);
				BigDecimal a = BigDecimal.ZERO;
				map.put("data",a);
				list.add(map);
			}
		}
		Map<String, Object> returnMap = new HashMap<String,Object>();
		returnMap.put("list",list);
		returnMap.put("totalData",totalData);
		return AjaxResult.success(returnMap);
	}
	@Override
	public AjaxResult energyDataByMonth(Long deviceId, Integer year, Integer month) {
		boolean isMock = true;//模拟数据
		Random random = new Random();
		DeviceBriefRespInfo info = deviceMapper.selectBriefDeviceByDeviceId(deviceId);
		String gatewaySn = info.getSerialNumber();
		List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
		BigDecimal oneThur = new BigDecimal(1000);
		LocalDate curDate = LocalDate.now();
		if(year == null) {
			year = curDate.getYear();
		}
		if(month == null) {
			month = curDate.getMonthValue();
		}
		int unit = 2;//kwh
		JSONArray dataArray = new JSONArray();
		int dayOfMonth = curDate.getDayOfMonth();
		DateTimeFormatter dateFor = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		//发布一个模拟app  进入网关查找设备模式
		BigDecimal totalData = BigDecimal.ZERO;
		int totalDay = LocalDate.of(year, month, 1).with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();

		DboxEle dboxEle = new DboxEle();
		dboxEle.setDeviceId(deviceId);
		dboxEle.setYear((long)year);
		dboxEle.setMonth((long)month);
		List<DboxEle> dboxEles = dboxEleService.selectDboxEleList(dboxEle);
		Map<Long, DboxEle> eleMap = new HashMap<>();
		for (DboxEle ele : dboxEles){
			eleMap.put(ele.getDay(),ele);
		}
		for (int j=1;j<=totalDay;j++) {
			DboxEle ele = eleMap.get(new Long(j));
			if(ele==null){
				ele = new DboxEle();
				LocalDate of = LocalDate.of(year, month, j);
				ZoneId zoneId = ZoneId.systemDefault();
				Instant instant = of.atStartOfDay().atZone(zoneId).toInstant();
				ele.setEleTime(Date.from(instant));
				ele.setEleValue(BigDecimal.ZERO);
			}
			Map<String, Object> map = new HashMap<String,Object>();
			Date eleTime = ele.getEleTime();
			Instant instant = eleTime.toInstant();
			ZoneId zoneId = ZoneId.systemDefault();
			LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);
			map.put("date", localDateTime.toLocalDate().format(dateFor));
			BigDecimal eleValue = ele.getEleValue();
			BigDecimal a = 1==unit?eleValue:eleValue.divide(oneThur, 2, RoundingMode.HALF_UP);
			totalData  = totalData.add(a);
			map.put("data",a);
			list.add(map);
		}

//		for(int j=0;j<dataArray.size();j++) {
//			if(totalDay<=j) {
//				break;
//			}
//			Integer object = (Integer) dataArray.get(j);
//			Map<String, Object> map = new HashMap<String,Object>();
//			map.put("date", LocalDate.of(year, month, j+1).format(dateFor));
//			BigDecimal a = 1==unit?new BigDecimal(object):new BigDecimal(object).divide(oneThur, 2, RoundingMode.HALF_UP);
//			while(isMock) {
//				double nextDouble = random.nextDouble();
//				if(nextDouble>0.2 && nextDouble<0.8) {
//					a = new BigDecimal(nextDouble*100).setScale(2,RoundingMode.HALF_EVEN);
//					break;
//				}
//			}
//			totalData  = totalData.add(a);
//			map.put("data",a);
//			list.add(map);
//		}
		Map<String, Object> returnMap = new HashMap<String,Object>();
		returnMap.put("list",list);
		returnMap.put("totalData",totalData);
		return AjaxResult.success(returnMap);
	}
	@Override
	public AjaxResult energyDataByYear(Long deviceId, Integer year) {
		boolean isMock = false;//模拟数据
		Random random = new Random();
		DeviceBriefRespInfo info = deviceMapper.selectBriefDeviceByDeviceId(deviceId);
		String gatewaySn = info.getSerialNumber();
		List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
		BigDecimal oneThur = new BigDecimal(1000);
		LocalDate curDate = LocalDate.now();
		if(year == null) {
			year = curDate.getYear();
		}
		
		int unit = 2;//Kwh
		BigDecimal totalData = BigDecimal.ZERO;
		for(int m = 0;m<12;m++) {
			LocalDate queryDate = curDate.withYear(year).withMonth(m+1);
			DateTimeFormatter dateFor = DateTimeFormatter.ofPattern("yyyy年MM月");

			Map<String, Object> map = new HashMap<String,Object>();
			map.put("date", queryDate.format(dateFor));
			BigDecimal total = BigDecimal.ZERO;

			DboxEle dboxEle = new DboxEle();
			dboxEle.setDeviceId(deviceId);
			dboxEle.setYear((long)queryDate.getYear());
			dboxEle.setMonth((long)queryDate.getMonthValue());
			List<DboxEle> dboxEles = dboxEleService.selectDboxEleList(dboxEle);
			for (DboxEle ele : dboxEles) {
				BigDecimal eleValue = ele.getEleValue();
				BigDecimal a = 1==unit?eleValue:eleValue.divide(oneThur, 2, RoundingMode.HALF_UP);
				total = total.add(a);
			}
			while(isMock) {
				double nextDouble = random.nextDouble();
				if(nextDouble>0.1 && nextDouble<0.5) {
					total = new BigDecimal(nextDouble*1000).setScale(2,RoundingMode.HALF_EVEN);
					break;
				}
			}
			map.put("data",total);
			totalData  = totalData.add(total);
			list.add(map);
			//TODO 查询网关才需要延迟
//			try {
//				Thread.sleep(500L);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
		Map<String, Object> returnMap = new HashMap<String,Object>();
		returnMap.put("list",list);
		returnMap.put("totalData",totalData);
		return AjaxResult.success(returnMap);
	}
	@Override
	@Transactional(rollbackFor = Exception.class)
	public AjaxResult unBindGateway(Long deviceId) throws SchedulerException {
		DeviceBriefRespInfo info = deviceMapper.selectBriefDeviceByDeviceId(deviceId);
		Long familyId = info.getBelongFamilyId();
		if(familyId==null) {
			log.info("网关已解绑  无需重复解绑--------------");
			return AjaxResult.success("网关已解绑,请勿重复操作");
		}
//    	Family family = familyMapper.selectFamilyOnlyByFamilyId(familyId);
//    	if(family==null){
//    		//网关里家庭不存在,则直接清除网关里的家庭信息就可以
//    		deviceMapper.clearDeviceFamilyInfo(info.getDeviceId());
//    		return AjaxResult.success("解绑成功");
//    	}
    	//删除设备
    	log.info("通过家庭业务删除(非网关删除)设备--------------");
    	familyDeviceService.deleteCsDeviceNoGwByFamilyId(familyId);
    	//删除家庭
    	//清除网关家庭信息
    	log.info("清除网关家庭信息---------------");
    	deviceMapper.clearDeviceFamilyInfo(info.getDeviceId());
    	log.info("清除家庭里的网关绑定信息信息---------------");
    	familyMapper.clearFamilyGwBindInfo(familyId);
    	return AjaxResult.success();
	}
	@Override
	public AjaxResult delGateway(Long deviceId) throws SchedulerException {
		log.info("先解绑网关--------------");
		familyDeviceService.unBindGateway(deviceId);
		deviceService.deleteDeviceAllInfoByDeviceId(deviceId);
		return AjaxResult.success();
	}
	@Override
	public AjaxResult airSwitchData(Long deviceId) {
		DeviceBriefRespInfo info = deviceMapper.selectBriefDeviceByDeviceId(deviceId);
		String gatewaySn = info.getSerialNumber();
		//发布一个模拟app  进入网关查找设备模式
		String topic = IotConstant.ZNJJ_APP_PREFIX +gatewaySn;
		JSONObject pushMessage = new JSONObject();
		pushMessage.put("au", transfer.getAuInfo());
		pushMessage.put("fun",IotOperMsgConstant.CS_REQ_GW_FUN_GETPOWDEV2);
		emqxClient.publish(1, false, topic, pushMessage.toString());
		transfer.createOperFunReq(gatewaySn, IotOperMsgConstant.CS_REQ_GW_FUN_GETPOWDEV2);
		JSONObject operFunResp = transfer.getContentOperFunResp(gatewaySn, IotOperMsgConstant.CS_REQ_GW_FUN_GETPOWDEV2);
		if(operFunResp==null) {
			log.error("获取配电数据超时:{}",IotOperMsgConstant.CS_REQ_GW_FUN_GETPOWDEV2);
			return AjaxResult.error("获取配电信息失败");
		}
		
		String dboxType = operFunResp.getString("dboxType")+"-"+operFunResp.getString("powDevsCRC");
		Dbox dbox = dboxService.selectDboxByType(dboxType);
		if(dbox==null) {
			return AjaxResult.error("未查询到配电信息,请联系管理员");
		}
		PowerDistribution pd = new PowerDistribution();
		DboxSwitch query = new DboxSwitch();
//		query.setDboxId(dbox.getId());
		query.setEnable(1);
		List<DboxSwitch> dboxSwitch = dboxSwitchService.selectDboxSwitchList(query );
		List<DetectingBoard> dbs = new ArrayList<DetectingBoard>();
		//以类型-状态 来标识唯一键  例如 4031-4
		Map<String, DboxSwitch> dboxSwitchMap = new HashMap<String, DboxSwitch>();
		Map<String, List<DboxSwitch>> dboxSwitchTypeMap = dboxSwitch.stream().collect(Collectors.groupingBy(DboxSwitch::getSwitchType));
		dboxSwitchTypeMap.forEach((a,b)->{
			b.forEach(db->{
				dboxSwitchMap.put(a+"-"+db.getSwitchStatus(), db);
			});
		});
		/**
		 * 合闸数
		 */
		int switchOnCount = 0;
		/**
		 * 分闸数
		 */
		int switchOffCount = 0;
		/**
		 * 故障数 (失压数+温度过高数)
		 */
		int switchErrorCount = 0;
		/**
		 * 失压数
		 */
		int voltageLossCount = 0;
		/**
		 * 温度过高数
		 */
		int temperatureAboveCount = 0;
		
		int jcbSocket = 4;//一个检测板插槽数量
		JSONArray jcbs = operFunResp.getJSONArray("jcbs");//检测板
		JSONArray powDevs = operFunResp.getJSONArray("powDevs");//空开
		JSONArray sockets = operFunResp.getJSONArray("sockets");//插槽
		JSONArray vap = operFunResp.getJSONArray("vap");//电气参数数据
		JSONArray socketss = operFunResp.getJSONArray("sockets");//电气参数数据
		int jcbIndex = 1;
		DetectingBoard db = null;
		Map<Integer, DetectingBoard> map = new HashMap<Integer,DetectingBoard>();//检测板map 用于快速获取检测板 更新在线状态
		List<BoardAirSwitch> airSwitchs = null;;
		for (int i = 0; i < powDevs.size(); i++) {
			JSONArray switchJson = powDevs.getJSONArray(i);
			int kkIndex = switchJson.getIntValue(0);//空开索引
			int switchType = switchJson.getIntValue(1);//空开类型
			int socketIndex = switchJson.getIntValue(2);//插槽起始位置
			int socketTotal = switchJson.getIntValue(3);//插槽数量
			Long switchStatus = switchJson.getLongValue(7);//空开状态
			if(0==switchStatus) {
				switchOnCount++;
			}else if(1==switchStatus) {
				switchOffCount++;
			}else if(2==switchStatus) {
				voltageLossCount++;
			}else if(4==switchStatus) {
				temperatureAboveCount++;
			}else {
				log.error("空开当前状态有误:{}",switchStatus);
			}
			if(socketIndex>(jcbIndex-1)*jcbSocket) {
				//添加一个检测板
				db = new DetectingBoard();
				airSwitchs = new ArrayList<BoardAirSwitch>();
				db.setAirSwitchs(airSwitchs);
				dbs.add(db);
				map.put(jcbIndex++, db);
			}
			BoardAirSwitch boardAirSwitch = new BoardAirSwitch();//空开信息
			boardAirSwitch.setIndex(kkIndex);
			boardAirSwitch.setSocketIndex(socketIndex);
			boardAirSwitch.setSocketTotal(socketTotal);
			boardAirSwitch.setSwitchType(switchType+"");
			boardAirSwitch.setSwitchStatus(switchStatus);
			DboxSwitch dboxSwitch2 = dboxSwitchMap.get(switchType+"-"+switchStatus);
			boardAirSwitch.setdDboxSwitch(dboxSwitch2);
			airSwitchs.add(boardAirSwitch);//检测板添加空开信息
		}
		//更新检测板状态在线状态以及空开在线状态
		jcbs.forEach(o->{
			JSONArray jcbJson = (JSONArray)(o);
			int jcbIndex22 = jcbJson.getIntValue(0);//检测板索引
			int line = jcbJson.getIntValue(4);//在线状态
			DetectingBoard detectingBoard = map.get(jcbIndex22);
			if(detectingBoard!=null) {
				detectingBoard.setLine(line);
				detectingBoard.updateAirSwitchsLine();
			}
			if(line==1) {
				//如果是离线 则空开需要取离线素材
				detectingBoard.getAirSwitchs().forEach(as->{
					int offStatus = 99;
					as.setdDboxSwitch(dboxSwitchMap.get(as.getSwitchType()+"-"+offStatus));
				});
			}
		});
		
		/**
		 * 电压
		 */
		BigDecimal voltage = null;
		String voltageText = null;
		/**
		 * 电流
		 */
		BigDecimal current = null;
		String currentText = null;
		/**
		 * 功率
		 */
		BigDecimal power = null;
		String powerText = null;
		/**
		 * 温度
		 */
//		BigDecimal temperature = null;
		BigDecimal lowTemperature = new BigDecimal(100);
		BigDecimal HighTemperature = new BigDecimal(0);
		String temperatureText = null;
		int a = (char)'A';
		int v = (char)'V';
		int p = (char)'P';
		for (int i = 0; i < vap.size(); i++) {
			JSONArray vapJson = vap.getJSONArray(i);
			int type = vapJson.getIntValue(0);
			int sta = vapJson.getIntValue(1);
			int value = vapJson.getIntValue(2);
			if(type==a) {
				if(sta==0) {
					current = new BigDecimal(value);
					currentText = current.divide(new BigDecimal(10),2,RoundingMode.HALF_UP)+"A";
				}else {
					currentText = "异常";	
				}
				
			}else if(type==v) {
				if(sta==0) {
					voltage = new BigDecimal(value);
					voltageText = voltage.divide(new BigDecimal(10),0,RoundingMode.HALF_UP)+"V";
				}else {
					voltageText = "异常";	
				}
				
			}else if(type==p) {
				if(sta==0) {
					power = new BigDecimal(value);
					powerText = power.divide(new BigDecimal(1000),2,RoundingMode.HALF_UP)+"KW";
				}else {
					powerText = "异常";	
				}
			}else {
				//-----
			}
		}
		for (int i = 0; i < socketss.size(); i++) {
			JSONArray socketJson = socketss.getJSONArray(i);
			BigDecimal temp1 = socketJson.getBigDecimal(0);
			BigDecimal temp2 = socketJson.getBigDecimal(1);
			if(temp1.compareTo(lowTemperature)<0) {
				lowTemperature = temp1;
			}
			if(temp2.compareTo(lowTemperature)<0) {
				lowTemperature = temp2;
			}
			if(temp1.compareTo(HighTemperature)>0) {
				HighTemperature = temp1;
			}
			if(temp2.compareTo(HighTemperature)>0) {
				HighTemperature = temp2;
			}
		}
		temperatureText = lowTemperature+"-"+HighTemperature+"℃";
		pd.setCurrent(current);
		pd.setCurrentText(currentText);
		pd.setVoltage(voltage);
		pd.setVoltageText(voltageText);
		pd.setPower(power);
		pd.setPowerText(powerText);
		pd.setTemperatureText(temperatureText);
		pd.setSwitchOnCount(switchOnCount);
		pd.setSwitchOffCount(switchOffCount);
		pd.setVoltageLossCount(voltageLossCount);
		pd.setTemperatureAboveCount(temperatureAboveCount);
		switchErrorCount = voltageLossCount+temperatureAboveCount;
		pd.setSwitchErrorCount(switchErrorCount);
		pd.setMaterial(dboxSwitch);
		pd.setJcbs(dbs);
		pd.setDbox(dbox);
		return AjaxResult.success(pd);
	}
	
	public static void main(String[] args) {
    	
//    	String binaryString = Integer.toBinaryString(3);
//		String leftPad = StringUtils.leftPad(binaryString, 8, '0');
//		System.out.println(binaryString);
//		System.out.println(leftPad);
//    	int lastMonthYear = 2022;
//    	int lastMonth = 2;
//    	//上月1号
//		LocalDate lastMonthOne = LocalDate.of(lastMonthYear, lastMonth, 1);
//		LocalDate with = lastMonthOne.with(TemporalAdjusters.lastDayOfMonth());
//		int dayOfMonth2 = with.getDayOfMonth();
//		System.out.println(dayOfMonth2);
//		System.out.println(900>>3);
//		
//		int a = (char)'P';
//		System.out.println(a);
//		System.out.println(Pattern.matches("^([a-zA-Z0-9]){20,24}$", "AAaABBB44442222223d60000"));
//		System.out.println("AAABBB44442222223d6".length());
//		Math.random();
	}
}
