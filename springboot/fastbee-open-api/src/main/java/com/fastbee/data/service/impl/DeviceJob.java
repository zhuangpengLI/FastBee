package com.fastbee.data.service.impl;

import com.fastbee.common.enums.DeviceStatus;
import com.fastbee.common.utils.DateUtils;
import com.fastbee.iot.service.IDeviceService;
import com.fastbee.iot.service.cache.IDeviceCache;
import com.fastbee.mqtt.manager.MqttRemoteManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author gsb
 * @date 2023/2/20 17:13
 */
@Component
@Slf4j
public class DeviceJob {

    @Resource
    private IDeviceCache deviceCache;
    @Resource
    private IDeviceService deviceService;
    @Resource
    private MqttRemoteManager remoteManager;


    /**
     * 设备定时任务,更新超时设备状态
     */
    public void timingUpdateDeviceStatusStatus(){
        try {
            log.info("=>设备状态定时任务:" + DateUtils.getTimestamp());
            List<String> expiredDevice = deviceCache.removeExpiredDevice();
            deviceService.batchChangeStatus(expiredDevice, DeviceStatus.OFFLINE);
            for (String s : expiredDevice) {
                remoteManager.pushDeviceStatus(-1L,s,DeviceStatus.OFFLINE);
            }
        }catch (Exception e){
            log.warn("=>批量更新设备状态异常",e);
        }
    }
}
