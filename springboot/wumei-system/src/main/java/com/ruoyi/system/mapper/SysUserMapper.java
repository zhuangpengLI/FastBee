package com.ruoyi.system.mapper;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.otherDto.SysUserWithFamilyStat;

/**
 * 用户表 数据层
 * 
 * @author ruoyi
 */
public interface SysUserMapper
{
    /**
     * 根据条件分页查询用户列表
     * 
     * @param sysUser 用户信息
     * @return 用户信息集合信息
     */
    public List<SysUser> selectUserList(SysUser sysUser);
    
    /**
     * 查询用户列表(包含家庭统计)

     * @param sysUser
     * @return
     */
    public List<SysUserWithFamilyStat> selectUserFamilyList(SysUser sysUser);

    /**
     * 根据条件分页查询未已配用户角色列表
     * 
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    public List<SysUser> selectAllocatedList(SysUser user);

    /**
     * 根据条件分页查询未分配用户角色列表
     * 
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    public List<SysUser> selectUnallocatedList(SysUser user);

    /**
     * 通过用户名查询用户
     * 
     * @param userName 用户名
     * @return 用户对象信息
     */
    public SysUser selectUserByUserName(String userName);

    /**
     * 通过用户ID查询用户
     * 
     * @param userId 用户ID
     * @return 用户对象信息
     */
    public SysUser selectUserById(Long userId);

    /**
     * 新增用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    public int insertUser(SysUser user);

    /**
     * 修改用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    public int updateUser(SysUser user);

    /**
     * 修改用户头像
     * 
     * @param userName 用户名
     * @param avatar 头像地址
     * @return 结果
     */
    public int updateUserAvatar(@Param("userName") String userName, @Param("avatar") String avatar);

    /**
     * 重置用户密码
     * 
     * @param userName 用户名
     * @param password 密码
     * @return 结果
     */
    public int resetUserPwd(@Param("userName") String userName, @Param("password") String password);

    /**
     * 通过用户ID删除用户
     * 
     * @param userId 用户ID
     * @return 结果
     */
    public int deleteUserById(Long userId);

    /**
     * 批量删除用户信息
     * 
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    public int deleteUserByIds(Long[] userIds);

    /**
     * 校验用户名称是否唯一
     * 
     * @param userName 用户名称
     * @return 结果
     */
    public int checkUserNameUnique(String userName);

    /**
     * 校验手机号码是否唯一
     *
     * @param phonenumber 手机号码
     * @param userType  用户类型 00系统用户 01 app用户 
     * @return 结果
     */
    public SysUser checkPhoneUnique(@Param("phonenumber")String phonenumber,@Param("userType")String userType);
    /**
     * 查询用户基本信息通过手机号
     *
     * @param phonenumber 手机号码
     * @param userType  用户类型 00系统用户 01 app用户 
     * @return 结果
     */
    public SysUser selectUserByPhone(@Param("phonenumber")String phonenumber,@Param("userType")String userType);

    /**
     * 校验email是否唯一
     *
     * @param email 用户邮箱
     * @return 结果
     */
    public SysUser checkEmailUnique(@Param("email")String email,@Param("userType")String userType);
    
    /**
     * 绑定用户推送客户端
     * 
     * @param user 用户信息
     * @return 结果
     */
    public void bindUserJPushClient(@Param("userId")Long userId,@Param("registrationId")String registrationId);
    
    /**
     * 删除用户极光绑定
     * @param regs
     */
    public void deleteUserJPushClient(@Param("regs")Set<String> regs);
}
