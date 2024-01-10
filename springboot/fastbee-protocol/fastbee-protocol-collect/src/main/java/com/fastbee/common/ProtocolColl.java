package com.fastbee.common;

import com.fastbee.protocol.base.protocol.IProtocol;
import lombok.Data;

/**
 * @author bill
 */
@Data
public class ProtocolColl {

    private IProtocol protocol;

    private Long productId;
}
