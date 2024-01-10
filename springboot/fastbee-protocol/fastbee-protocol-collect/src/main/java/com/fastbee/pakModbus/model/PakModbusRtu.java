package com.fastbee.pakModbus.model;

import com.fastbee.common.core.protocol.Message;
import com.fastbee.common.core.protocol.modbus.ModbusCode;
import com.fastbee.protocol.base.annotation.Column;
import com.fastbee.protocol.util.ToStringBuilder;
import com.fastbee.base.session.Session;
import io.netty.buffer.ByteBuf;
import lombok.NoArgsConstructor;

import java.util.Arrays;

/**
 * modbus采集方式二：dtu或模组主动轮询，变化上报，以约定报文格式进行上报
 *
 * @author gsb
 * @date 2022/12/5 16:43
 */
@NoArgsConstructor
public class PakModbusRtu extends Message {

    /**
     * 约定报文格式如下
     * 1.设备主动上报数据组成：
     * * 上报的指令数据
     * * FFAA 0001 010302030578B7 0D
     * * FFAA   0001         010302030578B7   0D
     * * 包头   起始寄存器   数据包           包尾
     * <p>
     * * 数据包
     * * 01        03      02              0305  78B7
     * * 设备地址  命令号  返回数据字节数  数据  CRC校验
     * <p>
     * 2.服务下发数据组成
     * * 下发的指令数据
     * * FFDD 000000000109 01 06 0101 0015 1839 0D
     * * FFDD         000000000109   0106010100151839
     * * 固定报文头   6字节消息ID    数据包
     * <p>
     * * 数据包
     * * 01         06       0101         0015    1839
     * * 设备地址   命令号   寄存器地址   数据位  CRC校验
     * <p>
     * 3.设备应答服务下发数据组成
     * * 下发的指令数据
     * * FFDD 000000000109 01 06 0101 0015 1839 0D
     * * FFDD         000000000109   0106010100151839
     * * 固定报文头   6字节消息ID    数据包
     * <p>
     * * 数据包
     * * 01         06       0101         0015    1839
     * * 设备地址   命令号   寄存器地址   数据位  CRC校验
     */

    @Column(length = 2, desc = "固定报文头: FFDD或FFAA")
    protected int start;
    @Column(length = 2, desc = "寄存器地址")
    protected int address;
    @Column(length = 1,desc = "从机地址")
    protected int slaveId;
    @Column(length = 1,desc = "功能码")
    protected int code;
    @Column(totalUnit = 1,desc = "上报数据",version = 0)
    protected short[] data;
    @Column(length = 2, desc = "下发数据", version = 2)
    protected int writeData;

    /**
     * crc校验
     */
    protected boolean verified = true;

    protected transient Session session;

    protected transient ByteBuf payload;

    protected transient int serialNo;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public int getSlaveId() {
        return slaveId;
    }

    public void setSlaveId(int slaveId) {
        this.slaveId = slaveId;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public short[] getData() {
        return data;
    }

    public void setData(short[] data) {
        this.data = data;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public ByteBuf getPayload() {
        return payload;
    }

    public void setPayload(ByteBuf payload) {
        this.payload = payload;
    }

    public int getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(int serialNo) {
        this.serialNo = serialNo;
    }
    protected StringBuilder toStringHead() {
        final StringBuilder sb = new StringBuilder();
        String des = ModbusCode.getDes(this.code);
        sb.append(des);
        sb.append("[");
        sb.append(",slaveId[从机地址]=").append(slaveId);
        sb.append(",code[功能码]=").append(code);
        sb.append(",length[返回数据个数]").append(data==null? 0: data.length);
        sb.append(",data[上报数据]=").append(Arrays.toString(data));
        // sb.append(",count[读取寄存器个数]=").append(len);
        sb.append(",address[寄存器地址]=").append(address);
        sb.append(",writeData[下发数据]=").append(writeData);
        sb.append("]");
        return sb;
    }

    @Override
    public String toString() {
        return ToStringBuilder.toString(toStringHead(), this, false);
    }
}
