package com.ruoyi.iot.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ruoyi.iot.security.handle.MobileAuthenticationProvider;

@Configuration
public class MobileSecurityConfig {

    @Bean
    public MobileAuthenticationProvider customerAuthenticationProvider(){
    	return new MobileAuthenticationProvider();
    }
}
