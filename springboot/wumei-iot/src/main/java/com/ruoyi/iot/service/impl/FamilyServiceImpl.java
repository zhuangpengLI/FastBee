package com.ruoyi.iot.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.iot.domain.Device;
import com.ruoyi.iot.domain.Family;
import com.ruoyi.iot.domain.FamilyUserRela;
import com.ruoyi.iot.domain.MsgNoticeSetting;
import com.ruoyi.iot.domain.MsgOperMsg;
import com.ruoyi.iot.domain.Room;
import com.ruoyi.iot.domain.Scene;
import com.ruoyi.iot.mapper.AlertLogMapper;
import com.ruoyi.iot.mapper.DeviceMapper;
import com.ruoyi.iot.mapper.FamilyMapper;
import com.ruoyi.iot.mapper.MsgMapper;
import com.ruoyi.iot.mapper.MsgNoticeSettingMapper;
import com.ruoyi.iot.mapper.MsgOperMsgMapper;
import com.ruoyi.iot.mapper.SceneMapper;
import com.ruoyi.iot.mobile.constant.IotConstant;
import com.ruoyi.iot.mobile.constant.IotOperMsgConstant;
import com.ruoyi.iot.mobile.model.InviteFamilyUserReqDto;
import com.ruoyi.iot.mobile.model.UpdateFamilyUserReqDto;
import com.ruoyi.iot.mobile.respModel.FamilyStat;
import com.ruoyi.iot.mobile.respModel.RoomStat;
import com.ruoyi.iot.mobile.service.IFamilyDeviceService;
import com.ruoyi.iot.mobile.transferModel.MessageAndResponseTransfer;
import com.ruoyi.iot.model.DeviceShortOutput;
import com.ruoyi.iot.mqtt.EmqxClient;
import com.ruoyi.iot.service.IFamilyService;
import com.ruoyi.iot.service.ISceneService;
import com.ruoyi.system.MsgTypeConstant;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.otherDomain.BaseConfig;
import com.ruoyi.system.otherDomain.Msg;
import com.ruoyi.system.otherDto.MsgSettingEnum;
import com.ruoyi.system.otherService.IBaseConfigService;
import com.ruoyi.system.otherService.IMsgService;

/**
 * 家庭管理Service业务层处理
 * 
 * @author renjiayue
 * @date 2022-09-07
 */
@Service
public class FamilyServiceImpl implements IFamilyService 
{
	
    private static final Logger log = LoggerFactory.getLogger(FamilyServiceImpl.class);
    @Autowired
    private FamilyMapper familyMapper;
    //自引用
    @Autowired
    private IFamilyService familyService;
    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private IBaseConfigService baseConfigService;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private MsgOperMsgMapper msgOperMsgMapper;
    @Autowired
    private IFamilyDeviceService familyDeviceService;
    @Autowired
    private ISceneService sceneService;
    @Autowired
    private SceneMapper sceneMapper;
    @Autowired
    private MessageAndResponseTransfer transfer;
    @Autowired
    private EmqxClient emqxClient;
    @Autowired
    private MsgMapper msgMapper;
    @Autowired
    private MsgNoticeSettingMapper msgNoticeSettingMapper;
    @Autowired
    private AlertLogMapper alertLogMapper;
    @Autowired
    private IMsgService msgService;
    

    @Override
    public Family selectFamilyOnlyByFamilyId(Long familyId,Long userId)
    {
    	FamilyUserRela userRela = familyMapper.selectUserByFamilyIdAndUserId(familyId, userId);
    	if(userRela==null){
    		//非家庭成员无法查看家庭信息
    		return null;
    	}
    	return familyMapper.selectFamilyOnlyByFamilyId(familyId);
    }
    
    @Override
    public FamilyStat selectFamilyAndStatByFamilyId(Long familyId,Long userId)
    {
    	FamilyUserRela userRela = familyMapper.selectUserByFamilyIdAndUserId(familyId, userId);
    	if(userRela==null){
    		//非家庭成员无法查看家庭信息
    		return null;
    	}
    	Family family = familyMapper.selectFamilyOnlyByFamilyId(familyId);
    	FamilyStat familyStat = new FamilyStat();
    	BeanUtils.copyProperties(family, familyStat);
    	familyStat.setCountRoom(familyMapper.countRoomByFamilyId(familyId));
    	familyStat.setCountUser(familyMapper.countUserByFamilyId(familyId));
    	familyStat.setCountDevice(deviceMapper.countDeviceByRomm(null,familyId));
    	return familyStat;
    }
    /**
     * 查询家庭管理
     * 只有家庭信息 没有房间和成员
     * 
     * @param familyId 家庭管理主键
     * @return 家庭管理
     */
    @Override
    public Family selectFamilyOnlyByFamilyId(Long familyId)
    {
        return familyMapper.selectFamilyOnlyByFamilyId(familyId);
    }
    @Override
    public Family selectFamilyByFamilyId(Long familyId)
    {
    	return familyMapper.selectFamilyAllByFamilyId(familyId);
    }
    @Override
    public int selectCountTotalFamily(Long userId)
    {
    	return familyMapper.selectCountTotalFamily(userId);
    }
    
    @Override
    public int selectCountCreateFamily(Long userId)
    {
    	return familyMapper.selectCountCreateFamily(userId);
    }
    
    /**
	 * 查询家庭管理 
	 * 只查询家庭信息
	 * 
	 * @param familyId 家庭管理主键
	 * @return 家庭管理
	 */
    @Override
	public Family selectFamilyOnlyByGatewaySn(String gatewaySn){
    	return familyMapper.selectFamilyOnlyByGatewaySn(gatewaySn);
    }

    /**
     * 查询家庭管理列表
     * 
     * @param family 家庭管理
     * @return 家庭管理
     */
    @Override
    public List<Family> selectFamilyList(Family family)
    {
        return familyMapper.selectFamilyList(family);
    }
    @Override
    public List<FamilyStat> selectFamilyStatList(Family family)
    {
    	List<FamilyStat> list = familyMapper.selectFamilyStatList(family);
    	list.forEach(familyStat->{
    		Long familyId2 = familyStat.getFamilyId();
    		familyStat.setCountRoom(familyMapper.countRoomByFamilyId(familyId2));
        	familyStat.setCountUser(familyMapper.countUserByFamilyId(familyId2));
        	familyStat.setCountDevice(deviceMapper.countDeviceByRomm(null, familyId2));
        	familyStat.setCountScene(sceneMapper.countSceneByFamilyId(familyId2));
    	});
    	return list;
    }
    
    @Override
    public List<FamilyUserRela> selectUserListByFamilyIdAndUserId(Long familyId,Long userId){
    	if(familyId == null && userId == null) {
//    		客户端查询 不能都为空
    		return new ArrayList<>();
    	}
    	return familyMapper.selectUserListByFamilyIdAndUserId(familyId,userId);
    }
    @Override
    public List<FamilyUserRela> selectUserListByFamilyIdAndUserId(Long familyId,Long userId,Long loginUserId){
    	//判断自己是否有权限
    	FamilyUserRela userRela = familyMapper.selectUserByFamilyIdAndUserId(familyId, loginUserId);
    	if(userRela==null){
//    		非家庭成员
    		return new ArrayList<>();
    	}
    	return familyMapper.selectUserListByFamilyIdAndUserId(familyId,userId);
    }
    @Override
    public List<FamilyUserRela> selectUserStatListByFamilyIdAndUserId(Long familyId,Long userId){
    	List<FamilyUserRela> list = familyMapper.selectUserListByFamilyIdAndUserId(familyId,userId);
    	list.forEach(familyStat->{
    		Long familyId2 = familyStat.getFamilyId();
    		familyStat.setCountRoom(familyMapper.countRoomByFamilyId(familyId2));
        	familyStat.setCountUser(familyMapper.countUserByFamilyId(familyId2));
        	familyStat.setCountDevice(deviceMapper.countDeviceByRomm(null, familyId2));
    	});
    	return list;
    }

    /**
     * 新增家庭管理
     * 
     * @param family 家庭管理
     * @return 结果
     */
    @Transactional
    @Override
    public AjaxResult insertFamily(Family family)
    {
    	int count = familyMapper.countFamilyByUserId(family.getBelongUserId());
    	// 数量校验
    	BaseConfig baseConfig = baseConfigService.selectOneBaseConfigByCache();
    	if(count>=baseConfig.getUserCreateRoomMax()) {
			return AjaxResult.error("一个账号最多创建"+baseConfig.getUserCreateRoomMax()+"个家庭");
    	}
        family.setCreateTime(DateUtils.getNowDate());
        family.setIsBind(0);//未绑定网关
    	family.setGatewaySn(null);
        familyMapper.insertFamily(family);
        List<FamilyUserRela> familyUserRelaList = new ArrayList<FamilyUserRela>();
        //建立自己与家庭的绑定关系
        FamilyUserRela e = new FamilyUserRela();
        e.setUserId(family.getCreateUserId());
        e.setFamilyUserRole("2");//当前创建用户为管理员角色
		familyUserRelaList.add(e );
        family.setFamilyUserRelaList(familyUserRelaList);
        List<Room> roomList = initRoom(family.getFamilyId());
		family.setRoomList(roomList);
        insertRoom(family);
        insertFamilyUserRela(family);
        return AjaxResult.success(family.getFamilyId());
    }

    /**
     * 修改家庭管理
     * 
     * @param family 家庭管理
     * @return 结果
     */
    @Transactional
    @Override
    public int updateFamily(Family family)
    {
        family.setUpdateTime(DateUtils.getNowDate());
        familyMapper.deleteRoomByFamilyId(family.getFamilyId());
        insertRoom(family);
        familyMapper.deleteFamilyUserRelaByFamilyId(family.getFamilyId());
        insertFamilyUserRela(family);
        return familyMapper.updateFamily(family);
    }
    
    /**
     * 初始化默认房间
     * @param familyId
     * @return
     */
    private List<Room> initRoom(Long familyId){
    	List<Room> list = new ArrayList<Room>();
    	String[] names = {"客厅","主卧","次卧1","次卧2","厨房","卫生间"};
    	Date nowDate = DateUtils.getNowDate();
    	for(int i=0;i<names.length;i++) {
    		Room room = new Room();
    		room.setName(names[i]);
    		room.setFamilyId(familyId);
    		room.setRoomOrder((long)(i+1));
    		room.setDelFlag("0");
    		room.setCreateTime(nowDate);
    		list.add(room);
    	}
    	return list;
    }
    /**
     * 新增房间信息
     * 
     * @param family 家庭管理对象
     */
    private void insertRoom(Family family)
    {
        List<Room> roomList = family.getRoomList();
        Long familyId = family.getFamilyId();
        if (StringUtils.isNotNull(roomList))
        {
            List<Room> list = new ArrayList<Room>();
            for (Room room : roomList)
            {
                room.setFamilyId(familyId);
                list.add(room);
            }
            if (list.size() > 0)
            {
                familyMapper.batchRoom(list);
            }
        }
    }
    
    /**
     * 新增家庭用户关联信息
     * 
     * @param family 家庭管理对象
     */
    private void insertFamilyUserRela(Family family)
    {
        List<FamilyUserRela> familyUserRelaList = family.getFamilyUserRelaList();
        Long familyId = family.getFamilyId();
        if (StringUtils.isNotNull(familyUserRelaList))
        {
            List<FamilyUserRela> list = new ArrayList<FamilyUserRela>();
            for (FamilyUserRela familyUserRela : familyUserRelaList)
            {
                familyUserRela.setFamilyId(familyId);
                list.add(familyUserRela);
            }
            if (list.size() > 0)
            {
                familyMapper.batchFamilyUserRela(list);
            }
        }
    }
    
    /**
     * 查询唯一用户信息
     * 均不为空才可以使用
     * 
     * @param familyId 家庭管理主键
     * @param userId 
     * @return 家庭管理
     */
    @Override
    public FamilyUserRela selectUserByFamilyIdAndUserId(Long familyId,Long userId){
    	return familyMapper.selectUserByFamilyIdAndUserId(familyId, userId);
    }
    @Transactional
    @Override
    public AjaxResult updateFamily(Family family,Long userId)
    {
    	Long familyId = family.getFamilyId();
    	//判断是否是自己拥有的家庭
    	Family old = familyMapper.selectFamilyOnlyByFamilyId(familyId);
    	if(old==null){
    		return AjaxResult.error("家庭信息不存在");
    	}
    	Integer newIsEnableJoinAuth = family.getIsEnableJoinAuth();
    	Integer oldIsEnableJoinAuth = old.getIsEnableJoinAuth();
    	//不支持修改网关信息
//    	String newSn = family.getGatewaySn();
//    	String oldSn = old.getGatewaySn();
//    	if(StringUtils.isNotBlank(newSn) && !newSn.equals(oldSn)){
//    		//修改网关信息
//    		if(old.getBelongUserId()!=userId){
//        		return AjaxResult.error("不是自己的家庭,无法修改网关信息");
//        	}
//    		family.setGatewaySn(newSn);
//    		family.setIsBind(1);
//    	}
    	if(newIsEnableJoinAuth!=null && !newIsEnableJoinAuth.equals(oldIsEnableJoinAuth)){
    		if(!old.getBelongUserId().equals(userId)){
    			return AjaxResult.error("不是自己的家庭,无法设置网关权限");
    		}
    		String oldSn = old.getGatewaySn();
    		if(StringUtils.isBlank(oldSn)){
    			return AjaxResult.error("当前家庭未绑定网关,无法设置网关权限");
    		}
    	}
    	//判断自己是否有修改权限
    	FamilyUserRela userRela = familyMapper.selectUserByFamilyIdAndUserId(familyId, userId);
    	if(userRela==null){
    		return AjaxResult.error("非家庭成员,无法修改家庭信息");
    	}
    	if(!"2".equals(userRela.getFamilyUserRole())){
    		return AjaxResult.error("权限不足");
    	}
    	//仅修改家庭信息 不修改成员和房间
    	family.setUpdateTime(DateUtils.getNowDate());
//    	familyMapper.deleteRoomByFamilyId(family.getFamilyId());
//    	insertRoom(family);
//    	familyMapper.deleteFamilyUserRelaByFamilyId(family.getFamilyId());
//    	insertFamilyUserRela(family);
    	familyMapper.updateFamily(family);
    	return AjaxResult.success();
    }
    /**
     * 暂时只有绑定网关操作  没有修改和删除网关信息
     */
    @Transactional
    @Override
    public AjaxResult bindFamilyGateway(Family family,Long userId,String userName)
    {
    	Long familyId = family.getFamilyId();
    	String gatewaySn = family.getGatewaySn();
    	Integer isEnableJoinAuth = family.getIsEnableJoinAuth();
    	if(StringUtils.isBlank(gatewaySn)){
    		return AjaxResult.error("网关SN码不能为空");
    	}
    	gatewaySn = gatewaySn.toUpperCase();//转换为大写
    	//判断网关是否合法
    	if(!familyDeviceService.isGatewayDevice(gatewaySn)) {
    		return AjaxResult.error(1000,"网关不合法");
    	}
    	if(isEnableJoinAuth==null){
    		return AjaxResult.error("扫描网关加入家庭权限不能为空");
    	}else if(isEnableJoinAuth.intValue()!=0 && isEnableJoinAuth.intValue()!=1){
    		return AjaxResult.error("扫描网关加入家庭权限信息有误");
    	}
    	//判断是否是自己拥有的家庭
    	Family old = familyMapper.selectFamilyOnlyByFamilyId(familyId);
    	if(old==null){
    		return AjaxResult.error("家庭信息不存在");
    	}
    	// 判断自己是否有修改权限
    	if(!old.getBelongUserId().equals(userId)){
    		return AjaxResult.error("权限不足");
    	}
    	if(old.getIsBind().intValue()==1){
    		return AjaxResult.error("家庭已经绑定过网关");
    	}
    	Family gatewayFamily = familyMapper.selectFamilyOnlyByGatewaySn(gatewaySn);
    	if(gatewayFamily!=null){
    		return AjaxResult.error(999,"网关已绑定家庭",gatewayFamily);
    	}
    	//判断网关是否在线
    	//发布一个模拟app  获取所有设备请求 GetDevsRT 挂起请求
    	String topic = IotConstant.ZNJJ_APP_PREFIX +gatewaySn;
    	JSONObject pushMessage = new JSONObject();
    	pushMessage.put("au", transfer.getAuInfo());
    	pushMessage.put("ta", null);
    	pushMessage.put("fun",IotOperMsgConstant.APP_REQ_GW_FUN_GETHSV);
    	emqxClient.publish(1, false, topic, pushMessage.toString());
    	transfer.createOperFunReq(gatewaySn, IotOperMsgConstant.APP_REQ_GW_FUN_GETHSV);
    	Boolean operFunResp = transfer.getOperFunResp(gatewaySn, IotOperMsgConstant.APP_REQ_GW_FUN_GETHSV);
    	if(operFunResp) {
    		//网关在线
    		//TODO 看网关是否已存在  不存在则添加网关
    		Device gw = deviceMapper.selectDeviceBySerialNumber(gatewaySn);
        	if (gw == null) {
        		int insertGatewayDevice = familyDeviceService.insertGatewayDevice(gatewaySn,familyId);
        		if(insertGatewayDevice<=0) {
        			return AjaxResult.error("绑定失败");
        		}
        	}else {
        		//网关设置所属家庭
        		Device updateGw = new Device();
        		updateGw.setDeviceId(gw.getDeviceId());
        		updateGw.setBelongFamilyId(familyId);
        		updateGw.setUpdateTime(DateUtils.getNowDate());
        		deviceMapper.updateDevice(updateGw);
        	}
    	}else {
    		return AjaxResult.error(998,"网关不在线");
    	}
    	Family updateFamily = new Family();
    	updateFamily.setFamilyId(familyId);
    	updateFamily.setUpdateTime(DateUtils.getNowDate());
    	updateFamily.setGatewaySn(gatewaySn);
    	updateFamily.setIsBind(1);//已绑定网关
    	updateFamily.setIsEnableJoinAuth(isEnableJoinAuth);
    	familyMapper.updateFamily(updateFamily);
    	
    	//自动添加网关设备
    	JSONObject pushMessage1 = new JSONObject();
    	pushMessage1.put("au", transfer.getAuInfo());
    	pushMessage1.put("ta", null);
    	pushMessage1.put("fun",IotOperMsgConstant.APP_REQ_GW_FUN_GETDEVSRT);
    	emqxClient.publish(1, false, topic, pushMessage1.toString());
    	transfer.createOperFunReq(gatewaySn, IotOperMsgConstant.APP_REQ_GW_FUN_GETDEVSRT);
    	Boolean operFunResp1 = transfer.getOperFunResp(gatewaySn, IotOperMsgConstant.APP_REQ_GW_FUN_GETDEVSRT);
    	log.info("绑定网关后自动添加网关已关联设备结果:{}",operFunResp1);
    	
    	//绑定成功后内置场景
    	for(int i=101;i<106;i++) {
    		String sceneName = null;
    		String imgUrl = "";
    		if(i==101) {
    			sceneName = "下班到家场景";
    			imgUrl = "https://a.png";
    		}else if(i==102) {
    			sceneName = "上班出门场景";
    			imgUrl = "https://a.png";
    		}else if(i==103) {
    			sceneName = "睡觉场景";
    			imgUrl = "https://a.png";
    		}else if(i==104) {
    			sceneName = "起床场景";
    			imgUrl = "https://a.png";
    		}else if(i==105) {
    			sceneName = "我的场景";
    			imgUrl = "https://a.png";
    		}
    		Scene scene = new Scene();
    		scene.setIsSys(1);
    		scene.setSysSceneId((long)i);
    		scene.setScenceImgUrl(imgUrl);//TODO 需要默认图片
    		scene.setSceneName(sceneName);
    		scene.setUserId(userId);
    		scene.setFamilyId(familyId);
    		scene.setTriggerType(3L);
//    		scene.setTriggers("[]");
//    		scene.setActions("[]");
    		scene.setEnabled(1);
    		scene.setJobEnabled(0);
    		scene.setUserName(userName);
    		sceneService.insertScene(scene);
    		//TODO 网关默认场景数据推送
    	}
    	
    	//其他系统消息
    	msgService.sendMsg(new Msg("您的网关已绑定",MsgTypeConstant.MSG_TYPE_SYS_OTHER,old.getName()+"已成功绑定网关",null,
    			familyId,null,null,userId,null), true, false, false,MsgSettingEnum.SYS_OFFICAL.getIdentifier(),
				null, null);
    	return AjaxResult.success();
    }
    
    @Transactional
    @Override
    public AjaxResult applyJoinFamily(Long familyId,Long userId)
    {
    	//判断是否是自己拥有的家庭
    	Family old = familyMapper.selectFamilyOnlyByFamilyId(familyId);
    	if(old==null){
    		return AjaxResult.error("家庭信息不存在");
    	}
    	String gatewaySn = old.getGatewaySn();
    	Integer isEnableJoinAuth = old.getIsEnableJoinAuth();
    	if(StringUtils.isBlank(gatewaySn)){
    		return AjaxResult.error("当前家庭未绑定网关");
    	}
    	//是否已加入
    	FamilyUserRela userRela = familyMapper.selectUserByFamilyIdAndUserId(familyId, userId);
		if(userRela!=null){
			//非家庭成员,意外添加 不允许
			return AjaxResult.error("已经成为家庭成员,无需重复申请");
		}
		int userCount = familyMapper.countFamilyUser(familyId);
		// 数量校验
		BaseConfig baseConfig = baseConfigService.selectOneBaseConfigByCache();
		if(userCount>=baseConfig.getFamilUserMax()){
			//非家庭成员,意外添加 不允许
			return AjaxResult.error("家庭成员已满,请联系管理员");
		}
    	if(isEnableJoinAuth==null){
    		return AjaxResult.error("扫描网关加入家庭权限不能为空");
    	}else if(isEnableJoinAuth.intValue()==0){
    		//未开启权限校验  直接加入家庭
    		familyService.commonInsertUserToFamily(familyId, userId,"1");
            //生成成功加入家庭消息通知 
    		applyJoinFamilySendMsg(familyId, userId, false);
            return AjaxResult.success("成功加入家庭");
    	}else if(isEnableJoinAuth.intValue()==1){
    		// 是否已申请  --  未过期的申请
    		int count = countApplyFamilySendMsg(familyId, userId);
    		if(count>0) {
    			return AjaxResult.error("已经申请过,无需重复申请");
    		}
    		//已开启权限校验  需要审核通过后加入家庭
    		//生成家庭加入申请  生成对应消息通知 
    		applyJoinFamilySendMsg(familyId, userId, true);
    		return AjaxResult.success("申请成功");
    	}else {
    		return AjaxResult.error("扫描网关加入家庭权限信息有误");
    	}
    }
    /**
     * 判断是否重复申请
     * @param familyId
     * @param applyUserId
     * @return
     */
    private int countApplyFamilySendMsg(Long familyId,Long applyUserId) {
    	MsgOperMsg msg = new MsgOperMsg();
		msg.setSendUserId(applyUserId);
		msg.setMsgType("03");//申请
		msg.setIsOper(1);
		msg.setStatus("00");
		msg.setFamilyId(familyId);
		return msgOperMsgMapper.countMsgOperMsgList(msg );
    }
    /**
     * 
     * @param familyId
     * @param applyUserId
     * @param isNeedCheck 是否需要审核
     */
    private void applyJoinFamilySendMsg(Long familyId,Long applyUserId,boolean isNeedCheck){
    	List<FamilyUserRela> list = familyMapper.selectUserListByFamilyIdAndUserId(familyId, null);
    	SysUser user = sysUserMapper.selectUserById(applyUserId);
    	Date nowDate = DateUtils.getNowDate();
    	list.forEach(u -> {
    		/** 角色  1普通用户 2管理员 */
    		if("2".equals(u.getFamilyUserRole())) {
    			//每个管理员都需要发送
    			MsgOperMsg msgOperMsg = new MsgOperMsg();
    			msgOperMsg.setSendUserId(applyUserId);
    			msgOperMsg.setSendUserName(user.getNickName());
    			msgOperMsg.setReceiveUserId(u.getUserId());
    			msgOperMsg.setReceiveUserName(u.getNickName());
    			msgOperMsg.setMsgType("03");
    			msgOperMsg.setIsOper(isNeedCheck?1:0);
    			msgOperMsg.setMsgTypeName(isNeedCheck?"申请加入":"成功加入");
    			msgOperMsg.setFamilyId(familyId);
    			msgOperMsg.setFamilyName(u.getfamilyName());
    			msgOperMsg.setStatus(isNeedCheck?"00":"01");//00 未处理 01已同意 02已拒绝 98已失效(别的请求已同意) 99已过期
    			msgOperMsg.setAddTime(nowDate);
    			msgOperMsgMapper.insertMsgOperMsg(msgOperMsg);
    			
    			//其他系统消息
    			String title = isNeedCheck?"申请加入家庭":"成功加入家庭";
            	msgService.sendMsg(new Msg(title,MsgTypeConstant.MSG_TYPE_SYS_OTHER,user.getNickName()+title,null,
            			familyId,null,null,u.getUserId(),null), true, false, false,MsgSettingEnum.SYS_OFFICAL.getIdentifier(),
        				null, null);
    		}
    	});
    }
    
    @Transactional
    @Override
	public void commonInsertUserToFamily(Long familyId, Long userId,String role) {
		List<FamilyUserRela> list = new ArrayList<FamilyUserRela>();
		FamilyUserRela familyUserRela = new FamilyUserRela();
		familyUserRela.setFamilyId(familyId);
		familyUserRela.setUserId(userId);
		familyUserRela.setFamilyUserRole(role);//普通成员   刚加入都是普通成员
		familyUserRela.setCreateTime(DateUtils.getNowDate());
		list.add(familyUserRela);
		familyMapper.batchFamilyUserRela(list);
	}

    /**
     * 批量删除家庭管理
     * 
     * @param familyIds 需要删除的家庭管理主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteFamilyByFamilyIds(Long[] familyIds)
    {
        familyMapper.deleteRoomByFamilyIds(familyIds);
        familyMapper.deleteFamilyUserRelaByFamilyIds(familyIds);
        return familyMapper.deleteFamilyByFamilyIds(familyIds);
    }

    /**
     * 删除家庭管理信息
     * 
     * @param familyId 家庭管理主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteFamilyByFamilyId(Long familyId)
    {
        familyMapper.deleteRoomByFamilyId(familyId);
        familyMapper.deleteFamilyUserRelaByFamilyId(familyId);
        return familyMapper.deleteFamilyByFamilyId(familyId);
    }
    
    /**
     * 删除家庭管理信息
     * 
     * @param familyId 家庭管理主键
     * @return 结果
     * @throws SchedulerException 
     */
    @Deprecated
    @Transactional
    @Override
    public AjaxResult deleteFamilyByFamilyId(Long familyId,Long userId) throws SchedulerException
    {
    	//判断是否是自己拥有的家庭
    	Family family = familyMapper.selectFamilyOnlyByFamilyId(familyId);
    	if(family==null){
    		return AjaxResult.error("家庭信息不存在");
    	}
    	if(!family.getBelongUserId().equals(userId)){
    		return AjaxResult.error("不是自己的家庭,无法删除");
    	}
    	Family query = new Family();
    	query.setBelongUserId(userId);
		List<Family> list = familyMapper.selectFamilyList(query );
		if(list.size()<2){
			return AjaxResult.error("无法删除默认家庭");
		}
    	//删除网关中的家庭信息  因为网关不做删除 只做数据清除
		Integer isBind = family.getIsBind();
		if(1==isBind) {
			Device device = new Device();
			device.setDeviceType(2);//网关字设备
			device.setBelongFamilyId(familyId);
			List<DeviceShortOutput> deviceList = deviceMapper.selectDeviceShortList2(device);
			if(deviceList.size()>0) {
				return AjaxResult.error("当前家庭存在使用的设备，请先删除后再删除家庭");
			}
			String gatewaySn = family.getGatewaySn();
			log.info("当前家庭已绑定网关,需要清除网关中的家庭信息 ,家庭id:{},网关sn码:{}",familyId,gatewaySn);
			if(StringUtils.isNotBlank(gatewaySn)) {
				//查询网关设备 确认网关设备sn码不会与普通设备sn码重复
				Device gwDevice = deviceMapper.selectDeviceBySerialNumber(gatewaySn);
				if(gwDevice==null) {
					log.error("网关设备不存在,请查看原因");
				}else {
					deviceMapper.clearDeviceFamilyInfo(gwDevice.getDeviceId());
				}
			}else {
				log.warn("网关信息为空...无法处理该数据");
			}
		}
		//删除家庭房间
		log.info("通过家庭删除房间--------------");
    	familyMapper.deleteRoomByFamilyId(familyId);
    	//删除家庭成员
    	log.info("通过家庭删除成员--------------");
    	familyMapper.deleteFamilyUserRelaByFamilyId(familyId);
    	//删除设备
    	log.info("通过家庭业务删除(非网关删除)设备--------------");
    	familyDeviceService.deleteCsDeviceNoGwByFamilyId(familyId);
    	//删除场景(删除设备后场景中理论上没有任何设备)
    	log.info("通过家庭删除场景--------------");
    	sceneService.deleteSceneByFamilyId(familyId);
    	//  删除家庭设备告警记录 (删除设备后理论上没有任何告警记录)
    	log.info("通过家庭删除告警记录--------------");
    	alertLogMapper.deleteAlertLog(familyId, null);
    	//删除家庭相关审核消息 (删除设备后理论上没有任何设备相关的审核消息,只有家庭相关的审核消息)
    	log.info("通过家庭删除审核消息--------------");
    	msgOperMsgMapper.deleteMsgOperMsg(null, null, familyId, null);
    	//删除消息(删除设备后消息中理论上没有任何设备相关的消息,只有家庭相关的消息)
    	log.info("通过家庭删除 系统消息/家庭消息--------------");
    	msgMapper.deleteMsg(null, familyId, null);
    	//删除消息通知设置(删除设备后消息中理论上没有任何设备相关的设置,只有单独家庭通知的设置)
    	log.info("通过家庭删除 消息通知设置--------------");
    	String busId = familyId+"";
    	String identifier = MsgSettingEnum.DEV_FAMILY.getIdentifier();
    	MsgNoticeSetting dto = new MsgNoticeSetting();
    	dto.setIdentifier(identifier);
    	dto.setBusId(busId);
		List<MsgNoticeSetting> msgSList = msgNoticeSettingMapper.selectMsgNoticeSettingList(dto);
		for(MsgNoticeSetting aa: msgSList) {
			log.info("通过家庭删除 消息用户设备通知设置--------------用户id:{},parentId:{}",aa.getUserId(),aa.getId());
			msgNoticeSettingMapper.deleteMsgNoticeSettingByParentId(aa.getId());
		}
    	msgNoticeSettingMapper.deleteMsgNoticeSetting(null, identifier, busId);
    	//删除家庭
    	log.info("最后删除家庭信息---------------");
    	familyMapper.deleteFamilyByFamilyId(familyId);
    	return AjaxResult.success();
    }
    
    @Transactional
    @Override
    public AjaxResult deleteSoftFamilyByFamilyId(Long familyId,Long userId) throws SchedulerException
    {
    	//判断是否是自己拥有的家庭
    	Family family = familyMapper.selectFamilyOnlyByFamilyId(familyId);
    	if(family==null){
    		return AjaxResult.error("家庭信息不存在");
    	}
    	if(!family.getBelongUserId().equals(userId)){
    		return AjaxResult.error("不是自己的家庭,无法删除");
    	}
    	Family query = new Family();
    	query.setBelongUserId(userId);
    	List<Family> list = familyMapper.selectFamilyList(query );
    	if(list.size()<2){
    		return AjaxResult.error("无法删除默认家庭");
    	}
    	//删除网关中的家庭信息  因为网关不做删除 只做数据清除
    	Integer isBind = family.getIsBind();
    	if(1==isBind) {
    		String gatewaySn = family.getGatewaySn();
    		log.info("当前家庭已绑定网关,需要清除网关中的家庭信息 ,家庭id:{},网关sn码:{}",familyId,gatewaySn);
    		if(StringUtils.isNotBlank(gatewaySn)) {
    			//查询网关设备 确认网关设备sn码不会与普通设备sn码重复
    			Device gwDevice = deviceMapper.selectDeviceBySerialNumber(gatewaySn);
    			if(gwDevice==null) {
    				log.error("网关设备不存在,请查看原因");
    			}else {
    				deviceMapper.clearDeviceFamilyInfo(gwDevice.getDeviceId());
    			}
    		}else {
    			log.warn("网关信息为空...无法处理该数据");
    		}
    	}
    	//删除家庭房间
    	log.info("通过家庭删除房间--------------");
    	familyMapper.deleteRoomByFamilyId(familyId);
    	//删除家庭成员
    	log.info("通过家庭删除成员--------------");
    	familyMapper.deleteFamilyUserRelaByFamilyId(familyId);
    	//删除设备
    	log.info("通过家庭业务删除(非网关删除)设备--------------");
    	familyDeviceService.deleteCsDeviceNoGwByFamilyId(familyId);
    	//删除场景(删除设备后场景中理论上没有任何设备)
    	log.info("通过家庭删除场景--------------");
    	sceneService.deleteSceneByFamilyId(familyId);
    	//  删除家庭设备告警记录 (删除设备后理论上没有任何告警记录)
    	log.info("通过家庭删除告警记录--------------");
    	alertLogMapper.deleteAlertLog(familyId, null);
    	//删除家庭相关审核消息 (删除设备后理论上没有任何设备相关的审核消息,只有家庭相关的审核消息)
    	log.info("通过家庭删除审核消息--------------");
    	msgOperMsgMapper.deleteMsgOperMsg(null, null, familyId, null);
    	//删除消息(删除设备后消息中理论上没有任何设备相关的消息,只有家庭相关的消息)
    	log.info("通过家庭删除 系统消息/家庭消息--------------");
    	msgMapper.deleteMsg(null, familyId, null);
    	//删除消息通知设置(删除设备后消息中理论上没有任何设备相关的设置,只有单独家庭通知的设置)
    	log.info("通过家庭删除 消息通知设置--------------");
    	String busId = familyId+"";
    	String identifier = MsgSettingEnum.DEV_FAMILY.getIdentifier();
    	MsgNoticeSetting dto = new MsgNoticeSetting();
    	dto.setIdentifier(identifier);
    	dto.setBusId(busId);
		List<MsgNoticeSetting> msgSList = msgNoticeSettingMapper.selectMsgNoticeSettingList(dto);
		for(MsgNoticeSetting aa: msgSList) {
			log.info("通过家庭删除 消息用户设备通知设置--------------用户id:{},parentId:{}",aa.getUserId(),aa.getId());
			msgNoticeSettingMapper.deleteMsgNoticeSettingByParentId(aa.getId());
		}
    	msgNoticeSettingMapper.deleteMsgNoticeSetting(null, identifier, busId);
    	//删除家庭
    	log.info("最后删除家庭信息---------------");
    	familyMapper.deleteFamilyByFamilyId(familyId);
    	return AjaxResult.success();
    }
    @Transactional
    @Override
    public AjaxResult deleteRoomByRoomId(Long roomId,Long userId)
    {
    	Room oldRoom = familyMapper.selectRoomByRoomId(roomId);
    	Long familyId = oldRoom.getFamilyId();
		FamilyUserRela userRela = familyMapper.selectUserByFamilyIdAndUserId(familyId, userId);
		if(userRela==null){
			//非家庭成员,意外添加 不允许
			return AjaxResult.error("非家庭成员删除失败");
		}
		//// 权限校验
		if(!"2".equals(userRela.getFamilyUserRole())){
			return AjaxResult.error("权限不足");
		}
		familyMapper.deleteRoomByFamilyIdAndRoomId(familyId, roomId);
		//清除设备中的 房间信息 将所属房间改为未分配
		deviceMapper.clearDeviceRoomInfo(roomId);
    	return AjaxResult.success();
    }
    @Transactional
    @Override
    public AjaxResult quitFamilyByFamilyId(Long familyId,Long userId)
    {
    	//判断是否是自己拥有的家庭
    	Family family = familyMapper.selectFamilyOnlyByFamilyId(familyId);
    	if(family==null){
    		return AjaxResult.error("家庭信息不存在");
    	}
    	if(family.getBelongUserId().equals(userId)){
    		return AjaxResult.error("不能退出自己的家庭");
    	}
    	FamilyUserRela userRela = familyMapper.selectUserByFamilyIdAndUserId(familyId, userId);
    	if(userRela==null){
    		return AjaxResult.error("非家庭成员,无需退出");
    	}
    	deleteFamilyUserInfo(familyId,userId);
    	return AjaxResult.success();
    }
    
    @Override
    public List<Room> selectRoomByFamilyId(Long familyId){
    	return familyMapper.selectRoomByFamilyId(familyId);
    }
    @Override
    public List<RoomStat> selectRoomAndStatByFamilyId(Long familyId,Long userId){
    	FamilyUserRela userRela = familyMapper.selectUserByFamilyIdAndUserId(familyId, userId);
    	if(userRela==null){
//    		/非家庭成员,
    		return new ArrayList<>();
    	}
    	return familyMapper.selectRoomAndStatByFamilyId(familyId);
    }
    @Override
    public List<RoomStat> selectRoomAndStatByFamilyId(Long familyId){
    	if(familyId==null){
    		return new ArrayList<>();
    	}
    	return familyMapper.selectRoomAndStatByFamilyId(familyId);
    }
    

    @Transactional
    @Override
    public AjaxResult insertRoom(Room	 room,Long userId)
    {
        Long familyId = room.getFamilyId();
        
        if (StringUtils.isBlank(room.getName()))
        {
        	//没有名称不能创建
        	return AjaxResult.error("房间名称不能为空");
        }
        if (familyId!=null)
        {
        	FamilyUserRela userRela = familyMapper.selectUserByFamilyIdAndUserId(familyId, userId);
        	if(userRela==null){
        		//非家庭成员,意外添加 不允许
        		return AjaxResult.error("权限不足");
        	}
        	// 权限校验
        	if(!"2".equals(userRela.getFamilyUserRole())){
    			return AjaxResult.error("权限不足");
    		}
        	// 房间数校验  无房间数校验
        	room.setRoomId(null);
        	room.setDelFlag("0");
        	room.setCreateTime(DateUtils.getNowDate());
        	familyMapper.insertRoom(room);
        	return AjaxResult.success(room.getRoomId());
        }else {
        	return AjaxResult.error("家庭信息有误");
        }
    }
    @Transactional
    @Override
    public AjaxResult updateRoomName(Room	 room,Long userId)
    {
    	Long roomId = room.getRoomId();
    	Room oldRoom = familyMapper.selectRoomByRoomId(roomId);
    	Long familyId = oldRoom.getFamilyId();
    	if (StringUtils.isBlank(room.getName()))
    	{
    		//没有名称不能
    		return AjaxResult.error("房间名称不能为空");
    	}
		FamilyUserRela userRela = familyMapper.selectUserByFamilyIdAndUserId(familyId, userId);
		if(userRela==null){
    		//非家庭成员,意外添加 不允许
    		return AjaxResult.error("权限不足");
    	}
		//权限校验
		if(!"2".equals(userRela.getFamilyUserRole())){
			return AjaxResult.error("权限不足");
		}
		oldRoom.setName(room.getName());
		oldRoom.setUpdateTime(DateUtils.getNowDate());
		familyMapper.updateRoom(oldRoom);
		return AjaxResult.success();
    }
    
    @Transactional
    @Override
    public AjaxResult updateFamilyUser(UpdateFamilyUserReqDto dto,Long loginUserId) {
    	Long familyId = dto.getFamilyId();
    	Long userId = dto.getUserId();
    	if(loginUserId.equals(userId)) {
    		return AjaxResult.error("不能修改自己的角色");
    	}
    	String familyUserRole = dto.getFamilyUserRole();
    	if(!"1".equals(familyUserRole) && !"2".equals(familyUserRole)) {
    		return AjaxResult.error("设置角色有误");
    	}
//    	TODO 如果是拥有者有权限  需要校验家庭信息
    	Family family = familyMapper.selectFamilyOnlyByFamilyId(familyId);
    	if(family==null){
    		return AjaxResult.error("家庭信息不存在");
    	}
    	if(userId.equals(family.getBelongUserId())) {
    		return AjaxResult.error("不能修改家庭创建者角色");
    	}
    	FamilyUserRela loginUserRela = familyMapper.selectUserByFamilyIdAndUserId(familyId, loginUserId);
		if(loginUserRela==null){
    		//非家庭成员 不允许
    		return AjaxResult.error("权限不足");
    	}
		//权限校验  
		if(!"2".equals(loginUserRela.getFamilyUserRole())){
			return AjaxResult.error("权限不足");
		}
		FamilyUserRela userRela = familyMapper.selectUserByFamilyIdAndUserId(familyId, userId);
		if(userRela==null){
    		//非家庭成员 不允许
    		return AjaxResult.error("用户不属于家庭成员");
    	}
		familyMapper.updateUserRoleByFamilyIdAndUserId(familyId, userId, familyUserRole);
		return AjaxResult.success("设置成功");
    	
    }
    @Transactional
    @Override
    public AjaxResult inviteFamilyUser(InviteFamilyUserReqDto dto,Long loginUserId) {
    	Long familyId = dto.getFamilyId();
    	String phonenumber = dto.getPhonenumber();
    	SysUser info = sysUserMapper.checkPhoneUnique(phonenumber,"01");
    	if(info==null) {
    		return AjaxResult.error("手机号未注册,请通知对方下载APP注册后,再进行分享");
    	}
    	Long userId = info.getUserId();
    	String familyUserRole = dto.getFamilyUserRole();
    	if(familyUserRole==null) {
    		familyUserRole = "1";//不传则邀请为普通成员
    	}
//    	familyUserRole = "1";//不管是谁邀请过来 都是普通用户
//    	 如果是拥有者有权限  需要校验家庭信息
    	Family family = familyMapper.selectFamilyOnlyByFamilyId(familyId);
    	if(family==null){
    		return AjaxResult.error("家庭信息不存在");
    	}
    	FamilyUserRela userRela = familyMapper.selectUserByFamilyIdAndUserId(familyId, userId);
		if(userRela!=null){
    		//用户已经属于家庭成员,无需重复邀请"
    		return AjaxResult.error("用户已经属于家庭成员,无需重复邀请");
    	}
    	FamilyUserRela loginUserRela = familyMapper.selectUserByFamilyIdAndUserId(familyId, loginUserId);
		if(loginUserRela==null){
    		//非家庭成员 不允许
    		return AjaxResult.error("权限不足");
    	}
		//权限校验  
		if(!"2".equals(loginUserRela.getFamilyUserRole())){
//			return AjaxResult.error("权限不足");
//			不是管理员 只能邀请普通成员
			familyUserRole = "1";
			return AjaxResult.error("权限不足");
		}
//		userRela.setFamilyUserRole(familyUserRole);
//		理论上邀请过来都是普通成员比较合理 邀请完后 再主动调整为管理员  这样邀请信息不需要存 邀请角色 权限也比较合理
		// 生成邀请信息  消息通知 等
		//判断是否已经邀请过
		int count = familyService.countFamilyOrDeviceSendMsg(familyId,family.getName(),null,null, userId,loginUserId);
		if(count>0) {
			return AjaxResult.error("已经邀请过,无需重复邀请");
		}
		familyService.shareFamilyOrDeviceSendMsg(familyId,family.getName(),null,null, userId,loginUserId, familyUserRole);
		return AjaxResult.success("邀请成功");
    }
    @Override
    public int countFamilyOrDeviceSendMsg(Long familyId,String familyName,
    		Long deviceId,String deviceName,
    		Long userId,Long loginUserId) {
    	MsgOperMsg msg = new MsgOperMsg();
		msg.setReceiveUserId(userId);
		msg.setSendUserId(loginUserId);
		msg.setMsgType(deviceId==null?"01":"02");
		msg.setIsOper(1);
		msg.setStatus("00");
		msg.setFamilyId(familyId);
		msg.setDeviceId(deviceId);
		return msgOperMsgMapper.countMsgOperMsgList(msg );
    }
    //要有事务 不能失败
    @Transactional
    @Override
    public void shareFamilyOrDeviceSendMsg(Long familyId,String familyName,
    		Long deviceId,String deviceName,
    		Long userId,Long loginUserId, String familyUserRole){
    	SysUser user = sysUserMapper.selectUserById(userId);
    	SysUser u = sysUserMapper.selectUserById(loginUserId);
    	Date nowDate = DateUtils.getNowDate();
    	
		MsgOperMsg msgOperMsg = new MsgOperMsg();
		msgOperMsg.setReceiveUserId(user.getUserId());
		msgOperMsg.setReceiveUserName(user.getNickName());
		msgOperMsg.setSendUserId(u.getUserId());
		msgOperMsg.setSendUserName(u.getNickName());
		msgOperMsg.setMsgType(deviceId==null?"01":"02");
		msgOperMsg.setIsOper(1);
		msgOperMsg.setMsgTypeName(deviceId==null?"分享了":"共享");//家庭显示分享了 设备显示共享
		msgOperMsg.setFamilyId(familyId);
		msgOperMsg.setFamilyName(familyName);
		msgOperMsg.setDeviceId(deviceId);
		msgOperMsg.setDeviceName(deviceName);
		msgOperMsg.setStatus("00");//00 未处理 01已同意 02已拒绝 98已失效(别的请求已同意) 99已过期
		msgOperMsg.setAddTime(nowDate);
		msgOperMsg.setFamilyUserRole(familyUserRole);
		msgOperMsgMapper.insertMsgOperMsg(msgOperMsg);
		//其他系统消息
		String title = deviceId==null?"收到家庭分享":"收到设备共享";
		String alertName = u.getNickName()+msgOperMsg.getMsgTypeName()+(deviceId==null?"家庭"+familyName:"设备"+deviceName);
    	msgService.sendMsg(new Msg(title,MsgTypeConstant.MSG_TYPE_SYS_OTHER,alertName,null,
    			familyId,null,deviceId,user.getUserId(),null), true, false, false,deviceId==null?MsgSettingEnum.SYS_FAMILY_SHARE.getIdentifier():MsgSettingEnum.SYS_DEVICE_SHARE.getIdentifier(),
				null, null);
    }
    @Transactional
    @Override
    public AjaxResult deleteFamilyUser( Long familyId, Long userId, Long loginUserId) {
    	if(loginUserId.equals(userId)) {
    		return AjaxResult.error("不能删除自己");
    	}
//    	TODO 如果是拥有者有权限  需要校验家庭信息
    	Family family = familyMapper.selectFamilyOnlyByFamilyId(familyId);
    	if(family==null){
    		return AjaxResult.error("家庭信息不存在");
    	}
    	if(userId.equals(family.getBelongUserId())) {
    		return AjaxResult.error("不能删除家庭创建者");
    	}
    	FamilyUserRela loginUserRela = familyMapper.selectUserByFamilyIdAndUserId(familyId, loginUserId);
		if(loginUserRela==null){
    		//非家庭成员 不允许
    		return AjaxResult.error("权限不足");
    	}
		//权限校验  
		if(!"2".equals(loginUserRela.getFamilyUserRole())){
			return AjaxResult.error("权限不足");
		}
		FamilyUserRela userRela = familyMapper.selectUserByFamilyIdAndUserId(familyId, userId);
		if(userRela==null){
    		//非家庭成员 不允许
			//相当于直接删除了
			return AjaxResult.success("用户已删除");
    	}
		// 删除家庭成员 和  家庭成员退出 应该有同样的操作  
		deleteFamilyUserInfo(familyId, userId);
		return AjaxResult.success("删除成功");
    }
    
    /**
     * 删除/退出家庭时成员信息处理   俩种情况应该保持处理一致
     * @param familyId
     * @param userId
     */
    public void deleteFamilyUserInfo(Long familyId,Long userId) {
    	// 判断此成员在此家庭是否有设备定时
    	// 判断此成员在此家庭是否有场景
    	//无需判断 上述两种情况 场景和 设备均属于家庭所有  成员退出后家庭中设备定时和场景扔需保留
    	familyMapper.deleteFamilyUserRelaByFamilyIdAndUserId(familyId, userId);
    	
    	//删除该成员的消息设置
    	log.info("通过家庭删除 消息通知设置--------------");
    	String busId = familyId+"";
    	String identifier = MsgSettingEnum.DEV_FAMILY.getIdentifier();
    	MsgNoticeSetting dto = new MsgNoticeSetting();
    	dto.setIdentifier(identifier);
    	dto.setBusId(busId);
    	dto.setUserId(userId);
		List<MsgNoticeSetting> msgSList = msgNoticeSettingMapper.selectMsgNoticeSettingList(dto);
		for(MsgNoticeSetting aa: msgSList) {
			log.info("通过家庭删除 消息用户设备通知设置--------------用户id:{},parentId:{}",aa.getUserId(),aa.getId());
			msgNoticeSettingMapper.deleteMsgNoticeSettingByParentId(aa.getId());
		}
    	msgNoticeSettingMapper.deleteMsgNoticeSetting(userId, identifier, busId);
    	//TODO 删除本人在此家庭中的消息
    	//TODO 删除本人在此家庭中的操作记录 等等===
    }
    
    @Override
    public boolean isCommonPerm(Long familyId, Long userId) {
    	Family family = familyMapper.selectFamilyOnlyByFamilyId(familyId);
    	if(family==null){
    		log.error("家庭信息不存在");
    		return false;
    	}
    	FamilyUserRela loginUserRela = familyMapper.selectUserByFamilyIdAndUserId(familyId, userId);
    	if(loginUserRela==null){
    		//非家庭成员 不允许
    		log.error("非家庭成员");
    		return false;
    	}else {
    		log.info("家庭成员");
    		return true;
    	}
    }
    @Override
    public boolean isAdminPerm(Long familyId, Long userId) {
    	Family family = familyMapper.selectFamilyOnlyByFamilyId(familyId);
    	if(family==null){
    		log.error("家庭信息不存在");
    		return false;
    	}
    	FamilyUserRela loginUserRela = familyMapper.selectUserByFamilyIdAndUserId(familyId, userId);
		if(loginUserRela==null){
    		//非家庭成员 不允许
			log.error("非家庭成员");
    		return false;
    	}
		//权限校验  
		if(!"2".equals(loginUserRela.getFamilyUserRole())){
			log.info("普通家庭成员");
			return false;
		}else {
			log.info("管理员");
			return true;
		}
    }
    @Override
    public boolean isSuperAdminPerm(Long familyId, Long userId) {
    	Family family = familyMapper.selectFamilyOnlyByFamilyId(familyId);
    	if(family==null){
    		log.error("家庭信息不存在");
    		return false;
    	}
		//权限校验  
		if(!userId.equals(family.getBelongUserId())){
			log.info("普通家庭成员或一般管理员");
			return false;
		}else {
			log.info("超级管理员");
			return true;
		}
    }
}
