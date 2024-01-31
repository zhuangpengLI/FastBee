package com.ruoyi.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 腾讯云配置
 * @author yue
 *
 */
@Configuration
public class TencentCOSProperties {
    @Value("${tecent.cos.secret-id}")
    private String secretId;

    @Value("${tecent.cos.secret-key}")
    private String secretKey;

    @Value("${tecent.cos.bucket}")
    private String bucket;

    @Value("${tecent.cos.dir}")
    private String dir;

    @Value("${tecent.cos.key}")
    private String key;
    
    @Value("${tecent.cos.region}")
    private String region;

    @Value("${tecent.cos.web-url}")
    private String webUrl;

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }


    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

	public String getSecretId() {
		return secretId;
	}

	public void setSecretId(String secretId) {
		this.secretId = secretId;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getBucket() {
		return bucket;
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}
    
}
