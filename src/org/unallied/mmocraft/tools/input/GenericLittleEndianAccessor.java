package org.unallied.mmocraft.tools.input;

import java.io.IOException;
import java.io.ObjectInputStream;

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
    public int read7BitEncodedInt() {
        int count = 0;
        int shift = 0;
        byte b;
        do {
            b = readByte();
            count |= (b & 0x7F) << shift;
            shift += 7;
        } while ((b & 0x80) != 0 && shift != 5*7); // 5 bytes max to prevent hanging
        
        return count;
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
        result += (long) (bs.readByte());
        result += (long) (bs.readByte()) << 8;
        result += (long) (bs.readByte()) << 16;
        result += (long) (bs.readByte()) << 24;
        result += (long) (bs.readByte()) << 32;
        result += (long) (bs.readByte()) << 40;
        result += (long) (bs.readByte()) << 48;
        result += (long) (bs.readByte()) << 56;
        
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
    public String read7BitPrefixedAsciiString() {
        return readAsciiString(read7BitEncodedInt());
    }
    
    @Override
    public long getBytesRead() {
        return bs.getBytesRead();
    }

    @Override
    public long available() {
        return bs.available();
    }

    @Override
    public Object readObject() {
        try {
            ObjectInputStream ois = new ObjectInputStream(bs);
            return ois.readObject();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

}
