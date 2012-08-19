package org.unallied.mmocraft.net.mina;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class MMOPacketDecoder extends CumulativeProtocolDecoder {
    
    // This state lets us retrieve the partial packet later on
    private static final String DECODER_STATE_KEY = 
            MMOPacketDecoder.class.getName() + ".STATE";
    
    private static class DecoderState {
        public int packetLength = -1;
    }
    
    @Override
    protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out)
            throws Exception {
        
        // Get the current decoder state
        DecoderState decoderState = (DecoderState) session.getAttribute(DECODER_STATE_KEY);
        
        // Decoder state is null, so make a new one
        if (decoderState == null) {
            decoderState = new DecoderState();
            session.setAttribute(DECODER_STATE_KEY, decoderState);
        }
        
        /*
         * If we have at least 4 bytes and our packet length is still invalid,
         * update it.  Packets are of the following form: 
         * [length][opcode][payload].  Length is 4 bytes, opcode is 2 bytes
         */
        if (in.remaining() >= 4 && decoderState.packetLength == -1) {
            // We have to convert from big-ENDian to lil-ENDian.  That's why we don't use getInt()
            decoderState.packetLength = 0;
            decoderState.packetLength += (int) (in.get())       & 0x000000FF;
            decoderState.packetLength += (int) (in.get() << 8)  & 0x0000FF00;
            decoderState.packetLength += (int) (in.get() << 16) & 0x00FF0000;
            decoderState.packetLength += (int) (in.get() << 24) & 0xFF000000;
            
        } else if (in.remaining() < 4 && decoderState.packetLength == -1) {
            return false; // Nothing to do.  We don't have the full header
        }
        
        // If we have at least the bytes for this packet
        if (in.remaining() >= decoderState.packetLength) {
            byte decryptedPacket[] = new byte[decoderState.packetLength];
            in.get(decryptedPacket, 0, decoderState.packetLength);
            decoderState.packetLength = -1; // Prepare decoder for the next packet
            
            // we have a full packet, so process it!
            out.write(decryptedPacket);
            return true;
        }
        return false;
    }

}
