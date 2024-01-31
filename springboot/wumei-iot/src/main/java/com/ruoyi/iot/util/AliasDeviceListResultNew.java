package com.ruoyi.iot.util;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

import cn.jiguang.common.resp.BaseResult;

public class AliasDeviceListResultNew extends BaseResult {
	   
	@Expose public List<RegInfo> data = new ArrayList<RegInfo>();

	public List<RegInfo> getData() {
		return data;
	}

	public void setData(List<RegInfo> data) {
		this.data = data;
	}
}

class RegInfo{
	@Expose private String registration_id;
	@Expose private String platform;
	@Expose private String last_online_date;
	public String getRegistration_id() {
		return registration_id;
	}
	public void setRegistration_id(String registration_id) {
		this.registration_id = registration_id;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getLast_online_date() {
		return last_online_date;
	}
	public void setLast_online_date(String last_online_date) {
		this.last_online_date = last_online_date;
	}
	
}
