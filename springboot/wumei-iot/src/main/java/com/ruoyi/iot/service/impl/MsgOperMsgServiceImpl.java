package com.ruoyi.iot.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.iot.domain.FamilyUserRela;
import com.ruoyi.iot.domain.MsgOperMsg;
import com.ruoyi.iot.mapper.FamilyMapper;
import com.ruoyi.iot.mapper.MsgOperMsgMapper;
import com.ruoyi.iot.mobile.model.FamilyShareStatRespDto;
import com.ruoyi.iot.mobile.model.UpdateMsgOperMsg;
import com.ruoyi.iot.mobile.respModel.DeviceShareInfoRespDto;
import com.ruoyi.iot.service.IFamilyService;
import com.ruoyi.iot.service.IMsgOperMsgService;

/**
 * 需操作消息列表Service业务层处理
 * 
 * @author renjiayue
 * @date 2022-09-18
 */
@Transactional
@Service
public class MsgOperMsgServiceImpl implements IMsgOperMsgService 
{
    @Autowired
    private MsgOperMsgMapper msgOperMsgMapper;
    @Autowired
    private FamilyMapper familyMapper;
    @Autowired
    private IFamilyService familyService;

    /**
     * 查询需操作消息列表
     * 
     * @param id 需操作消息列表主键
     * @return 需操作消息列表
     */
    @Override
    public MsgOperMsg selectMsgOperMsgById(Long id)
    {
        return msgOperMsgMapper.selectMsgOperMsgById(id);
    }

    /**
     * 查询需操作消息列表列表
     * 
     * @param msgOperMsg 需操作消息列表
     * @return 需操作消息列表
     */
    @Override
    public List<MsgOperMsg> selectMsgOperMsgList(MsgOperMsg msgOperMsg)
    {
        return msgOperMsgMapper.selectMsgOperMsgList(msgOperMsg);
    }
    @Override
    public List<MsgOperMsg> selectShareOperMsgList(MsgOperMsg msgOperMsg)
    {
    	return msgOperMsgMapper.selectShareOperMsgList(msgOperMsg);
    }
    @Override
    public List<DeviceShareInfoRespDto> selectShareMsgWithRecDetailList(MsgOperMsg msgOperMsg){
    	return msgOperMsgMapper.selectShareMsgWithRecDetailList(msgOperMsg);
    }
    
    @Override
    public List<FamilyShareStatRespDto> selectFamilyShareStatList(Long sendUserId, Long familyId)
    {
    	return msgOperMsgMapper.selectFamilyShareStatList(sendUserId,familyId);
    }
    
    @Override
    public List<FamilyShareStatRespDto> selectDeviceShareStatList(Long sendUserId, Long familyId, Long deviceId) {
    	return msgOperMsgMapper.selectDeviceShareStatList(sendUserId,familyId,deviceId);
    }

    /**
     * 新增需操作消息列表
     * 
     * @param msgOperMsg 需操作消息列表
     * @return 结果
     */
    @Override
    public int insertMsgOperMsg(MsgOperMsg msgOperMsg)
    {
        return msgOperMsgMapper.insertMsgOperMsg(msgOperMsg);
    }

    /**
     * 修改需操作消息列表
     * 
     * @param msgOperMsg 需操作消息列表
     * @return 结果
     */
    @Override
    public int updateMsgOperMsg(MsgOperMsg msgOperMsg)
    {
        return msgOperMsgMapper.updateMsgOperMsg(msgOperMsg);
    }

    /**
     * 批量删除需操作消息列表
     * 
     * @param ids 需要删除的需操作消息列表主键
     * @return 结果
     */
    @Override
    public int deleteMsgOperMsgByIds(Long[] ids)
    {
        return msgOperMsgMapper.deleteMsgOperMsgByIds(ids);
    }

    /**
     * 删除需操作消息列表信息
     * 
     * @param id 需操作消息列表主键
     * @return 结果
     */
    @Override
    public int deleteMsgOperMsgById(Long id)
    {
        return msgOperMsgMapper.deleteMsgOperMsgById(id);
    }
    
    @Transactional
    @Override
	public AjaxResult operateOperMsg(UpdateMsgOperMsg msg,Long userId) {
    	Long id = msg.getId();
    	MsgOperMsg oldMsg = msgOperMsgMapper.selectMsgOperMsgById(id);
    	if(oldMsg==null || !userId.equals(oldMsg.getReceiveUserId())) {
    		//消息不存在或消息有误
    		return AjaxResult.error("操作失败");
    	}
    	if(!"00".equals(oldMsg.getStatus())) {
    		return AjaxResult.error("消息已处理过");
    	}
    	String status = msg.getStatus();
    	String msgType = oldMsg.getMsgType();//01家庭分享 02设备分享 03家庭申请
    	if("01".equals(status)) {
    		//如果是同意 则需要处理消息内容
    		if("01".equals(msgType) || "02".equals(msgType) || "03".equals(msgType)) {
    			Long familyId = oldMsg.getFamilyId();
    			if("03".equals(msgType)) {
    				//主动申请加入家庭的情况需要 管理员有权限才能审核成功
    				//判断当前用户是否有权限
    				FamilyUserRela userRela = familyMapper.selectUserByFamilyIdAndUserId(familyId, userId);
    				if(userRela==null){
    					return AjaxResult.error("权限不足");
    				}
    				if(!"2".equals(userRela.getFamilyUserRole())){
    					return AjaxResult.error("权限不足");
    				}
    			}
    			//分享需要消息接收人加入家庭  申请需要消息发送人加入家庭
    	    	
    	    	//判断需要加入家庭的用户是否已经加入家庭
    	    	
    			Long needJoinUserId = null;
    			String defaultRole = "1";
    			if("01".equals(msgType) || "02".equals(msgType) ) {
    				needJoinUserId = userId;
    				if(oldMsg.getFamilyUserRole()!=null) {
    					defaultRole = oldMsg.getFamilyUserRole();//分享/邀请的用户 需要按邀请时的角色赋予对应角色
    				}
    			}else if("03".equals(msgType) ) {
    				needJoinUserId = oldMsg.getSendUserId();
    			}
    			FamilyUserRela needUserRela = familyMapper.selectUserByFamilyIdAndUserId(familyId, needJoinUserId);
    			if(needUserRela!=null) {
    				//已经加入 不需要做额外处理
    			}else {
    				//未加入 则新增家庭成员关系
    				familyService.commonInsertUserToFamily(familyId, needJoinUserId,defaultRole);
    			}
    			//更新当前消息为成功
    			MsgOperMsg msgOperMsg = new MsgOperMsg();
    	    	msgOperMsg.setId(msg.getId());
    	    	msgOperMsg.setStatus(status);
    	    	msgOperMsg.setOperTime(DateUtils.getNowDate());
    			msgOperMsgMapper.updateMsgOperMsg(msgOperMsg);
				//成功后 需要把其他消息置为失效  即不需要再处理其他的 接收分享或同意申请
    			//更新其他 和加入家庭相关消息更新为失效 
    			//01 02 03
    			MsgOperMsg otherMsg1 = new MsgOperMsg();
    			otherMsg1.setReceiveUserId(needJoinUserId);
    			otherMsg1.setMsgType("01");
    			otherMsg1.setFamilyId(familyId);
    			msgOperMsgMapper.updateOperMsgTo98(otherMsg1);
    			MsgOperMsg otherMsg2 = new MsgOperMsg();
    			otherMsg2.setReceiveUserId(needJoinUserId);
    			otherMsg2.setMsgType("02");
    			otherMsg2.setFamilyId(familyId);
    			msgOperMsgMapper.updateOperMsgTo98(otherMsg2);
    			MsgOperMsg otherMsg3 = new MsgOperMsg();
    			otherMsg3.setSendUserId(needJoinUserId);
    			otherMsg3.setMsgType("03");
    			otherMsg3.setFamilyId(familyId);
    			msgOperMsgMapper.updateOperMsgTo98(otherMsg3);
    		}
    	}else if("02".equals(status)) {
    		//如果不同意 则只更新消息状态即可 不做其他操作
    		//更新当前消息为失败
			MsgOperMsg msgOperMsg = new MsgOperMsg();
	    	msgOperMsg.setId(msg.getId());
	    	msgOperMsg.setStatus(status);
	    	msgOperMsg.setOperTime(DateUtils.getNowDate());
			msgOperMsgMapper.updateMsgOperMsg(msgOperMsg);
    	}else {
    		return AjaxResult.error("操作有误");
    	}
    	
		return AjaxResult.success();
	}
}
