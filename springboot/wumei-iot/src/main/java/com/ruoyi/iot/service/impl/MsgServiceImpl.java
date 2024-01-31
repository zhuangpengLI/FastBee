package com.ruoyi.iot.service.impl;

import java.util.List;
import java.util.Map;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SmsUtils;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruoyi.iot.mapper.MsgMapper;
import com.ruoyi.iot.service.IMsgNoticeSettingService;
import com.ruoyi.iot.service.IMsgSmsNoticeSettingService;
import com.ruoyi.iot.util.JPushUtil;
import com.ruoyi.system.otherDomain.Msg;
import com.ruoyi.system.otherDto.MsgSettingEnum;
import com.ruoyi.system.otherDto.SmsSettingEnum;
import com.ruoyi.system.otherService.IMsgService;
import com.ruoyi.system.service.ISysNoticeService;

/**
 * 消息通知Service业务层处理
 * 
 * @author renjiayue
 * @date 2022-10-06
 */
@Service
public class MsgServiceImpl implements IMsgService 
{
    private static final Logger log = LoggerFactory.getLogger(MsgServiceImpl.class);
    @Autowired
    private MsgMapper msgMapper;
    @Autowired
    private ISysNoticeService noticeService;
    @Lazy
    @Autowired
    private IMsgNoticeSettingService msgNoticeSettingService;
    @Lazy
    @Autowired
    private IMsgSmsNoticeSettingService msgSmsNoticeSettingService;
    @Autowired
    private JPushUtil jPushUtil;

    /**
     * 查询消息通知
     * 
     * @param id 消息通知主键
     * @return 消息通知
     */
    @Override
    public Msg selectMsgById(Long id)
    {
        return msgMapper.selectMsgById(id);
    }

    /**
     * 查询消息通知列表
     * 
     * @param msg 消息通知
     * @return 消息通知
     */
    @Override
    public List<Msg> selectMsgList(Msg msg)
    {
        return msgMapper.selectMsgList(msg);
    }
    @Override
    public List<Msg> selectSystemMsgList(Msg msg)
    {
    	return msgMapper.selectSystemMsgList(msg);
    }

    @Async
    @Override
    public void sendMsg(Msg msg,boolean isPush,boolean isSms,boolean isCall,String pushType,String pushBusId, Map<String, String> smsParam)
    {
    	int insertMsg = insertMsg(msg);
    	if(isPush) {
    		try {
    			try {
					boolean sendNotice = msgNoticeSettingService.isSendNotice(pushType, pushBusId, msg.getUserId());
					if(sendNotice) {
						//如果用户启用了通知 再通知
						jPushUtil.sendAlertAndMessageToMemberId(msg.getUserId(), msg.getMsgTitle(), msg.getMsgContent(), null);
					}
				} catch (Exception e) {
					log.error("Jpush 推送异常",e);
					e.printStackTrace();
				}
    			log.info("是否发送报警短信====={},",isSms);
    			if(isSms) {
    				try {
						String smsType = "ALERT";//报警类型再做推送
						log.info("开始发送报警短信====={},pushId:{},",smsParam.get("smsType"),pushBusId);
						if(smsType.equals(smsParam.get("smsType"))) {
							//暂时短信只用单一统一设置
							boolean sendNotice2 = msgSmsNoticeSettingService.isSendNotice(SmsSettingEnum.DEV_TOTAL.getIdentifier(), null, msg.getUserId());
							log.info("当前用户userId:{},是否开启短信通知====={},",msg.getUserId(),sendNotice2);
							if(sendNotice2) {
								int sendAlertMsg = SmsUtils.sendAlertMsg(smsParam.get("phone"), smsParam.get("deviceName"), smsParam.get("alertInfo"));
								log.info("短信发送结果====={},",sendAlertMsg);
								if(sendAlertMsg==1) {
									//短信发送成功则更新短信限制条数及禁用状态
									msgSmsNoticeSettingService.sendSmsSucProcess(SmsSettingEnum.DEV_TOTAL.getIdentifier(), pushBusId, msg.getUserId());
								}
							}
						}
					} catch (Exception e) {
						log.error("短信 推送异常",e);
						e.printStackTrace();
					}
    			}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error("消息推送  短信报警失败", e);
				e.printStackTrace();
			}
    	}
    }
    /**
     * 新增消息通知
     * 
     * @param msg 消息通知
     * @return 结果
     */
    @Override
    public int insertMsg(Msg msg)
    {
        msg.setCreateTime(DateUtils.getNowDate());
        return msgMapper.insertMsg(msg);
    }

    /**
     * 修改消息通知
     * 
     * @param msg 消息通知
     * @return 结果
     */
    @Override
    public int updateMsg(Msg msg)
    {
        msg.setUpdateTime(DateUtils.getNowDate());
        return msgMapper.updateMsg(msg);
    }
    
    /**
     * 更新消息已读
     * 
     * @param id 消息通知主键
     * @return 消息通知
     */
    @Transactional
    @Override
    public AjaxResult updateMsgRead(Long id,Long userId) {
    	Msg msg = msgMapper.selectMsgById(id);
    	if(msg==null || !msg.getUserId().equals(userId)) {
    		return AjaxResult.error("消息有误");
    	}
    	if(1==msg.getIsRead()) {
    		return AjaxResult.success("消息已读");
    	}
    	int updateMsgRead = msgMapper.updateMsgRead(id);
    	if("1".equals(msg.getMsgType()) || "2".equals(msg.getMsgType())) {
    		// 系统消息需要更新通知人数
    		if(updateMsgRead>0) {
    			noticeService.updateNoticeReadCount(msg.getNoticeId());
    		}
    	}
    	if(updateMsgRead>0) {
    		return AjaxResult.success();
		}else {
			return AjaxResult.error();
		}
    }
    
    /**
     * 更新消息已读
     * 
     * @param id 消息通知主键
     * @return 消息通知
     */
    @Transactional
    @Override
    public AjaxResult updateFamilyMsgRead(Long userId) {
    	msgMapper.updateMsgReadByType(userId, new String[]{"99"});
    	return AjaxResult.success();
    }
    @Transactional
    @Override
    public AjaxResult updateSystemMsgRead(Long userId) {
    	//更新系统消息已读
    	Msg msg = new Msg();
    	msg.setUserId(userId);
    	List<Msg> list = msgMapper.selectSystemMsgList(msg);
    	list.forEach(msg1->{
    		updateMsgRead(msg1.getId(), userId);
    	});
    	return AjaxResult.success();
    }
    @Transactional
    @Override
    public AjaxResult updateAllMsgRead(Long userId) {
    	//更新家庭消息已读
    	updateFamilyMsgRead(userId);
    	//更新系统消息已读
    	updateSystemMsgRead(userId);
    	return AjaxResult.success();
    }

    /**
     * 批量删除消息通知
     * 
     * @param ids 需要删除的消息通知主键
     * @return 结果
     */
    @Override
    public int deleteMsgByIds(Long[] ids)
    {
        return msgMapper.deleteMsgByIds(ids);
    }

    /**
     * 删除消息通知信息
     * 
     * @param id 消息通知主键
     * @return 结果
     */
    @Override
    public int deleteMsgById(Long id)
    {
        return msgMapper.deleteMsgById(id);
    }
    @Override
    public int deleteMsgByNoticeId(String msgType,Long noticeId)
    {
    	return msgMapper.deleteMsgByNoticeId(msgType,noticeId);
    }
}
