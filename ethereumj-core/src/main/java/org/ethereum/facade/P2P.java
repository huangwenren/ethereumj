package org.ethereum.facade;

import org.ethereum.listener.EthereumListener;
import org.ethereum.net.client.PeerClient;
import org.ethereum.net.rlpx.Node;
import org.ethereum.net.server.ChannelManager;

import java.net.InetAddress;

/**
 * @author: HuShili
 * @date: 2018/8/6
 * @description: none
 */
public interface P2P {

    void startPeerDiscovery();

    void stopPeerDiscovery();

    void connect(InetAddress addr, int port, String remoteId);

    void connect(String ip, int port, String remoteId);

    void connect(Node node);

    void addListener(EthereumListener listener);

    PeerClient getDefaultPeer();

    ChannelManager getChannelManager();

    boolean isConnected();

    void close();

}
