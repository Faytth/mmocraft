package org.unallied.mmocraft.monsters;

import java.util.Map;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;
import org.unallied.mmocraft.BoundLocation;
import org.unallied.mmocraft.CollisionBlob;
import org.unallied.mmocraft.Direction;
import org.unallied.mmocraft.Living;
import org.unallied.mmocraft.Velocity;
import org.unallied.mmocraft.animations.AnimationType;
import org.unallied.mmocraft.animations.generics.GenericIdle;
import org.unallied.mmocraft.client.FontHandler;
import org.unallied.mmocraft.client.FontID;
import org.unallied.mmocraft.constants.ClientConstants;
import org.unallied.mmocraft.constants.WorldConstants;
import org.unallied.mmocraft.gui.fills.BarFill;
import org.unallied.mmocraft.tools.output.GenericLittleEndianWriter;

public abstract class Monster extends Living {    

    /**
     * 
     */
    private static final long serialVersionUID = -882236010116737540L;

    /** 
     * The unique identifier that specifies a specific monster data type for
     * this monster.
     */
    protected int dataId;
    
    protected final MonsterData data;
    
    /** If true, displays a white frame for the living object for one frame to show damage. */
    protected boolean showDamaged = false;
    
    public Monster(final MonsterData data, int id, BoundLocation location) {
        this(data, id, location, AnimationType.IDLE.getValue());
    }
    
    public Monster(final MonsterData data, int id, BoundLocation location, short animationId) {
        super(data.getWidth(), data.getHeight());
        this.data = data;
        this.id = id;
        this.location = location;
        dataId = data.getId();
        hpMax = data.getMaxHP();
        hpCurrent = data.getMaxHP();
        this.name = data.getName();
        this.movementSpeed *= data.getMovementSpeed();
        Map<AnimationType, String> animations = data.getAnimations();
        this.current = new GenericIdle(this, null, animations);
    }

    public void render(GameContainer container, StateBasedGame game, Graphics g, BoundLocation camera) {
        float x = (location.getX() - camera.getX());
        x = x > 0.0f ? x : WorldConstants.WORLD_WIDTH + x;
        x *= WorldConstants.WORLD_BLOCK_WIDTH;
        x += location.getXOffset() - camera.getXOffset();
        x = x > 0.0f ? x : WorldConstants.WORLD_BLOCK_WIDTH + x;
        float y = (location.getY() - camera.getY()) * WorldConstants.WORLD_BLOCK_HEIGHT;
        y += location.getYOffset() - camera.getYOffset();
        
        current.render(x, y, direction == Direction.LEFT, showDamaged);
        showDamaged = false;
        
        try {
            Color hpColor = new Color(0, 170, 0);
            final String NAME_FONT = FontID.STATIC_TEXT_SMALL_BOLD.toString();
            float centeredX = (int)(x + collisionWidth / 2 - FontHandler.getInstance().getMaxWidth(NAME_FONT, name) / 2);
            
            // Render monster name.
            Color nameColor = ClientConstants.MONSTER_NAME_NORMAL;
            FontHandler.getInstance().draw(NAME_FONT, name, centeredX, 
                    y + collisionHeight + 5, nameColor, -1, -1, false);

            // Render HP bar
            if (this.hpCurrent != this.hpMax) {
                final int HP_HEIGHT = 2;
                float hpWidth = collisionWidth * 1.5f;
                g.fill(new org.newdawn.slick.geom.Rectangle(centeredX, y - HP_HEIGHT, hpWidth, HP_HEIGHT), 
                        new BarFill(new Color(255, 0, 0)));
                if (hpCurrent > 0) {
                    float remainingHpWidth = 1.0f * hpWidth * hpCurrent / hpMax;
                    remainingHpWidth = remainingHpWidth < 1 ? 1 : remainingHpWidth;
                    g.fill(new org.newdawn.slick.geom.Rectangle(centeredX, y - HP_HEIGHT, 
                            remainingHpWidth + 2, HP_HEIGHT), new BarFill(hpColor));
                }
                Color oldColor = g.getColor();
                g.setColor(new Color(0, 0, 0));
                g.drawRect(centeredX, y - HP_HEIGHT - 1, hpWidth, HP_HEIGHT + 1);
                g.setColor(oldColor);
            }
        } catch (Throwable t) {
            // We really don't care about rendering errors.
        }
    }
    
    public void update(long delta) {
        BoundLocation tempLocation = new BoundLocation(location);
        
        // Perform gravity checks
        accelerateDown((int)delta, ClientConstants.FALL_ACCELERATION * current.moveDownMultiplier(), 
                ClientConstants.FALL_TERMINAL_VELOCITY * current.moveDownMultiplier());
        
        if (this.getHpCurrent() <= 0) {
            current.die(); // No need to announce.  Server doesn't accept changes if you're dead.
        }
        
        /*
         *  TODO:  This is a pretty big kludge.  When a collision occurs, move 
         *         needs to use proper physics to fix the destination.
         */
        Velocity newVelocity = new Velocity(velocity);
        newVelocity.setY(0);
        move((int)delta, newVelocity, 0, 0);
        newVelocity.setY(velocity.getY());
        newVelocity.setX(0);
        move((int)delta, newVelocity, fallSpeed, initialVelocity);
        
        current.update(delta);
        if (isStuck()) {
            location = tempLocation;
        }
        unstuck();
    }
    
    public void doCollisionChecks(CollisionBlob[] collisionArc, int startingIndex,
            int endingIndex, float horizontalOffset, float verticalOffset) {
        // Intentionally left as a no-op.
    }
    
    /**
     * Returns the data id.  This is the id for the monster's data, as found in
     * monsters.ump.
     * @return dataId
     */
    public int getDataId() {
        return dataId;
    }
    
    /**
     * Retrieves the bytes needed to construct this monster's current location
     * and animation as well as any movement.
     * @return bytes
     */
    public byte[] getMovement() {
        GenericLittleEndianWriter writer = new GenericLittleEndianWriter();
        
        writer.writeInt(getId());
        writer.write(location.getBytes());
        writer.writeShort(current.getId());
        writer.write(direction.getValue());
        writer.write(velocity.getBytes());
        writer.writeFloat(fallSpeed);
        writer.writeFloat(initialVelocity);

        return writer.toByteArray();
    }
    
    /**
     * Retrieves the bytes that make up this monster.  This includes its current HP,
     * monster type, location, and other information needed by the client to properly
     * construct an instance of this monster.
     * @return bytes
     */
    public byte[] getBytes() {
        GenericLittleEndianWriter writer = new GenericLittleEndianWriter();
        
        writer.writeInt(getDataId());
        writer.write(getMovement());
        writer.writeInt(hpCurrent);
        
        return writer.toByteArray();
    }
}
