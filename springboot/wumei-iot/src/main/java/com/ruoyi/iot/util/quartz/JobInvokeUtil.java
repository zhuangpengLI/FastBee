package com.ruoyi.iot.util.quartz;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.iot.domain.DeviceJob;
import com.ruoyi.iot.domain.SysHoliday;
import com.ruoyi.iot.mobile.service.IFamilyDeviceService;
import com.ruoyi.iot.mobile.transferModel.MessageAndResponseTransfer;
import com.ruoyi.iot.model.Action;
import com.ruoyi.iot.model.ThingsModels.IdentityAndName;
import com.ruoyi.iot.mqtt.EmqxService;
import com.ruoyi.iot.service.ISceneService;
import com.ruoyi.iot.service.ISysHolidayService;

/**
 * 任务执行工具
 *
 * @author kerwincui
 */
public class JobInvokeUtil
{
    /**
     * 执行方法
     *
     * @param deviceJob 系统任务
     */
    public static void invokeMethod(DeviceJob deviceJob) throws Exception
    {
    	if(EmqxService.deviceMsgMap.get(EmqxService.MSG_ZNJJ_TYPE)) {
			znjjProcess(deviceJob);
    	}else if(EmqxService.deviceMsgMap.get(EmqxService.MSG_WUMEI_TYPE)) {
    		wumeiProcess(deviceJob);
    	}
        
    }
    
    /**
     * znjj process
     * @param deviceJob
     */
    private static void znjjProcess(DeviceJob deviceJob) {
    	IFamilyDeviceService familyDeviceService=SpringUtils.getBean(IFamilyDeviceService.class);
    	ISysHolidayService sysHolidayService=SpringUtils.getBean(ISysHolidayService.class);
    	MessageAndResponseTransfer transfer=SpringUtils.getBean(MessageAndResponseTransfer.class);
    	ISceneService sceneService=SpringUtils.getBean(ISceneService.class);
    	EmqxService emqxService=SpringUtils.getBean(EmqxService.class);
    	Integer jobTimeType = deviceJob.getJobTimeType();
    	if(jobTimeType==2) {
    		//法定节假日
    		Calendar instance = Calendar.getInstance();
    		instance.set(Calendar.HOUR_OF_DAY, 0);
    		instance.set(Calendar.MINUTE, 0);
    		instance.set(Calendar.SECOND, 0);
    		Date time = instance.getTime();
    		int dayOfWeek = instance.get(Calendar.DAY_OF_WEEK);
    		SysHoliday query1 = new SysHoliday();
    		query1.setSetDate(time);
    		query1.setDateType(1L);
    		List<SysHoliday> list1= sysHolidayService.selectSysHolidayList(query1);
    		SysHoliday query = new SysHoliday();
    		query.setSetDate(time);
    		query.setDateType(2L);
			List<SysHoliday> list = sysHolidayService.selectSysHolidayList(query );
			//非节假日
			if(list1.isEmpty()) {
				//工作日  或者 周一到周五(非周六日)
				if(!list.isEmpty() || (dayOfWeek!=Calendar.SATURDAY && dayOfWeek!=Calendar.SUNDAY)) {
					System.out.println("当天非节假日"+time);
					return;
				}
			}
    	}else if(jobTimeType==3) {
    		//法定工作日
    		Calendar instance = Calendar.getInstance();
    		instance.set(Calendar.HOUR_OF_DAY, 0);
    		instance.set(Calendar.MINUTE, 0);
    		instance.set(Calendar.SECOND, 0);
    		Date time = instance.getTime();
    		int dayOfWeek = instance.get(Calendar.DAY_OF_WEEK);
    		SysHoliday query2 = new SysHoliday();
    		query2.setSetDate(time);
    		query2.setDateType(2L);// 1节假日 2工作日
    		List<SysHoliday> list2 = sysHolidayService.selectSysHolidayList(query2 );
    		SysHoliday query = new SysHoliday();
    		query.setSetDate(time);
    		query.setDateType(1L);
			List<SysHoliday> list = sysHolidayService.selectSysHolidayList(query );
			//非工作日
			if(list2.isEmpty()) {
				//节假日 或者 周六日
				if(!list.isEmpty() || dayOfWeek==Calendar.SATURDAY || dayOfWeek==Calendar.SUNDAY) {
					System.out.println("当天非工作日"+time);
					return;
				}
			}
    	}
    	if(deviceJob.getJobType()==1){
            System.out.println("------------------------执行定时任务-----------------------------");
            List<Action> actions= JSON.parseArray(deviceJob.getActions(),Action.class);
            List<IdentityAndName> functions=new ArrayList<>();
            for(int i=0;i<actions.size();i++){
//                IdentityAndName model=new IdentityAndName();
//                model.setId(actions.get(i).getId());
//                model.setValue(actions.get(i).getValue());
//                if(actions.get(i).getType()==2){
//                    functions.add(model);
//                }
            	familyDeviceService.operDeviceFunNoPerm(deviceJob.getDeviceId(), null, transfer.getRandomAuInfo(), 
            			actions.get(i).getId(), actions.get(i).getValue(), 9999L);
            }
            // 没有发布属性 只有执行功能
            // 发布功能
//            if(functions.size()>0){
//                emqxService.publishFunction(deviceJob.getProductId(),deviceJob.getSerialNumber(),functions);
//            }
            System.out.println("执行 znjj  设备定时任务------");

        }else if(deviceJob.getJobType()==2){
            // 告警

        	System.out.println("执行 znjj  告警定时任务------");
            System.out.println("------------------------执行告警-----------------------------");
        }else if(deviceJob.getJobType()==3){
            // 场景联动
        	sceneService.runSceneBySceneId(deviceJob.getSceneId(),2,null,deviceJob.getJobId(),deviceJob.getJobName());
        	System.out.println("执行 znjj  场景定时任务------");
            System.out.println("------------------------执行场景联动-----------------------------");
        }
    }
    
    public static void main(String[] args) {
    	//法定工作日
		Calendar instance = Calendar.getInstance();
		instance.setTime(new Date(2023-1900,1,10));
		instance.set(Calendar.HOUR_OF_DAY, 0);
		instance.set(Calendar.MINUTE, 0);
		instance.set(Calendar.SECOND, 0);
		Date time = instance.getTime();
		int dayOfWeek = instance.get(Calendar.DAY_OF_WEEK);
		SysHoliday query1 = new SysHoliday();
		query1.setSetDate(time);
		query1.setDateType(1L);
		List<SysHoliday> list1= null;
//		List<SysHoliday> list1= new ArrayList();
		SysHoliday query = new SysHoliday();
		query.setSetDate(time);
		query.setDateType(2L);
//		List<SysHoliday> list = null;
		List<SysHoliday> list = new ArrayList();
		//非节假日
		if(list1==null) {
			//工作日  或者 周一到周五(非周六日)
			if(list!=null || (dayOfWeek!=Calendar.SATURDAY && dayOfWeek!=Calendar.SUNDAY)) {
				System.out.println("当天非节假日"+time);
				return;
			}
		}
		System.out.println("执行");
	}
    
    /**
     * wumei process
     * @param deviceJob
     */
    private static void wumeiProcess(DeviceJob deviceJob) {
    	if(deviceJob.getJobType()==1){
            System.out.println("------------------------执行定时任务-----------------------------");
            List<Action> actions= JSON.parseArray(deviceJob.getActions(),Action.class);
            List<IdentityAndName> propertys=new ArrayList<>();
            List<IdentityAndName> functions=new ArrayList<>();
            for(int i=0;i<actions.size();i++){
                IdentityAndName model=new IdentityAndName();
                model.setId(actions.get(i).getId());
                model.setValue(actions.get(i).getValue());
                if(actions.get(i).getType()==1){
                    propertys.add(model);
                }else if(actions.get(i).getType()==2){
                    functions.add(model);
                }
            }
            EmqxService emqxService=SpringUtils.getBean(EmqxService.class);
            // 发布属性
            if(propertys.size()>0){
                emqxService.publishProperty(deviceJob.getProductId(),deviceJob.getSerialNumber(),propertys);
            }
            // 发布功能
            if(functions.size()>0){
                emqxService.publishFunction(deviceJob.getProductId(),deviceJob.getSerialNumber(),functions);
            }

        }else if(deviceJob.getJobType()==2){
            // 告警

            System.out.println("------------------------执行告警-----------------------------");
        }else if(deviceJob.getJobType()==3){
            // 场景联动

            System.out.println("------------------------执行场景联动-----------------------------");
        }
    }


}
