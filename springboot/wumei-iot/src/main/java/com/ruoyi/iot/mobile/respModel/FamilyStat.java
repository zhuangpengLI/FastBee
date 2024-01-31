package com.ruoyi.iot.mobile.respModel;

import com.ruoyi.iot.domain.Family;

public class FamilyStat extends Family {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//房间数量
	private Integer countRoom;
	//	成员数量
	private Integer countUser;
	//设备数量
	private Integer countDevice;
	//场景数量
	private Integer countScene;
	//用户昵称
	private String nickName;
	//用户手机号
	private String phonenumber;
	public Integer getCountRoom() {
		return countRoom;
	}
	public void setCountRoom(Integer countRoom) {
		this.countRoom = countRoom;
	}
	public Integer getCountUser() {
		return countUser;
	}
	public void setCountUser(Integer countUser) {
		this.countUser = countUser;
	}
	public Integer getCountDevice() {
		return countDevice;
	}
	public void setCountDevice(Integer countDevice) {
		this.countDevice = countDevice;
	}
	public Integer getCountScene() {
		return countScene;
	}
	public void setCountScene(Integer countScene) {
		this.countScene = countScene;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getPhonenumber() {
		return phonenumber;
	}
	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}
	
	
}
