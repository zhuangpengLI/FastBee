package com.ruoyi.iot.dto;

import java.util.List;

import com.ruoyi.iot.domain.DboxSwitch;

/**
 * 空开信息
 * @author yue
 *
 */
public class BoardAirSwitch {
	
	/**
	 * 当前空开对应的检测板的实时在线状态 1=离线 0=在线
	 */
	private int line;
	
	/**
	 * 当前状态下空开图
	 */
	private DboxSwitch dDboxSwitch;
	
	/** 空开类型 */
    private String switchType;

    /** 空开状态 0=正常(默认合闸)， 1=跳闸 分闸   2=欠压（上无电，合闸）  4=高温（合闸） */
    private Long switchStatus;
    
    /**
     * 插槽序号 即 空开顺序序号
     */
    private int index;
    
    /**
     * 空开占用 插槽起始位置
     */
    private int socketIndex;

    /**
     * 空开占用 插槽数量
     */
    private int socketTotal;
    
    /**
     * 空开插槽列表信息
     */
    private List<BoardSocket> socketList;

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public DboxSwitch getdDboxSwitch() {
		return dDboxSwitch;
	}

	public void setdDboxSwitch(DboxSwitch dDboxSwitch) {
		this.dDboxSwitch = dDboxSwitch;
	}

	public String getSwitchType() {
		return switchType;
	}

	public void setSwitchType(String switchType) {
		this.switchType = switchType;
	}

	public Long getSwitchStatus() {
		return switchStatus;
	}

	public void setSwitchStatus(Long switchStatus) {
		this.switchStatus = switchStatus;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getSocketIndex() {
		return socketIndex;
	}

	public void setSocketIndex(int socketIndex) {
		this.socketIndex = socketIndex;
	}

	public int getSocketTotal() {
		return socketTotal;
	}

	public void setSocketTotal(int socketTotal) {
		this.socketTotal = socketTotal;
	}

	public List<BoardSocket> getSocketList() {
		return socketList;
	}

	public void setSocketList(List<BoardSocket> socketList) {
		this.socketList = socketList;
	}

    
}
