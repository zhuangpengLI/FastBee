package com.ruoyi.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author fcs
 * @title: AliyunOssProperties
 * @projectName zmb-api
 * @description: 阿里云OSS 配置
 * @date 2020/5/2211:35
 */
@Configuration
public class OSSProperties {
    @Value("${aliyun.oss.access-key-id}")
    private String accessKeyId;

    @Value("${aliyun.oss.access-key-secret}")
    private String accessKeySecret;

    @Value("${aliyun.oss.buketname}")
    private String buketname;

    @Value("${aliyun.oss.dir}")
    private String dir;

    @Value("${aliyun.oss.end-point}")
    private String endPoint;

    @Value("${aliyun.oss.web-url}")
    private String webUrl;

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getBuketname() {
        return buketname;
    }

    public void setBuketname(String buketname) {
        this.buketname = buketname;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }
}
