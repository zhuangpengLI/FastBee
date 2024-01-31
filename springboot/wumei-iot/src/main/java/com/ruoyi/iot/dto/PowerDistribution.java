package com.ruoyi.iot.dto;

import java.math.BigDecimal;
import java.util.List;

import com.ruoyi.iot.domain.Dbox;
import com.ruoyi.iot.domain.DboxSwitch;

/**
 * 配电信息
 * @author yue
 *
 */
public class PowerDistribution {

	/**
	 * 电压
	 */
	private BigDecimal voltage;
	private String voltageText;
	/**
	 * 电流
	 */
	private BigDecimal current;
	private String currentText;
	/**
	 * 功率
	 */
	private BigDecimal power;
	private String powerText;
	/**
	 * 温度
	 */
//	private BigDecimal temperature;
	private String temperatureText;
	/**
	 * 合闸数
	 */
	private int switchOnCount;
	/**
	 * 分闸数
	 */
	private int switchOffCount;
	/**
	 * 故障数 (失压数+温度过高数)
	 */
	private int switchErrorCount;
	/**
	 * 失压数
	 */
	private int voltageLossCount;
	/**
	 * 温度过高数
	 */
	private int temperatureAboveCount;
	
	/**
	 * 检测板列表
	 */
	private List<DetectingBoard> jcbs;
	
	/**
	 * 空开素材列表
	 */
	private List<DboxSwitch> material;
	
	/**
	 * 配电箱信息
	 */
	private Dbox dbox;

	public BigDecimal getVoltage() {
		return voltage;
	}

	public void setVoltage(BigDecimal voltage) {
		this.voltage = voltage;
	}

	public String getVoltageText() {
		return voltageText;
	}

	public void setVoltageText(String voltageText) {
		this.voltageText = voltageText;
	}

	public BigDecimal getCurrent() {
		return current;
	}

	public void setCurrent(BigDecimal current) {
		this.current = current;
	}

	public String getCurrentText() {
		return currentText;
	}

	public void setCurrentText(String currentText) {
		this.currentText = currentText;
	}

	public BigDecimal getPower() {
		return power;
	}

	public void setPower(BigDecimal power) {
		this.power = power;
	}

	public String getPowerText() {
		return powerText;
	}

	public void setPowerText(String powerText) {
		this.powerText = powerText;
	}

//	public BigDecimal getTemperature() {
//		return temperature;
//	}
//
//	public void setTemperature(BigDecimal temperature) {
//		this.temperature = temperature;
//	}

	public String getTemperatureText() {
		return temperatureText;
	}

	public void setTemperatureText(String temperatureText) {
		this.temperatureText = temperatureText;
	}

	public int getSwitchOnCount() {
		return switchOnCount;
	}

	public void setSwitchOnCount(int switchOnCount) {
		this.switchOnCount = switchOnCount;
	}

	public int getSwitchOffCount() {
		return switchOffCount;
	}

	public void setSwitchOffCount(int switchOffCount) {
		this.switchOffCount = switchOffCount;
	}

	public int getSwitchErrorCount() {
		return switchErrorCount;
	}

	public void setSwitchErrorCount(int switchErrorCount) {
		this.switchErrorCount = switchErrorCount;
	}

	public int getVoltageLossCount() {
		return voltageLossCount;
	}

	public void setVoltageLossCount(int voltageLossCount) {
		this.voltageLossCount = voltageLossCount;
	}

	public int getTemperatureAboveCount() {
		return temperatureAboveCount;
	}

	public void setTemperatureAboveCount(int temperatureAboveCount) {
		this.temperatureAboveCount = temperatureAboveCount;
	}

	public List<DetectingBoard> getJcbs() {
		return jcbs;
	}

	public void setJcbs(List<DetectingBoard> jcbs) {
		this.jcbs = jcbs;
	}

	public List<DboxSwitch> getMaterial() {
		return material;
	}

	public void setMaterial(List<DboxSwitch> material) {
		this.material = material;
	}

	public Dbox getDbox() {
		return dbox;
	}

	public void setDbox(Dbox dbox) {
		this.dbox = dbox;
	}
	
}
