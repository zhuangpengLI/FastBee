package com.ruoyi.common.utils.tengxunyun;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.Bucket;
import com.qcloud.cos.model.CannedAccessControlList;
import com.qcloud.cos.model.CreateBucketRequest;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import com.ruoyi.common.config.OSSProperties;
import com.ruoyi.common.config.TencentCOSProperties;
import com.ruoyi.common.exception.file.FileException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class COSUtil {

    @Autowired
    private TencentCOSProperties properties;
    
    private COSClient cosClient ;

    private static final Logger logger = LoggerFactory.getLogger(COSUtil.class);

    @PostConstruct
    public void init() {
    	
    	if(cosClient==null) {
    		String regionS = properties.getRegion();
            logger.info("获取到的region为:" + regionS);
            String secretId = properties.getSecretId();
            String secretKey = properties.getSecretKey();
    		logger.info("开始初始化腾讯cos对象");
    		// 1 初始化用户身份信息（secretId, secretKey）。
    		// SECRETID 和 SECRETKEY 请登录访问管理控制台 https://console.cloud.tencent.com/cam/capi 进行查看和管理
//    		String secretId = System.getenv("secretId");//用户的 SecretId，建议使用子账号密钥，授权遵循最小权限指引，降低使用风险。子账号密钥获取可参见 https://cloud.tencent.com/document/product/598/37140
//    		String secretKey = System.getenv("secretKey");//用户的 SecretKey，建议使用子账号密钥，授权遵循最小权限指引，降低使用风险。子账号密钥获取可参见 https://cloud.tencent.com/document/product/598/37140
    		COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
    		// 2 设置 bucket 的地域, COS 地域的简称请参见 https://cloud.tencent.com/document/product/436/6224
    		// clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
    		Region region = new Region(regionS);
    		ClientConfig clientConfig = new ClientConfig(region);
    		// 这里建议设置使用 https 协议
    		// 从 5.6.54 版本开始，默认使用了 https
    		clientConfig.setHttpProtocol(HttpProtocol.https);
    		// 3 生成 cos 客户端。
    		cosClient = new COSClient(cred, clientConfig);
    	}
    }
    
    @PreDestroy
    public void destroy() {
    	logger.info("开始关闭腾讯cos对象");
    	if(cosClient!=null) {
    		// 关闭客户端(关闭后台线程)
    		cosClient.shutdown();
    		logger.info("关闭腾讯cos对象成功");
    	}
    }
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
        	logger.info("------腾讯COS文件上传开始originalFilename--------" + originalFilename);
        }else{
        	throw new RuntimeException("上传失败");
        }
        logger.info("------腾讯COS文件上传开始--------" + file.getName());
        String region = properties.getRegion();
        logger.info("获取到的region为:" + region);
//        String secretId = properties.getSecretId();
//        String secretKey = properties.getSecretKey();
        String bucket = properties.getBucket();
        String fileHost = properties.getDir();
        if(savaDir == null){
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            savaDir = format.format(new Date());
        }
        String uuid = System.currentTimeMillis()+"";
       // String suffix = file.getName().substring(file.getName().lastIndexOf(".") + 1);
        
//        OSS client = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            // 判断容器是否存在,不存在就创建
            if (!cosClient.doesBucketExist(bucket)) {
            	CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucket);
            	// 设置 bucket 的权限为 Private(私有读写)、其他可选有 PublicRead（公有读私有写）、PublicReadWrite（公有读写）
            	createBucketRequest.setCannedAcl(CannedAccessControlList.PublicRead);
            	try{
            	    Bucket bucketResult = cosClient.createBucket(createBucketRequest);
            	} catch (CosServiceException serverException) {
            	    serverException.printStackTrace();
            	} catch (CosClientException clientException) {
            	    clientException.printStackTrace();
            	}
            }
            // 设置文件路径和名称
            String fileUrl = fileHost + "/" + savaDir + "/" + uuid + "_" + file.getOriginalFilename();
            ObjectMetadata objectMetadata = new ObjectMetadata();
	         // 上传的流如果能够获取准确的流长度，则推荐一定填写 content-length
	         // 如果确实没办法获取到，则下面这行可以省略，但同时高级接口也没办法使用分块上传了
            objectMetadata.setContentLength(file.getSize());
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileUrl, file.getInputStream(),null);
//            PutObjectRequest putObjectRequest = PutObjectRequest.withb
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);

            if (putObjectResult != null) {
                logger.info("------COS文件上传成功------" + fileUrl);
                return properties.getWebUrl() + "/" + fileUrl;//文件的web访问地址;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException("上传失败");
        }finally {
        	
        }
        return null;
    }
}