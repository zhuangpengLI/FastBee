package com.ruoyi.system.otherService;

import java.util.List;
import java.util.Map;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.otherDomain.Msg;

/**
 * 消息通知Service接口
 * 
 * @author renjiayue
 * @date 2022-10-06
 */
public interface IMsgService 
{
    /**
     * 查询消息通知
     * 
     * @param id 消息通知主键
     * @return 消息通知
     */
    public Msg selectMsgById(Long id);

    /**
     * 查询消息通知列表
     * 
     * @param msg 消息通知
     * @return 消息通知集合
     */
    public List<Msg> selectMsgList(Msg msg);
    /**
     * 查询消息通知列表
     * 
     * @param msg 消息通知
     * @return 消息通知集合
     */
    public List<Msg> selectSystemMsgList(Msg msg);
    
    /**
     * 发送消息
     * 
     * @param msg 消息通知
     * @param isPush 是否进行消息推送
     * @param isSms 是否发送短信
     * @param isCall 是否电话通知
     * @param smsParam TODO
     * @return 结果
     */
    public void sendMsg(Msg msg,boolean isPush,boolean isSms,boolean isCall,String pushType,String pushBusId, Map<String, String> smsParam);

    /**
     * 新增消息通知
     * 
     * @param msg 消息通知
     * @return 结果
     */
    public int insertMsg(Msg msg);

    /**
     * 修改消息通知
     * 
     * @param msg 消息通知
     * @return 结果
     */
    public int updateMsg(Msg msg);
    
    /**
     * 更新消息已读
     * 
     * @param id 消息通知主键
     * @return 消息通知
     */
    public AjaxResult updateMsgRead(Long id,Long userId);
    
    /**
     * 更新家庭消息已读
     * 
     * @param id 消息通知主键
     * @return 消息通知
     */
    public AjaxResult updateFamilyMsgRead(Long userId);
    
    /**
     * 更新系统消息已读
     * 
     * @param id 消息通知主键
     * @return 消息通知
     */
    public AjaxResult updateSystemMsgRead(Long userId);
    /**
     * 更新所有消息已读
     * 
     * @param id 消息通知主键
     * @return 消息通知
     */
    public AjaxResult updateAllMsgRead(Long userId);

    /**
     * 批量删除消息通知
     * 
     * @param ids 需要删除的消息通知主键集合
     * @return 结果
     */
    public int deleteMsgByIds(Long[] ids);

    /**
     * 删除消息通知信息
     * 
     * @param id 消息通知主键
     * @return 结果
     */
    public int deleteMsgById(Long id);
    
    /**
     * 删除消息通知信息
     * 
     * @param id 消息通知主键
     * @return 结果
     */
    public int deleteMsgByNoticeId(String msgType,Long noticeId);
}
