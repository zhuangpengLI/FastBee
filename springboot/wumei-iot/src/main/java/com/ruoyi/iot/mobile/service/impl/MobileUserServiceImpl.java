package com.ruoyi.iot.mobile.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.exception.user.CaptchaException;
import com.ruoyi.common.exception.user.CaptchaExpireException;
import com.ruoyi.common.exception.user.UserPasswordNotMatchException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.SmsUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.framework.security.handle.MpWeixinAuthenticationToken;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.iot.domain.Family;
import com.ruoyi.iot.mapper.FamilyMapper;
import com.ruoyi.iot.mobile.model.MobileLoginUserInput;
import com.ruoyi.iot.mobile.service.IMobileUserService;
import com.ruoyi.iot.security.handle.MobileAuthenticationProvider;
import com.ruoyi.iot.service.IFamilyService;
import com.ruoyi.iot.util.JPushUtil;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.otherService.IBaseConfigService;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysUserService;

import cn.jpush.api.JPushClient;

/**
 * 
 * @author kerwincui
 * @date 2021-12-16
 */
@Service
public class MobileUserServiceImpl implements IMobileUserService
{
    private static final Logger log = LoggerFactory.getLogger(MobileUserServiceImpl.class);

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private FamilyMapper familyMapper;
    
    @Autowired
    private IFamilyService familyService;
    
    @Resource
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private TokenService tokenService;
    
    @Autowired
    private JPushUtil jPushUtil;
    
    @Resource
    private MobileAuthenticationProvider mobileAuthenticationProvider;
    @Resource
    private IBaseConfigService baseConfigService;

    /**
     * 生成随机数字和字母
     */
    @Override
    public String getStringRandom(int length) {
        String val = "";
        Random random = new Random();
        //参数length，表示生成几位随机数
        for(int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if( "char".equalsIgnoreCase(charOrNum) ) {
                //输出是大写字母还是小写字母
                // int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char)(random.nextInt(26) + 65);
            } else if( "num".equalsIgnoreCase(charOrNum) ) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

    @Override
    public AjaxResult updatePhoneCode(String phonenumber,SysUser user){
    	String oldPhone = user.getPhonenumber();
    	if(oldPhone.equals(phonenumber)){
    		return AjaxResult.error("手机号码未修改");
    	}
    	SysUser testUser = new SysUser();
    	testUser.setUserType(user.getUserType());
    	testUser.setPhonenumber(phonenumber);
    	if (UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(testUser))){
            return AjaxResult.error("手机号码已被注册");
        }
    	String verifyKey = Constants.MOBILE_UPDATE_PHONE_CAPTCHA_CODE_KEY + phonenumber;
    	String captcha = redisCache.getCacheObject(verifyKey);
    	if (captcha != null){
    		return AjaxResult.error("已发送成功,请1分钟后重试");
    	}else{
    		int random1 = (int)(Math.random()*10);
    		int random2 = (int)(Math.random()*10);
    		int random3 = (int)(Math.random()*10);
    		int random4 = (int)(Math.random()*10);
    		captcha = new String(""+random1+random2+random3+random4);
    		//1分钟过期
    		redisCache.setCacheObject(verifyKey, captcha, 1, TimeUnit.MINUTES);
    		//TODO 接入第三方后 需要不返回验证码
    		return AjaxResult.success("验证码发送成功!"+captcha);
    	}
    	
    }
    @Override
    public AjaxResult updatePhone(String phonenumber,String code,LoginUser loginUser){
    	validateUpdateMobileCaptcha(phonenumber, code);
    	SysUser sysUser = loginUser.getUser();
    	SysUser user = new SysUser();
        user.setUserId(sysUser.getUserId());
        user.setPhonenumber(phonenumber);
        if (userService.updateUserProfile(user) > 0)
        {
            // 更新缓存用户信息
            sysUser.setPhonenumber(user.getPhonenumber());
            tokenService.setLoginUser(loginUser);
            return AjaxResult.success();
        }
        return AjaxResult.error("修改个人信息异常，请联系管理员");
    }
    /**
     * 校验修改手机验证码
     *
     * @param phone 手机号
     * @param code 验证码
     * @return 结果
     */
    @Override
    public void validateUpdateMobileCaptcha(String phone, String code)
    {
    	String verifyKey = Constants.MOBILE_UPDATE_PHONE_CAPTCHA_CODE_KEY + phone;
    	String captcha = redisCache.getCacheObject(verifyKey);
    	redisCache.deleteObject(verifyKey);
    	if (captcha == null)
    	{
    		throw new CaptchaExpireException();
    	}
    	if (!code.equalsIgnoreCase(captcha))
    	{
    		//TODO 确认短信验证码提示错误是否为中文
    		throw new CaptchaException();
    	}
    }
    
    @Override
    public AjaxResult loginCode(String phonenumber){
    	String verifyKey = Constants.MOBILE_LOGIN_CAPTCHA_CODE_KEY + phonenumber;
    	String frequencyKey = Constants.MOBILE_LOGIN_CAPTCHA_CODE_FREQUENCY_KEY + phonenumber;
    	String errorCountKey = Constants.MOBILE_LOGIN_CAPTCHA_CODE_ERROR_COUNT_KEY + phonenumber;
    	String send = redisCache.getCacheObject(frequencyKey);
    	String captcha = redisCache.getCacheObject(verifyKey);
    	if (send != null){
    		return AjaxResult.error("已发送成功,请"+Constants.MOBILE_LOGIN_CAPTCHA_CODE_FREQUENCYY+"分钟后重试");
    	}else{
    		int random1 = (int)(Math.random()*10);
    		int random2 = (int)(Math.random()*10);
    		int random3 = (int)(Math.random()*10);
    		int random4 = (int)(Math.random()*10);
    		captcha = new String(""+random1+random2+random3+random4);
    		// 接入第三方后 需要不返回验证码
    		log.info("{}本次登录验证码:{}",phonenumber,captcha);
    		int sendMSLoginFunc = SmsUtils.sendMSLoginFunc(phonenumber, captcha, Constants.MOBILE_LOGIN_CAPTCHA_CODE+"");
//    		int sendMSLoginFunc = 1;
    		if(sendMSLoginFunc>0) {
    			//1分钟可再次发送
    			redisCache.setCacheObject(frequencyKey, "send", Constants.MOBILE_LOGIN_CAPTCHA_CODE_FREQUENCYY, TimeUnit.MINUTES);
    			//5分钟过期
    			redisCache.setCacheObject(verifyKey, captcha, Constants.MOBILE_LOGIN_CAPTCHA_CODE, TimeUnit.MINUTES);
                //5分钟过期 错误次数随验证码清除
    			redisCache.setCacheObject(errorCountKey, 0, Constants.MOBILE_LOGIN_CAPTCHA_CODE, TimeUnit.MINUTES);
    			return AjaxResult.success("验证码发送成功!");
    		}else {
    			return AjaxResult.error("发送失败!");
    		}
//    		return AjaxResult.success("验证码发送成功!"+captcha);
    	}
    	
    }
    /**
     * 注册
     * @throws Exception 
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult login(MobileLoginUserInput registerBody) throws Exception
    {
    	
    	String msg = "";
    	String phonenumber=registerBody.getPhonenumber();
    	String code = registerBody.getCode();
    	String registrationId = registerBody.getRegistrationId();//推送id
    	log.info("当前登录信息,{}",JSONObject.toJSON(registerBody));
    	validateMobileLoginCaptcha(phonenumber,code);
    	SysUser info = sysUserMapper.selectUserByPhone(phonenumber,"01");
    	if(info==null){
    		//已经注册成功,直接登录
    		String uuid = IdUtils.simpleUUID();
    		String username = uuid;//使用uuid作为用户名
    		if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(username)))
    		{
    			msg = "保存用户'" + username + "'失败，注册账号已存在";
    			/** 用户类型 00系统用户 01 app用户 */
    		}else if (UserConstants.NOT_UNIQUE.equals(checkPhoneUnique(phonenumber,"01")))
    		{
    			msg = "保存用户'" + username + "'失败，注册手机号码已存在";
    		}
    		else
    		{
    			SysUser sysUser = new SysUser();
    			//username使用UUID
    			sysUser.setUserName(username);
    			sysUser.setNickName(phonenumber);
    			sysUser.setPhonenumber(phonenumber);
    			sysUser.setUserType("01");//app用户
    			sysUser.setAvatar("https://a.png");
    			//移动端不需要密码登录
//            sysUser.setPassword(SecurityUtils.encryptPassword(registerBody.getPassword()));
    			boolean regFlag = userService.registerUser(sysUser);
    			info = sysUserMapper.selectUserByPhone(phonenumber,"01");
    			//用户需要创建一个默认家庭
    			Family family = new Family();
    			family.setName(phonenumber.substring(7)+"的家");
    			family.setAvatarUrl("https://a.png");//TODO 默认头像;
    			family.setCreateUserId(info.getUserId());
    			family.setBelongUserId(info.getUserId());
				familyService.insertFamily(family);
    			//移动端不分配角色
    			if (!regFlag)
    			{
    				msg = "注册失败,请联系系统管理人员";
    			}
    		}
    	}
    	//注册成功  或 已经注册成功  开始登录
    	if(StringUtils.isNotBlank(msg)){
    		return AjaxResult.error(msg);
    	}
    	String token = login(info);
    	//登录成功记录极光推送id
    	if(StringUtils.isNotBlank(registrationId)) {
    		boolean b = false;
    		try {
    			Set<String> clearRegistrationIdsByAlias = jPushUtil.queryNeedClearRegistrationIdsByAlias(info.getUserId(), baseConfigService.selectOneBaseConfigByCache().getUserLoginDeviceMax().intValue());
    			if(clearRegistrationIdsByAlias.size()>0) {
    				boolean clear = jPushUtil.clearUserIdToRegistrationIdAlias(info.getUserId(), clearRegistrationIdsByAlias);
    				if(clear) {
    					//清除多余设备别名成功,清理数据库数据
    					sysUserMapper.deleteUserJPushClient(clearRegistrationIdsByAlias);
    				}
    			}
    			b = jPushUtil.updateUserIdToRegistrationIdAlias(registrationId, info.getUserId());
    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		if(b) {
    			sysUserMapper.bindUserJPushClient(info.getUserId(), registrationId);
    		}
    	}
        return AjaxResult.successStrData(token);
    }
    /**
     * 校验注册验证码
     *
     * @param phone 手机号
     * @param code 验证码
     * @return 结果
     */
    private void validateMobileLoginCaptcha(String phone, String code)
    {
    	String verifyKey = Constants.MOBILE_LOGIN_CAPTCHA_CODE_KEY + phone;
    	String errorCountKey = Constants.MOBILE_LOGIN_CAPTCHA_CODE_ERROR_COUNT_KEY + phone;
    	String captcha = redisCache.getCacheObject(verifyKey);
    	Integer errorCount = redisCache.getCacheObject(errorCountKey);
    	if (captcha == null)
    	{
    		throw new CaptchaExpireException();
    	}
//    	if (!code.equalsIgnoreCase(captcha))
    	//TODO 设置4321为万能验证码
    	if (!"4321".equals(code) && !code.equalsIgnoreCase(captcha))
    	{
    		/**
    		 * 错误10次再失效
    		 */
    		if(errorCount+1>=Constants.MOBILE_LOGIN_CAPTCHA_CODE_MAX_ERROR){
    			redisCache.deleteObject(verifyKey);
    		}else{
    			//此处不设置时间 看是否按获取时的5分钟失效 如果不是也没关系 次数达到或者验证码重置后也会失效
    			redisCache.setCacheObject(errorCountKey, errorCount+1);
    		}
    		//TODO 确认短信验证码提示错误是否为中文
    		throw new CaptchaException();
    	}
    }
    
    private String login(SysUser user){
        // 用户验证
        Authentication authentication = null;
        try
        {
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
//        	authentication = authenticationManager
//                    .authenticate(new MpWeixinAuthenticationToken(new LoginUser(user,new HashSet<>())));
        	authentication = mobileAuthenticationProvider.authenticate(new MpWeixinAuthenticationToken(new LoginUser(user,new HashSet<>())));
        }
        catch (Exception e)
        {
            if (e instanceof BadCredentialsException)
            {
            	//不记录登录
//                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            }
            else
            {
            	//不记录登录
//                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new ServiceException(e.getMessage());
            }
        }
        //不记录登录
//        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        recordLoginInfo(loginUser.getUserId());
        // 生成token
        return tokenService.createToken(loginUser,tokenService.getAppExpireTime());
    }
    
    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    private void recordLoginInfo(Long userId)
    {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLoginIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
        sysUser.setLoginDate(DateUtils.getNowDate());
        userService.updateUserProfile(sysUser);
    }

    /**
     * 根据条件分页查询用户列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    public List<SysUser> selectUserList(SysUser user)
    {
        return sysUserMapper.selectUserList(user);
    }

    /**
     * 校验手机号码是否唯一
     *
     * @param phonenumber 手机号码
     * @param userType  用户类型 00系统用户 01 app用户 
     * @return
     */
    private String checkPhoneUnique(String phonenumber,String userType)
    {
        SysUser info = sysUserMapper.checkPhoneUnique(phonenumber,userType);
        if (StringUtils.isNotNull(info))
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }
}
