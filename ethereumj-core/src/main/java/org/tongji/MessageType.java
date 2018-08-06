package org.tongji;

/**
 * @author: HuShili
 * @date: 2018/8/2
 * @description: none
 */
public enum MessageType {

    STATUS(0x00),

    REQUEST(0x01),

    RESPONSE(0x02);

    private int cmd;
    private MessageType(int cmd) {
        this.cmd = cmd;
    }
}
