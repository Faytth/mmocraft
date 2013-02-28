package org.unallied.mmocraft;

import org.unallied.mmocraft.tools.input.GenericSeekableLittleEndianAccessor;
import org.unallied.mmocraft.tools.output.GenericLittleEndianWriter;

/**
 * A class for describing passives, such as monster passives (armor, etc.).
 * @author Alexandria
 *
 */
public class Passive {
    /** The type of the passive, such as Armor. */
    private PassiveType type;
    
    /** The values of the passive. */
    private Number[] values;
    
    /**
     * Creates a new passive object with the following type and values.
     * @param type The type of the passive.
     * @param values The values of the passive.
     */
    public Passive(PassiveType type, Number[] values) {
        this.type = type;
        this.values = values;
    }
    
    /**
     * Retrieves the type of this passive.  The type dictates what values this
     * passive contains.
     * @return
     */
    public PassiveType getType() {
        return type;
    }
    
    /**
     * Retrieves the values used for this passive.
     * @return values
     */
    public Number[] getValues() {
        return values;
    }
    
    /**
     * Retrieves the byte array representation of this passive class.
     * TODO:  Update this to reflect the array of values.  Also needs
     * updating in the monster file format.
     * @return
     */
    public byte[] getBytes() {
        GenericLittleEndianWriter writer = new GenericLittleEndianWriter();
        
        writer.writeShort(type.getValue());
        
        // Write values
        writer.write((byte)values.length);
        for (Number number : values) {
            // FIXME:  This is broken for float values.
            writer.writeInt(number.intValue());
        }
        
        return writer.toByteArray();
    }

    /**
     * Retrieves this class from an SLEA, which contains the raw bytes of this class
     * obtained from the getBytes() method.
     * @param slea A seekable little endian accessor that is currently at the position containing
     *             the bytes of an ItemRequirement object.
     * @return passive
     */
    public static Passive fromBytes(GenericSeekableLittleEndianAccessor slea) {
        PassiveType type = PassiveType.fromValue(slea.readShort());
        int count = slea.readInt();
        Number[] values = new Number[count];
        for (int i=0; i < count; ++i) {
            // FIXME:  This only works with integers
            values[i] = slea.readInt();
        }
        return new Passive(type, values);
    }
}
