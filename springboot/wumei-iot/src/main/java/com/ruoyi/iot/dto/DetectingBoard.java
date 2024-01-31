package com.ruoyi.iot.dto;

import java.util.List;

/**
 * 检测板信息
 * @author yue
 *
 */
public class DetectingBoard {

	/**
	 * 检测板的实时在线状态 1=离线 0=在线
	 */
	private int line;
	
	/**
	 * 空开列表
	 */
	private List<BoardAirSwitch> airSwitchs;

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public List<BoardAirSwitch> getAirSwitchs() {
		return airSwitchs;
	}

	public void setAirSwitchs(List<BoardAirSwitch> airSwitchs) {
		this.airSwitchs = airSwitchs;
	}
	
	/**
	 * 更新空开的在线状态
	 */
	public void updateAirSwitchsLine() {
		if(airSwitchs!=null) {
			airSwitchs.forEach(s->{
				s.setLine(line);
			});
		}
	}
	
	
}
