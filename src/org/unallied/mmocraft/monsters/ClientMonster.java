package org.unallied.mmocraft.monsters;

import org.unallied.mmocraft.BoundLocation;
import org.unallied.mmocraft.Direction;
import org.unallied.mmocraft.Velocity;
import org.unallied.mmocraft.animations.AnimationType;
import org.unallied.mmocraft.client.Game;
import org.unallied.mmocraft.tools.input.SeekableLittleEndianAccessor;

public class ClientMonster extends Monster {

    /**
     * 
     */
    private static final long serialVersionUID = 4938157797304244749L;
    
    public ClientMonster(final MonsterData data, int id, BoundLocation location) {
        super(data, id, location);
    }

    /**
     * Updates the monster's movement, location, and direction.  The data in the
     * SLEA should be from getBytes().
     * @param slea
     */
    public void fromMovement(SeekableLittleEndianAccessor slea) {
        // Guard
        if (slea == null || slea.available() < 1) {
            return;
        }
        setLocation(BoundLocation.fromBytes(slea));
        setState(AnimationType.getState(this, null, slea.readShort()));
        setDirection(slea.readByte() == 0 ? Direction.RIGHT : Direction.LEFT);
        setVelocity(Velocity.fromBytes(slea));
        setFallSpeed(slea.readFloat());
        setInitialVelocity(slea.readFloat());
        
    }
    
    /**
     * Retrieves a new ClientMonster from an SLEA.  This is used by the MonsterInfo
     * handler.
     * @param slea
     * @return
     */
    public static ClientMonster fromBytes(SeekableLittleEndianAccessor slea) {
        // Guard
        if (slea == null || slea.available() < 1) {
            return null;
        }
        
        int dataId = slea.readInt();
        int monsterId = slea.readInt();
        MonsterData data = ClientMonsterManager.getInstance().getMonsterData(dataId);
        ClientMonster monster = new ClientMonster(data, monsterId, new BoundLocation(0, 0));
        monster.fromMovement(slea);
        
        return monster;
    }
    
    @Override
    /**
     * Sets the current HP for this creature.  Removes this creature / activates death animation
     * if the HP reaches 0.
     * Current HP cannot exceed max HP.
     * @param hpCurrent current HP
     */
    public void setHpCurrent(int hpCurrent) {
        synchronized (this) {
            hpCurrent = hpCurrent < 0     ? 0     : hpCurrent;
            hpCurrent = hpCurrent > hpMax ? hpMax : hpCurrent;
            
            if (hpCurrent < this.hpCurrent) {
                showDamaged = true; // Display white "damaged" on next frame.
            }
            
            this.hpCurrent = hpCurrent;
        }
        
        // If monster has died, remove it from the client
        // TODO:  Display a death animation first if it exists.
        if (!isAlive()) {
            Game.getInstance().getClient().monsterPoolSession.removeMonster(getId());
        }
    }
}
