package com.ruoyi.iot.mobile.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.quartz.SchedulerException;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.iot.domain.Device;
import com.ruoyi.iot.mobile.model.InviteDeviceUserReqDto;
import com.ruoyi.iot.mobile.model.UpdateDeviceBaseInfoReqDto;
import com.ruoyi.iot.mobile.respModel.DeviceBriefRespInfo;
import com.ruoyi.iot.mobile.respModel.DeviceNoticeSetting;

/**
 * 设备Service接口
 * 
 * @author kerwincui
 * @date 2021-12-16
 */
public interface IFamilyDeviceService 
{
    /**
     * 查看某个家庭中未分配的设备数量
     * @param familyId
     * @return
     */
    public int countNotInRomm(Long familyId,Long userId);
    /**
     * 查看某个家庭中未分配的设备数量
     * @param familyId
     * @return
     */
    public int countNotInRomm(Long familyId);
    
    
    /**
     * 获取网关状态
     * @param familyId
     * @param userId
     * @return
     */
    public AjaxResult getGatewayStatus(Long familyId,Long userId);
    
    /**
     * 获取网关状态
     * @param familyId
     * @param userId
     * @return
     */
    public AjaxResult getGatewayStatus(String gatewaySn);
    
    /**
	 * 查询简要设备信息
	 * 
	 * @param roomId 房间id 可为空 空查询所有设备
	 * @param familyId 家庭id 不可为空
	 * @return 设备集合
	 */
	public DeviceBriefRespInfo selectBriefDeviceByDeviceId(Long deviceId);
	/**
	 * 查询简要设备信息
	 * 
	 * @param roomId 房间id 可为空 空查询所有设备
	 * @param familyId 家庭id 不可为空
	 * @return 设备集合
	 */
	public DeviceBriefRespInfo selectBriefDeviceBySerialNumber(String serialNumber);
    /**
     * 查询房间设备列表
     * 
     * @param family 家庭管理
     * @return 家庭管理集合
     */
    public List<DeviceBriefRespInfo> selectRoomDeviceList(Long roomId,Long familyId,Long userId);
    /**
     * 查询未分配设备列表
     * 
     * @param family 家庭管理
     * @return 家庭管理集合
     */
    public List<DeviceBriefRespInfo> selectFamilyNotInRoomDeviceList(Long familyId, Long userId);
    /**
     * 查询家庭常用设备列表
     * 
     * @param family 家庭管理
     * @return 家庭管理集合
     */
    public List<DeviceBriefRespInfo> selectFamilyUsualDeviceList(Long familyId, Long userId);
    
    /**
     * 通过设备邀请家庭成员
     * @param dto
     * @param loginUserId
     * @return
     */
    public AjaxResult inviteDeviceUser(InviteDeviceUserReqDto dto,Long loginUserId);
    
    /**
     * 网关进入添加设备模式
     * @param familyId
     * @param userId
     * @param au 请求标识 由客户端提供
     * @return
     */
    public AjaxResult startAddDevice(Long familyId,Long userId,String au);
    /**
     * 网关退出添加设备模式
     * @param familyId
     * @param userId
     * @param au 请求标识 由客户端提供
     * @return
     */
    public AjaxResult endAddDevice(Long familyId,Long userId,String au);

    /**
     * 添加设备
     *
     * @return 结果 1添加成功 <=0 添加失败  1000代表之前添加过 本次只做返回
     */
	public int insertDevice(String serialNumber, Long userId, String gatewaySn);
	
	/**
	 * 判断网关是否合法
	 *
	 * @return 结果
	 */
	public boolean isGatewayDevice(String serialNumber);
	/**
	 * 添加设备
	 *
	 * @return 结果
	 */
	public int insertGatewayDevice(String serialNumber,Long familyId);
	
	/**
	 * 更新设备基本信息
	 * @param dto
	 * @return
	 */
    public AjaxResult updateDeviceBaseInfo(UpdateDeviceBaseInfoReqDto dto, Long userId);
    /**
     * 删除设备请求
     * @param dto
     * @return
     */
    public AjaxResult deleteDeviceReq(Long deviceId, Long userId,String au);
    /**
     * 删除设备请求  --  无需校验权限   一般为管理端使用
     * @param dto
     * @return
     */
    public AjaxResult deleteDeviceReq(Long deviceId,String au);
    /**
     * 删除设备请求 同步
     * @param dto
     * @return
     */
    public int deleteDeviceReqSync(Long deviceId,String au);
    
    /**
     * 实际删除设备(需要保证幂等性)
     * @param deviceId
     * @throws SchedulerException 
     */
    public void deleteDeviceActual(Long deviceId) throws SchedulerException;
    /**
     * 实际删除业务设备不删除网关设备(需要保证幂等性)
     * @param deviceId
     * @throws SchedulerException 
     */
    public void deleteCsDeviceNoGw(Long deviceId) throws SchedulerException;
    /**
     * 实际删除设备(需要保证幂等性)
     * @param deviceId
     * @throws SchedulerException 
     */
    public void deleteDeviceActualBySn(String sn) throws SchedulerException;
    /**
     * 实际删除设备通过家庭(意味着删除家庭下所有设备)
     * @param deviceId
     * @throws SchedulerException 
     */
    public void deleteDeviceActualByFamilyId(Long familyId) throws SchedulerException;
    /**
     * 删除服务器设备而不删除网关设备通过家庭
     * @param deviceId
     * @throws SchedulerException 
     */
    public void deleteCsDeviceNoGwByFamilyId(Long familyId) throws SchedulerException;
    
    /**
     * 发起查询所有设备请求
     * @param familyId
     * @param au
     * @param userId
     * @return
     */
    public AjaxResult getAllDeviceGwInfo(Long familyId,String au,Long userId) ;
    
    /**
     * 更新设备状态及物模型 即实时数据
     * @param device
     * @return
     */
    public AjaxResult updateDeviceStatusAndThingsModelById(Device device);
    
    /**
     * 操作设备功能
     * @param deviceId
     * @param userId
     * @param au
     * @param type normal 普通参数 afd 语音参数
     * @return
     */
    public AjaxResult operDeviceFun(Long deviceId, Long userId,String au,String param,String value,Long fid);
    /**
     * 操作设备功能
     * @param deviceId
     * @param userId
     * @param au
     * @param type normal 普通参数 afd 语音参数
     * @return
     */
    public AjaxResult operDeviceFunNoPerm(Long deviceId, Long userId,String au,String param,String value,Long fid);
    
    /**
     * 设备日志保存
     * @param device
     * @param identity 标识符
     * @param value 对应值
     * @param text 日志文本描述
     * @param logType  1==属性上报，2=事件上报，3=调用功能，4=设备升级，5=设备上线，6=设备离线
     * @param userId
     */
    public void saveLog(Device device,String identity,String value,String text,Integer logType,Long userId) ;
    /**
     * 修改设备模型名称
     * @param deviceId
     * @param userId
     * @param value 名称
     * @return
     */
    public AjaxResult updateDeviceModelName(Long deviceId, Long userId,String value);
    /**
     * 设置设备参数
     * @param deviceId
     * @param userId
     * @param au
     * @param type normal 普通参数 afd 语音参数
     * @return
     */
    public AjaxResult setDeviceParamReq(Long deviceId, Long userId,String au,String type,String param,String value);
    /**
     * 获取设备参数
     * @param deviceId
     * @param userId
     * @param au
     * @param type normal 普通参数 afd 语音参数
     * @return
     */
    public AjaxResult getDeviceParamReq(Long deviceId, Long userId,String au,String type);
    
    /**
     * 判断用户是否有管理权限
     * @param deviceId
     * @param userId
     * @return
     */
    public boolean isAdminPerm(Long deviceId, Long userId);
    /**
     * 判断用户是否有普通用户操作设备权限
     * @param deviceId
     * @param userId
     * @return
     */
    public boolean isCommonPerm(Long deviceId, Long userId);
    
    /**
     * 升级网关
     * @param deviceId
     * @param firmwarePath
     * @param remark
     * @return
     */
    public AjaxResult upgradeGateway(Long deviceId,String firmwarePath,String version,String remark);
    /**
     * 批量升级网关
     * @param deviceId
     * @param firmwarePath
     * @param remark
     * @return
     */
    public AjaxResult upgradeAllGateway(String firmwarePath,String version,String remark);
    

    /**
     * 查询设备设置列表
     *
     * @param device 设备
     * @return 设备集合
     */
    public List<DeviceNoticeSetting> selectDeviceNoticeSettingList(DeviceNoticeSetting device);
    
    /**
     * 能耗数据
     * @param deviceId
     * @param type 类型 1今日 2最近7天 3最近一年  4最近7天(横轴为小时)
     * @param unit 单位 1=0.001Kw.h   2=1Kw.h
     * @return
     */
	public AjaxResult energyData(Long deviceId, Integer type, Integer unit);
	/**
	 * 能耗数据
	 * @param deviceId
	 * @return
	 */
	public AjaxResult energyDataByDay(Long deviceId, Integer year, Integer month, Integer day);
	/**
	 * 能耗数据
	 * @param deviceId
	 * @return
	 */
	public AjaxResult energyDataByMonth(Long deviceId, Integer year, Integer month);
	/**
	 * 能耗数据
	 * @param deviceId
	 * @return
	 */
	public AjaxResult energyDataByYear(Long deviceId, Integer year);
	
	/**
	 * 解绑网关
	 * @param deviceId
	 * @return
	 * @throws SchedulerException
	 */
	public AjaxResult unBindGateway(Long deviceId) throws SchedulerException;
	/**
	 * 删除网关
	 * @param deviceId
	 * @return
	 * @throws SchedulerException
	 */
	public AjaxResult delGateway(Long deviceId) throws SchedulerException;
	
	/**
	 * 设备对应配电信息
	 * @param deviceId
	 * @return
	 */
	public AjaxResult airSwitchData(Long deviceId);
    
}
