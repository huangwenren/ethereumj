package org.ethereum.sync;

import org.ethereum.config.SystemProperties;
import org.ethereum.core.Apa;
import org.ethereum.db.ApaStore;
import org.ethereum.net.server.ChannelManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.ethereum.util.ByteUtil.toHexString;

/**
 * @author: HuShili
 * @date: 2018/8/10
 * @description: Sync Apa msg to other active peers
 */
@Component
public class ApaSyncManager {

    private final static Logger logger = LoggerFactory.getLogger("sync");

    @Autowired
    private ApaStore apaStore;

    private BlockingQueue<Apa> apaQueue = new LinkedBlockingQueue<>();

    private Thread syncQueueThread;

    private SystemProperties config;

    private ChannelManager channelManager;

    public ApaSyncManager() {

    }

    @Autowired
    public ApaSyncManager(final SystemProperties config) {
        this.config = config;
    }

    public void init(final ChannelManager channelManager) {
        if (this.channelManager == null) {  // First init
            this.channelManager = channelManager;
        }
        if (!config.isSyncEnabled()) {
            logger.info("Sync Manager: OFF");
            return;
        }
        logger.info("Sync Manager: ON");

        Runnable queueProducer = this::produceQueue;

        syncQueueThread = new Thread (queueProducer, "SyncQueueThread");
        syncQueueThread.start();
    }

    private void produceQueue() {
        while (!Thread.currentThread().isInterrupted()) {

            Apa apa = null;
            try {

                // Save new msg
                apa = apaQueue.take();

                apaStore.saveApa(apa);

            } catch (InterruptedException e) {
                break;
            } catch (Throwable e) {
                if (apa != null) {
                    logger.error("Error processing apa {}: ", apa.getMessage().getCommand(), e);
                } else {
                    logger.error("Error processing unknown block", e);
                }
            }

        }
    }

    public void close() {
        try {
            logger.info("Shutting down SyncManager");
            if (syncQueueThread != null) {
                syncQueueThread.interrupt();
                syncQueueThread.join(10 * 1000);
            }
        } catch (Exception e) {
            logger.warn("Problems closing SyncManager", e);
        }
    }

    public boolean validateAndAddNewApa(Apa apa){

        logger.info("Validating apa: " + apa.getMessage().getCommand() + apa.getHash());

        // Just do add, no validation
        // Check new
        if(!apaStore.isApaExist(apa.getHash())) {
            apaQueue.add(apa);
            return true;
        }
        else {
            logger.info("Dropped apa: " + apa.getMessage().getCommand() + apa.getHash());
        }

        return false;
    }

    public ApaStore getApaStore() {
        return apaStore;
    }
}
