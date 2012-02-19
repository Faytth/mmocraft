package org.unallied.mmocraft.tools.input;

public class GenericLittleEndianAccessor implements LittleEndianAccessor {
    private ByteInputStream bs;

    public GenericLittleEndianAccessor(ByteInputStream bs) {
        this.bs = bs;
    }
    
    @Override
    public byte readByte() {
        return (byte) bs.readByte();
    }

    @Override
    public short readShort() {
        short result = 0;
        result += bs.readByte();
        result += bs.readByte() << 8;
        
        return result;
    }

    @Override
    public int readInt() {
        int result = 0;
        result += bs.readByte();
        result += bs.readByte() << 8;
        result += bs.readByte() << 16;
        result += bs.readByte() << 24;
        
        return result;
    }

    @Override
    public long readLong() {
        long result = 0;
        result += bs.readByte();
        result += bs.readByte() << 8;
        result += bs.readByte() << 16;
        result += bs.readByte() << 24;
        result += bs.readByte() << 32;
        result += bs.readByte() << 40;
        result += bs.readByte() << 48;
        result += bs.readByte() << 56;
        
        return result;
    }

    @Override
    public void skip(int num) {
        for (int i=0; i < num; ++i) {
            readByte();
        }
    }

    @Override
    public byte[] read(int num) {
        byte[] result = new byte[num];
        
        for (int i=0; i < num; ++i) {
            result[i] = readByte();
        }
        
        return result;
    }

    @Override
    public float readFloat() {
        return Float.intBitsToFloat(readInt());
    }

    @Override
    public double readDouble() {
        return Double.longBitsToDouble(readLong());
    }

    @Override
    public String readAsciiString(int n) {
        String result = "";
        
        for(int i=0; i < n; ++i) {
            result += (char) readByte();
        }
        return result;
    }

    @Override
    public String readNullTerminatedAsciiString() {
        String result = "";
        
        for(;;) {
            byte b = readByte();
            if (b != 0) {
                result += (char) b;
            } else {
                break;
            }
        }
        return result;
    }

    @Override
    public String readPrefixedAsciiString() {
        return readAsciiString(readShort());
    }

    @Override
    public long getBytesRead() {
        return bs.getBytesRead();
    }

    @Override
    public long available() {
        return bs.available();
    }

}
