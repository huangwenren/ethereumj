package org.ethereum.net.apa.message;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: HuShili
 * @date: 2018/7/31
 * @description: none
 */
public enum ApaMessageCodes {

    /* Apa Protocol */
    STATUS(0x00),

    REQUEST(0x01),

    RESPONSE(0x02);

    private final int cmd;

    private static final Map<Integer, ApaMessageCodes> intToTypeMap = new HashMap<>();

    static {
        for (ApaMessageCodes type : ApaMessageCodes.values()) {
            intToTypeMap.put(type.cmd, type);
        }
    }

    private ApaMessageCodes(int cmd) {
        this.cmd = cmd;
    }

    public static ApaMessageCodes fromByte(byte i) {
        return intToTypeMap.get((int) i);
    }

    public static boolean inRange(byte code) {
        return code >= STATUS.asByte() && code <= RESPONSE.asByte();
    }

    public byte asByte() {
        return (byte) (cmd);
    }
}
