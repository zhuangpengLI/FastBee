package com.ruoyi.iot.mobile.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.iot.domain.CmsUserAgreement;
import com.ruoyi.iot.service.ICmsUserAgreementService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 用户协议Controller
 * 
 * @author renjiayue
 * @date 2022-09-09
 */
@Api(tags="a移动端----用户协议接口")
@RestController
@RequestMapping("/mobile/agreement")
public class MobileUserAgreementController extends BaseController
{
    @Autowired
    private ICmsUserAgreementService cmsUserAgreementService;

    /**
     * 获取用户协议详细信息
     */
    @ApiOperation("用户协议查询")
    @ApiImplicitParams({
    	@ApiImplicitParam(dataType = "String", name = "type", value = "协议类型 01用户协议 02隐私政策 03注销协议 04关于我们", required = true, example = "01"),
    	@ApiImplicitParam(dataType = "String", name = "format", value = "格式化 html格式为html 其他为json返回", required = false, example = "html")
    })
    @GetMapping(value = "/getInfo")
    public void getInfo(@RequestParam("type") String type,@RequestParam(value="format",required=false)String format,HttpServletResponse response )
    {
    	CmsUserAgreement dto = cmsUserAgreementService.selectCmsUserAgreementByType(type);
    	if("html".equals(format)){
    		response.setContentType("text/html; charset=utf-8");
    		response.setCharacterEncoding("UTF-8");
    		PrintWriter writer = null;
			try {
				writer = response.getWriter();
				writer.write(dto.getDetail());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if(writer!=null) {
					writer.flush();
					writer.close();
				}
			}
//			return ;
		}else {
			response.setContentType("application/json;charset:utf-8;");
			response.setCharacterEncoding("UTF-8");
    		PrintWriter writer = null;
			try {
				writer = response.getWriter();
				writer.write(JSONObject.toJSONString(AjaxResult.success("查询成功",dto.getDetail())));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if(writer!=null) {
					writer.flush();
					writer.close();
				}
			}
		}
//        return AjaxResult.success(dto);
    }
}
