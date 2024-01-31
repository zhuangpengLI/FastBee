package com.ruoyi.iot.mobile.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.iot.domain.CmsFeedback;
import com.ruoyi.iot.mobile.model.SubmitFeedbackReqDto;
import com.ruoyi.iot.service.ICmsFeedbackService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 意见反馈Controller
 * 
 * @author renjiayue
 * @date 2022-09-07
 */
@Api(tags="a移动端----用户反馈")
@RestController
@RequestMapping("/mobile/feedback")
public class MobileFeedbackController extends BaseController
{
    @Autowired
    private ICmsFeedbackService cmsFeedbackService;

    /**
     * 新增意见反馈
     */
    @ApiOperation("提交反馈")
    @PostMapping
    public AjaxResult add(@RequestBody SubmitFeedbackReqDto dto)
    {
    	SysUser user = getSysUser();
    	CmsFeedback cmsFeedback = new CmsFeedback();
    	BeanUtils.copyProperties(dto, cmsFeedback);
    	String picUrls = cmsFeedback.getPicUrls();
    	//TODO
//    	picUrls.replace("\\[", "");
//    	picUrls.replace("\\]", "");
//    	picUrls.replace("[", "");
//    	picUrls.replace("]", "");
    	cmsFeedback.setHasPicture(StringUtils.isBlank(picUrls)?0:1);
    	cmsFeedback.setUserId(user.getUserId());
    	cmsFeedback.setUsername(user.getUserName());
    	cmsFeedback.setMobile(user.getPhonenumber());
    	cmsFeedback.setStatus(0L);//未回复
    	Date now = DateUtils.getNowDate();
    	cmsFeedback.setAddTime(now);
    	cmsFeedback.setCreateBy(user.getUserName());
//    	cmsFeedback.setUpdateTime(now);
//    	cmsFeedback.setUpdateBy(user.getUserName());
        return toAjax(cmsFeedbackService.insertCmsFeedback(cmsFeedback));
    }

}
