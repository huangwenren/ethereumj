package org.ethereum.manager;

import org.ethereum.config.SystemProperties;
import org.ethereum.core.EventDispatchThread;
import org.ethereum.core.PendingState;
import org.ethereum.db.DbFlushManager;
import org.ethereum.listener.CompositeEthereumListener;
import org.ethereum.listener.EthereumListener;
import org.ethereum.net.client.PeerClient;
import org.ethereum.net.rlpx.discover.NodeManager;
import org.ethereum.net.rlpx.discover.UDPListener;
import org.ethereum.net.server.ChannelManager;
import org.ethereum.sync.ApaSyncManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author: HuShili
 * @date: 2018/8/6
 * @description: none
 */
@Component
public class P2PManager {
    private static final Logger logger = LoggerFactory.getLogger("general");

    @Autowired
    private PeerClient activePeer;

    @Autowired
    private ChannelManager channelManager;

    @Autowired
    private ApaSyncManager apaSyncManager;

    @Autowired
    private NodeManager nodeManager;

    @Autowired
    private PendingState pendingState;

    @Autowired
    private UDPListener discoveryUdpListener;

    @Autowired
    private EventDispatchThread eventDispatchThread;

    @Autowired
    private DbFlushManager dbFlushManager;

    @Autowired
    private ApplicationContext ctx;

    private SystemProperties config;

    private EthereumListener listener;

    @Autowired
    public P2PManager(final SystemProperties config,
                        final EthereumListener listener) {
        this.listener = listener;
        this.config = config;

    }

    @PostConstruct
    private void init() {
        this.apaSyncManager.init(channelManager);
    }

    public void addListener(EthereumListener listener) {
        logger.info("Ethereum listener added");
        ((CompositeEthereumListener) this.listener).addListener(listener);
    }

    public void startPeerDiscovery() {
    }

    public void stopPeerDiscovery() {
        discoveryUdpListener.close();
        nodeManager.close();
    }

    public ChannelManager getChannelManager() {
        return channelManager;
    }

    public ApaSyncManager getApaSyncManager() {
        return apaSyncManager;
    }

    public EthereumListener getListener() {
        return listener;
    }

    public PeerClient getActivePeer() {
        return activePeer;
    }

    public PendingState getPendingState() {
        return pendingState;
    }

    public void close() {
        logger.info("close: stopping peer discovery ...");
        stopPeerDiscovery();
        logger.info("close: stopping ChannelManager ...");
        channelManager.close();
        logger.info("close: stopping PeerClient ...");
        activePeer.close();
        logger.info("close: shutting down event dispatch thread used by EventBus ...");
        eventDispatchThread.shutdown();
        logger.info("close: database flush manager ...");
        dbFlushManager.close();
    }
}
