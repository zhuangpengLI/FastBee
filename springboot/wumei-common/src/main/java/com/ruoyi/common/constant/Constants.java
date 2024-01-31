package com.ruoyi.common.constant;

import io.jsonwebtoken.Claims;

/**
 * 通用常量信息
 * 
 * @author ruoyi
 */
public class Constants
{
    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";

    /**
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";

    /**
     * 通用成功标识
     */
    public static final String SUCCESS = "0";

    /**
     * 通用失败标识
     */
    public static final String FAIL = "1";

    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS = "Success";

    /**
     * 注销
     */
    public static final String LOGOUT = "Logout";

    /**
     * 注册
     */
    public static final String REGISTER = "Register";

    /**
     * 登录失败
     */
    public static final String LOGIN_FAIL = "Error";

    /**
     * 网关离线消息通知 redis key
     */
    public static final String GW_OFFLINE_NOTICE_KEY = "gw_offline_notice_key:";
    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";
    /**
     * 移动端注册短信验证码 redis key
     */
    public static final String MOBILE_LOGIN_CAPTCHA_CODE_KEY = "mobile_login_captcha_codes:";
    /**
     * 有效期为5分钟
     */
    public static final int MOBILE_LOGIN_CAPTCHA_CODE = 5;
    /**
     * 验证码最多输入错误次数
     */
    public static final int MOBILE_LOGIN_CAPTCHA_CODE_MAX_ERROR = 10;
    /**
     * 当前验证码已经输入错误次数
     */
    public static final String MOBILE_LOGIN_CAPTCHA_CODE_ERROR_COUNT_KEY = "mobile_login_captcha_codes_error_count:";
    /**
     * 移动端注册短信验证码频率 redis key
     */
    public static final String MOBILE_LOGIN_CAPTCHA_CODE_FREQUENCY_KEY = "mobile_login_captcha_codes_frequency:";
    /**
     * 频率为1分钟
     */
    public static final int MOBILE_LOGIN_CAPTCHA_CODE_FREQUENCYY = 1;
    /**
     * 移动端注册短信验证码 redis key
     */
    public static final String MOBILE_UPDATE_PHONE_CAPTCHA_CODE_KEY = "mobile_update_phone_captcha_codes:";

    /**
     * 登录用户 redis key
     */
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";
    
    /**
     * 登录用户 redis 次数控制key
     */
    public static final String LOGIN_TOKEN_CONTROLLER_COUNT = "login_tokens_controller_count:";

    /**
     * 防重提交 redis key
     */
    public static final String REPEAT_SUBMIT_KEY = "repeat_submit:";

    /**
     * 限流 redis key
     */
    public static final String RATE_LIMIT_KEY = "rate_limit:";

    /**
     * 验证码有效期（分钟）
     */
    public static final Integer CAPTCHA_EXPIRATION = 2;

    /**
     * 令牌
     */
    public static final String TOKEN = "token";

    /**
     * 令牌前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 令牌前缀
     */
    public static final String LOGIN_USER_KEY = "login_user_key";

    /**
     * 用户ID
     */
    public static final String JWT_USERID = "userid";

    /**
     * 用户名称
     */
    public static final String JWT_USERNAME = Claims.SUBJECT;

    /**
     * 用户头像
     */
    public static final String JWT_AVATAR = "avatar";

    /**
     * 创建时间
     */
    public static final String JWT_CREATED = "created";

    /**
     * 用户权限
     */
    public static final String JWT_AUTHORITIES = "authorities";

    /**
     * 参数管理 cache key
     */
    public static final String SYS_CONFIG_KEY = "sys_config:";
    /**
     * 系统参数管理 cache key(智能家居)
     */
    public static final String SYS_BASE_CONFIG_KEY = "sys_base_config:znjj";

    /**
     * 字典管理 cache key
     */
    public static final String SYS_DICT_KEY = "sys_dict:";

    /**
     * 资源映射路径 前缀
     */
    public static final String RESOURCE_PREFIX = "/profile";

    /**
     * RMI 远程方法调用
     */
    public static final String LOOKUP_RMI = "rmi://";

    /**
     * LDAP 远程方法调用
     */
    public static final String LOOKUP_LDAP = "ldap://";

    /**
     * 定时任务违规的字符
     */
    public static final String[] JOB_ERROR_STR = { "java.net.URL", "javax.naming.InitialContext", "org.yaml.snakeyaml",
            "org.springframework.jndi" };
}