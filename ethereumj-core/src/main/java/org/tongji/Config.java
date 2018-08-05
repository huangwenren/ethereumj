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

    private String name = "Test Node";

    private int port = 30303;

    private boolean discovery_enabled = true;

    private int networkId = 3;

    private String active_peers;

    private boolean sync_enabled = true;

    private String genesis = "ropsten.json";

    private String blockchain_name = "ropsten";

    private String database_dir = "testnetSampleDb";

    private int flush_memory = 0;

    private Config(){
        active_peers = "[{url = 'enode://6ce05930c72abc632c58e2e4324f7c7ea478cec0ed4fa2528982cf34483094e9cbc9216e7aa349691242576d552a2a56aaeae426c5303ded677ce455ba1acd9d@13.84.180.240:30303'}," +
                "{url = 'enode://20c9ad97c081d63397d7b685a412227a40e23c8bdc6688c6f37e97cfbc22d2b4d1db1510d8f61e6a8866ad7f0e17c02b14182d37ea7c3c8b9c2683aeb6b733a1@52.169.14.227:30303'}" +
                "]";
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
        config.put("blockchain.config.name", blockchain_name);
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
                case "blockchain.config.name":
                    blockchain_name = (String)entry.getValue();
                    break;
                case "database.dir":
                    database_dir = (String)entry.getValue();
                    break;
                case "cache.flush.memory":
                    flush_memory = (int)entry.getValue();
                    break;
                default:
                    System.out.println("User config name: " + entry.getKey());
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
        str += "peer.active = " + active_peers + " \n";
        str += "sync.enabled = " + sync_enabled + " \n";
        str += "genesis = " + genesis + " \n";
        str += "blockchain.config.name = " + blockchain_name + " \n";
        str += "database.dir = " + database_dir + " \n";
        str += "cache.flush.memory = " + flush_memory + " \n";

        return str;
    }

    @Bean
    public SystemProperties systemProperties() {
        SystemProperties props = new SystemProperties();
        props.overrideParams(ConfigFactory.parseString(this.toString().replaceAll("'", "\"")));
        return props;
    }
}
