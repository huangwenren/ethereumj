package org.tongji;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.tongji.MessageType.STATUS;

/**
 * @author: HuShili
 * @date: 2018/8/3
 * @description: none
 */
public class Main {

    public static void main(String[] args){

        // Config protocols
        ArrayList<String> protocols = new ArrayList<>();
        protocols.add("eth");
        protocols.add("shh");
        protocols.add("apa");

        // Other with default
        Map params = new HashMap();
        params.put("peer.capabilities", protocols);

        UserConfig config = new UserConfig(params);
        // Start a node
        Node node = new Node(config);
        node.start();

        // Broadcast a message
        Map payload = new HashMap();
        payload.put("text", "hello");
        Message msg = new Message(payload, STATUS);

        node.broadcastMessage(msg);

        // Receive the cached messages
        ArrayList<Message> messages = node.receiveMessage();

        System.out.println(messages);

        // Stop the node
        node.stop();
    }

}
