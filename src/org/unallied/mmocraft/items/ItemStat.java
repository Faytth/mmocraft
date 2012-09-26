package org.unallied.mmocraft.items;

import org.unallied.mmocraft.StatType;
import org.unallied.mmocraft.tools.input.SeekableLittleEndianAccessor;
import org.unallied.mmocraft.tools.output.GenericLittleEndianWriter;

/**
 * An item requirement.  This is a requirement that must be met in order to use
 * or equip the item.
 * 
 * @author Alexandria
 *
 */
public class ItemStat {
    private StatType type;
    private short value;
    
    public ItemStat(StatType type, short value) {
        this.type = type;
        this.value = value;
    }
    
    public StatType getType() {
        return type;
    }
    
    public void setType(StatType type) {
        this.type = type;
    }
    
    public short getValue() {
        return value;
    }
    
    public void setValue(short value) {
        this.value = value;
    }

    public byte[] getBytes() {
        GenericLittleEndianWriter writer = new GenericLittleEndianWriter();
        
        writer.write(type.getValue());
        writer.writeShort(value);
        
        return writer.toByteArray();
    }
    
    /**
     * Retrieves this class from an SLEA, which contains the raw bytes of this class
     * obtained from the getBytes() method.
     * @param slea A seekable little endian accessor that is currently at the position containing
     *             the bytes of an ItemStat object.
     * @return itemStat
     */
    public static ItemStat fromBytes(SeekableLittleEndianAccessor slea) {
        StatType type = StatType.fromValue(slea.readByte());
        short value = slea.readShort();
        
        return new ItemStat(type, value);
    }
}
