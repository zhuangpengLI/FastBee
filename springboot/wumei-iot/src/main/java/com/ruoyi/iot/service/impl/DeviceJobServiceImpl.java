package com.ruoyi.iot.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruoyi.common.constant.ScheduleConstants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.exception.job.TaskException;
import com.ruoyi.iot.domain.Device;
import com.ruoyi.iot.domain.DeviceJob;
import com.ruoyi.iot.domain.Scene;
import com.ruoyi.iot.mapper.DeviceJobMapper;
import com.ruoyi.iot.mapper.DeviceMapper;
import com.ruoyi.iot.mapper.SceneMapper;
import com.ruoyi.iot.mobile.model.AddDeviceJobReqDto;
import com.ruoyi.iot.service.IDeviceJobService;
import com.ruoyi.iot.util.quartz.CronUtils;
import com.ruoyi.iot.util.quartz.ScheduleUtils;

/**
 * 定时任务调度信息 服务层
 * 
 * @author kerwincui
 */
@Service
public class DeviceJobServiceImpl implements IDeviceJobService
{
    @Autowired
    private Scheduler scheduler;

    @Autowired
    private DeviceJobMapper deviceJobMapper;
    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private SceneMapper sceneMapper;

    /**
     * 项目启动时，初始化定时器 主要是防止手动修改数据库导致未同步到定时任务处理（注：不能手动修改数据库ID和任务组名，否则会导致脏数据）
     */
    @PostConstruct
    public void init() throws SchedulerException, TaskException
    {
        scheduler.clear();
        List<DeviceJob> jobList = deviceJobMapper.selectJobAll();
        for (DeviceJob deviceJob : jobList)
        {
            ScheduleUtils.createScheduleJob(scheduler, deviceJob);
        }
    }

    /**
     * 获取quartz调度器的计划任务列表
     * 
     * @param job 调度信息
     * @return
     */
    @Override
    public List<DeviceJob> selectJobList(DeviceJob job)
    {
        return deviceJobMapper.selectJobList(job);
    }

    /**
     * 通过调度任务ID查询调度信息
     * 
     * @param jobId 调度任务ID
     * @return 调度任务对象信息
     */
    @Override
    public DeviceJob selectJobById(Long jobId)
    {
        return deviceJobMapper.selectJobById(jobId);
    }

    /**
     * 暂停任务
     * 
     * @param job 调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int pauseJob(DeviceJob job) throws SchedulerException
    {
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        job.setStatus(ScheduleConstants.Status.PAUSE.getValue());
        int rows = deviceJobMapper.updateJob(job);
        if (rows > 0)
        {
            scheduler.pauseJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
        return rows;
    }

    /**
     * 恢复任务
     * 
     * @param job 调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int resumeJob(DeviceJob job) throws SchedulerException
    {
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        job.setStatus(ScheduleConstants.Status.NORMAL.getValue());
        int rows = deviceJobMapper.updateJob(job);
        if (rows > 0)
        {
            scheduler.resumeJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
        return rows;
    }

    /**
     * 删除任务后，所对应的trigger也将被删除
     * 
     * @param job 调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteJob(DeviceJob job) throws SchedulerException
    {
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        int rows = deviceJobMapper.deleteJobById(jobId);
        if (rows > 0)
        {
            scheduler.deleteJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
        return rows;
    }

    /**
     * 批量删除调度信息
     * 
     * @param jobIds 需要删除的任务ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteJobByIds(Long[] jobIds) throws SchedulerException
    {
        for (Long jobId : jobIds)
        {
            DeviceJob job = deviceJobMapper.selectJobById(jobId);
            deleteJob(job);
        }
    }

    /**
     * 根据设备Ids批量删除调度信息
     *
     * @param deviceIds 需要删除数据的设备Ids
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteJobByDeviceIds(Long[] deviceIds) throws SchedulerException
    {
        // 查出所有job
        List<DeviceJob> deviceJobs=deviceJobMapper.selectShortJobListByDeviceIds(deviceIds);

//        // 批量删除job
//        int rows=deviceJobMapper.deleteJobByDeviceIds(deviceIds);
//
//        // 批量删除调度器
//        for(DeviceJob job:deviceJobs){
//            scheduler.deleteJob(ScheduleUtils.getJobKey(job.getJobId(), job.getJobGroup()));
//        }
        
//        调整为调用统一删除job方法
        for(DeviceJob job:deviceJobs) {
        	deleteJob(job);
        }

    }

    /**
     * 任务调度状态修改
     * 
     * @param job 调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int changeStatus(DeviceJob job) throws SchedulerException
    {
        int rows = 0;
        String status = job.getStatus();
        if (ScheduleConstants.Status.NORMAL.getValue().equals(status))
        {
            rows = resumeJob(job);
        }
        else if (ScheduleConstants.Status.PAUSE.getValue().equals(status))
        {
            rows = pauseJob(job);
        }
        return rows;
    }

    /**
     * 立即运行任务
     * 
     * @param job 调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void run(DeviceJob job) throws SchedulerException
    {
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        DeviceJob properties = selectJobById(job.getJobId());
        // 参数
        JobDataMap dataMap = new JobDataMap();
        dataMap.put(ScheduleConstants.TASK_PROPERTIES, properties);
        scheduler.triggerJob(ScheduleUtils.getJobKey(jobId, jobGroup), dataMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult insertDeviceJob(AddDeviceJobReqDto dto) throws SchedulerException, TaskException
    {
    	Long deviceId = dto.getDeviceId();
    	if(deviceId==null) {
    		return AjaxResult.error("设备信息有误");
    	}
    	Integer jobTimeType = dto.getJobTimeType();
    	List<Integer> timeList = Stream.of(1,2,3).collect(Collectors.toList());
    	if(!timeList.contains(jobTimeType)) {
    		return AjaxResult.error("定时时间类型有误");
    	}
    	Device device = deviceMapper.selectDeviceByDeviceId(deviceId);
    	DeviceJob deviceJob = new DeviceJob();
    	BeanUtils.copyProperties(dto, deviceJob);
    	
    	//设备产品信息
    	deviceJob.setDeviceName(device.getDeviceName());
    	deviceJob.setSerialNumber(device.getSerialNumber());
    	deviceJob.setProductId(device.getProductId());
    	deviceJob.setProductName(device.getProductName());
    	
    	//默认job信息
    	deviceJob.setIsAdvance(1);//设置为详细表达式 不需要按类型在页面分开处理
    	deviceJob.setMisfirePolicy(ScheduleConstants.MISFIRE_FIRE_AND_PROCEED);//失败触发一次执行
    	deviceJob.setJobGroup(ScheduleConstants.DEFAULT_JOB_GROUP);//默认分组
    	deviceJob.setStatus(ScheduleConstants.Status.NORMAL.getValue());//任务状态 正常
    	deviceJob.setConcurrent("1");//禁止并发执行
    	
    	//其他job信息
    	deviceJob.setJobType(1);//1 设备定时
//    	
    	int rows = deviceJobMapper.insertJob(deviceJob);
    	if (rows > 0)
    	{
    		ScheduleUtils.createScheduleJob(scheduler, deviceJob);
    	}
    	return AjaxResult.success();
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult insertSceneJob(AddDeviceJobReqDto dto) throws SchedulerException, TaskException
    {
    	Long sceneId = dto.getSceneId();
    	if(sceneId==null) {
    		return AjaxResult.error("场景信息有误");
    	}
    	Scene scene = sceneMapper.selectSceneBySceneId(sceneId);
    	if(scene==null) {
    		return AjaxResult.error("场景不存在");
    	}
    	Integer jobTimeType = dto.getJobTimeType();
    	List<Integer> timeList = Stream.of(1,2,3).collect(Collectors.toList());
    	if(!timeList.contains(jobTimeType)) {
    		return AjaxResult.error("定时时间类型有误");
    	}
//    	Device device = deviceMapper.selectDeviceByDeviceId(deviceId);
    	DeviceJob deviceJob = new DeviceJob();
    	BeanUtils.copyProperties(dto, deviceJob);
    	
    	//设备产品信息
//    	deviceJob.setDeviceName(device.getDeviceName());
//    	deviceJob.setSerialNumber(device.getSerialNumber());
//    	deviceJob.setProductId(device.getProductId());
//    	deviceJob.setProductName(device.getProductName());
    	
    	//循环添加设备动作信息
//    	for()
    	
    	//默认job信息
    	deviceJob.setIsAdvance(1);//设置为详细表达式 不需要按类型在页面分开处理
    	deviceJob.setMisfirePolicy(ScheduleConstants.MISFIRE_FIRE_AND_PROCEED);//失败触发一次执行
    	deviceJob.setJobGroup(ScheduleConstants.DEFAULT_JOB_GROUP);//默认分组
    	deviceJob.setStatus(ScheduleConstants.Status.NORMAL.getValue());//任务状态 正常
    	deviceJob.setConcurrent("1");//禁止并发执行
    	
    	//其他job信息
    	deviceJob.setJobType(3);//3 场景定时
//    	
    	int rows = deviceJobMapper.insertJob(deviceJob);
    	if (rows > 0)
    	{
    		ScheduleUtils.createScheduleJob(scheduler, deviceJob);
    	}
    	return AjaxResult.success();
    }
    /**
     * 新增任务
     * 
     * @param deviceJob 调度信息 调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertJob(DeviceJob deviceJob) throws SchedulerException, TaskException
    {
        int rows = deviceJobMapper.insertJob(deviceJob);
        if (rows > 0)
        {
            ScheduleUtils.createScheduleJob(scheduler, deviceJob);
        }
        return rows;
    }

    /**
     * 更新任务的时间表达式
     * 
     * @param deviceJob 调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateJob(DeviceJob deviceJob) throws SchedulerException, TaskException
    {
        DeviceJob properties = selectJobById(deviceJob.getJobId());
        int rows = deviceJobMapper.updateJob(deviceJob);
        if (rows > 0)
        {
            updateSchedulerJob(deviceJob, properties.getJobGroup());
        }
        return rows;
    }

    /**
     * 更新任务
     * 
     * @param deviceJob 任务对象
     * @param jobGroup 任务组名
     */
    public void updateSchedulerJob(DeviceJob deviceJob, String jobGroup) throws SchedulerException, TaskException
    {
        Long jobId = deviceJob.getJobId();
        // 判断是否存在
        JobKey jobKey = ScheduleUtils.getJobKey(jobId, jobGroup);
        if (scheduler.checkExists(jobKey))
        {
            // 防止创建时存在数据问题 先移除，然后在执行创建操作
            scheduler.deleteJob(jobKey);
        }
        ScheduleUtils.createScheduleJob(scheduler, deviceJob);
    }

    /**
     * 校验cron表达式是否有效
     * 
     * @param cronExpression 表达式
     * @return 结果
     */
    @Override
    public boolean checkCronExpressionIsValid(String cronExpression)
    {
        return CronUtils.isValid(cronExpression);
    }
}
