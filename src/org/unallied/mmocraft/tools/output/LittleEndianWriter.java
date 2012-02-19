package org.unallied.mmocraft.tools.output;

import org.unallied.mmocraft.net.SendOpcode;

public interface LittleEndianWriter {
    
    /**
     * Write an array of bytes to the sequence
     * @param b The bytes to write
     */
    public void write(byte b[]);
    
    /**
     * Write a byte to the sequence
     * @param b The byte to write
     */
    public void write(byte b);
   
    /**
     * Write an opcode to the sequence
     * @param o
     */
    public void write(SendOpcode o);
    
    /**
     * Writes a short to the sequence
     * @param s The short to write
     */
    public void writeShort(int s);
    
    /**
     * Writes an integer to the sequence
     * @param i The int to write
     */
    public void writeInt(int i);
   
    /**
     * Writes a long (8 bytes) to the sequence
     * @param l The long to write
     */
    public void writeLong(long l);
    
    /**
     * Writes an ASCII string to the sequence
     * @param str The ASCII string to write
     */
    public void writeAsciiString(String str);
    
    /**
     * Writes a null-terminated ASCII string to the sequence
     * @param str The ASCII string to write
     */
    public void writeNullTerminatedAsciiString(String str);
    
    /**
     * Writes an ASCII string with the number of bytes in front.
     * The number of bytes is in a short.
     * @param str The ASCII string to write
     */
    public void writePrefixedAsciiString(String str);
    
    /**
     * Creates a newly allocated byte array. Its size is the current size of 
     * this output stream and the valid contents of the buffer have been copied 
     * into it. 
     * @return the current contents of this output stream, as a byte array.
     */
    public byte[] toByteArray();
}
