package org.tongji;

import com.typesafe.config.ConfigFactory;
import org.ethereum.config.NoAutoscan;
import org.ethereum.config.SystemProperties;

import org.ethereum.facade.Ethereum;
import org.ethereum.facade.EthereumFactory;
import org.ethereum.facade.P2P;
import org.ethereum.facade.P2PFactory;
import org.ethereum.net.apa.message.RequestMessage;
import org.ethereum.net.apa.message.ResponseMessage;
import org.ethereum.net.apa.message.StatusMessage;
import org.ethereum.net.server.ChannelManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Stack;

import static org.ethereum.crypto.HashUtil.sha3;

/**
 * @author: HuShili
 * @date: 2018/8/2
 * @description: none
 */
@Configuration
@NoAutoscan
public class Node{

    private static final Logger logger = LoggerFactory.getLogger("apa");

    private Config config;

    public static SystemProperties props = new SystemProperties();

    private Ethereum ethereum;

    private P2P p2P;

    private ChannelManager channelManager;

    private Stack<Message> messages = new Stack<>();

    public Node(){
        this.config = new Config();
    }

    public Node(Config config){
        this.config = config;
    }

    /**
     * Start a Ethereum node
     * with UserConfig
     */
    public void start(){

        props = new SystemProperties();

        props.overrideParams(ConfigFactory.parseString(config.toString()));

        // Get eth
        //ethereum = EthereumFactory.createEthereum(props, this.getClass());
        p2P = P2PFactory.createP2P(props, this.getClass());

        // Get CM
        //channelManager = ethereum.getChannelManager();
        channelManager = p2P.getChannelManager();

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
            case RESPONSE:
                channelManager.sendApaMessage(new ResponseMessage(message.getPayload()));
                break;
            default:
                logger.error("Unknown type:" + message.getType());
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
  
    @Bean
    public SystemProperties systemProperties() {
        return props;
    }
}
