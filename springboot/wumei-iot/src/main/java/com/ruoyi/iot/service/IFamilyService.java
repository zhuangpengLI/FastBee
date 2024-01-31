package com.ruoyi.iot.service;

import java.util.List;

import org.quartz.SchedulerException;
import org.springframework.web.bind.annotation.RequestParam;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.iot.domain.Family;
import com.ruoyi.iot.domain.FamilyUserRela;
import com.ruoyi.iot.domain.Room;
import com.ruoyi.iot.mobile.model.InviteFamilyUserReqDto;
import com.ruoyi.iot.mobile.model.UpdateFamilyUserReqDto;
import com.ruoyi.iot.mobile.respModel.DeviceNoticeSetting;
import com.ruoyi.iot.mobile.respModel.FamilyStat;
import com.ruoyi.iot.mobile.respModel.RoomStat;

/**
 * 家庭管理Service接口
 * 
 * @author renjiayue
 * @date 2022-09-07
 */
public interface IFamilyService 
{
	
	/**
	 * 查询家庭管理 
	 * 只查询家庭信息
	 * 
	 * @param familyId 家庭管理主键
	 * @return 家庭管理
	 */
	public Family selectFamilyOnlyByFamilyId(Long familyId);
	
	/**
	 * 查询用户总共家庭数量统计
	 * @param userId
	 * @return
	 */
	public int selectCountTotalFamily(Long userId);
	
	/**
	 * 查询用户创建家庭数量统计
	 * @param userId
	 * @return
	 */
	public int selectCountCreateFamily(Long userId);
	/**
	 * 查询家庭管理 
	 * 只查询家庭信息
	 * 
	 * @param familyId 家庭管理主键
	 * @return 家庭管理
	 */
	public Family selectFamilyOnlyByFamilyId(Long familyId,Long userId);
	/**
	 * 查询家庭管理 
	 * 只查询家庭信息 和 统计信息
	 * 
	 * @param familyId 家庭管理主键
	 * @return 家庭管理
	 */
	public FamilyStat selectFamilyAndStatByFamilyId(Long familyId,Long userId);
    /**
     * 查询家庭管理
     * 
     * @param familyId 家庭管理主键
     * @return 家庭管理
     */
    public Family selectFamilyByFamilyId(Long familyId);

    /**
	 * 查询家庭管理 
	 * 只查询家庭信息
	 * 
	 * @param familyId 家庭管理主键
	 * @return 家庭管理
	 */
	public Family selectFamilyOnlyByGatewaySn(String gatewaySn);
    /**
     * 查询家庭管理列表
     * 
     * @param family 家庭管理
     * @return 家庭管理集合
     */
    public List<Family> selectFamilyList(Family family);
    /**
     * 查询家庭管理列表
     * 
     * @param family 家庭管理
     * @return 家庭管理集合
     */
    public List<FamilyStat> selectFamilyStatList(Family family);
    
    /**
     * 查查询用户&家庭列表
     * 通过家庭或用户 或 家庭&用户 
     * 均可为空
     * 
     * @param familyId 家庭管理主键
     * @return 家庭管理
     */
    public List<FamilyUserRela> selectUserListByFamilyIdAndUserId(Long familyId,Long userId);
    /**
     * 查查询用户列表
     * 
     * @param familyId 家庭管理主键  不可为空 用于判断权限 以及查询
     * @param userId 用户id
     * @param loginUserId  当前登录用户 判断是否有权限查看列表
     * @return 家庭管理
     */
    public List<FamilyUserRela> selectUserListByFamilyIdAndUserId(Long familyId,Long userId,Long loginUserId);
    /**
     * 查查询用户&家庭列表    包含家庭统计信息
     * 通过家庭或用户 或 家庭&用户 
     * 均可为空
     * 
     * @param familyId 家庭管理主键
     * @return 家庭管理
     */
    public List<FamilyUserRela> selectUserStatListByFamilyIdAndUserId(Long familyId,Long userId);

    /**
     * 新增家庭管理
     * 
     * @param family 家庭管理
     * @return 结果
     */
    public AjaxResult insertFamily(Family family);

    /**
     * 修改家庭管理
     * 
     * @param family 家庭管理
     * @return 结果
     */
    public int updateFamily(Family family);
    /**
     * 查询唯一用户信息
     * 均不为空才可以使用
     * 
     * @param familyId 家庭管理主键
     * @param userId 
     * @return 家庭管理
     */
    public FamilyUserRela selectUserByFamilyIdAndUserId(Long familyId,Long userId);
    /**
     * 修改家庭管理
     * 
     * @param family 家庭管理
     * @return 结果
     */
    public AjaxResult updateFamily(Family family,Long userId);
    /**
     * 绑定家庭网关信息管理
     * 
     * @param family 家庭管理
     * @return 结果
     */
    public AjaxResult bindFamilyGateway(Family family,Long userId,String userName);
    /**
     * 申请加入家庭
     * 
     * @param family 家庭管理
     * @return 结果
     */
    public AjaxResult applyJoinFamily(Long familyId,Long userId);
    
    /**
     * 用户加入家庭 公共方法
     * @param familyId
     * @param userId
     * @param role 角色  1普通用户 2管理员
     */
    public void commonInsertUserToFamily(Long familyId, Long userId,String role);

    /**
     * 批量删除家庭管理
     * 
     * @param familyIds 需要删除的家庭管理主键集合
     * @return 结果
     */
    public int deleteFamilyByFamilyIds(Long[] familyIds);

    /**
     * 删除家庭管理信息
     * 
     * @param familyId 家庭管理主键
     * @return 结果
     */
    public int deleteFamilyByFamilyId(Long familyId);
    /**
     * 删除家庭管理信息
     * 
     * @param familyId 家庭管理主键
     * @return 结果
     * @throws SchedulerException 
     */
    public AjaxResult deleteFamilyByFamilyId(Long familyId,Long userId) throws SchedulerException;
    /**
     * 软删除家庭管理信息 硬件和网关关系不解绑
     * 
     * @param familyId 家庭管理主键
     * @return 结果
     * @throws SchedulerException 
     */
    public AjaxResult deleteSoftFamilyByFamilyId(Long familyId,Long userId) throws SchedulerException;
    /**
     * 删除房间管理信息
     * 
     * @param familyId 家庭管理主键
     * @return 结果
     */
    public AjaxResult deleteRoomByRoomId(Long roomId,Long userId);
    /**
     * 非拥有者退出家庭
     * 
     * @param familyId 家庭管理主键
     * @return 结果
     */
    public AjaxResult quitFamilyByFamilyId(Long familyId,Long userId);
    
    /**
     * 只查询房间列表
     * 
     * @param familyId 家庭管理主键
     * @return 家庭管理
     */
    public List<Room> selectRoomByFamilyId(Long familyId);
    /**
     * 只查询房间列表和房间统计信息
     * 
     * @param familyId 家庭管理主键
     * @return 家庭管理
     */
    public List<RoomStat> selectRoomAndStatByFamilyId(Long familyId,Long userId);
    /**
     * 只查询房间列表和房间统计信息
     * 
     * @param familyId 家庭管理主键
     * @return 家庭管理
     */
    public List<RoomStat> selectRoomAndStatByFamilyId(Long familyId);
    
    /**
     * 新增房间
     * @param room
     * @param userId
     */
    public AjaxResult insertRoom(Room	 room,Long userId);
    /**
     * 修改房间
     * @param room
     * @param userId
     */
    public AjaxResult updateRoomName(Room	 room,Long userId);
    
    /**
     * 更新家庭成员权限
     * @param dto
     * @param loginUserId
     * @return
     */
    public AjaxResult updateFamilyUser(UpdateFamilyUserReqDto dto,Long loginUserId);
    /**
     * 邀请家庭成员
     * @param dto
     * @param loginUserId
     * @return
     */
    public AjaxResult inviteFamilyUser(InviteFamilyUserReqDto dto,Long loginUserId);
    
    /**
     * 判断已分享未处理的消息
     * @param familyId
     * @param familyName
     * @param deviceId
     * @param deviceName
     * @param userId
     * @param loginUserId
     * @return
     */
    public int countFamilyOrDeviceSendMsg(Long familyId,String familyName,
    		Long deviceId,String deviceName,
    		Long userId,Long loginUserId);
    
    /**
     * 分享家庭 发送消息  分享了设备可以通用
     * @param familyId
     * @param familyUserRole TODO
     * @param applyUserId
     */
    public void shareFamilyOrDeviceSendMsg(Long familyId,String familyName,
    		Long deviceId,String deviceName,
    		Long userId,Long loginUserId, String familyUserRole);
    /**
     * 删除家庭成员
     * @param familyId 家庭id
     * @param userId 用户id
     * @param loginUserId
     * @return
     */
    public AjaxResult deleteFamilyUser( Long familyId, Long userId, Long loginUserId);
    
    /**
     * 是否家庭普通用户权限
     * @param familyId
     * @param userId
     * @return
     */
    public boolean isCommonPerm(Long familyId, Long userId);
    /**
     * 是否家庭管理员权限
     * @param familyId
     * @param userId
     * @return
     */
    public boolean isAdminPerm(Long familyId, Long userId);
    /**
     * 是否家庭超级管理员权限(即拥有者权限)
     * @param familyId
     * @param userId
     * @return
     */
    public boolean isSuperAdminPerm(Long familyId, Long userId);
}
