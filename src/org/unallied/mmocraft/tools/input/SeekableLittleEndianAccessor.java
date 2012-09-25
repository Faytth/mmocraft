package org.unallied.mmocraft.tools.input;

public interface SeekableLittleEndianAccessor extends LittleEndianAccessor {
    public void seek(long offset);
    public long getPosition();
    public String read7BitPrefixedAsciiString();
}
