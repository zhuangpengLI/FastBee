package com.ruoyi.iot.mobile.controller;

import java.util.ArrayList;
import java.util.List;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.exception.job.TaskException;
import com.ruoyi.iot.domain.DeviceJob;
import com.ruoyi.iot.domain.Scene;
import com.ruoyi.iot.mobile.model.AddDeviceJobReqDto;
import com.ruoyi.iot.mobile.model.RunDeviceJobReqDto;
import com.ruoyi.iot.mobile.model.UpdateDeviceJobStatusReqDto;
import com.ruoyi.iot.mobile.service.IFamilyDeviceService;
import com.ruoyi.iot.service.IDeviceJobService;
import com.ruoyi.iot.service.IFamilyService;
import com.ruoyi.iot.service.ISceneService;
import com.ruoyi.quartz.util.CronUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 调度任务信息操作处理
 * 
 * @author kerwincui
 */
@Api(tags="a移动端----定时任务相关接口")
@RestController
@RequestMapping("/mobile/job")
public class MobileDeviceJobController extends BaseController
{
    @Autowired
    private IDeviceJobService deviceJobService;
    @Autowired
    private IFamilyDeviceService familyDeviceService;
    @Autowired
    private ISceneService sceneService;
    @Autowired
    private IFamilyService familyService;

    /**
     * 查询定时任务列表
     */
    @ApiOperation("查询设备定时任务列表")
    @GetMapping("/deviceJobList")
    public TableDataInfo deviceJobList(@RequestParam("deviceId") Long deviceId)
    {
//    	boolean commonPerm = familyDeviceService.isCommonPerm(deviceId, getUserId());
//    	if(!commonPerm) {
//    		return getDataTable(new ArrayList<>());
//    	}
    	DeviceJob deviceJob = new DeviceJob();
    	deviceJob.setDeviceId(deviceId);
    	deviceJob.setJobType(1);
        startPage();
        List<DeviceJob> list = deviceJobService.selectJobList(deviceJob);
        return getDataTable(list);
    }
    
    @ApiOperation("查询场景定时任务列表")
    @GetMapping("/sceneJobList")
    public TableDataInfo sceneJobList(@RequestParam("sceneId") Long sceneId)
    {
    	Scene scene = sceneService.selectSceneBySceneId(sceneId);
    	if(scene==null) {
    		return getDataTable(new ArrayList<>());
    	}
//    	boolean commonPerm = familyService.isCommonPerm(scene.getFamilyId(), getUserId());
//    	if(!commonPerm) {
//    		return getDataTable(new ArrayList<>());
//    	}
    	DeviceJob deviceJob = new DeviceJob();
    	deviceJob.setSceneId(sceneId);
    	deviceJob.setJobType(3);
    	startPage();
    	List<DeviceJob> list = deviceJobService.selectJobList(deviceJob);
    	return getDataTable(list);
    }

    /**
     * 获取定时任务详细信息
     */
    @GetMapping(value = "/getInfo")
    public AjaxResult getInfo(@RequestParam("jobId") Long jobId)
    {
        return AjaxResult.success(deviceJobService.selectJobById(jobId));
    }

    /**
     * 新增定时任务
     */
    @ApiOperation("新增 设备/场景 定时任务")
    @PostMapping("/add")
    public AjaxResult add(@RequestBody AddDeviceJobReqDto dto) throws SchedulerException, TaskException
    {
        if (!CronUtils.isValid(dto.getCronExpression()))
        {
            return error("新增任务'" + dto.getJobName() + "'失败，Cron表达式不正确");
        }
        Integer jobType = dto.getJobType();
        if(1==jobType) {
        	return deviceJobService.insertDeviceJob(dto);
        }else if(3==jobType) {
        	return deviceJobService.insertSceneJob(dto);
        }else {
        	return AjaxResult.error("不支持的定时类型");
        }
    }

    /**
     * 修改定时任务
     */
    @PutMapping
    public AjaxResult edit(@RequestBody DeviceJob job) throws SchedulerException, TaskException
    {
        if (!CronUtils.isValid(job.getCronExpression()))
        {
            return error("修改任务'" + job.getJobName() + "'失败，Cron表达式不正确");
        }
        job.setUpdateBy(getUsername());
        return toAjax(deviceJobService.updateJob(job));
    }

    /**
     * 定时任务状态修改
     */
    @ApiOperation("启动/关闭定时任务")
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody UpdateDeviceJobStatusReqDto dto) throws SchedulerException 
    {
        DeviceJob newJob = deviceJobService.selectJobById(dto.getJobId());
        newJob.setStatus(dto.getStatus());
        return toAjax(deviceJobService.changeStatus(newJob));
    }

    /**
     * 定时任务立即执行一次
     */
    @ApiOperation("执行一次定时任务")
    @PutMapping("/run")
    public AjaxResult run(@RequestBody RunDeviceJobReqDto dto) throws SchedulerException
    {
    	DeviceJob job = new DeviceJob();
    	job.setJobId(dto.getJobId());
    	job.setJobGroup(dto.getJobGroup());
        deviceJobService.run(job);
        return AjaxResult.success();
    }

    /**
     * 删除定时任务
     */
    @ApiOperation("删除定时任务")
    @DeleteMapping("/delete")
    public AjaxResult remove(@RequestParam("jobId") Long jobId) throws SchedulerException, TaskException
    {
    	Long[] jobIds = {jobId};
        deviceJobService.deleteJobByIds(jobIds);
        return AjaxResult.success();
    }
}
