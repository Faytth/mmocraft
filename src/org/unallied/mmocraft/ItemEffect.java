package org.unallied.mmocraft;

import org.unallied.mmocraft.tools.input.SeekableLittleEndianAccessor;
import org.unallied.mmocraft.tools.output.GenericLittleEndianWriter;

/**
 * An item effect.  This is an effect that an item can do, also known as a "Use"
 * effect.  The vast majority of consumables use this type of effect.  Note that
 * not all effects need to be Use effects.  Some can be "Equip" effects and so on.
 * 
 * @author Alexandria
 *
 */
public class ItemEffect {
    private EffectType type;
    private short value;
    
    public ItemEffect(EffectType type, short value) {
        this.type = type;
        this.value = value;
    }
    
    public EffectType getType() {
        return type;
    }
    
    public void setType(EffectType type) {
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
        
        writer.writeShort(type.getValue());
        writer.writeShort(value);
        
        return writer.toByteArray();
    }
    
    /**
     * Retrieves this class from an SLEA, which contains the raw bytes of this class
     * obtained from the getBytes() method.
     * @param slea A seekable little endian accessor that is currently at the position containing
     *             the bytes of an ItemRequirement object.
     * @return itemRequirement
     */
    public static ItemEffect fromBytes(SeekableLittleEndianAccessor slea) {
        EffectType type = EffectType.fromValue(slea.readShort());
        short value = slea.readShort();
        
        return new ItemEffect(type, value);
    }
}
