package com.fastbee.jsonPak.pak;

import com.alibaba.fastjson2.JSON;
import com.fastbee.base.core.annotation.Node;
import com.fastbee.base.core.annotation.PakMapping;
import com.fastbee.base.session.Session;
import com.fastbee.common.core.mq.DeviceReport;
import com.fastbee.common.core.protocol.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author gsb
 * @date 2023/5/4 15:09
 */
@Node
@Component
@Slf4j
public class JsonEndPoint {


    @PakMapping(types = 0)
    public void register(DeviceReport message, Session session){
    // 注册设备
        session.register(message);
        //String callback = "{\"pak_ty\":\"set_inf\",\"cj_s\":null,\"up_s\":3600,\"xt_s\":3600,\"x_yz\":500,\"y_yz\":500,\"z_yz\":500,\"nian\":2022,\"yue\":3,\"ri\":25,\"shi\":12,\"fen\":23,\"miao\":33}";
        //message.setBody(callback);
        //return message;
    }
}
