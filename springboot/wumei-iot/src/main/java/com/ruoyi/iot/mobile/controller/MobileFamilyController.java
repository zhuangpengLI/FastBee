package com.ruoyi.iot.mobile.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.BeanUtils;
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
import com.ruoyi.iot.domain.Family;
import com.ruoyi.iot.domain.FamilyUserRela;
import com.ruoyi.iot.mobile.model.AddOrUpdateFamilyReqDto;
import com.ruoyi.iot.mobile.model.BindFamilyGatewaySnReqDto;
import com.ruoyi.iot.service.IFamilyService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * 家庭管理Controller
 * 
 * @author renjiayue
 * @date 2022-09-07
 */
@Api(tags="a移动端----家庭相关接口")
@RestController
@RequestMapping("/mobile/family")
public class MobileFamilyController extends BaseController
{
    @Autowired
    private IFamilyService familyService;

    /**
     * 查询家庭管理列表
     */
    @ApiOperation("查看我的家庭列表")
    @GetMapping("/list")
    public TableDataInfo list()
    {
        List<FamilyUserRela> list = familyService.selectUserListByFamilyIdAndUserId(null,getUserId());
        return getDataTable(list);
    }
    @ApiOperation("查看我的家庭列表 -- 包含统计信息 设备/成员/房间等")
    @GetMapping("/statList")
    public TableDataInfo statList()
    {
    	List<FamilyUserRela> list = familyService.selectUserStatListByFamilyIdAndUserId(null,getUserId());
    	return getDataTable(list);
    }

    @ApiOperation("通过网关查询家庭信息 -- 有无家庭都是true 需要自行判断data是否为null 进行展示网关已绑定家庭")
    @ApiImplicitParam(name="gatewaySn",value="网关sn码")
    @GetMapping(value = "/getFamilyInfoByGateway")
    public AjaxResult getFamilyInfoByGateway(@RequestParam("gatewaySn") String gatewaySn)
    {
    	return AjaxResult.success(familyService.selectFamilyOnlyByGatewaySn(gatewaySn));
    }
    
    @ApiOperation("获取家庭详细信息 --  可以在详情使用,也可以在切换家庭时根据是自己家庭来判断是否需要跳转绑定网关")
    @ApiImplicitParam(name="familyId",value="家庭id")
    @GetMapping(value = "/getInfo")
    public AjaxResult getInfo(@RequestParam("familyId") Long familyId)
    {
    	//非家庭成员不可查看
        return AjaxResult.success(familyService.selectFamilyOnlyByFamilyId(familyId,getUserId()));
    }
    @ApiOperation("获取家庭详细信息和统计信息")
    @ApiImplicitParam(name="familyId",value="家庭id")
    @GetMapping(value = "/getInfoAndStat")
    public AjaxResult getInfoAndStat(@RequestParam("familyId") Long familyId)
    {
    	//非家庭成员不可查看
    	return AjaxResult.success(familyService.selectFamilyAndStatByFamilyId(familyId,getUserId()));
    }

    /**
     * 新增家庭管理
     */
    @ApiOperation("创建家庭信息---返回家庭id")
    @PostMapping("/add")
    public AjaxResult add(@RequestBody @Valid AddOrUpdateFamilyReqDto req)
    {
    	//新增时id为空 网关为空
    	req.setFamilyId(null);
    	Family family = new Family();
    	BeanUtils.copyProperties(req, family);
    	if(StringUtils.isBlank(family.getAvatarUrl())) {
    		family.setAvatarUrl("https://a.png");
    	}
    	Long userId = getUserId();
    	family.setBelongUserId(userId);
    	family.setCreateUserId(userId);
    	return familyService.insertFamily(family);
    }

    /**
     * 修改家庭管理
     */
    @ApiOperation("修改家庭信息")
    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody @Valid AddOrUpdateFamilyReqDto req)
    {
    	Long familyId = req.getFamilyId();
    	if(familyId==null){
    		return AjaxResult.error("家庭信息为空");
    	}
    	//修改不允许修改网关
    	Family family = new Family();
    	BeanUtils.copyProperties(req, family);
        return familyService.updateFamily(family,getUserId());
    }
    @ApiOperation("绑定家庭网关")
    @PutMapping("/bindGatewaySn")
    public AjaxResult bindGatewaySn(@RequestBody @Valid BindFamilyGatewaySnReqDto req)
    {
    	Long familyId = req.getFamilyId();
    	if(familyId==null){
    		return AjaxResult.error("家庭信息为空");
    	}
    	//修改不允许修改网关
    	Family family = new Family();
    	BeanUtils.copyProperties(req, family);
    	
    	return familyService.bindFamilyGateway(family,getUserId(),getUsername());
    }

    /**
     * 删除家庭管理
     * @throws SchedulerException 
     */
    @ApiOperation("删除家庭")
    @ApiImplicitParam(name="familyId",value="家庭id")
	@DeleteMapping("/delete")
    public AjaxResult remove(@RequestParam("familyId") Long familyId) throws SchedulerException
    {
        return familyService.deleteSoftFamilyByFamilyId(familyId,getUserId());
    }
    
    /**
     * 删除家庭管理
     */
    @ApiOperation("退出家庭--非房间拥有者退出")
    @ApiImplicitParam(name="familyId",value="家庭id")
    @DeleteMapping("/quit")
    public AjaxResult quit(@RequestParam("familyId") Long familyId)
    {
    	return familyService.quitFamilyByFamilyId(familyId,getUserId());
    }
}
