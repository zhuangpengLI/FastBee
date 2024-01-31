package com.ruoyi.iot.mobile.respModel;

import com.ruoyi.iot.domain.Room;

public class RoomStat extends Room {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//设备数量
	private Integer countDevice;
	public Integer getCountDevice() {
		return countDevice;
	}
	public void setCountDevice(Integer countDevice) {
		this.countDevice = countDevice;
	}
	
}
