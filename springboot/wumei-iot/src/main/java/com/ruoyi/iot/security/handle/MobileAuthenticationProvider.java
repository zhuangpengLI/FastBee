package com.ruoyi.iot.security.handle;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.framework.security.handle.MpWeixinAuthenticationToken;

public class MobileAuthenticationProvider implements AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		//实现认证内容
		LoginUser loginUser = (LoginUser)authentication.getPrincipal();
		return new MpWeixinAuthenticationToken(loginUser,true);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (MpWeixinAuthenticationToken.class
				.isAssignableFrom(authentication));
	}

}
