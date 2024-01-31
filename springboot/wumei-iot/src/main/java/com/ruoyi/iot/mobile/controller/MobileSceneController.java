package com.ruoyi.iot.mobile.controller;

import java.util.List;

import org.quartz.SchedulerException;
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
import com.ruoyi.iot.domain.Scene;
import com.ruoyi.iot.domain.SceneDevice;
import com.ruoyi.iot.mobile.service.IFamilyDeviceService;
import com.ruoyi.iot.service.ISceneService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 场景联动Controller
 * 
 * @author kerwincui
 * @date 2022-01-13
 */
@Api(tags="a移动端----场景相关接口")
@RestController
@RequestMapping("/mobile/scene")
public class MobileSceneController extends BaseController
{
    @Autowired
    private ISceneService sceneService;
    @Autowired
    private IFamilyDeviceService familyDeviceService;

    /**
     * 查询场景联动列表
     */
    @ApiOperation("查看家庭场景列表")
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam("familyId") Long familyId)
    {
    	Scene scene = new Scene();
    	scene.setFamilyId(familyId);
        startPage();
        List<Scene> list = sceneService.selectSceneList(scene);
        return getDataTable(list);
    }

    @ApiOperation("场景设备列表")
    @GetMapping("/deviceList")
    public TableDataInfo deviceList(@RequestParam("sceneId") Long sceneId)
    {
    	startPage();
    	List<SceneDevice> list = sceneService.selectDeviceBySceneId(sceneId);
    	return getDataTable(list);
    }
    /**
     * 获取场景联动详细信息
     */
    @ApiOperation("获取场景联动详细信息")
    @GetMapping(value = "/getInfo")
    public AjaxResult getInfo(@RequestParam("sceneId") Long sceneId)
    {
        return AjaxResult.success(sceneService.selectSceneBySceneId(sceneId));
    }
    @ApiOperation("执行一次场景联动")
    @PostMapping(value = "/run")
    public AjaxResult run(@RequestParam("sceneId") Long sceneId)
    {
    	sceneService.runSceneBySceneId(sceneId,1,getUserId(),null,null);
    	return AjaxResult.success();
    }

    /**
     * 新增场景联动
     */
    @ApiOperation("新增场景联动")
    @PostMapping
    public AjaxResult add(@RequestBody Scene scene)
    {
    	scene.setIsSys(0);//非系统内置
        return toAjax(sceneService.insertScene(scene));
    }

    /**
     * 修改场景联动
     */
    @ApiOperation("修改场景联动")
    @PutMapping
    public AjaxResult edit(@RequestBody Scene scene)
    {
        return toAjax(sceneService.updateScene(scene));
    }
    /**
     * 修改场景联动
     */
    @ApiOperation("修改场景定时状态")
    @ApiImplicitParams({
    	@ApiImplicitParam(name="jobEnabled",value = "是否启用定时场景 0否 1是"),
    	@ApiImplicitParam(name="sceneId",value = "场景ID"),
    })
    @PutMapping("editJobEnabled")
    public AjaxResult editJobEnabled(@RequestParam("jobEnabled") Integer jobEnabled,@RequestParam("sceneId") Long sceneId)
    {
    	Scene scene = new Scene();
    	scene.setSceneId(sceneId);
    	scene.setJobEnabled(jobEnabled);
    	return toAjax(sceneService.updateSceneOnly(scene));
    }

    /**
     * 删除场景联动
     * @throws SchedulerException 
     */
    @ApiOperation("删除场景联动")
	@DeleteMapping("/delete")
    public AjaxResult remove(@RequestParam("sceneId") Long sceneId) throws SchedulerException
    {
    	Long[] sceneIds = {sceneId};
        return toAjax(sceneService.deleteSceneBySceneIds(sceneIds));
    }
}
