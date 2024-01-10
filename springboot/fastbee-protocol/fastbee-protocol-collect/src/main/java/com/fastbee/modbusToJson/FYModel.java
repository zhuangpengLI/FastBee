package com.fastbee.modbusToJson;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author gsb
 * @date 2023/8/14 17:24
 */
@Data
@Accessors(chain = true)
public class FYModel {

    /**
     * 采集点标识符
     */
    private String name;
    /**
     * 数量
     */
    private Integer quality;
    /**
     * 数值
     */
    private String value;

}
