package com.ruoyi.iot.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.iot.domain.DeviceJob;
import com.ruoyi.iot.domain.Scene;
import com.ruoyi.iot.domain.SceneDevice;
import com.ruoyi.iot.domain.SceneRecord;
import com.ruoyi.iot.mapper.DeviceJobMapper;
import com.ruoyi.iot.mapper.DeviceMapper;
import com.ruoyi.iot.mapper.SceneMapper;
import com.ruoyi.iot.mobile.constant.IotConstant;
import com.ruoyi.iot.mobile.constant.IotOperMsgConstant;
import com.ruoyi.iot.mobile.service.IFamilyDeviceService;
import com.ruoyi.iot.mobile.transferModel.MessageAndResponseTransfer;
import com.ruoyi.iot.model.Action;
import com.ruoyi.iot.mqtt.EmqxClient;
import com.ruoyi.iot.service.IDeviceJobService;
import com.ruoyi.iot.service.IFamilyService;
import com.ruoyi.iot.service.ISceneRecordService;
import com.ruoyi.iot.service.ISceneService;

/**
 * 场景联动Service业务层处理
 * 
 * @author kerwincui
 * @date 2022-09-21
 */
@Service
public class SceneServiceImpl implements ISceneService 
{
    @Autowired
    private SceneMapper sceneMapper;
    
    
    @Autowired
    private DeviceJobMapper deviceJobMapper;
    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private IDeviceJobService deviceJobService;
    @Autowired
    private MessageAndResponseTransfer transfer;
    @Autowired
    private IFamilyService familyService;
    @Autowired
    private IFamilyDeviceService familyDeviceService;
    @Autowired
    private ISceneRecordService sceneRecordService;
    @Autowired
    private EmqxClient emqxClient;


    /**
     * 查询场景联动
     * 
     * @param sceneId 场景联动主键
     * @return 场景联动
     */
    @Override
    public Scene selectSceneBySceneId(Long sceneId)
    {
    	Scene scene = sceneMapper.selectOnlySceneBySceneId(sceneId);
    	scene.setSceneDeviceList(sceneMapper.selectDeviceBySceneId(sceneId));
        return scene;
    }
    @Override
    public void runSceneBySceneId(Long sceneId,Integer runType,Long userId,Long jobId,String jobName)
    {
    	Scene scene = sceneMapper.selectSceneBySceneId(sceneId);
    	boolean isAlbeRun = true;
    	if(runType.intValue()==1) {
    		if(scene.getEnabled().intValue()!=1) {
    			isAlbeRun = false;
    		}
    	}else if(runType.intValue()==2) {
    		if(scene.getEnabled().intValue()!=1 || scene.getJobEnabled().intValue()!=1 ) {
    			isAlbeRun = false;
    		}
    	}
    	if(!isAlbeRun) {
    		System.out.println("不允许执行");
    		return;
    	}
    	List<SceneDevice> devices = sceneMapper.selectDeviceBySceneId(sceneId);
    	int init = 1;
    	for(SceneDevice devi:devices) {
    		List<Action> actions= JSON.parseArray(devi.getActions(),Action.class);
    		for(int i=0;i<actions.size();i++){
    			if(init++ != 1) {
    				try {
    					//延迟500ms执行下一次操作
						Thread.sleep(500L);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    			}
    			familyDeviceService.operDeviceFunNoPerm(devi.getDeviceId(), null, transfer.getRandomAuInfo(), 
          			actions.get(i).getId(), actions.get(i).getValue(), 9999L);
          }
    	}
    	SceneRecord record = new SceneRecord();
    	record.setSceneId(sceneId);
    	record.setRunType(runType);
    	record.setUserId(userId);
    	record.setJobId(jobId);
    	record.setJobName(jobName);
    	record.setDeviceCount(devices.size());
    	record.setSucDeviceCount(devices.size());
		sceneRecordService.insertSceneRecord(record );
    }
    @Override
    public List<SceneDevice>  selectDeviceBySceneId(Long sceneId)
    {
    	return sceneMapper.selectDeviceBySceneId(sceneId);
    }

    /**
     * 查询场景联动列表
     * 
     * @param scene 场景联动
     * @return 场景联动
     */
    @Override
    public List<Scene> selectSceneList(Scene scene)
    {
        return sceneMapper.selectSceneList(scene);
    }

    /**
     * 新增场景联动
     * 
     * @param scene 场景联动
     * @return 结果
     */
    @Transactional
    @Override
    public int insertScene(Scene scene)
    {
        scene.setCreateTime(DateUtils.getNowDate());
        int rows = sceneMapper.insertScene(scene);
        insertSceneDevice(scene);
        return rows;
    }

    /**
     * 修改场景联动
     * 
     * @param scene 场景联动
     * @return 结果
     */
    @Transactional
    @Override
    public int updateScene(Scene scene)
    {
        scene.setUpdateTime(DateUtils.getNowDate());
        sceneMapper.deleteSceneDeviceBySceneId(scene.getSceneId());
        insertSceneDevice(scene);
        return sceneMapper.updateScene(scene);
    }
    @Transactional
    @Override
    public int updateSceneOnly(Scene scene)
    {
    	scene.setUpdateTime(DateUtils.getNowDate());
    	return sceneMapper.updateScene(scene);
    }

    /**
     * 批量删除场景联动
     * 
     * @param sceneIds 需要删除的场景联动主键
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteSceneBySceneIds(Long[] sceneIds) throws SchedulerException
    {
//        sceneMapper.deleteSceneDeviceBySceneIds(sceneIds);
//        int delete = sceneMapper.deleteSceneBySceneIds(sceneIds);
        //因为不是简单删除 所有修改为调用单个删除
    	int delete = 0;
    	for(Long sceneId:sceneIds) {
    		int i = deleteSceneBySceneId(sceneId);
    		delete = delete+i;
    	}
        return delete;
    }
    @Override
    public int countDeviceBySceneId(Long sceneId) {
    	return sceneMapper.countDeviceBySceneId(sceneId);
    }
    /**
     * 删除场景联动信息
     * 
     * @param sceneId 场景联动主键
     * @return 结果
     * @throws SchedulerException 
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteSceneBySceneId(Long sceneId) throws SchedulerException
    {
        sceneMapper.deleteSceneDeviceBySceneId(sceneId);
        int i = sceneMapper.deleteSceneBySceneId(sceneId);
        DeviceJob query = new DeviceJob();
    	query.setJobType(3);
    	query.setSceneId(sceneId);
    	List<DeviceJob> jobs = deviceJobMapper.selectJobList(query );
    	for(DeviceJob job:jobs) {
    		deviceJobService.deleteJob(job);
    	}
        return i;
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteSceneByFamilyId(Long familyId) throws SchedulerException
    {
    	
    	Scene scene = new Scene();
    	scene.setFamilyId(familyId);
		List<Scene> list = sceneMapper.selectSceneList(scene );
		int delete = 0;
		for(Scene s:list) {
			int i = deleteSceneBySceneId(s.getSceneId());
			delete = delete+i;
		}
    	return delete;
    }

    /**
     * 新增场景设备信息
     * 
     * @param scene 场景联动对象
     */
    public void insertSceneDevice(Scene scene)
    {
        List<SceneDevice> sceneDeviceList = scene.getSceneDeviceList();
        Long sceneId = scene.getSceneId();
        Scene scene2 = sceneMapper.selectOnlySceneBySceneId(sceneId);
        JSONArray jsonArray = new JSONArray();
        if (StringUtils.isNotNull(sceneDeviceList))
        {
            List<SceneDevice> list = new ArrayList<SceneDevice>();
            for (SceneDevice sceneDevice : sceneDeviceList)
            {
            	
                sceneDevice.setSceneId(sceneId);
                list.add(sceneDevice);
                
                String sn = sceneDevice.getSerialNumber();
                String deviceModelType = sn.substring(0, 4);
                List<Action> actions= JSON.parseArray(sceneDevice.getActions(),Action.class);
        		for(int i=0;i<actions.size();i++){
        			if(Stream.of("2001","2002").anyMatch(p->deviceModelType.equals(p))) {
        				//暂时只支持onOff
            			int pow = Integer.valueOf(actions.get(i).getValue());
            			JSONObject jsonObject = new JSONObject();
            			jsonObject.put("pow", pow);
            			jsonObject.put("sn", sn);
            			jsonArray.add(jsonObject);
            		}else if(Stream.of("1001","1002","1003","1004","1005","1011","1012","1013","1014","1015").anyMatch(p->deviceModelType.equals(p))) {
            			//暂时只支持onOff
            			String value = actions.get(i).getValue();
        				String[] split = value.split(",",-1);
        				JSONObject jsonObject = new JSONObject();
        				jsonObject.put("sn", sn);
        				for(int ii=0;ii<split.length;ii++) {
        					String s = split[ii];
        					if(StringUtils.isBlank(s)) {
        						//当前为空 ,则当前按键不操作
        					}else {
        						int ope = Integer.valueOf(s);
        						jsonObject.put("s"+(ii+1), ope);
        					}
        				}
        				jsonArray.add(jsonObject);
        			}else if(Stream.of("3001","3002","3011","3012").anyMatch(p->deviceModelType.equals(p))) {
        				//暂时只支持clctrclctr
        				String value = actions.get(i).getValue();
        				String[] split = value.split(",",-1);
        				JSONObject jsonObject = new JSONObject();
        				jsonObject.put("sn", sn);
        				for(int ii=0;ii<split.length;ii++) {
        					String s = split[i];
        					if(StringUtils.isBlank(s)) {
        						//当前为空 ,则当前按键不操作
        					}else {
        						int ope = Integer.valueOf(s);
        						jsonObject.put("ac"+(ii+1), ope);
        					}
        				}
        				jsonArray.add(jsonObject);
        			}
//        			familyDeviceService.operDeviceFunNoPerm(sceneDevice.getDeviceId(), null, transfer.getRandomAuInfo(), 
//              			actions.get(i).getId(), actions.get(i).getValue(), 9999L);
              }
            }
            if (list.size() > 0)
            {
                sceneMapper.batchSceneDevice(list);
            }
        }
        if(scene2.getIsSys().intValue()==1) {
        	//系统内置场景 需要同步到网关
        	
        	String gatewaySn = familyService.selectFamilyOnlyByFamilyId(scene2.getFamilyId()).getGatewaySn();
        	if(StringUtils.isBlank(gatewaySn)) {
        		throw new RuntimeException("未绑定网关,无法添加设备");
        	}
        	
        	//发布一个模拟app  获取所有设备请求 GetDevsRT 挂起请求
        	String topic = IotConstant.ZNJJ_APP_PREFIX +gatewaySn;
        	JSONObject pushMessage = new JSONObject();
        	pushMessage.put("au", transfer.getAuInfo());
        	pushMessage.put("ta", null);
        	pushMessage.put("cjId", scene2.getSysSceneId());
        	pushMessage.put("cjs", jsonArray);
        	pushMessage.put("fun",IotOperMsgConstant.APP_REQ_GW_FUN_SETCJTABLE);
        	pushMessage.put("cjTimeStap", new Date().getTime()/1000);
        	emqxClient.publish(1, false, topic, pushMessage.toString());
        	transfer.createOperFunReq(gatewaySn, IotOperMsgConstant.APP_REQ_GW_FUN_SETCJTABLE);
        	Boolean operFunResp = transfer.getOperFunResp(gatewaySn, IotOperMsgConstant.APP_REQ_GW_FUN_SETCJTABLE);
        	if(!operFunResp) {
        		throw new RuntimeException("场景修改失败");
        	}
        }
    }
}
