package org.tongji;

import org.ethereum.crypto.ECKey;
import org.ethereum.facade.Ethereum;
import org.ethereum.facade.EthereumFactory;
import org.ethereum.net.apa.message.RequestMessage;
import org.ethereum.net.apa.message.StatusMessage;
import org.ethereum.net.server.ChannelManager;
import org.ethereum.samples.BasicSample;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Stack;

import static org.ethereum.crypto.HashUtil.sha3;

/**
 * @author: HuShili
 * @date: 2018/8/2
 * @description: none
 */
public class Node extends BasicSample{

    /**
     * Use that sender key to sign transactions
     */
    protected final byte[] senderPrivateKey = sha3("cow".getBytes());
    // sender address is derived from the private key
    protected final byte[] senderAddress = ECKey.fromPrivate(senderPrivateKey).getAddress();

    private Config config;

    private Ethereum ethereum;

    private ChannelManager channelManager;

    private Stack<Message> messages = new Stack<>();

    public Node(){
        super();
    }

    public Node(Config config){
        this.config = config;
    }

    /**
     * Start a Ethereum node
     * with UserConfig
     */
    public void start(){

        // Get eth
        ethereum = EthereumFactory.createEthereum(this.getClass());

        // Get CM
        channelManager = ethereum.getChannelManager();

        // Set the cache
        channelManager.setApaStack(messages);
    }

    public void stop(){

        channelManager.close();

        ethereum.close();
    }

    /**
     * Broadcast a message to all the active peers
     * @param message  message to be sent
     */
    public void broadcastMessage(Message message){

        switch (message.getType()){
            case STATUS:
                channelManager.sendApaMessage(new StatusMessage(new byte[]{}));
                break;
            case REQUEST:
                channelManager.sendApaMessage(new RequestMessage(message.getPayload()));
                break;
            default:
                System.out.println("Unknown type:" + message.getType());
        }
    }

    /**
     * Output the messages which are cached in the cm
     * @return messages
     */
    public ArrayList<Message> receiveMessage(){
        ArrayList<Message> messages = new ArrayList<>();

        while(!this.messages.empty()){
            messages.add(this.messages.pop());
        }

        return messages;
    }

    @Override
    public void onSyncDone() throws Exception {
        super.onSyncDone();
    }

    @Bean
    public Node Node() {
        return new Node();
    }
}
