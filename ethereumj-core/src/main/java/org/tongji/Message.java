package org.tongji;

import java.util.Map;
import java.util.UUID;

/**
 * @author: HuShili
 * @date: 2018/8/2
 * @description: none
 */
public class Message {

    private MessageType type;

    private Map payload;

    private Message(){
    }

    public Message(Map payload, MessageType type){
        this.payload = payload;
        this.type = type;
        this.payload.put("uuid", UUID.randomUUID());
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public Map getPayload() {
        return payload;
    }

    public void setPayload(Map payload) {
        this.payload = payload;
    }

    public String getUUID() {
        return (String)this.payload.get("uuid");
    }

    public void addSingle(String key, Object value){
        payload.put(key, value);
    }

    public Object getSingle(String key){
        return payload.get(key);
    }
}
