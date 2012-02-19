package org.unallied.mmocraft.net.mina;

import org.unallied.mmocraft.net.Packet;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

public class MMOPacketEncoder implements ProtocolEncoder {

    @Override
    public void encode(IoSession session, Object message,
            ProtocolEncoderOutput out) throws Exception {
        // No encryption
        out.write(IoBuffer.wrap(((Packet) message).getBytes()));
    }

    @Override
    public void dispose(IoSession session) throws Exception {
    }

}
