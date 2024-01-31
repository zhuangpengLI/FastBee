package com.ruoyi.iot.mobile.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.iot.domain.FamilyUserRela;
import com.ruoyi.iot.mobile.model.InviteFamilyUserReqDto;
import com.ruoyi.iot.mobile.model.UpdateFamilyUserReqDto;
import com.ruoyi.iot.service.IFamilyService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 家庭管理Controller
 * 
 * @author renjiayue
 * @date 2022-09-07
 */
@Api(tags="a移动端----家庭成员相关接口")
@RestController
@RequestMapping("/mobile/familyUser")
public class MobileFamilyUserController extends BaseController
{
    @Autowired
    private IFamilyService familyService;


    @ApiOperation("申请加入家庭 ---  通过网关查到房间后主动申请加入")
    @GetMapping("/applyJoinFamily")
    public AjaxResult applyJoinFamily(@RequestParam("familyId") Long familyId)
    {
    	if(familyId==null){
    		return AjaxResult.error("家庭信息为空");
    	}
    	return familyService.applyJoinFamily(familyId,getUserId());
    }
    
    @ApiOperation("查看家庭用户列表")
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam("familyId") Long familyId)
    {
        List<FamilyUserRela> list = familyService.selectUserListByFamilyIdAndUserId(familyId,null,getUserId());
        return getDataTable(list);
    }
    /**
     * 修改家庭成员权限
     */
    @ApiOperation("修改成员权限")
    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody @Valid UpdateFamilyUserReqDto req)
    {
        return familyService.updateFamilyUser(req,getUserId());
    }
    /**
     * 邀请家庭成员
     */
    @ApiOperation("邀请家庭成员(分享家庭)")
    @PostMapping("/invite")
    public AjaxResult invite(@RequestBody @Valid InviteFamilyUserReqDto req)
    {
    	return familyService.inviteFamilyUser(req,getUserId());
    }
    @ApiOperation("删除家庭成员")
    @ApiImplicitParams({
    	@ApiImplicitParam(name="familyId",value="家庭id"),
    	@ApiImplicitParam(name="userId",value="用户id")
    })
	@DeleteMapping("/delete")
    public AjaxResult remove(@RequestParam("familyId") Long familyId,@RequestParam("userId") Long userId)
    {
        return familyService.deleteFamilyUser(familyId,userId,getUserId());
    }
}
