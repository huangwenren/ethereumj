package org.ethereum.net.apa.message;

import org.ethereum.net.shh.WhisperMessage;
import org.ethereum.util.RLP;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

import static org.ethereum.net.apa.message.ApaMessageCodes.REQUEST;

/**
 * @author: HuShili
 * @date: 2018/7/31
 * @description: none
 */
public class RequestMessage extends ApaMessage {
    private Map messages = new HashMap<>();

    public RequestMessage(byte[] encoded) {
        super(encoded);
        parse();
    }

    public RequestMessage(Map msg) {
        messages = msg;
        parsed = true;
    }

    @Override
    public ApaMessageCodes getCommand() {
        return REQUEST;
    }

    public void parse() {
        if (!parsed) {
            try
            {
                ByteArrayInputStream byteArrayInStream = new ByteArrayInputStream(encoded);
                ObjectInputStream objectInStream = new ObjectInputStream(byteArrayInStream);
                messages = (HashMap)objectInStream.readObject();
                byteArrayInStream.close();
                objectInStream.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

            this.parsed = true;
        }
    }

    @Override
    public byte[] getEncoded() {
        if(encoded == null) {
            try {
                ByteArrayOutputStream byteArrayOutStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutStream);
                objectOutputStream.writeObject(messages);
                encoded = byteArrayOutStream.toByteArray();
                byteArrayOutStream.close();
                objectOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return encoded;
    }

    public Map getMessages() {
        return messages;
    }

    @Override
    public Class<?> getAnswerMessage() {
        return null;
    }

    @Override
    public String toString() {
        return "[" + this.getCommand().name() +
                " msg=" + this.messages + "]";
    }
}
