package com.ruoyi.iot.mobile.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.iot.mobile.model.MobileLoginUserInput;
import com.ruoyi.iot.mobile.service.IMobileUserService;
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
@Api(tags="aa移动端----登录接口")
@RestController
@RequestMapping("/mobile")
public class MobileLoginController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(MobileLoginController.class);

    @Lazy
    @Autowired
    private EmqxService emqxService;

    @Autowired
    private IMobileUserService mobileUserService;

    // 令牌秘钥
    @Value("${token.secret}")
    private String secret;

    /**
     * 移动端用户注册
     */
    @ApiOperation("用户获取验证码")
    @ApiImplicitParam(name="phonenumber",value="手机号")
    @GetMapping("/loginCode")
    public AjaxResult loginCode(@RequestParam("phonenumber") String phonenumber) {
    	return mobileUserService.loginCode(phonenumber);
    }
    /**
     * 移动端用户注册
     * @throws Exception 
     */
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public AjaxResult login(@RequestBody MobileLoginUserInput user) throws Exception {
    	return mobileUserService.login(user);
    }
}
