package org.tongji;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: HuShili
 * @date: 2018/8/2
 * @description: none
 */
public class Config {

    private static String NAME = "Test Node";

    private static int PORT = 30303;

    private static boolean DISCOVERY_ENABLED = true;

    private static int NETWORK_ID = 3;

    private static Map ACTIVE_PEERS;

    private static boolean SYNC_ENABLED = true;

    private static String GENESIS = "ropsten.json";

    private static String BLOCKCHAIN_NAME = "ropsten";

    private static String DATABASE_DIR = "testnetSampleDb";

    private static int FLUSH_MEMORY = 0;

    private Config(){}

    public Config(Map<String, Object> config) {
        diaConfig(config);
    }

    public void setConfig(Map<String, Object> config) {
        diaConfig(config);
    }

    public Map getConfig() {
        Map<String, Object> config = new HashMap<>();

        config.put("node.name", NAME);
        config.put("peer.listen.port", PORT);
        config.put("peer.discovery.enabled", DISCOVERY_ENABLED);
        config.put("peer.networkId", NETWORK_ID);
        config.put("peer.active", ACTIVE_PEERS);
        config.put("sync.enabled", SYNC_ENABLED);
        config.put("genesis", GENESIS);
        config.put("blockchain.config.name", BLOCKCHAIN_NAME);
        config.put("database.dir", DATABASE_DIR);
        config.put("cache.flush.memory", FLUSH_MEMORY);

        return config;
    }

    /**
     * Deal with the Map of params
     * @param config  configuration
     */
    public void diaConfig(Map<String, Object> config){
        for (Map.Entry<String, Object> entry : config.entrySet()) {
            switch (entry.getKey()){
                case "node.name":
                    NAME = (String)entry.getValue();
                    break;
                case "peer.listen.port":
                    PORT = (int)entry.getValue();
                    break;
                case "peer.discovery.enabled":
                    DISCOVERY_ENABLED = (boolean)entry.getValue();
                    break;
                case "peer.networkId":
                    NETWORK_ID = (int)entry.getValue();
                    break;
                case "peer.active":
                    ACTIVE_PEERS = (Map)entry.getValue();
                    break;
                case "sync.enabled":
                    SYNC_ENABLED = (boolean)entry.getValue();
                    break;
                case "genesis":
                    GENESIS = (String)entry.getValue();
                    break;
                case "blockchain.config.name":
                    BLOCKCHAIN_NAME = (String)entry.getValue();
                    break;
                case "database.dir":
                    DATABASE_DIR = (String)entry.getValue();
                    break;
                case "cache.flush.memory":
                    FLUSH_MEMORY = (int)entry.getValue();
                    break;
                default:
                    System.out.println("Unknown config name: " + entry.getKey());
            }
        }
    }
}
