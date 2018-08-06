package org.ethereum.facade;

import org.ethereum.config.CommonConfig;
import org.ethereum.config.SystemProperties;
import org.ethereum.listener.CompositeEthereumListener;
import org.ethereum.listener.EthereumListener;
import org.ethereum.manager.AdminInfo;
import org.ethereum.manager.P2PManager;
import org.ethereum.net.client.PeerClient;
import org.ethereum.net.rlpx.Node;
import org.ethereum.net.server.ChannelManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

import static org.ethereum.util.ByteUtil.toHexString;

/**
 * @author: HuShili
 * @date: 2018/8/6
 * @description: none
 */

@Component
public class P2PImpl implements P2P, SmartLifecycle {

    private static final Logger logger = LoggerFactory.getLogger("facade");
    private static final Logger gLogger = LoggerFactory.getLogger("general");

    @Autowired
    P2PManager p2PManager;

    @Autowired
    AdminInfo adminInfo;

    @Autowired
    ChannelManager channelManager;

    @Autowired
    ApplicationContext ctx;

    @Autowired
    CommonConfig commonConfig = CommonConfig.getDefault();

    private SystemProperties config;

    private CompositeEthereumListener compositeEthereumListener;

    @Autowired
    public P2PImpl(final SystemProperties config, final CompositeEthereumListener compositeEthereumListener) {
        this.compositeEthereumListener = compositeEthereumListener;
        this.config = config;
        System.out.println();
        gLogger.info("EthereumJ node started: enode://" + toHexString(config.nodeId()) + "@" + config.externalIp() + ":" + config.listenPort());
    }

    @Override
    public void startPeerDiscovery() {
        p2PManager.startPeerDiscovery();
    }

    @Override
    public void stopPeerDiscovery() {
        p2PManager.stopPeerDiscovery();
    }

    @Override
    public void connect(InetAddress addr, int port, String remoteId) {
        connect(addr.getHostName(), port, remoteId);
    }

    @Override
    public void connect(final String ip, final int port, final String remoteId) {
        logger.debug("Connecting to: {}:{}", ip, port);
        p2PManager.getActivePeer().connectAsync(ip, port, remoteId, false);
    }

    @Override
    public void connect(Node node) {
        connect(node.getHost(), node.getPort(), Hex.toHexString(node.getId()));
    }

    @Override
    public void close() {
        logger.info("### Shutdown initiated ### ");
        ((AbstractApplicationContext) getApplicationContext()).close();
    }

    @Override
    public void addListener(EthereumListener listener) {
        p2PManager.addListener(listener);
    }

    @Override
    public PeerClient getDefaultPeer() {
        return p2PManager.getActivePeer();
    }

    @Override
    public ChannelManager getChannelManager() {
        return channelManager;
    }

    @Override
    public boolean isConnected() {
        return p2PManager.getActivePeer() != null;
    }

    /**
     * For testing purposes and 'hackers'
     */
    public ApplicationContext getApplicationContext() {
        return ctx;
    }

    @Override
    public boolean isAutoStartup() {
        return false;
    }

    /**
     * Shutting down all app beans
     */
    @Override
    public void stop(Runnable callback) {
        logger.info("Shutting down Ethereum instance...");
        p2PManager.close();
        callback.run();
    }

    @Override
    public void start() {}

    @Override
    public void stop() {}

    @Override
    public boolean isRunning() {
        return true;
    }

    /**
     * Called first on shutdown lifecycle
     */
    @Override
    public int getPhase() {
        return Integer.MAX_VALUE;
    }
}
