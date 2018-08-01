package org.ethereum.net.apa.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.ethereum.listener.EthereumListener;
import org.ethereum.net.MessageQueue;
import org.ethereum.net.apa.message.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author: HuShili
 * @date: 2018/7/31
 * @description: none
 */

/**
 * Process the messages between peers with 'apa' capability on the network.
 *
 * Peers with 'apa' capability can send/receive:
 */

@Component
@Scope("prototype")
public class ApaHandler extends SimpleChannelInboundHandler<ApaMessage> implements Apa {

    private final static Logger logger = LoggerFactory.getLogger("net.apa");
    public final static byte VERSION = 01;

    private MessageQueue msgQueue = null;
    private boolean active = false;

    @Autowired
    private EthereumListener ethereumListener;

    public ApaHandler() {
    }

    public ApaHandler(MessageQueue msgQueue) {
        this.msgQueue = msgQueue;
    }

    @Override
    public void channelRead0(final ChannelHandlerContext ctx, ApaMessage msg) throws InterruptedException {

        if (!isActive()) return;

        if (ApaMessageCodes.inRange(msg.getCommand().asByte()))
            logger.info("ApaHandler invoke: [{}]", msg.getCommand());

        ethereumListener.trace(String.format("ApaHandler invoke: [%s]", msg.getCommand()));

        switch (msg.getCommand()) {
            case STATUS:
                ethereumListener.trace("[Recv: " + msg + "]");
                break;
            case REQUEST:
                ethereumListener.trace("[Recv: " + msg + "]");
                sendResponse("hello");
                break;
            case RESPONSE:
                ethereumListener.trace("[Recv: " + msg + "]");
                break;
            default:
                logger.error("Unknown Apa message type: " + msg.getCommand());
                break;
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("Apa handling failed", cause);
        super.exceptionCaught(ctx, cause);
        ctx.close();
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        active = false;
        logger.debug("handlerRemoved: ... ");
    }

    public void activate() {
        logger.info("Apa protocol activated");
        ethereumListener.trace("Apa protocol activated");
        sendStatus();

        this.active = true;
    }

    @Override
    public void sendRequest(String string){
        RequestMessage msg = new RequestMessage();
        sendMessage(msg);
    }

    @Override
    public void sendResponse(String string){
        ResponseMessage msg = new ResponseMessage();
        sendMessage(msg);
    }

    private void sendStatus() {
        byte protocolVersion = ApaHandler.VERSION;
        StatusMessage msg = new StatusMessage(protocolVersion);
        sendMessage(msg);
    }

    public void sendMessage(ApaMessage msg) {
        msgQueue.sendMessage(msg);
    }

    public boolean isActive() {
        return active;
    }

    public void setMsgQueue(MessageQueue msgQueue) {
        this.msgQueue = msgQueue;
    }
}
