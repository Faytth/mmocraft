package org.unallied.mmocraft.monsters;

import java.util.HashMap;
import java.util.Map;

import org.unallied.mmocraft.LootIntf;
import org.unallied.mmocraft.Passive;
import org.unallied.mmocraft.PassiveType;
import org.unallied.mmocraft.animations.AnimationType;
import org.unallied.mmocraft.constants.ClientConstants;
import org.unallied.mmocraft.tools.input.GenericSeekableLittleEndianAccessor;
import org.unallied.mmocraft.tools.output.GenericLittleEndianWriter;

public class ClientMonsterData implements MonsterData {

    /** The monster's id.  This should be unique for each kind of monster. */
    protected final int id;
    
    /** The monster's name. */
    protected final String name;
    
    /** The maximum HP of the monster. */
    protected final int maxHP;
    
    /** The level of the monster. */
    protected final short level;
    
    /** The width of this monster's hit box. */
    protected final short width;
    
    /** The height of this monster's hit box. */
    protected final short height;
    
    /** The damage multiplier to use for the monster's abilities. */
    protected final double damageMultiplier;
    
    /** The movement speed, where 1f is the player's default speed. */
    protected final float movementSpeed;
    
    /** The type of the monster, such as ground or flying. */
    protected final MonsterType type;
    
    /** All of the monster's animations, such as Walk or Idle. */
    protected Map<AnimationType, String> animations = 
            new HashMap<AnimationType, String>();
    
    /** All of the monster's passives, such as HP Regen. */
    protected Map<PassiveType, Passive> passives = 
            new HashMap<PassiveType, Passive>();
    
    /**
     * 
     * @param id
     * @param name
     * @param maxHP
     * @param level
     * @param damageMultiplier
     * @param type
     */
    public ClientMonsterData(final int id, final String name, final int maxHP,
            final short level, final short width, final short height, 
            final double damageMultiplier, final float movementSpeed, 
            final MonsterType type) {
        this.id = id;
        this.name = name;
        this.maxHP = maxHP;
        this.level = level;
        this.width = width;
        this.height = height;
        this.damageMultiplier = damageMultiplier;
        this.movementSpeed = movementSpeed;
        this.type = type;
    }
    
    /**
     * Serializes the bytes for this class.  This is used in place of
     * writeObject / readObject because Java adds a lot of "extra"
     * stuff that isn't particularly useful in this case.
     * 
     * @return byteArray
     */
    public byte[] getBytes() {
        GenericLittleEndianWriter writer = new GenericLittleEndianWriter();
        
        writer.writeInt(id);
        writer.write7BitPrefixedAsciiString(name);
        writer.writeInt(maxHP);
        writer.writeShort(level);
        writer.writeDouble(damageMultiplier);
        writer.write(type.getValue());
        
        // Write animations
        writer.write((byte)animations.size());
        for (Map.Entry<AnimationType, String> animation : animations.entrySet()) {
            writer.writeShort(animation.getKey().getValue());
            writer.write7BitPrefixedAsciiString(animation.getValue());
        }
        
        // Write passives
        writer.write((byte)passives.size());
        for (Passive passive : passives.values()) {
            writer.write(passive.getBytes());
        }
        
        return writer.toByteArray();
    }

    /**
     * Retrieves this class from an SLEA, which contains the raw bytes of this class
     * obtained from the getBytes() method.
     * @param slea A seekable little endian accessor that is currently at the position
     *             containing the bytes of a MonsterData object.
     * @return monsterData
     */
    public static MonsterData fromBytes(GenericSeekableLittleEndianAccessor slea) {
        try {
            if (slea.available() <= 0) {
                return null;
            }
            int id = slea.readInt();
            String name = slea.read7BitPrefixedAsciiString();
            int maxHP = slea.readInt();
            short level = slea.readShort();
            short width = slea.readShort();
            short height = slea.readShort();
            double damageMultiplier = slea.readDouble();
            float movementSpeed = slea.readFloat();
            MonsterType monsterType = MonsterType.fromValue(slea.readByte());
            MonsterData result = new ClientMonsterData(id, name, maxHP, level, 
                    width, height, damageMultiplier, movementSpeed, monsterType);
            
            // Read animations
            int count = slea.readByte();
            for (int i=0; i < count; ++i) {
                AnimationType type = AnimationType.fromValue(slea.readShort());
                String animationLocation = ClientConstants.CLIENT_RESOURCE_ANIMATION_LOCATION + slea.read7BitPrefixedAsciiString();
                result.getAnimations().put(type, animationLocation);
            }
            // Add any animations that must be present but are missing
            try {
                if (!result.getAnimations().containsKey(AnimationType.WALK)) {
                    result.getAnimations().put(AnimationType.WALK, result.getAnimations().get(AnimationType.IDLE));
                }
                if (!result.getAnimations().containsKey(AnimationType.JUMP)) {
                    result.getAnimations().put(AnimationType.JUMP, result.getAnimations().get(AnimationType.IDLE));
                }
                if (!result.getAnimations().containsKey(AnimationType.RUN)) {
                    result.getAnimations().put(AnimationType.RUN, result.getAnimations().get(AnimationType.WALK)); 
                }
                if (!result.getAnimations().containsKey(AnimationType.SHIELD)) {
                    result.getAnimations().put(AnimationType.SHIELD, result.getAnimations().get(AnimationType.IDLE));
                }
                if (!result.getAnimations().containsKey(AnimationType.FALL)) {
                    result.getAnimations().put(AnimationType.FALL, result.getAnimations().get(AnimationType.JUMP));
                }
                if (!result.getAnimations().containsKey(AnimationType.DEAD)) {
                    result.getAnimations().put(AnimationType.DEAD, result.getAnimations().get(AnimationType.IDLE));
                }
            } catch (Throwable t) {
                System.err.println("Error populating monster's animations for monster data ID: " + id + ".\n" +
                        "Note:  All monsters MUST have an IDLE animation.");
                t.printStackTrace();
            }
            
            // Read passives
            count = slea.readByte();
            for (int i=0; i < count; ++i) {
                Passive passive = Passive.fromBytes(slea);
                result.getPassives().put(passive.getType(), passive);
            }
            
            return result;
        } catch (Exception e) {
            return null;
        }
    }
    
    public Integer getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public int getMaxHP() {
        return maxHP;
    }
    
    public short getLevel() {
        return level;
    }
    
    public double getDamageMultiplier() {
        return damageMultiplier;
    }
    
    public MonsterType getType() {
        return type;
    }
    
    public Map<AnimationType, String> getAnimations() {
        return animations;
    }
    
    public Map<PassiveType, Passive> getPassives() {
        return passives;
    }

    @Override
    public int getExperience() {
        return 0;
    }

    @Override
    public int getMinimumGold() {
        return 0;
    }

    @Override
    public int getMaximumGold() {
        return 0;
    }

    @Override
    public LootIntf getLoot() {
        return null;
    }

    @Override
    public short getWidth() {
        return width;
    }

    @Override
    public short getHeight() {
        return height;
    }

    @Override
    public float getMovementSpeed() {
        return movementSpeed;
    }
}
