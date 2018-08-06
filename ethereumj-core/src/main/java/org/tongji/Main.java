package org.tongji;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.tongji.MessageType.REQUEST;
import static org.tongji.MessageType.RESPONSE;
import static org.tongji.MessageType.STATUS;

/**
 * @author: HuShili
 * @date: 2018/8/3
 * @description: none
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {

        // Config protocols
        ArrayList<String> protocols = new ArrayList<>();
        protocols.add("eth");
        protocols.add("apa");

        // Other with default
        Map params1 = new HashMap();
        params1.put("peer.capabilities", protocols);
        params1.put("peer.listen.port", 30334);
        params1.put("peer.privateKey", "3ec771c31cac8c0dba77a69e503765701d3c2bb62435888d4ffa38fed60c445c");
        params1.put("database.dir", "testDB-1");

        Map params2 = new HashMap();
        params2.put("peer.capabilities", protocols);
        params2.put("peer.listen.port", 30335);
        params2.put("peer.privateKey", "6ef8da380c27cea8fdf7448340ea99e8e2268fc2950d79ed47cbf6f85dc977ec");
        params2.put("peer.active", "[{ url = \"enode://3973cb86d7bef9c96e5d589601d788370f9e24670dcba0480c0b3b1b0647d13d0f0fffed115dd2d4b5ca1929287839dcd4e77bdc724302b44ae48622a8766ee6@localhost:30334\" }]");
        params2.put("database.dir", "testDB-2");

        Map params3 = new HashMap();
        params3.put("peer.capabilities", protocols);
        params3.put("peer.listen.port", 30336);
        params3.put("peer.privateKey", "f67c4032a7ff79bbfa7a780331b235c4eb681d51a0704cb1562064fb6c4bced4");
        params3.put("peer.active", "[{ url = \"enode://3973cb86d7bef9c96e5d589601d788370f9e24670dcba0480c0b3b1b0647d13d0f0fffed115dd2d4b5ca1929287839dcd4e77bdc724302b44ae48622a8766ee6@localhost:30334\" }]");
        params3.put("database.dir", "testDB-3");

        UserConfig config1 = new UserConfig(params1);
        UserConfig config2 = new UserConfig(params2);
        UserConfig config3 = new UserConfig(params3);

        // Start a node
        Node node1 = new Node(config1);
        node1.start();

        Node node2 = new Node(config2);
        node2.start();

        Node node3 = new Node(config3);
        node3.start();

        System.out.println("======= Waiting for connecting...");
        Thread.sleep(20000);
        System.out.println("======= Sending test message...");
        // Broadcast a message
        Map payload = new HashMap();
        payload.put("text", "hello");
        Message msg = new Message(payload, REQUEST);

        node1.broadcastMessage(msg);

        Thread.sleep(10000);
        // Receive the cached messages
        ArrayList<Message> messages = node2.receiveMessage();

        for (Message message : messages) {
            System.out.println("=====" + message.getType() + " " + message.getPayload().get("text") + "=====");
        }

        // Broadcast a message
        payload = new HashMap();
        payload.put("text", "hello");
        msg = new Message(payload, RESPONSE);

        node2.broadcastMessage(msg);

        Thread.sleep(10000);
        // Receive the cached messages
        messages = node1.receiveMessage();

        for (Message message : messages) {
            System.out.println(message.getType() + " " + message.getPayload().get("text"));
        }

        // Stop the node
        //node1.stop();
        //node2.stop();
    }

}
