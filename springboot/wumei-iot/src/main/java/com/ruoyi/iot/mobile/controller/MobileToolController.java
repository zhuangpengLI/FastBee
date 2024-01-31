package com.ruoyi.iot.mobile.controller;

import static com.ruoyi.common.utils.file.FileUploadUtils.getExtension;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.exception.file.FileNameLengthLimitExceededException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.aliyun.OSSUtil;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.common.utils.tengxunyun.COSUtil;
import com.ruoyi.iot.mqtt.EmqxService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * 产品分类Controller
 *
 * @author kerwincui
 * @date 2021-12-16
 */
@Api(tags="a移动端----工具相关")
@RestController
@RequestMapping("/mobile/tool")
public class MobileToolController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(MobileToolController.class);

    @Lazy
    @Autowired
    private EmqxService emqxService;

    // 令牌秘钥
    @Value("${token.secret}")
    private String secret;
    
    @Autowired
    private COSUtil cosUtil;
    @Autowired
    private OSSUtil ossUtil;
    @Value("${ruoyi.fileUploadType}")
    private String fileUploadType;

    /**
     * 文件上传
     */
    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public AjaxResult uploadFile(MultipartFile file) throws Exception {
    	if(fileUploadType.equals("system")){
    		try {
                String filePath = RuoYiConfig.getProfile();
                // 文件名长度限制
                int fileNamelength = file.getOriginalFilename().length();
                if (fileNamelength > FileUploadUtils.DEFAULT_FILE_NAME_LENGTH) {
                    throw new FileNameLengthLimitExceededException(FileUploadUtils.DEFAULT_FILE_NAME_LENGTH);
                }
                // 文件类型限制
                // assertAllowed(file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);

                // 获取文件名和文件类型
                String fileName = file.getOriginalFilename();
                String extension = getExtension(file);
                //设置日期格式
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MMdd-HHmmss");
                fileName = "/iot/" + getLoginUser().getUserId().toString() + "/" + df.format(new Date()) + "." + extension;
                //创建目录
                File desc = new File(filePath + File.separator + fileName);
                if (!desc.exists()) {
                    if (!desc.getParentFile().exists()) {
                        desc.getParentFile().mkdirs();
                    }
                }
                // 存储文件
                file.transferTo(desc);

                String url = "/profile" + fileName;
                AjaxResult ajax = AjaxResult.success();
                ajax.put("fileName", url);
                ajax.put("url", url);
                return ajax;
            } catch (Exception e) {
                return AjaxResult.error(e.getMessage());
            }
    	}else if(fileUploadType.equals("aliyun")){
    		String upLoad = ossUtil.upLoad(file, null);
    		if(upLoad!=null){
    			AjaxResult ajax = AjaxResult.success();
    			ajax.put("fileName", upLoad.substring(upLoad.lastIndexOf("/")+1));
    			ajax.put("url", upLoad);
    			return ajax;
    		}
    		return AjaxResult.error("上传失败");
    	}else if(fileUploadType.equals("tecentyun")){
    		String upLoad = cosUtil.upLoad(file, null);
    		if(upLoad!=null){
    			AjaxResult ajax = AjaxResult.success();
    			ajax.put("fileName", upLoad.substring(upLoad.lastIndexOf("/")+1));
    			ajax.put("url", upLoad);
    			return ajax;
    		}
    		return AjaxResult.error("上传失败");
    	}else{
    		return AjaxResult.error("上传失败,未指定上传方式");
    	}
    }

    /**
     * 下载文件
     */
    @ApiOperation("文件下载")
    @ApiImplicitParam(name="fileName",value="文件名")
    @GetMapping("/download")
    public void download(String fileName, HttpServletResponse response, HttpServletRequest request) {
        try {
//            if (!FileUtils.checkAllowDownload(fileName)) {
//                throw new Exception(StringUtils.format("文件名称({})非法，不允许下载。 ", fileName));
//            }
            String filePath = RuoYiConfig.getProfile();
            // 资源地址
            String downloadPath = filePath + fileName.replace("/profile", "");
            // 下载名称
            String downloadName = StringUtils.substringAfterLast(downloadPath, "/");
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, downloadName);
            FileUtils.writeBytes(downloadPath, response.getOutputStream());
        } catch (Exception e) {
            log.error("下载文件失败", e);
        }
    }
}
