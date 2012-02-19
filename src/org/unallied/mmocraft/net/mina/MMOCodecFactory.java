package org.unallied.mmocraft.net.mina;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class MMOCodecFactory implements ProtocolCodecFactory {
    private final ProtocolEncoder encoder;
    private final ProtocolDecoder decoder;

    public MMOCodecFactory() {
        encoder = new MMOPacketEncoder();
        decoder = new MMOPacketDecoder();
    }
    
    @Override
    public ProtocolEncoder getEncoder(IoSession session) throws Exception {
        return encoder;
    }

    @Override
    public ProtocolDecoder getDecoder(IoSession session) throws Exception {
        return decoder;
    }

}
