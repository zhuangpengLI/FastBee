package com.ruoyi.iot.mobile.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.aliyun.OSSUtil;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.tengxunyun.COSUtil;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.iot.domain.FamilyUserRela;
import com.ruoyi.iot.mobile.model.MobileLoginUserInput;
import com.ruoyi.iot.mobile.model.UpdateUserReqDto;
import com.ruoyi.iot.mobile.service.IMobileUserService;
import com.ruoyi.iot.service.IFamilyService;
import com.ruoyi.system.service.ISysUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * 个人信息 业务处理
 *
 * @author ruoyi
 */
@Api(tags="a移动端----个人信息接口")
@RestController
@RequestMapping("/mobile/user/profile")
public class MobileProfileController extends BaseController {
	
    private static final Logger log = LoggerFactory.getLogger(MobileProfileController.class);

    @Autowired
    private ISysUserService userService;

    @Autowired
    private TokenService tokenService;
    
    @Autowired
    private IMobileUserService mobileUserService;
    
    @Autowired
    private IFamilyService familyService;
    
    @Autowired
    private COSUtil cosUtil;
    @Autowired
    private OSSUtil ossUtil;
    @Value("${ruoyi.fileUploadType}")
    private String fileUploadType;


//    @Autowired
//    private IUserSocialProfileService iUserSocialProfileService;

    /**
     * 个人信息
     */
    @ApiOperation("获取个人用户信息")
    @GetMapping("/info")
    public AjaxResult profile() {
        LoginUser loginUser = getLoginUser();
        SysUser user = loginUser.getUser();
        Long userId = user.getUserId();
        HashMap<String, Object> map = new HashMap<String,Object>();
        AjaxResult ajax = AjaxResult.success(map);
        map.put("user", user);
        List<FamilyUserRela> list = familyService.selectUserListByFamilyIdAndUserId(null, userId);
        map.put("isJoinFamily", list.isEmpty()?false:true);//是否加入或创建过家庭 true false 如果是false则需要提升创建家庭
//        ajax.put("roleGroup", userService.selectUserRoleGroup(loginUser.getUsername()));
//        ajax.put("postGroup", userService.selectUserPostGroup(loginUser.getUsername()));
//        ajax.put("socialGroup", iUserSocialProfileService.selectUserSocialProfile(loginUser.getUserId()));
        return ajax;
    }

    /**
     * 修改用户
     */
    @ApiOperation("修改用户信息(暂时只有昵称)")
    @PutMapping("/updateProfile")
    public AjaxResult updateProfile(@RequestBody UpdateUserReqDto req)
    {
    	log.info("updateProfile============{}",JSON.toJSON(req));
    	SysUser user = new SysUser();
        LoginUser loginUser = getLoginUser();
        SysUser sysUser = loginUser.getUser();
        user.setUserId(sysUser.getUserId());
        if(StringUtils.isBlank(req.getNickName())){
        	user.setNickName(sysUser.getNickName());
        }else{
        	user.setNickName(req.getNickName());
        }
        if (userService.updateUserProfile(user) > 0)
        {
            // 更新缓存用户信息
            sysUser.setNickName(user.getNickName());
            tokenService.setLoginUser(loginUser);
            return AjaxResult.success();
        }
        return AjaxResult.error("修改个人信息异常，请联系管理员");
    }

    /**
     * 头像上传
     */
    @ApiOperation("头像上传并修改")
//    @ApiImplicitParam(name="avatarfile",value="文件参数名",dataType="MultipartFile")
    @PostMapping("/avatar")
    public AjaxResult avatar(@RequestParam("avatarfile") MultipartFile file) throws IOException
    {
        if (!file.isEmpty())
        {
            LoginUser loginUser = getLoginUser();
            String avatar = null;
            if(fileUploadType.equals("system")){
            	avatar = FileUploadUtils.upload(RuoYiConfig.getAvatarPath(), file);
        	}else if(fileUploadType.equals("aliyun")){
        		String upLoad = ossUtil.upLoad(file, null);
        		if(upLoad!=null){
        			avatar = upLoad;
        		}else {
        			return AjaxResult.error("上传失败");
        		}
        	}else if(fileUploadType.equals("tecentyun")){
        		String upLoad = cosUtil.upLoad(file, null);
        		if(upLoad!=null){
        			avatar = upLoad;
        		}else {
        			return AjaxResult.error("上传失败");
        		}
        	}else{
        		return AjaxResult.error("上传失败,未指定上传方式");
        	}
            if (userService.updateUserAvatar(loginUser.getUsername(), avatar))
            {
                AjaxResult ajax = AjaxResult.success();
                ajax.put("imgUrl", avatar);
                // 更新缓存用户头像
                loginUser.getUser().setAvatar(avatar);
                tokenService.setLoginUser(loginUser);
                return ajax;
            }
        }
        return AjaxResult.error("上传图片异常，请联系管理员");
    }
    
    /**
     * 移动端用户注册
     */
    @ApiOperation("用户获取修改手机验证码")
    @ApiImplicitParam(name="phonenumber",value="手机号")
    @GetMapping("/updatePhoneCode")
    public AjaxResult updatePhoneCode(@RequestParam("phonenumber") String phonenumber) {
    	return mobileUserService.updatePhoneCode(phonenumber,getLoginUser().getUser());
    }
    /**
     * 移动端用户注册
     */
    @ApiOperation("用户修改手机号")
    @PostMapping("/updatePhone")
    public AjaxResult updatePhone(@RequestBody MobileLoginUserInput user) {
    	return mobileUserService.updatePhone(user.getPhonenumber(),user.getCode(),getLoginUser());
    }
    @ApiOperation("注销账号")
    @PostMapping("/deleteAccount")
    public AjaxResult deleteAccount() {
    	Long userId = getUserId();
    	return AjaxResult.success("申请成功");
    }
}
