package org.tongji;

import com.typesafe.config.ConfigFactory;
import org.ethereum.config.SystemProperties;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: HuShili
 * @date: 2018/8/2
 * @description: none
 */
public class Config {

    private String name = "Apa Node";

    private int port = 30303;

    private boolean discovery_enabled = true;

    private int networkId = 1;

    private String active_peers;

    private boolean sync_enabled = true;

    private String genesis = "sample-genesis.json";

    private String database_dir = "testDb";

    private int flush_memory = 0;

    public Config(){
    }

    public Config(Map<String, Object> config) {
        this();
        diaConfig(config);
    }

    public void setConfig(Map<String, Object> config) {
        diaConfig(config);
    }

    public Map getConfig() {
        Map<String, Object> config = new HashMap<>();

        config.put("node.name", name);
        config.put("peer.listen.port", port);
        config.put("peer.discovery.enabled", discovery_enabled);
        config.put("peer.networkId", networkId);
        config.put("peer.active", active_peers);
        config.put("sync.enabled", sync_enabled);
        config.put("genesis", genesis);
        config.put("database.dir", database_dir);
        config.put("cache.flush.memory", flush_memory);

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
                    name = (String)entry.getValue();
                    break;
                case "peer.listen.port":
                    port = (int)entry.getValue();
                    break;
                case "peer.discovery.enabled":
                    discovery_enabled = (boolean)entry.getValue();
                    break;
                case "peer.networkId":
                    networkId = (int)entry.getValue();
                    break;
                case "peer.active":
                    active_peers = (String)entry.getValue();
                    break;
                case "sync.enabled":
                    sync_enabled = (boolean)entry.getValue();
                    break;
                case "genesis":
                    genesis = (String)entry.getValue();
                    break;
                case "database.dir":
                    database_dir = (String)entry.getValue();
                    break;
                case "cache.flush.memory":
                    flush_memory = (int)entry.getValue();
                    break;
                default:
                    System.out.println("======= User config name: " + entry.getKey());
            }
        }
    }

    @Override
    public String toString(){

        String str = "";
        str += "node.name = " + name + " \n";
        str += "peer.listen.port = " + port + " \n";
        str += "peer.discovery.enabled = " + discovery_enabled + " \n";
        str += "peer.networkId = " + networkId + " \n";
        if (active_peers != null)
            str += "peer.active = " + active_peers + " \n";
        str += "sync.enabled = " + sync_enabled + " \n";
        str += "genesis = " + genesis + " \n";
        str += "database.dir = " + database_dir + " \n";
        str += "cache.flush.memory = " + flush_memory + " \n";

        str += "peer.discovery.ip.list = [] \n";

        return str;
    }

}
