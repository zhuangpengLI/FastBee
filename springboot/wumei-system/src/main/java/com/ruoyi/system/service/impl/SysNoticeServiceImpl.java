package com.ruoyi.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.mapper.SysNoticeMapper;
import com.ruoyi.system.otherService.IMsgService;
import com.ruoyi.system.service.ISysNoticeService;

/**
 * 公告 服务层实现
 * 
 * @author ruoyi
 */
@Service
public class SysNoticeServiceImpl implements ISysNoticeService
{
    @Autowired
    private SysNoticeMapper sysNoticeMapper;
    @Autowired
    private IMsgService msgService;

    /**
     * 查询公告信息
     * 
     * @param noticeId 公告ID
     * @return 公告信息
     */
    @Override
    public SysNotice selectNoticeById(Long noticeId)
    {
        return sysNoticeMapper.selectNoticeById(noticeId);
    }

    /**
     * 查询公告列表
     * 
     * @param notice 公告信息
     * @return 公告集合
     */
    @Override
    public List<SysNotice> selectNoticeList(SysNotice notice)
    {
        return sysNoticeMapper.selectNoticeList(notice);
    }

    /**
     * 更新公告读取人数
     * 
     * @param notice 公告信息
     * @return 结果
     */
    @Transactional
    @Override
    public int updateNoticeReadCount(Long noticeId) {
    	return sysNoticeMapper.updateNoticeReadCount(noticeId);
    }
    /**
     * 新增公告
     * 
     * @param notice 公告信息
     * @return 结果
     */
    @Override
    public int insertNotice(SysNotice notice)
    {
    	Integer type1 =1;
    	if(type1.equals(notice.getSendType())) {
    		notice.setSendTime(DateUtils.getNowDate());
    	}
    	int insertNotice = sysNoticeMapper.insertNotice(notice);
        return insertNotice;
    }

    /**
     * 修改公告
     * 
     * @param notice 公告信息
     * @return 结果
     */
    @Override
    public int updateNotice(SysNotice notice)
    {
        return sysNoticeMapper.updateNotice(notice);
    }
    @Override
    public int updateNoticeSendCount(SysNotice notice)
    {
    	return sysNoticeMapper.updateNoticeSendCount(notice);
    }

    /**
     * 删除公告对象
     * 
     * @param noticeId 公告ID
     * @return 结果
     */
    @Override
    public int deleteNoticeById(Long noticeId)
    {
        return sysNoticeMapper.deleteNoticeById(noticeId);
    }

    /**
     * 批量删除公告信息
     * 
     * @param noticeIds 需要删除的公告ID
     * @return 结果
     */
    @Override
    public int deleteNoticeByIds(Long[] noticeIds)
    {
    	int delete = sysNoticeMapper.deleteNoticeByIds(noticeIds);
    	if(delete>0) {
    		for (int i = 0; i < noticeIds.length; i++) {
    			msgService.deleteMsgByNoticeId(null, noticeIds[i]);
			}
    	}
    	
        return delete;
    }
}
