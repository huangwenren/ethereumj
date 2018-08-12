package org.tongji;

import com.typesafe.config.ConfigFactory;
import org.ethereum.config.NoAutoscan;
import org.ethereum.config.SystemProperties;

import org.ethereum.core.Apa;
import org.ethereum.facade.Ethereum;
import org.ethereum.facade.P2P;
import org.ethereum.facade.P2PFactory;
import org.ethereum.listener.EthereumListenerAdapter;
import org.ethereum.net.apa.message.RequestMessage;
import org.ethereum.net.apa.message.ResponseMessage;
import org.ethereum.net.apa.message.StatusMessage;
import org.ethereum.net.server.Channel;
import org.ethereum.net.server.ChannelManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

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

    //private Queue<Message> messages = new LinkedList<>();

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
                //channelManager.sendApaMessage(new StatusMessage(new byte[]{}));
                break;
            case REQUEST:
                channelManager.sendApaMessage(new RequestMessage(message.getPayload()), (UUID)message.getPayload().get("uuid"));
                break;
            case RESPONSE:
                channelManager.sendApaMessage(new ResponseMessage(message.getPayload()), (UUID)message.getPayload().get("uuid"));
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

        List<Apa> apas = channelManager.getApaSyncManager().getApaStore().getAllApa();

        for (Apa apa : apas) {
            switch (apa.getMessage().getCommand()){
                case STATUS:
                    break;
                case REQUEST:
                    messages.add(new Message(((RequestMessage)apa.getMessage()).getMessages(), MessageType.REQUEST));
                    break;
                case RESPONSE:
                    messages.add(new Message(((ResponseMessage)apa.getMessage()).getMessages(), MessageType.RESPONSE));
                    break;
                default:
                    logger.error("Unknown message type: " + apa.getMessage().getCommand());
            }
        }

        return messages;
    }
  
    @Bean
    public SystemProperties systemProperties() {
        return props;
    }
}
