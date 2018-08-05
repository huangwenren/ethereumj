package org.tongji;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author: HuShili
 * @date: 2018/8/2
 * @description: none
 */
public class UserConfig extends Config {

    private ArrayList<String> protocols;

    private String privateKey;

    public UserConfig(Map config){

        // Class Config deals other
        super(config);

        // And here deals protocols
        if(config.containsKey("peer.capabilities"))
            protocols = (ArrayList<String>) config.get("peer.capabilities");
        else {
            protocols = new ArrayList<>();
            protocols.add("eth");
        }
        if(config.containsKey("peer.privateKey"))
            privateKey = (String) config.get("peer.privateKey");
    }

    @Override
    public void setConfig(Map<String, Object> config) {
        super.setConfig(config);

        if(config.containsKey("peer.capabilities"))
            protocols = (ArrayList<String>) config.get("peer.capabilities");
        if(config.containsKey("peer.privateKey"))
            privateKey = (String) config.get("peer.privateKey");
    }

    @Override
    public Map getConfig() {

        Map config = super.getConfig();
        if(protocols != null) {
            config.put("peer.capabilities", protocols);
        }
        if(privateKey != null) {
            config.put("peer.privateKey", privateKey);
        }

        return config;
    }

    @Override
    public String toString() {
        String str = super.toString();

        str += "peer.capabilities = [";
        for (String protocol : protocols) {
            str += protocol;
            if(protocols.indexOf(protocol) != protocols.size() - 1){
                str += ", ";
            }
        }
        str += "]\n";

        str += "peer.privateKey = " + privateKey + " \n";

        return str;
    }
}
