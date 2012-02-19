package org.unallied.mmocraft.tools.input;

public interface SeekableLittleEndianAccessor extends LittleEndianAccessor {
    void seek(long offset);
    long getPosition();
}
