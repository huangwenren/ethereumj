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

    public UserConfig(Map config){

        // Class Config deals other
        super(config);

        // And here deals protocols
        if(config.containsKey("peer.capabilities"))
            protocols = (ArrayList<String>) config.get("peer.capabilities");
        else {
            protocols = new ArrayList<>();
            protocols.add("eth");
            protocols.add("shh");
        }
    }

    @Override
    public void setConfig(Map<String, Object> config) {
        super.setConfig(config);

        if(config.containsKey("peer.capabilities"))
            protocols = (ArrayList<String>) config.get("peer.capabilities");
    }

    @Override
    public Map getConfig() {

        Map config = super.getConfig();
        config.put("peer.capabilities", protocols);

        return config;
    }

    @Override
    public String toString() {
        String str = super.toString();

        str += "\npeer.capabilities = [";
        for (String protocol : protocols) {
            str += protocol;
            if(protocols.indexOf(protocol) != protocols.size() - 1){
                str += ", ";
            }
            else{
                str += "]\n";
            }
        }

        return str;
    }
}
