/*
 * Copyright (c) [2016] [ <ether.camp> ]
 * This file is part of the ethereumJ library.
 *
 * The ethereumJ library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The ethereumJ library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the ethereumJ library. If not, see <http://www.gnu.org/licenses/>.
 */
package org.ethereum.net.apa.message;

import org.ethereum.util.RLP;
import org.ethereum.util.RLPList;

import static org.ethereum.net.apa.message.ApaMessageCodes.STATUS;

/**
 * @author: HuShili
 * @date: 2018/7/31
 * @description: none
 */
public class StatusMessage extends ApaMessage {

    private byte protocolVersion;

    public StatusMessage(byte[] encoded) {
        super(encoded);
    }

    public StatusMessage(byte protocolVersion) {
        this.protocolVersion = protocolVersion;
        this.parsed = true;
    }

    private void encode() {
        byte[] protocolVersion = RLP.encodeByte(this.protocolVersion);
        this.encoded = RLP.encodeList(protocolVersion);
    }

    private void parse() {
        RLPList paramsList = (RLPList) RLP.decode2(encoded).get(0);
        this.protocolVersion = paramsList.get(0).getRLPData()[0];
        parsed = true;
    }

    @Override
    public byte[] getEncoded() {
        if (encoded == null) encode();
        return encoded;
    }

    @Override
    public Class<?> getAnswerMessage() {
        return null;
    }

    @Override
    public ApaMessageCodes getCommand() {
        return STATUS;
    }

    @Override
    public String toString() {
        if (!parsed) parse();
        return "[" + this.getCommand().name() +
            " protocolVersion=" + this.protocolVersion + "]";
    }

}
