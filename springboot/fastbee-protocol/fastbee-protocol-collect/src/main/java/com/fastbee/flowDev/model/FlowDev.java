package com.fastbee.flowDev.model;

import com.fastbee.common.core.protocol.Message;
import com.fastbee.protocol.base.annotation.Column;
import com.fastbee.protocol.util.ToStringBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author gsb
 * @date 2023/5/17 15:02
 */
@NoArgsConstructor
public class FlowDev extends Message {

    @Column(length = 1,version = {0,1},desc = "起始地址")
    protected int start = 0x68;
    @Column(length = 1,version = {0,1},desc = "长度")
    protected int length;
    @Column(length = 1,version = {0,1} ,desc = "起始位")
    protected int start1 =0x68;
    @Column(length = 1,version = {0,1},desc = "方向")
    protected int dire;
    @Column(length = 5,version = {0,1},desc = "设备编号")
    protected String imei;
    @Column(length = 1,version = {0,1},desc = "功能码")
    protected String code = "C1";
    @Column(length = 5,version = 0,desc = "瞬时流量")
    protected String instant;
    @Column(length = 5,version = 0,desc = "累积流量")
    protected String sum;
    @Column(length = 4,version = 0,desc = "不解析值")
    protected int no;
    @Column(length = 6,version = 0,desc = "数据时间")
    protected String ts;
    @Column(length = 1,version = {1},desc = "无意义值")
    protected int oo = 0;
    @Column(length = 1,version = {0,1},desc = "CRC")
    protected int crc;
    @Column(length = 1,version = {0,1},desc = "结束地址")
    protected int end = 0x16;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getStart1() {
        return start1;
    }

    public void setStart1(int start1) {
        this.start1 = start1;
    }

    public int getDire() {
        return dire;
    }

    public void setDire(int dire) {
        this.dire = dire;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInstant() {
        return instant;
    }

    public void setInstant(String instant) {
        this.instant = instant;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public int getCrc() {
        return crc;
    }

    public void setCrc(int crc) {
        this.crc = crc;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getOo() {
        return oo;
    }

    public void setOo(int oo) {
        this.oo = oo;
    }

    protected StringBuilder toStringHead() {
        final StringBuilder sb = new StringBuilder();
        sb.append(";[长度]:  ").append(length);
        sb.append(";[方向]:  ").append(dire);
        sb.append(";[设备编号]:  ").append(imei);
        sb.append(";[功能码]:  ").append(code);
        sb.append(";[瞬时流量]:  ").append(instant);
        sb.append(";[累积流量]:  ").append(sum);
        sb.append(";[数据时间]:  ").append(ts);
        return sb;
    }

    @Override
    public String toString() {
        return ToStringBuilder.toString(toStringHead(), this, true);
    }
}
