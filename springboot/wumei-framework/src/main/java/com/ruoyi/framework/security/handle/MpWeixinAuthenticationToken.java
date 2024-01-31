package com.ruoyi.framework.security.handle;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class MpWeixinAuthenticationToken extends AbstractAuthenticationToken {

	/**
	 * 
	 */
	private static final long serialVersionUID = 460480914394464785L;

	private final Object principal;
	
	public MpWeixinAuthenticationToken(Object principal) {
		super(null);
		this.principal = principal;
		setAuthenticated(false);
	}
	
	/**
	 * 构建一个成功或失败的认证对象  只能用于认证过程中 即 provider的authenticate方法中
	 * @param principal
	 * @param authenticated
	 */
	public MpWeixinAuthenticationToken(Object principal,Boolean authenticated) {
		super(null);
		this.principal = principal;
		setAuthenticated(authenticated);
	}
	
	public MpWeixinAuthenticationToken(Object principal,Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = principal;
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return this.principal;
	}
	


}
