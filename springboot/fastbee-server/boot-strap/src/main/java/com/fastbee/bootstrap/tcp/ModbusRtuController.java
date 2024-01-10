package com.fastbee.bootstrap.tcp;

import com.fastbee.common.core.controller.BaseController;
import com.fastbee.common.core.domain.AjaxResult;
import com.fastbee.modbus.model.ModbusRtu;
import com.fastbee.mq.service.impl.MessageManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author gsb
 * @date 2022/11/22 10:28
 */
@RestController
@RequestMapping("/modbus")
@Api(tags = "ModbusRtu指令下发测试")
public class ModbusRtuController extends BaseController {

    @Autowired
    private MessageManager messageManager;

    @ApiOperation("下发读指令测试")
    @GetMapping("/read03")
    public Mono<AjaxResult> sendRead03(@RequestParam(name = "设备编号",required = false)String clientId,
                                       @RequestParam(name = "功能码",required = false)Integer code,
                                       @RequestParam(name = "从机地址",required = false) Integer slaveId,
                                       @RequestParam(name = "读取数据个数",required = true) Integer count,
                                       @RequestParam(name = "寄存器地址",required = true) Integer address){
       slaveId = slaveId != null ? slaveId : 1;
       code = code != null ? code : 3;
        ModbusRtu request = new ModbusRtu();
        request.setCode(code);
        request.setCount(count);
        request.setSlaveId(slaveId);
        request.setAddress(address);
        return messageManager.requestR(clientId, request, ModbusRtu.class);
    }




}
