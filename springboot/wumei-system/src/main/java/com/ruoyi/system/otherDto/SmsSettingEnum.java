package com.ruoyi.system.otherDto;

import java.util.ArrayList;
import java.util.List;

public enum SmsSettingEnum {
	DEV_TOTAL("DEV_TOTAL","短信通知",null) ,
	;
	
	
	private String identifierName;
	private String identifier;
	private SmsSettingEnum parent;
	SmsSettingEnum(String identifier,String identifierName,SmsSettingEnum parent){
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
	public SmsSettingEnum getParent() {
		return parent;
	}
	public void setParent(SmsSettingEnum parent) {
		this.parent = parent;
	}
	
	/**
	 * 查询本级的直接子级数据
	 * @param setting
	 * @return
	 */
	public static List<SmsSettingEnum> selectChildrenSetting(SmsSettingEnum setting){
		List<SmsSettingEnum> list = allMsgSettingEnum();
		List<SmsSettingEnum> returnList = new ArrayList<SmsSettingEnum>();
		for(SmsSettingEnum e:list) {
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
	public static SmsSettingEnum selectSettingByIdentifier(String identifier){
		List<SmsSettingEnum> list = allMsgSettingEnum();
		for(SmsSettingEnum e:list) {
			if(identifier.equals(e.getIdentifier())) {
				return e;
			}
		}
		return 	null;
	}
	
	private static List<SmsSettingEnum> allMsgSettingEnum(){
		List<SmsSettingEnum> list = new ArrayList<SmsSettingEnum>();
		list.add(DEV_TOTAL);
		return list;
	}

}
