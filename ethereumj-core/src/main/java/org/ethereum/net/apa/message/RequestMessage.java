package org.ethereum.net.apa.message;

import org.ethereum.net.shh.WhisperMessage;
import org.ethereum.util.RLP;
import org.ethereum.util.RLPList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.ethereum.net.apa.message.ApaMessageCodes.REQUEST;

/**
 * @author: HuShili
 * @date: 2018/7/31
 * @description: none
 */
public class RequestMessage extends ApaMessage {
    private List<WhisperMessage> messages = new ArrayList<>();

    public RequestMessage(byte[] encoded) {
        super(encoded);
        parse();
    }

    public RequestMessage(WhisperMessage ... msg) {
        Collections.addAll(messages, msg);
        parsed = true;
    }

    public RequestMessage(Collection<WhisperMessage> msg) {
        messages.addAll(msg);
        parsed = true;
    }

    @Override
    public ApaMessageCodes getCommand() {
        return REQUEST;
    }

    public void addMessage(WhisperMessage msg) {
        messages.add(msg);
    }

    public void parse() {
        if (!parsed) {
            RLPList paramsList = (RLPList) RLP.decode2(encoded).get(0);

            for (int i = 0; i < paramsList.size(); i++) {
                messages.add(new WhisperMessage(paramsList.get(i).getRLPData()));
            }
            this.parsed = true;
        }
    }

    @Override
    public byte[] getEncoded() {
        if (encoded == null) {
            byte[][] encodedMessages = new byte[messages.size()][];
            for (int i = 0; i < encodedMessages.length; i++) {
                encodedMessages[i] = messages.get(i).getEncoded();
            }
            encoded = RLP.encodeList(encodedMessages);
        }
        return encoded;
    }

    public List<WhisperMessage> getMessages() {
        return messages;
    }

    @Override
    public Class<ResponseMessage> getAnswerMessage() {
        return ResponseMessage.class;
    }

    @Override
    public String toString() {
        return "[" + this.getCommand().name() +
                " msg=" + this.messages + "]";
    }
}
