package com.ruoyi.iot.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.quartz.SchedulerException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.iot.domain.DeviceJob;
import com.ruoyi.iot.domain.Scene;
import com.ruoyi.iot.domain.SceneDevice;
import com.ruoyi.iot.domain.SceneRecord;
import com.ruoyi.iot.model.AdminSceneRespDto;
import com.ruoyi.iot.service.IDeviceJobService;
import com.ruoyi.iot.service.IFamilyService;
import com.ruoyi.iot.service.ISceneRecordService;
import com.ruoyi.iot.service.ISceneService;
import com.ruoyi.quartz.domain.SysJobLog;
import com.ruoyi.quartz.service.ISysJobLogService;
import com.ruoyi.quartz.service.ISysJobService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 场景联动Controller
 * 
 * @author kerwincui
 * @date 2022-01-13
 */
@Api(tags = "b管理端----场景管理")
@RestController
@RequestMapping("/iot/scene")
public class SceneController extends BaseController
{
    @Autowired
    private ISceneService sceneService;
    @Autowired
    private IFamilyService familyService;
    @Autowired
    private IDeviceJobService deviceJobService;
    @Autowired
    private ISysJobService jobService;
    @Autowired
    private ISysJobLogService jobLogService;
    @Autowired
    private ISceneRecordService sceneRecordService;


    /**
     * 查询场景联动列表
     */
    @ApiOperation("场景列表")
    @PreAuthorize("@ss.hasPermi('iot:scene:list')")
    @GetMapping("/list")
    public TableDataInfo list(Scene scene)
    {
        startPage();
        List<Scene> list = sceneService.selectSceneList(scene);
        List<AdminSceneRespDto> respList = new ArrayList<>();
        for(Scene s:list) {
        	AdminSceneRespDto dto = new AdminSceneRespDto();
        	BeanUtils.copyProperties(s, dto);
        	dto.setFamilyName(familyService.selectFamilyOnlyByFamilyId(dto.getFamilyId()).getName());
        	dto.setSceneDeviceCount(sceneService.countDeviceBySceneId(dto.getSceneId()));
        	respList.add(dto);
        }
        return getDataTable(list,respList);
    }
    @ApiOperation("场景设备列表")
    @PreAuthorize("@ss.hasPermi('iot:scene:deviceList')")
    @GetMapping("/deviceList")
    public TableDataInfo deviceList(@RequestParam("sceneId") Long sceneId)
    {
    	startPage();
    	List<SceneDevice> list = sceneService.selectDeviceBySceneId(sceneId);
    	return getDataTable(list);
    }
    
    @ApiOperation("查询场景定时任务列表")
    @PreAuthorize("@ss.hasPermi('iot:scene:jobList')")
    @GetMapping("/sceneJobList")
    public TableDataInfo sceneJobList(@RequestParam("sceneId") Long sceneId)
    {
    	Scene scene = sceneService.selectSceneBySceneId(sceneId);
    	if(scene==null) {
    		return getDataTable(new ArrayList<>());
    	}
    	DeviceJob deviceJob = new DeviceJob();
    	deviceJob.setSceneId(sceneId);
    	deviceJob.setJobType(3);
    	startPage();
    	List<DeviceJob> list = deviceJobService.selectJobList(deviceJob);
    	return getDataTable(list);
    }
    
    /**
     * 查询场景执行记录列表
     */
    @ApiOperation("查询场景执行记录列表")
    @PreAuthorize("@ss.hasPermi('iot:scene:recordList')")
    @GetMapping("/sceneRunRecordList")
    public TableDataInfo sceneRunRecordList(@RequestParam("sceneId") Long sceneId)
    {
    	SceneRecord sceneRecord = new SceneRecord();
    	sceneRecord.setSceneId(sceneId);
        startPage();
        List<SceneRecord> list = sceneRecordService.selectSceneRecordList(sceneRecord);
        return getDataTable(list);
    }

    /**
     * 导出场景联动列表
     */
    @PreAuthorize("@ss.hasPermi('iot:scene:export')")
    @Log(title = "场景联动", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Scene scene)
    {
        List<Scene> list = sceneService.selectSceneList(scene);
        ExcelUtil<Scene> util = new ExcelUtil<Scene>(Scene.class);
        util.exportExcel(response, list, "场景联动数据");
    }

    /**
     * 获取场景联动详细信息
     */
    @PreAuthorize("@ss.hasPermi('iot:scene:query')")
    @GetMapping(value = "/{sceneId}")
    public AjaxResult getInfo(@PathVariable("sceneId") Long sceneId)
    {
        return AjaxResult.success(sceneService.selectSceneBySceneId(sceneId));
    }

    /**
     * 新增场景联动
     */
    @PreAuthorize("@ss.hasPermi('iot:scene:add')")
    @Log(title = "场景联动", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Scene scene)
    {
        return toAjax(sceneService.insertScene(scene));
    }

    /**
     * 修改场景联动
     */
    @PreAuthorize("@ss.hasPermi('iot:scene:edit')")
    @Log(title = "场景联动", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Scene scene)
    {
        return toAjax(sceneService.updateScene(scene));
    }

    /**
     * 删除场景联动
     * @throws SchedulerException 
     */
    @PreAuthorize("@ss.hasPermi('iot:scene:remove')")
    @Log(title = "场景联动", businessType = BusinessType.DELETE)
	@DeleteMapping("/{sceneIds}")
    public AjaxResult remove(@PathVariable Long[] sceneIds) throws SchedulerException
    {
        return toAjax(sceneService.deleteSceneBySceneIds(sceneIds));
    }
}
