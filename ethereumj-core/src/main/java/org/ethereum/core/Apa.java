package org.ethereum.core;

import org.ethereum.net.apa.message.ApaMessage;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * @author: HuShili
 * @date: 2018/8/10
 * @description: class to represent apa message in the core part
 */
public class Apa {

    private ApaMessage message;

    private UUID uuid;
    private boolean newApa;
    private byte[] nodeId;

    private static ByteBuffer buffer = ByteBuffer.allocate(8);

    public Apa(ApaMessage message, UUID uuid, byte[] nodeId) {
        this(message, false, uuid, nodeId);
    }

    public Apa(ApaMessage message, boolean newApa, UUID uuid, byte[] nodeId) {
        this.message = message;
        this.newApa = newApa;
        this.uuid = uuid;
        this.nodeId = nodeId;
    }

    public ApaMessage getMessage() {
        return message;
    }

    public byte[] getNodeId() {
        return nodeId;
    }

    public boolean isNewApa() {
        return newApa;
    }

    public UUID getUUID() {
        return uuid;
    }

    public int getHash(){
        return uuid.hashCode();
    }
}
