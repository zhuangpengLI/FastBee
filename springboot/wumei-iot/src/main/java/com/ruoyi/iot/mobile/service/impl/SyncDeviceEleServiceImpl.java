package com.ruoyi.iot.mobile.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.iot.domain.DboxEle;
import com.ruoyi.iot.domain.Device;
import com.ruoyi.iot.mapper.DeviceLogMapper;
import com.ruoyi.iot.mapper.DeviceMapper;
import com.ruoyi.iot.mapper.DeviceUserMapper;
import com.ruoyi.iot.mapper.FamilyMapper;
import com.ruoyi.iot.mobile.constant.IotConstant;
import com.ruoyi.iot.mobile.constant.IotOperMsgConstant;
import com.ruoyi.iot.mobile.service.IFamilyDeviceService;
import com.ruoyi.iot.mobile.transferModel.MessageAndResponseTransfer;
import com.ruoyi.iot.model.DeviceShortOutput;
import com.ruoyi.iot.mqtt.EmqxClient;
import com.ruoyi.iot.mqtt.EmqxService;
import com.ruoyi.iot.service.*;
import com.ruoyi.iot.service.impl.DeviceJobServiceImpl;
import com.ruoyi.iot.service.impl.ThingsModelServiceImpl;
import com.ruoyi.iot.tdengine.service.ILogService;
import com.ruoyi.quartz.api.service.ISyncDeviceEleService;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * 设备Service业务层处理
 *
 * @author kerwincui
 * @date 2021-12-16
 */
@Service
public class SyncDeviceEleServiceImpl implements ISyncDeviceEleService {
	private static final Logger log = LoggerFactory.getLogger(SyncDeviceEleServiceImpl.class);

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


	public void energyTwoMonthDataByDay(){
		Device devices = new Device();
		devices.setDeviceType(3);
		List<DeviceShortOutput> devices1 = deviceService.selectDeviceNoRoleShortList(devices);
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (DeviceShortOutput device : devices1) {
					try{
						log.info("执行设备的能耗同步 deviceId:{},sn:{}",device.getDeviceId(),device.getSerialNumber());
						energyTwoMonthDataByDay(device.getDeviceId(),device.getSerialNumber());
					}catch (Exception e){
						log.info("执行设备的能耗同步失败 deviceId:{},sn:{}",device.getDeviceId(),device.getSerialNumber());

					}
				}

			}
		}).start();
	}
	@Override
	public void energyTwoMonthDataByDay(Long deviceId,String gatewaySn) {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		BigDecimal oneThur = new BigDecimal(1000);
		LocalDate curDate = LocalDate.now().minusDays(1L); //只保存前一天的数据,当天数据可能没更新
//		curDate = curDate.minusMonths(1);
		int year = curDate.getYear();
		int curMonth = curDate.getMonthValue();
		int curDay = curDate.getDayOfMonth();
//		int curHour = LocalDateTime.now().getHour();
		int unit = 1;

		boolean isMock = false;//模拟数据

		Random random = new Random();
		JSONArray dataArray = new JSONArray();
		int dayOfMonth = curDate.getDayOfMonth();

		LocalDate lasetMonthDay = curDate.with(TemporalAdjusters.firstDayOfMonth()).minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
		DboxEle queryEle = new DboxEle();
		queryEle.setDeviceId(deviceId);
		queryEle.setMonth((long)lasetMonthDay.getMonthValue());
		queryEle.setYear((long)lasetMonthDay.getYear());
		queryEle.setDay((long)lasetMonthDay.getDayOfMonth());
		List<DboxEle> dboxEles = dboxEleService.selectDboxEleList(queryEle);
		log.info("上个月最后一天是否有数据:"+lasetMonthDay+"  size"+dboxEles.size());
		if (dboxEles.isEmpty()) {
			//发布一个模拟app  进入网关查找设备模式
			int lastMonthYear = 0;
			int lastMonth = 0;
			if (curMonth == 1) {
				//去年12月份
				lastMonthYear = year - 1;
				lastMonth = 12;
			} else {
				lastMonthYear = year;
				lastMonth = curMonth - 1;
			}
			//上月1号
			LocalDate lastMonthOne = LocalDate.of(lastMonthYear, lastMonth, 1);
			LocalDate with = lastMonthOne.with(TemporalAdjusters.lastDayOfMonth());
			int lastMonthTotalDays = with.getDayOfMonth();
			String topic = IotConstant.ZNJJ_APP_PREFIX + gatewaySn;
			JSONObject pushMessage = new JSONObject();
			pushMessage.put("au", transfer.getAuInfo());
			pushMessage.put("fun", IotOperMsgConstant.CS_REQ_GW_FUN_GETELEBYDAY);
			pushMessage.put("year", lastMonthYear);
			pushMessage.put("month", lastMonth);
			emqxClient.publish(1, false, topic, pushMessage.toString());
			transfer.createOperFunReq(gatewaySn, IotOperMsgConstant.CS_REQ_GW_FUN_GETELEBYDAY);
			JSONObject operFunResp = transfer.getContentOperFunResp(gatewaySn, IotOperMsgConstant.CS_REQ_GW_FUN_GETELEBYDAY);
			if (operFunResp == null) {
				log.error("获取能耗数据超时:{}", IotOperMsgConstant.CS_REQ_GW_FUN_GETELEBYDAY);
				throw new RuntimeException("定时获取能耗数据出错,deviceId:" + deviceId);
			}
			JSONArray jsonArray = operFunResp.getJSONArray("eleValue");
			for (int i = 0; i < lastMonthTotalDays; i++) {
				dataArray.add(jsonArray.get(i));
			}
			dayOfMonth = dayOfMonth + lastMonthTotalDays;
			try {
				Thread.sleep(500L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//发布一个模拟app  进入网关查找设备模式
		String topic = IotConstant.ZNJJ_APP_PREFIX + gatewaySn;
		JSONObject pushMessage = new JSONObject();
		pushMessage.put("au", transfer.getAuInfo());
		pushMessage.put("fun", IotOperMsgConstant.CS_REQ_GW_FUN_GETELEBYDAY);
		pushMessage.put("year", curDate.getYear());
		pushMessage.put("month", curMonth);
		emqxClient.publish(1, false, topic, pushMessage.toString());
		transfer.createOperFunReq(gatewaySn, IotOperMsgConstant.CS_REQ_GW_FUN_GETELEBYDAY);
		JSONObject operFunResp = transfer.getContentOperFunResp(gatewaySn, IotOperMsgConstant.CS_REQ_GW_FUN_GETELEBYDAY);
		if (operFunResp == null) {
			log.error("获取能耗数据超时:{}", IotOperMsgConstant.CS_REQ_GW_FUN_GETELEBYDAY);
			throw new RuntimeException("定时获取能耗数据出错,deviceId:" + deviceId);
		}
		JSONArray jsonArray = operFunResp.getJSONArray("eleValue");
		dataArray.addAll(jsonArray);
		DateTimeFormatter dateFor = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		for (int j = dayOfMonth; j > 0; j--) {
			Integer object = (Integer) dataArray.get(j - 1);
			Map<String, Object> map = new HashMap<String, Object>();
			LocalDate localDate = curDate.minusDays(dayOfMonth-j);
//			map.put("date", localDate.format(dateFor));
			BigDecimal a = 1 == unit ? new BigDecimal(object) : new BigDecimal(object).divide(oneThur, 2, RoundingMode.HALF_UP);
			while (isMock) {
				double nextDouble = random.nextDouble();
				if (nextDouble > 0.2 && nextDouble < 0.8) {
					a = new BigDecimal(nextDouble * 100).setScale(2, RoundingMode.HALF_EVEN);
					break;
				}
			}

			DboxEle querySubEle = new DboxEle();
			querySubEle.setDeviceId(deviceId);
			querySubEle.setMonth((long)localDate.getMonthValue());
			querySubEle.setYear((long)localDate.getYear());
			querySubEle.setDay((long)localDate.getDayOfMonth());
			List<DboxEle> dboxSubEles = dboxEleService.selectDboxEleList(querySubEle);
			if(dboxSubEles.isEmpty()){
				querySubEle.setEleValue(a);
				Instant instant = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
				querySubEle.setEleTime(Date.from(instant));
				dboxEleService.insertDboxEle(querySubEle);
			}else{
				log.info("当前日期存在电量数据,不再往前推导,date"+querySubEle);
				break;
			}

//			map.put("data", a);
//			list.add(map);
		}
//		Map<String, Object> returnMap = new HashMap<String,Object>();
//		returnMap.put("list",list);
//		return AjaxResult.success(returnMap);
	}
}
