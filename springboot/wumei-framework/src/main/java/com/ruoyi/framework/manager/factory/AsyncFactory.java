package com.ruoyi.framework.manager.factory;

import java.util.List;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.LogUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ip.AddressUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.MsgTypeConstant;
import com.ruoyi.system.domain.SysLogininfor;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.domain.SysOperLog;
import com.ruoyi.system.otherDomain.Msg;
import com.ruoyi.system.otherDto.MsgSettingEnum;
import com.ruoyi.system.otherDto.SysUserWithFamilyStat;
import com.ruoyi.system.otherService.IMsgService;
import com.ruoyi.system.service.ISysLogininforService;
import com.ruoyi.system.service.ISysNoticeService;
import com.ruoyi.system.service.ISysOperLogService;
import com.ruoyi.system.service.ISysUserService;

import eu.bitwalker.useragentutils.UserAgent;

/**
 * 异步工厂（产生任务用）
 * 
 * @author ruoyi
 */
public class AsyncFactory
{
    private static final Logger sys_user_logger = LoggerFactory.getLogger("sys-user");
    private static final Logger sys_info_logger = LoggerFactory.getLogger("sys-info");

    /**
     * 记录登录信息
     * 
     * @param username 用户名
     * @param status 状态
     * @param message 消息
     * @param args 列表
     * @return 任务task
     */
    public static TimerTask recordLogininfor(final String username, final String status, final String message,
            final Object... args)
    {
        final UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        final String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
        return new TimerTask()
        {
            @Override
            public void run()
            {
                String address = AddressUtils.getRealAddressByIP(ip);
                StringBuilder s = new StringBuilder();
                s.append(LogUtils.getBlock(ip));
                s.append(address);
                s.append(LogUtils.getBlock(username));
                s.append(LogUtils.getBlock(status));
                s.append(LogUtils.getBlock(message));
                // 打印信息到日志
                sys_user_logger.info(s.toString(), args);
                // 获取客户端操作系统
                String os = userAgent.getOperatingSystem().getName();
                // 获取客户端浏览器
                String browser = userAgent.getBrowser().getName();
                // 封装对象
                SysLogininfor logininfor = new SysLogininfor();
                logininfor.setUserName(username);
                logininfor.setIpaddr(ip);
                logininfor.setLoginLocation(address);
                logininfor.setBrowser(browser);
                logininfor.setOs(os);
                logininfor.setMsg(message);
                // 日志状态
                if (StringUtils.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER))
                {
                    logininfor.setStatus(Constants.SUCCESS);
                }
                else if (Constants.LOGIN_FAIL.equals(status))
                {
                    logininfor.setStatus(Constants.FAIL);
                }
                // 插入数据
                SpringUtils.getBean(ISysLogininforService.class).insertLogininfor(logininfor);
            }
        };
    }

    /**
     * 操作日志记录
     * 
     * @param operLog 操作日志信息
     * @return 任务task
     */
    public static TimerTask recordOper(final SysOperLog operLog)
    {
        return new TimerTask()
        {
            @Override
            public void run()
            {
                // 远程查询操作地点
                operLog.setOperLocation(AddressUtils.getRealAddressByIP(operLog.getOperIp()));
                SpringUtils.getBean(ISysOperLogService.class).insertOperlog(operLog);
            }
        };
    }
    
    public static TimerTask sendTimerMsg(final Long noticeId)
    {
    	return new TimerTask()
    	{
    		@Override
    		public void run()
    		{
    			sys_info_logger.info("定时发送消息  哈哈哈");
    			ISysNoticeService noticeService = SpringUtils.getBean(ISysNoticeService.class);
    			ISysUserService userService = SpringUtils.getBean(ISysUserService.class);
    			IMsgService msgService = SpringUtils.getBean(IMsgService.class);
//    			IMsgService msgService = SpringUtils.getBean(IMsgService.class);
    			SysNotice notice = noticeService.selectNoticeById(noticeId);
    			if(notice==null) {
    				sys_info_logger.error("通知不存在,{}",noticeId);
    				return;
    			}
    			if(!"0".equals(notice.getStatus()) ) {
    				sys_info_logger.error("通知状态异常,id:{},状态:{}",noticeId,notice.getStatus());
    				return;
    			}
    			SysUser user = new SysUser();
				user .setUserType("01");//00系统用户 01 app用户
    	        List<SysUserWithFamilyStat> list = userService.selectUserFamilyList(user);
    	        for (SysUserWithFamilyStat user1:list) {
//    	        	MsgTypeConstant.MSG_TYPE_NOTICE_N
//    	        	MsgTypeConstant.MSG_TYPE_NOTICE_C
    	        	msgService.sendMsg(new Msg(notice.getNoticeTitle(),notice.getNoticeType(),notice.getNoticeContent(),notice.getImgUrl(),
    	        			null,null,null,user1.getUserId(),noticeId), true, false, false,MsgSettingEnum.SYS_OFFICAL.getIdentifier(),
							null, null);
				}
    	        SysNotice newNotice = new SysNotice();
    	        newNotice.setNoticeId(noticeId);
    	        newNotice.setSendCount(new Long(list.size()));
				noticeService.updateNoticeSendCount(newNotice );
    			// 远程查询操作地点
//    			operLog.setOperLocation(AddressUtils.getRealAddressByIP(operLog.getOperIp()));
    			
    		}
    	};
    }
}
