package org.unallied.mmocraft.tools.output;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;

import org.unallied.mmocraft.net.RecvOpcode;
import org.unallied.mmocraft.net.SendOpcode;

/**
 * Provides a generic writer for little-endian byte sequences
 * @author Faythless
 *
 */
public class GenericLittleEndianWriter implements LittleEndianWriter {

    private static final Charset ASCII = Charset.forName("US-ASCII");
    private ByteArrayOutputStream baos;
    
    public GenericLittleEndianWriter() {
        this(32);
    }
    
    public GenericLittleEndianWriter(int size) {
        baos = new ByteArrayOutputStream(size);
    }
    
    @Override
    public void write(byte b) {
        baos.write(b);
    }
    
    /**
     * Write an opcode to the sequence
     * @param o opcode to write
     */
    @Override
    public void write(SendOpcode o) {
        writeShort(o.getValue());
    }
    
    /**
     * Write an opcode to the sequence
     * @param o opcode to write
     */
    @Override
    public void write(RecvOpcode o) {
        writeShort(o.getValue());
    }
    
    /**
     * Serializes a serializable object into packet format
     * @param serializable
     */
    public void writeObject(Serializable serializable) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(serializable);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Override
    public void writeShort(int s) {
        baos.write((byte)((s >>> 0) & 0xFF));
        baos.write((byte)((s >>> 8) & 0xFF));
    }

    @Override
    public void write7BitEncodedInt(int i) {
        long v = (long)(i & 0x00000000FFFFFFFFL); // support negative numbers
        while (v >= 0x80) {
            baos.write((byte)(v | 0x80));
            v >>= 7;
        }
        baos.write((byte)v);
    }
    
    @Override
    public void writeInt(int i) {
        baos.write((byte)((i >>> 0) & 0xFF));
        baos.write((byte)((i >>> 8) & 0xFF));
        baos.write((byte)((i >>> 16) & 0xFF));
        baos.write((byte)((i >>> 24) & 0xFF));
    }

    @Override
    public void writeFloat(float f) {
        writeInt(Float.floatToRawIntBits(f));
    }
    
    @Override
    public void writeLong(long l) {
        baos.write((byte)((l >>> 0) & 0xFF));
        baos.write((byte)((l >>> 8) & 0xFF));
        baos.write((byte)((l >>> 16) & 0xFF));
        baos.write((byte)((l >>> 24) & 0xFF));
        baos.write((byte)((l >>> 32) & 0xFF));
        baos.write((byte)((l >>> 40) & 0xFF));
        baos.write((byte)((l >>> 48) & 0xFF));
        baos.write((byte)((l >>> 56) & 0xFF));
    }

    @Override
    public void writeAsciiString(String str) {
        write(str.getBytes(ASCII));
    }

    @Override
    public void writeNullTerminatedAsciiString(String str) {
        writeAsciiString(str);
        write((byte)0);
    }
    
    /**
     * Writes an ASCII string with the number of bytes in front.
     * The number of bytes is in a short.
     * @param str The ASCII string to write
     */
    public void writePrefixedAsciiString(String str) {
        writeShort(str.length());
        writeAsciiString(str);
    }

    /**
     * Creates a newly allocated byte array. Its size is the current size of 
     * this output stream and the valid contents of the buffer have been copied 
     * into it. 
     * @return the current contents of this output stream, as a byte array.
     */
    public byte[] toByteArray() {
        return baos.toByteArray();
    }

    @Override
    public void write(byte[] b) {
        try {
            baos.write(b);
        } catch (IOException e) {
        }
    }

    /**
     * Writes an ASCII string prefixed with an integer that uses 7 bits for size
     * and the 8th bit for determining whether there are more bytes to read for the size.
     * @param str The ASCII string to write
     */
    public void write7BitPrefixedAsciiString(String str) {
        write7BitEncodedInt(str.length());
        writeAsciiString(str);
    }
}
