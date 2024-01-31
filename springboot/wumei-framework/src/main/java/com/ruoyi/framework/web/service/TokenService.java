package com.ruoyi.framework.web.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ip.AddressUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.system.otherDomain.BaseConfig;
import com.ruoyi.system.otherService.IBaseConfigService;

import eu.bitwalker.useragentutils.UserAgent;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * token验证处理
 *
 * @author ruoyi
 */
@Component
public class TokenService {
    // 令牌自定义标识
    @Value("${token.header}")
    private String header;

    // 令牌秘钥
    @Value("${token.secret}")
    private String secret;

    // 令牌有效期（默认30分钟）
    @Value("${token.expireTime}")
    private int expireTime;
    // app令牌有效期（默认30分钟）
    @Value("${token.appExpireTime}")
    private int appExpireTime;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private static final Long MILLIS_MINUTE_TEN = 20 * 60 * 1000L;

    @Autowired
    private RedisCache redisCache;
    @Autowired
    private IBaseConfigService baseConfigService;

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = getToken(request);
        return getLoginUserByToken(token);
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUserByToken(String token) {
        if (StringUtils.isNotEmpty(token)) {
            try {
                Claims claims = parseToken(token);
                // 解析对应的权限以及用户信息
                String uuid = (String) claims.get(Constants.LOGIN_USER_KEY);
                String userKey = getTokenKey(uuid);
                LoginUser user = redisCache.getCacheObject(userKey);
                return user;
            } catch (Exception e) {
            }
        }
        return null;
    }

    /**
     * 设置用户身份信息
     */
    public void setLoginUser(LoginUser loginUser) {
        if (StringUtils.isNotNull(loginUser) && StringUtils.isNotEmpty(loginUser.getToken())) {
        	if("01".equals(loginUser.getUserType())) {
        		refreshToken(loginUser, appExpireTime);
        	}else {
        		refreshToken(loginUser);
        	}
        }
    }

    /**
     * 删除用户身份信息
     */
    public void delLoginUser(String token) {
        if (StringUtils.isNotEmpty(token)) {
            String userKey = getTokenKey(token);
            redisCache.deleteObject(userKey);
        }
    }

    /**
     * 创建令牌
     *
     * @param loginUser 用户信息
     * @return 令牌
     */
    public String createToken(LoginUser loginUser) {
        String token = IdUtils.fastUUID();
        loginUser.setToken(token);
        setUserAgent(loginUser);
        refreshToken(loginUser);

        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.LOGIN_USER_KEY, token);
        return createToken(claims);
    }
    /**
     * 创建令牌
     *
     * @param loginUser 用户信息
     * @param expireTime 指定到期时间
     * @return 令牌
     */
    public String createToken(LoginUser loginUser,int expireTime) {
    	String token = IdUtils.fastUUID();
    	loginUser.setToken(token);
    	setUserAgent(loginUser);
    	refreshToken(loginUser,expireTime);
    	
    	Map<String, Object> claims = new HashMap<>();
    	claims.put(Constants.LOGIN_USER_KEY, token);
    	return createToken(claims);
    }

    /**
     * 验证令牌有效期，相差不足20分钟，自动刷新缓存
     *
     * @param loginUser
     * @return 令牌
     */
    public void verifyToken(LoginUser loginUser) {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN) {
        	if("01".equals(loginUser.getUserType())) {
        		refreshToken(loginUser, appExpireTime);
        	}else {
        		refreshToken(loginUser);
        	}
        }
    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(loginUser.getToken());
        redisCache.setCacheObject(userKey, loginUser, expireTime, TimeUnit.MINUTES);
        if("01".equals(loginUser.getUserType())) {
        	//缓存多久 这里需要填多久
        	appLoginCountCheck(loginUser.getUserId(), userKey, expireTime);
    	}
    }
    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     * @param expireTime 指定到期时间
     */
    public void refreshToken(LoginUser loginUser,int expireTime) {
    	loginUser.setLoginTime(System.currentTimeMillis());
    	loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
    	// 根据uuid将loginUser缓存
    	String userKey = getTokenKey(loginUser.getToken());
    	redisCache.setCacheObject(userKey, loginUser, expireTime, TimeUnit.MINUTES);
    	if("01".equals(loginUser.getUserType())) {
    		//缓存多久 这里需要填多久
        	appLoginCountCheck(loginUser.getUserId(), userKey, expireTime);
    	}
    }
    
    public void appLoginCountCheck(Long userId,String userKey,int expireTime) {
    	String key = Constants.LOGIN_TOKEN_CONTROLLER_COUNT +userId;
    	BaseConfig config = baseConfigService.selectOneBaseConfigByCache();
    	Long max = config.getUserLoginDeviceMax();
    	List<Map<String,Object>> cacheList = null;
    	cacheList = redisCache.getCacheList(key);
    	if(cacheList==null) {
    		//缓存没有 则新增列表
    		cacheList  = new ArrayList<Map<String,Object>>();
    	}
//    	redisCache.deleteObject(cacheList)
    	boolean isAbove = cacheList.size()>=max;//人数已经够了
//    	int aboveCount = cacheList.size()-max.intValue()+1;
    	boolean isExist = false;
    	String delUserKey = null;
    	Date oldDate = new Date();
    	for(Map<String,Object> userMap:cacheList) {
    		String userKey1 = (String) userMap.get("userKey");
    		if(userKey1.equals(userKey)) {
    			isExist = true;
    			//如果已经存在,则只更新最近登录时间
    			userMap.put("refreshDate",new Date());
    		}else {
    			Date refreshDate = (Date) userMap.get("refreshDate");
    			if(refreshDate.before(oldDate)) {
    				oldDate = refreshDate;
    				delUserKey = userKey1;
    			}
    		}
    	}
    	if(!isExist) {
    		if(isAbove) {
    			//删除最先登录的用户
    			for(int i=cacheList.size()-1;i>=0;i--) {
    				Map<String, Object> map = cacheList.get(i);
    				String userKey1 = (String) map.get("userKey");
    	    		if(userKey1.equals(delUserKey)) {
    	    			cacheList.remove(i);
//    	    			aboveCount--;
//    	    			if(aboveCount<=0) {
//    	    				break;
//    	    			}
    	    		}
    			}
    		}
    		//存入最新的登录信息
    		Map<String,Object> userMap  = new HashMap<String,Object>();
    		userMap.put("userKey",userKey);
    		userMap.put("refreshDate",new Date());
    		cacheList.add(userMap);
    	}
    	redisCache.deleteObject(key);
    	redisCache.setCacheList(key, cacheList);
    	redisCache.expire(key, expireTime,  TimeUnit.MINUTES);
    }

    /**
     * 设置用户代理信息
     *
     * @param loginUser 登录信息
     */
    public void setUserAgent(LoginUser loginUser) {
        UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
        loginUser.setIpaddr(ip);
        loginUser.setLoginLocation(AddressUtils.getRealAddressByIP(ip));
        loginUser.setBrowser(userAgent.getBrowser().getName());
        loginUser.setOs(userAgent.getOperatingSystem().getName());
    }

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private String createToken(Map<String, Object> claims) {
        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret).compact();
        return token;
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }

    /**
     * 获取请求token
     *
     * @param request
     * @return token
     */
    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(header);
        if (StringUtils.isNotEmpty(token) && token.startsWith(Constants.TOKEN_PREFIX)) {
            token = token.replace(Constants.TOKEN_PREFIX, "");
        }
        return token;
    }

    private String getTokenKey(String uuid) {
        return Constants.LOGIN_TOKEN_KEY + uuid;
    }

	public int getAppExpireTime() {
		return appExpireTime;
	}

	public void setAppExpireTime(int appExpireTime) {
		this.appExpireTime = appExpireTime;
	}
}
