package com.ruoyi.iot.mobile.service;

import java.util.List;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.iot.mobile.model.MobileLoginUserInput;

/**
 * 
 * @author kerwincui
 * @date 2021-12-16
 */
public interface IMobileUserService
{
	/**
	 * 修改手机号验证码
	 */
	public AjaxResult updatePhoneCode(String phonenumber,SysUser user);
	
	
	/**
	 * 修改手机号
	 * @param phonenumber
	 * @param code 验证码
	 * @param loginUser
	 * @return
	 */
	public AjaxResult updatePhone(String phonenumber,String code,LoginUser loginUser);
	/**
     * 校验修改手机验证码
     *
     * @param phone 手机号
     * @param code 验证码
     * @return 结果
     */
	public void validateUpdateMobileCaptcha(String phone, String code);
	/**
	 * 登录验证码
	 */
	public AjaxResult loginCode(String phonenumber);
    /**
     * 注册
     * @throws Exception 
     */
    public AjaxResult login(MobileLoginUserInput registerBody) throws Exception;

    /**
     * 根据条件分页查询用户列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    public List<SysUser> selectUserList(SysUser user);

    /**
     * 生成随机数字和字母
     */
    public String getStringRandom(int length);

}
