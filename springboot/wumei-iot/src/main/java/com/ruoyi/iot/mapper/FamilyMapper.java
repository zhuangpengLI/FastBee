package com.ruoyi.iot.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.iot.domain.Family;
import com.ruoyi.iot.domain.FamilyUserRela;
import com.ruoyi.iot.domain.Room;
import com.ruoyi.iot.mobile.respModel.FamilyStat;
import com.ruoyi.iot.mobile.respModel.RoomStat;

/**
 * 家庭管理Mapper接口
 * 
 * @author renjiayue
 * @date 2022-09-07
 */
public interface FamilyMapper 
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
	public Family selectFamilyOnlyByGatewaySn(String gatewaySn);
    /**
     * 查询家庭管理
     * 
     * @param familyId 家庭管理主键
     * @return 家庭管理
     */
    public Family selectFamilyAllByFamilyId(Long familyId);
    
    /**
     * 查询家庭管理
     * 只查询房间列表
     * 
     * @param familyId 家庭管理主键
     * @return 家庭管理
     */
    public Family selectFamilyAndRoomByFamilyId(Long familyId);
    /**
     * 只查询房间列表
     * 
     * @param familyId 家庭管理主键
     * @return 家庭管理
     */
    public List<Room> selectRoomByFamilyId(Long familyId);
    /**
     * 只查询房间列表
     * 
     * @param roomId 房间主键
     * @return 家庭管理
     */
    public Room selectRoomByRoomId(Long roomId);
    /**
     * 只查询房间列表和房间统计信息
     * 
     * @param familyId 家庭管理主键
     * @return 家庭管理
     */
    public List<RoomStat> selectRoomAndStatByFamilyId(Long familyId);
    
    /**
     * 查询家庭管理
     * 只查询用户列表
     * 
     * @param familyId 家庭管理主键
     * @return 家庭管理
     */
    public Family selectFamilyAndUserByFamilyId(Long familyId);
    /**
     * 只查询用户列表
     * 
     * @param familyId 家庭管理主键
     * @return 家庭管理
     */
    public List<FamilyUserRela> selectUserByFamilyId(Long familyId);
    /**
     * 查查询用户列表
     * 通过家庭或用户 或 家庭&用户 
     * 均可为空
     * 
     * @param familyId 家庭管理主键
     * @return 家庭管理
     */
    public List<FamilyUserRela> selectUserListByFamilyIdAndUserId(@Param("familyId")Long familyId,@Param("userId")Long userId);
    /**
     * 查询唯一用户信息
     * 均不为空才可以使用
     * 
     * @param familyId 家庭管理主键
     * @param userId 
     * @return 家庭管理
     */
    public FamilyUserRela selectUserByFamilyIdAndUserId(@Param("familyId")Long familyId,@Param("userId")Long userId);
    /**
     * 更新家庭用户信息角色
     * 
     * @param familyId 家庭管理主键
     * @param userId 
     * @return 家庭管理
     */
    public int updateUserRoleByFamilyIdAndUserId(@Param("familyId")Long familyId,@Param("userId")Long userId,@Param("familyUserRole")String familyUserRole);
    /**
     * 删除家庭用户信息(重复接口 废弃)
     * 
     * @param familyId 家庭管理主键
     * @param userId 
     * @return 家庭管理
     */
    @Deprecated
    public int deleteUserByFamilyIdAndUserId(@Param("familyId")Long familyId,@Param("userId")Long userId);
    /**
     * 查询家庭成员数量
     * 
     * @param familyId 家庭管理主键
     * @return
     */
    public int countFamilyUser(@Param("familyId")Long familyId);

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
     * 新增家庭管理
     * 
     * @param family 家庭管理
     * @return 结果
     */
    public int insertFamily(Family family);
    /**
     * 查询所属自己的家庭数量
     * 
     * @param userId 用户id
     * @return 结果
     */
    public int countFamilyByUserId(Long userId);

    /**
     * 修改家庭管理
     * 
     * @param family 家庭管理
     * @return 结果
     */
    public int updateFamily(Family family);

    /**
     * 删除家庭管理
     * 
     * @param familyId 家庭管理主键
     * @return 结果
     */
    public int deleteFamilyByFamilyId(Long familyId);
    
    /**
     * 清除家庭网关绑定信息
     * 
     * @param familyId 家庭管理主键
     * @return 结果
     */
    public int clearFamilyGwBindInfo(Long familyId);

    /**
     * 批量删除家庭管理
     * 
     * @param familyIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFamilyByFamilyIds(Long[] familyIds);

    /**
     * 批量删除房间
     * 
     * @param familyIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRoomByFamilyIds(Long[] familyIds);
    
    /**
     * 批量新增房间
     * 
     * @param roomList 房间列表
     * @return 结果
     */
    public int batchRoom(List<Room> roomList);
    
    /**
     * 新增房间
     * 
     * @param roomList 房间列表
     * @return 结果
     */
    public int insertRoom(Room room);
    /**
     * 修改房间
     * 
     * @param roomList 房间列表
     * @return 结果
     */
    public int updateRoom(Room room);
    

    /**
     * 通过家庭管理主键删除房间信息
     * 
     * @param familyId 家庭管理ID
     * @return 结果
     */
    public int deleteRoomByFamilyId(Long familyId);
    
    /**
     * 通过家庭管理主键和房间主键删除房间信息
     * 
     * @param familyId 家庭管理ID
     * @param roomId 房间id
     * @return 结果
     */
    public int deleteRoomByFamilyIdAndRoomId(@Param("familyId")Long familyId,@Param("roomId")Long roomId);
    
    /**
     * 批量删除家庭用户关联
     * 
     * @param familyIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFamilyUserRelaByFamilyIds(Long[] familyIds);
    
    /**
     * 批量新增家庭用户关联
     * 
     * @param familyUserRelaList 家庭用户关联列表
     * @return 结果
     */
    public int batchFamilyUserRela(List<FamilyUserRela> familyUserRelaList);
    

    /**
     * 通过家庭管理主键删除家庭用户关联信息
     * 
     * @param familyId 家庭管理ID
     * @return 结果
     */
    public int deleteFamilyUserRelaByFamilyId(Long familyId);
    /**
     * 通过家庭管理主键删除家庭用户关联信息
     * 
     * @param familyId 家庭管理ID
     * @param userId userId
     * @return 结果
     */
    public int deleteFamilyUserRelaByFamilyIdAndUserId(@Param("familyId")Long familyId,@Param("userId")Long userId);
    
    /**
     * 查询家庭房间数量
     * @param familyId
     * @return
     */
    public int countRoomByFamilyId(Long familyId);
    /**
     * 查询家庭用户数量
     * @param familyId
     * @return
     */
    public int countUserByFamilyId(Long familyId);
}
