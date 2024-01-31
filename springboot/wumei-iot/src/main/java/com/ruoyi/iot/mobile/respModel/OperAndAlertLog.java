package com.ruoyi.iot.mobile.respModel;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 设备日志信息
 * 包含操作日志和告警日志
 * @author yue
 *
 */
public class OperAndAlertLog {

	/** 日志方式 1普通日志 2告警日志 */
	private Integer logMethod;
	/** 普通日志类型（1=属性上报，2=事件上报，3=调用功能，4=设备升级，5=设备上线，6=设备离线） */
    private Integer logType;
    /** 日志文本/告警名称 */
    private String logText;
    /** 设备ID */
    private Long deviceId;
    /** 设备名称 */
    private String deviceName;
    /** 设备编号 */
    private String serialNumber;
    /** 告警级别（1=提醒通知，2=轻微问题，3=严重警告，4=场景联动  98报警 99故障） */
    private Long alertLevel;
    /** 日志/告警硬件分类(1终端/设备 2配电设备 3配电检测设备) */
    private Integer alertCat;
    /** 家庭id */
    private Long familyId;
    /** 家庭名称 */
    private String familyName;
    /** 产品ID */
    private Long productId;
    /** 产品名称 */
    private String productName;
    /** 设备类型（1-直连设备、2-网关子设备、3-网关设备） */
    private Integer deviceType;
    //设备型号  例如
    private String deviceModelType;
    /** 日志/告警时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date logTime;
    /** 告警通知/操作用户ID */
    private Long userId;
    /** 告警通知/操作用户昵称 */
    private String nickName;
    //房间id
    private Long belongRoomId;
    //房间名称
    private String belongRoomName;
	public Integer getLogMethod() {
		return logMethod;
	}
	public void setLogMethod(Integer logMethod) {
		this.logMethod = logMethod;
	}
	public Integer getLogType() {
		return logType;
	}
	public void setLogType(Integer logType) {
		this.logType = logType;
	}
	public String getLogText() {
		return logText;
	}
	public void setLogText(String logText) {
		this.logText = logText;
	}
	public Long getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public Long getAlertLevel() {
		return alertLevel;
	}
	public void setAlertLevel(Long alertLevel) {
		this.alertLevel = alertLevel;
	}
	public Integer getAlertCat() {
		return alertCat;
	}
	public void setAlertCat(Integer alertCat) {
		this.alertCat = alertCat;
	}
	public Long getFamilyId() {
		return familyId;
	}
	public void setFamilyId(Long familyId) {
		this.familyId = familyId;
	}
	public String getFamilyName() {
		return familyName;
	}
	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Integer getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(Integer deviceType) {
		this.deviceType = deviceType;
	}
	public String getDeviceModelType() {
		return deviceModelType;
	}
	public void setDeviceModelType(String deviceModelType) {
		this.deviceModelType = deviceModelType;
	}
	public Date getLogTime() {
		return logTime;
	}
	public void setLogTime(Date logTime) {
		this.logTime = logTime;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public Long getBelongRoomId() {
		return belongRoomId;
	}
	public void setBelongRoomId(Long belongRoomId) {
		this.belongRoomId = belongRoomId;
	}
	public String getBelongRoomName() {
		return belongRoomName;
	}
	public void setBelongRoomName(String belongRoomName) {
		this.belongRoomName = belongRoomName;
	}
    
    
}
