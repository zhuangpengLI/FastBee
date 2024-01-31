package com.ruoyi.quartz.api.service;

/**
 * 设备Service接口
 * 
 * @author kerwincui
 * @date 2021-12-16
 */
public interface ISyncDeviceEleService {


	/**
	 *
	 */
	public void energyTwoMonthDataByDay();
	/**
	 * 能耗数据
	 * @param deviceId
	 * @return
	 */
	public void energyTwoMonthDataByDay(Long deviceId,String gatewaySn);
}

