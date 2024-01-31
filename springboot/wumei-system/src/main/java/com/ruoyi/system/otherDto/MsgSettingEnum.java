package com.ruoyi.system.otherDto;

import java.util.ArrayList;
import java.util.List;

public enum MsgSettingEnum {

	SYS_TOTAL("SYS_TOTAL","系统通知",null),
	SYS_FAMILY_SHARE("SYS_FAMILY_SHARE","家庭共享",SYS_TOTAL),
	SYS_DEVICE_SHARE("SYS_DEVICE_SHARE","设备共享",SYS_TOTAL) ,
	SYS_OFFICAL("SYS_OFFICAL","官方消息",SYS_TOTAL) ,
	DEV_TOTAL("DEV_TOTAL","设备通知",null) ,
	DEV_FAMILY("DEV_FAMILY","设备通知-家庭",DEV_TOTAL) ,
	DEV_DEVICE("DEV_DEVICE","设备通知-家庭-设备",DEV_FAMILY),
	;
	
	
	private String identifierName;
	private String identifier;
	private MsgSettingEnum parent;
	MsgSettingEnum(String identifier,String identifierName,MsgSettingEnum parent){
		this.identifierName = identifierName;
		this.identifier = identifier;
		this.parent = parent;
	}
	public String getIdentifierName() {
		return identifierName;
	}
	public void setIdentifierName(String identifierName) {
		this.identifierName = identifierName;
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public MsgSettingEnum getParent() {
		return parent;
	}
	public void setParent(MsgSettingEnum parent) {
		this.parent = parent;
	}
	
	/**
	 * 查询本级的直接子级数据
	 * @param setting
	 * @return
	 */
	public static List<MsgSettingEnum> selectChildrenSetting(MsgSettingEnum setting){
		List<MsgSettingEnum> list = allMsgSettingEnum();
		List<MsgSettingEnum> returnList = new ArrayList<MsgSettingEnum>();
		for(MsgSettingEnum e:list) {
			if(setting==null && e.getParent()==null) {
				returnList.add(e);
				continue;
			}else if(setting !=null && setting.equals(e.getParent())) {
				returnList.add(e);
				continue;
			}
		}
		return 	returnList;
	}
	/**
	 * 通过标识符查询唯一
	 * @param identifier
	 * @return
	 */
	public static MsgSettingEnum selectSettingByIdentifier(String identifier){
		List<MsgSettingEnum> list = allMsgSettingEnum();
		for(MsgSettingEnum e:list) {
			if(identifier.equals(e.getIdentifier())) {
				return e;
			}
		}
		return 	null;
	}
	
	private static List<MsgSettingEnum> allMsgSettingEnum(){
		List<MsgSettingEnum> list = new ArrayList<MsgSettingEnum>();
		list.add(SYS_TOTAL);
		list.add(SYS_FAMILY_SHARE);
		list.add(SYS_DEVICE_SHARE);
		list.add(SYS_OFFICAL);
		list.add(DEV_TOTAL);
		list.add(DEV_FAMILY);
		list.add(DEV_DEVICE);
		return list;
	}

}
