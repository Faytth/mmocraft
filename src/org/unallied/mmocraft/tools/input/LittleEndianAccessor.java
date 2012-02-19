package org.unallied.mmocraft.tools.input;

public interface LittleEndianAccessor {
    byte readByte();
    short readShort();
    int readInt();
    long readLong();
    void skip(int num);
    byte[] read(int num);
    float readFloat();
    double readDouble();
    String readAsciiString(int n);
    String readNullTerminatedAsciiString();
    String readPrefixedAsciiString();
    long getBytesRead();
    long available();
}
