package org.unallied.mmocraft.tools.input;

import java.io.InputStream;

public abstract class ByteInputStream extends InputStream {
    public abstract int readByte();
    public abstract long getBytesRead();
    public abstract int available();
}
