package com.fastbee.modbus.model;

import com.fastbee.protocol.base.annotation.Column;
import com.fastbee.common.core.protocol.modbus.ModbusCode;
import com.fastbee.protocol.util.ToStringBuilder;
import com.fastbee.common.core.protocol.Message;
import com.fastbee.base.session.Session;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import lombok.NoArgsConstructor;

import java.beans.Transient;
import java.util.Arrays;

/**
 * modbus采集方式一: 云端轮询方式，报文为标准ModbusRtu
 *
 * @author bill
 */
@NoArgsConstructor
public class ModbusRtu extends Message {
    /**
     * version来决定数据
     * 0: 读寄存器03/04(批量)上报  {@link ModbusCode}
     * 例如:    |从机地址|功能码|返回字节个数|寄存器40005数据|寄存器40006数据|CRC校验|
     * |01|03|04|00 00|00 00|21 33|
     * <p>
     * 1. 批量读寄存器      {@link ModbusCode}
     * 例如:     |从机地址|功能码|寄存器起始地址|读取寄存器个数|CRC校验|
     * |01|03|00 04|00 02|85 ca|
     * <p>
     * 2. 单个写保持寄存器 0x06   {@link ModbusCode}
     *    写单个线圈 0x05 {@link ModbusCode}
     * 例如：
     * |0x01|06|00 01|00 17|98 04|
     * |0x01|05|00 01|00 1|98 04|
     * <p>
     * 3. 写多个保持寄存器   {@link ModbusCode} 暂不适配
     * <p>
     * 4  读线圈 01/02
     * <p>
     */
    @Column(length = 1, version = {0x80, 0x81}, desc = "起始地址")
    protected int start;
    @Column(length = 1, version = {0x80, 0x81}, desc = "标识位,实例设备是0x80")
    private int mId;
    @Column(length = 6, version = 0x80, desc = "MAC地址,6个字节")
    private String mac;
    @Column(length = 1, version = {0x80, 0x81}, desc = "结尾,如：7E")
    private int end;
    @Column(length = 1, version = {0, 1, 2, 4}, desc = "从机地址")
    protected int slaveId;
    @Column(length = 1, version = {0, 1, 2, 4}, desc = "功能码")
    protected int code;
    @Column(length = 1, version = 4, desc = "返回字节数")
    protected int bitCount;
    @Column(length = 1, version = 4, desc = "bit数据")
    protected int bitData;
    @Column(length = 2, desc = "寄存器地址", version = {1, 2})
    protected int address;
    @Column(length = 2, desc = "读取寄存器个数", version = 1)
    protected int count;
    @Column(totalUnit = 1, desc = "上报数据", version = 0)
    protected short[] data;
    @Column(length = 2, desc = "下发数据", version = 2)
    protected int writeData;

    /**
     * crc校验
     */
    protected boolean verified = true;

    protected transient Session session;

    protected transient ByteBuf payload;

    protected transient String hex;

    protected transient int serialNo;

    protected transient boolean enableTest;

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    public boolean isEnableTest() {
        return enableTest;
    }

    public void setEnableTest(boolean enableTest) {
        this.enableTest = enableTest;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getWriteData() {
        return writeData;
    }

    public void setWriteData(int writeData) {
        this.writeData = writeData;
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

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public int getBitCount() {
        return bitCount;
    }

    public void setBitCount(int bitCount) {
        this.bitCount = bitCount;
    }

    public int getBitData() {
        return bitData;
    }

    public void setBitData(int bitData) {
        this.bitData = bitData;
    }

    @Transient
    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public void setPayload(ByteBuf payload) {
        this.hex = ByteBufUtil.hexDump(payload);
        this.payload = payload;
    }

    public void setSerialNo(int serialNo) {
        this.serialNo = serialNo;
    }

    protected StringBuilder toStringHead() {
        final StringBuilder sb = new StringBuilder();
        String des = ModbusCode.getDes(this.code);
        sb.append(des).append(": ");
        sb.append(";[从机地址]:  ").append(slaveId);
        sb.append(";[功能码]:  ").append(code);
        sb.append(";[返回数据个数]:  ").append(data == null ? 0 : data.length);
        sb.append(";[上报数据]:  ").append(Arrays.toString(data));
        sb.append(";[读取寄存器个数]:  ").append(count);
        sb.append(";[寄存器地址]:  ").append(address);
        return sb;
    }

    @Override
    public String toString() {
        return ToStringBuilder.toString(toStringHead(), this, true);
    }
}
