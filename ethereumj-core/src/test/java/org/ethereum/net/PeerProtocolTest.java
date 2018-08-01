package org.ethereum.net;

import com.typesafe.config.ConfigFactory;
import org.ethereum.config.NoAutoscan;
import org.ethereum.config.SystemProperties;
import org.ethereum.core.*;
import org.ethereum.facade.Ethereum;
import org.ethereum.facade.EthereumFactory;
import org.ethereum.listener.EthereumListenerAdapter;
import org.ethereum.net.eth.message.*;
import org.ethereum.net.message.Message;
import org.ethereum.net.server.Channel;
import org.ethereum.net.shh.ShhEnvelopeMessage;
import org.ethereum.util.RLP;
import org.junit.Test;
import org.spongycastle.util.encoders.Hex;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

import static org.ethereum.crypto.HashUtil.sha3;

/**
 * @author: HuShili
 * @date: 2018/7/30
 * @description: none
 */
public class PeerProtocolTest {

    @Configuration
    @NoAutoscan
    public static class SysPropConfig1 {

        static SystemProperties props = new SystemProperties();
        @Bean
        public SystemProperties systemProperties() {
            return props;
        }
    }

    @Configuration
    @NoAutoscan
    public static class SysPropConfig2 {
        static SystemProperties props= new SystemProperties();
        @Bean
        public SystemProperties systemProperties() {
            return props;
        }

    }

    @Test
    public void testTest() throws InterruptedException {
        SysPropConfig1.props.overrideParams(
                "peer.listen.port", "30334",
                "peer.privateKey", "3ec771c31cac8c0dba77a69e503765701d3c2bb62435888d4ffa38fed60c445c",
                "genesis", "genesis-light.json",
                "database.dir", "testDB-1");

        SysPropConfig2.props.overrideParams(ConfigFactory.parseString(
                "peer.listen.port = 30335 \n" +
                        "peer.privateKey = 6ef8da380c27cea8fdf7448340ea99e8e2268fc2950d79ed47cbf6f85dc977ec \n" +
                        "peer.active = [{ url = \"enode://3973cb86d7bef9c96e5d589601d788370f9e24670dcba0480c0b3b1b0647d13d0f0fffed115dd2d4b5ca1929287839dcd4e77bdc724302b44ae48622a8766ee6@localhost:30334\" }] \n" +
                        "sync.enabled = true \n" +
                        "genesis = genesis-light.json \n" +
                        "database.dir = testDB-2 \n"));


        Ethereum ethereum1 = EthereumFactory.createEthereum(SysPropConfig1.props, SysPropConfig1.class);

        Ethereum ethereum2 = EthereumFactory.createEthereum(SysPropConfig2.props, SysPropConfig2.class);

        final Channel[] channels = new Channel[1];
        ethereum1.addListener(new EthereumListenerAdapter() {
            @Override
            public void onEthStatusUpdated(Channel channel, StatusMessage statusMessage) {
                channels[0] = channel;
                System.out.println("==== E1 Got the Channel: " + channel);
            }
            @Override
            public void onRecvMessage(Channel channel, Message message) {
                System.out.println("==== E1 Got Message: " + message.toString());
            }
        });
        ethereum2.addListener(new EthereumListenerAdapter() {
            @Override
            public void onEthStatusUpdated(Channel channel, StatusMessage statusMessage) {
                System.out.println("==== E2 Got the Channel: " + channel);
            }
            @Override
            public void onRecvMessage(Channel channel, Message message) {
                System.out.println("==== E2 Got Message: " + message.toString());
            }
        });

        System.out.println("======= Waiting for connecting...");
        Thread.sleep(10000);

        System.out.println("======= Sending test message...");
        channels[0].getApaHandler().sendRequest("hello");
        Thread.sleep(10000);

        ethereum1.close();
        ethereum2.close();

        System.out.println("Passed.");

    }
}
