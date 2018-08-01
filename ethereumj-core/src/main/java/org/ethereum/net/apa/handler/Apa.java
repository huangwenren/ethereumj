package org.ethereum.net.apa.handler;

/**
 * @author: HuShili
 * @date: 2018/7/31
 * @description: none
 */
public interface Apa {

    /**
     * Sends a test request to the wire
     *
     * @param string sending message
     */
    void sendRequest(String string);

    /**
     * Sends a test response to the wire
     *
     * @param string sending message
     */
    void sendResponse(String string);
}
