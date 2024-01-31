package com.ruoyi.common.utils.aliyun;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CreateBucketRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.ruoyi.common.config.OSSProperties;
import com.ruoyi.common.exception.file.FileException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class OSSUtil {

    @Autowired
    private OSSProperties properties;

    private static final Logger logger = LoggerFactory.getLogger(OSSUtil.class);

    /**
     * 上传文件 返回地址
     * @param file
     * @param savaDir
     * @return
     */
    public String upLoad(MultipartFile file, String savaDir) {
    	// 判断文件
        if (file == null) {
        	throw new RuntimeException("上传失败");
        }
        String originalFilename = file.getOriginalFilename();
        if(StringUtils.isNotBlank(originalFilename)){
        	logger.info("------OSS文件上传开始originalFilename--------" + originalFilename);
        }else{
        	throw new RuntimeException("上传失败");
        }
        logger.info("------OSS文件上传开始--------" + file.getName());
        String endpoint = properties.getEndPoint();
        logger.info("获取到的Point为:" + endpoint);
        String accessKeyId = properties.getAccessKeyId();
        String accessKeySecret = properties.getAccessKeySecret();
        String bucketName = properties.getBuketname();
        String fileHost = properties.getDir();
        if(savaDir == null){
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            savaDir = format.format(new Date());
        }
        String uuid = System.currentTimeMillis()+"";
       // String suffix = file.getName().substring(file.getName().lastIndexOf(".") + 1);
        
        OSS client = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            // 判断容器是否存在,不存在就创建
            if (!client.doesBucketExist(bucketName)) {
                client.createBucket(bucketName);
                CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
                createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
                client.createBucket(createBucketRequest);
            }
            // 设置文件路径和名称
            String fileUrl = fileHost + "/" + savaDir + "/" + uuid + "_" + file.getOriginalFilename();
            // 设置权限(公开读)
            client.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
            // 上传文件
            PutObjectResult result = client.putObject(bucketName, fileUrl, file.getInputStream());
           // PutObjectResult result = client.putObject(new PutObjectRequest(bucketName, fileUrl, file));

            if (result != null) {
                logger.info("------OSS文件上传成功------" + fileUrl);
                return properties.getWebUrl() + "/" + fileUrl;//文件的web访问地址;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException("上传失败");
        }finally {
            if (client != null) {
                client.shutdown();
            }
        }
        return null;
    }
}