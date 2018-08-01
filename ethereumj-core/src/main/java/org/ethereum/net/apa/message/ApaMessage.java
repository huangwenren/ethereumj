package org.ethereum.net.apa.message;

import org.ethereum.net.message.Message;

/**
 * @author: HuShili
 * @date: 2018/7/31
 * @description: none
 */
public abstract class ApaMessage extends Message{
    public ApaMessage() {
    }

    public ApaMessage(byte[] encoded) {
        super(encoded);
    }

    public ApaMessageCodes getCommand(){
        return ApaMessageCodes.fromByte(code);
    }
}
