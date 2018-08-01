package org.ethereum.net.apa.handler;

import org.ethereum.net.apa.message.RequestMessage;
import org.ethereum.net.apa.message.ResponseMessage;

/**
 * @author: HuShili
 * @date: 2018/7/31
 * @description: none
 */
public interface Apa {

    /**
     * Sends a test request to the wire
     *
     * @param msg sending message
     */
    void sendRequest(RequestMessage msg);

    /**
     * Sends a test response to the wire
     *
     * @param msg sending message
     */
    void sendResponse(ResponseMessage msg);
}
