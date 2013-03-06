package org.unallied.mmocraft;

import java.awt.Rectangle;
import java.io.Serializable;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;
import org.unallied.mmocraft.animations.AnimationState;
import org.unallied.mmocraft.animations.sword.SwordIdle;
import org.unallied.mmocraft.client.FontHandler;
import org.unallied.mmocraft.client.FontID;
import org.unallied.mmocraft.client.Game;
import org.unallied.mmocraft.client.MMOClient;
import org.unallied.mmocraft.constants.ClientConstants;
import org.unallied.mmocraft.constants.WorldConstants;
import org.unallied.mmocraft.gui.GUIUtility;
import org.unallied.mmocraft.gui.fills.BarFill;
import org.unallied.mmocraft.items.Inventory;
import org.unallied.mmocraft.items.ItemRequirement;
import org.unallied.mmocraft.net.Packet;
import org.unallied.mmocraft.net.PacketCreator;
import org.unallied.mmocraft.sessions.TerrainSession;
import org.unallied.mmocraft.skills.SkillType;
import org.unallied.mmocraft.skills.Skills;
import org.unallied.mmocraft.tools.Authenticator;

/**
 * Contains all information for a given player.
 * TODO:  Generalize this into Player and ClientPlayer (just like how monsters
 * have Monster and ClientMonster).
 * @author Faythless
 *
 */
public class Player extends Living implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 5548280611678732479L;
    
    /** The HP gained with each level. */
    protected static final transient int PLAYER_HP_MULTIPLIER = 10;
    
    /** The player's starting HP. */
    protected static final transient int PLAYER_BASE_HP = 50;
    
    /** Used in calculations for the player's HP restoration rate. */
    protected long elapsedTime = 0;
    
    /**
     * The amount of time in milliseconds that it takes to restore all of a
     * player's HP.
     */
    protected static final int HP_RESTORE_RATE = 300000;

    /** 
     * The amount of time in milliseconds that HP restoration is halted for
     * upon receiving damage. 
     * */
    protected static final int HP_RESTORE_DAMAGE_DELAY = 10000;
    
    /** The inventory of the player. */
    protected transient Inventory inventory = new Inventory();
    
    /** The player's skills. */
    protected transient Skills skills = new Skills();

    /** 
     * The time at which the player's PvP will expire. A value of -1 indicates 
     * indefinite.  This only counts for other players.
     */
    protected long pvpExpireTime = 0;
    
    /** If true, displays a white frame for the living object for one frame to show damage. */
    protected boolean showDamaged = false;
    
    public Player() {
        super(24, 43); // TODO:  Find a better way of providing player width and height
        current = new SwordIdle(this, null);
    }
    
    public Player(Player other) {
        super(other);
        this.collisionWidth = 24;
        this.collisionHeight = 43;
        current = other.current;
        shielding = other.shielding;
        attacking = other.attacking;
        hitbox = other.hitbox;
        initialVelocity = other.initialVelocity;
        movementSpeed = other.movementSpeed;
        fallSpeed = other.fallSpeed;
        id = other.id;
        direction = other.direction;
        velocity = other.velocity;
        inventory = other.inventory;
        skills = other.skills;
        pvpExpireTime = other.pvpExpireTime;
    }

    /**
     * Updates current HP, buff duration, and so on.
     * @param delta The length of time that has passed since the last update in
     *              milliseconds.
     */
    public void updateLogic(long delta) {
        elapsedTime += delta;
        int hpToRestore = (int) (elapsedTime * getHpMax() / HP_RESTORE_RATE);
        if (hpToRestore > 0) {
            elapsedTime -= hpToRestore * HP_RESTORE_RATE / getHpMax();
            restoreHp(hpToRestore);
        }
    }
    
    /**
     * Sets the current HP for this creature.
     * Current HP cannot exceed max HP.
     * @param hpCurrent current HP
     */
    public void setHpCurrent(int hpCurrent) {
        synchronized (this) {
            boolean isDamaged = hpCurrent < this.hpCurrent;
            this.hpCurrent = hpCurrent;
            
            // Make sure we didn't go over the max
            if (this.hpCurrent > hpMax) {
                this.hpCurrent = hpMax; // If we went over, set the HP to max
            } else if (this.hpCurrent < 0) { // ... Or the minimum
                this.hpCurrent = 0;
            }
            
            // If the player was damaged, halt HP restoration.
            if (isDamaged) {
                elapsedTime = -HP_RESTORE_DAMAGE_DELAY;
                showDamaged = true;
            }
        }
    }
    
    /**
     * Restores up to <code>hp</code> hit points.  If the amount restored would
     * go over the max HP, the HP is set to the max HP.  Attempting to restore
     * HP while dead will do nothing.
     * @param hp The amount of HP to restore.
     */
    public void restoreHp(int hp) {
        if (hpCurrent > 0) {
            int newHp = hpCurrent + hp;
            hpCurrent = newHp > hpMax ? hpMax : newHp;
        }
    }
    
    /**
     * Sends a packet to the server if this player is the client's player.
     * @param packet The packet to send
     */
    protected void sendPacket(Packet packet) {
        if (this == Game.getInstance().getClient().getPlayer()) {
            Game.getInstance().getClient().announce(packet);
        }
    }
        
    @Override
    /**
     * Sets the animation state to a new state.
     * @param newState the new animation state
     */
    public void setState(AnimationState newState) {
        if (this.current != newState) {
            this.current = newState;
            sendPacket(PacketCreator.getPlayerMovement(this));
        }
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
            Color hpColor;
            // Color the HP bar based on if we're the player or not.
            if (Game.getInstance().getClient().getPlayer() == this) {
                hpColor = new Color(0, 2, 150);
            } else {
                hpColor = new Color(0, 170, 0);
            }
            final String NAME_FONT = FontID.STATIC_TEXT_SMALL_BOLD.toString();
            float centeredX = (int)(x + collisionWidth / 2 - FontHandler.getInstance().getMaxWidth(NAME_FONT, name) / 2);
            if (Game.getInstance().getClient().getPlayer() != this) {
                // Render player name if this player is not the current player.
                Color nameColor = isPvPFlagEnabled() ? ClientConstants.PLAYER_NAME_PVP : ClientConstants.PLAYER_NAME_NORMAL;
                FontHandler.getInstance().draw(NAME_FONT, name, centeredX, 
                        y + collisionHeight + 5, nameColor, -1, -1, false);
            }
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
                            remainingHpWidth + 0, HP_HEIGHT), new BarFill(hpColor));
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
    
    private void updateMovement(int delta) {
        if (this != Game.getInstance().getClient().getPlayer()) {
            return;
        }
        
        boolean idle = true;
        GameContainer container = Game.getInstance().getContainer();
        ControlIntf controls = Game.getInstance().getControls();
        Input input = Game.getInstance().getContainer().getInput();
        
        // Perform movement
        if (GUIUtility.getInstance().getActiveElement() == null && 
                container.hasFocus()) {
            if (Authenticator.canLivingMove(this)) {
                if (controls.isMovingLeft(input)) {
                    idle &= !this.tryMoveLeft(delta);
                }
                if (controls.isMovingRight(input)) {
                    idle &= !this.tryMoveRight(delta);
                }
                if (idle && this.getState().canChangeVelocity()) {
                    this.setVelocity(0, this.getVelocity().getY());
                }
                if (controls.isMovingUp(input)) {
                    this.tryMoveUp(delta);
                    idle = false;
                } else {
                    this.setMovingUp(false); // Have to tell the player that we're not moving up
                }
                if (controls.isMovingDown(input)) {
                    this.tryMoveDown(delta);
                    idle = false;
                }
                if (!controls.isMovingUp(input) && !controls.isMovingDown(input)) {
                    if (this.getState().canChangeVelocity()) {
                        this.setVelocity(this.getVelocity().getX(), 0);
                    }
                }
            }        
        
            // perform attacks
            if (Authenticator.canLivingAttack(this)) {
                this.attackUpdate(controls.isBasicAttack(input));
            }
        }

        if (idle) {
            this.idle();
            if (this.getState().canChangeVelocity()) {
                this.setVelocity(0, this.getVelocity().getY());
            }
        }
        
        // Perform player-based controls
        if (Authenticator.canLivingMove(this)) {
            try {
                if (Game.getInstance().getClient().getPlayer() == this) {
                    if (GUIUtility.getInstance().getActiveElement() == null) {
                        // Perform shielding
                        shieldUpdate(controls.isShielding(input));
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
    
    public void update(long delta) {
        updateLogic(delta);
        updateMovement((int)delta);
        
        BoundLocation tempLocation = new BoundLocation(location);
        
        // Perform gravity checks
        accelerateDown((int)delta, ClientConstants.FALL_ACCELERATION * current.moveDownMultiplier(), 
                ClientConstants.FALL_TERMINAL_VELOCITY * current.moveDownMultiplier());
        
        if (!this.isAlive()) {
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
        
    @Override
    /**
     * Sets the direction that the player is facing (left or right).
     * 
     * This function will also send a packet to the server when needed.
     * 
     * @param direction the direction the player should face
     */
    public void updateDirection(Direction direction) {
        // If we need to update the direction
        if (this.direction != direction) {
            this.direction = direction;
            sendPacket(PacketCreator.getPlayerDirection(this));
        }
    }

    public void init() {
        super.init();
    }

    @Override
    /**
     * Retrieves whether or not the player is holding down the shield button.
     * @return shielding
     */
    public boolean isShielding() {
        return shielding;
    }

    /**
     * Returns the player's inventory, which contains all items that the player has.
     * @return inventory
     */
	public Inventory getInventory() {
		return inventory;
	}

	/**
	 * Sets the player's inventory to the new inventory if the new inventory is not null.
	 * @param inventory
	 */
    public void setInventory(Inventory inventory) {
        if (inventory != null) {
            this.inventory = inventory;
        }
    }

    /**
     * Retrieves the player's skills, which contains all of the experience that the player has.
     * @return skills The player's skills.
     */
    public Skills getSkills() {
        return skills;
    }
    
    /**
     * Sets the player's skills to the new skills if the new skills list is not null.
     * @param skills The player's new skills.
     */
    public void setSkills(Skills skills) {
        if (skills != null) {
            this.skills = skills;
        }
    }
    
    /**
     * Returns whether the player meets the requirement.  This is based on the
     * player's skill levels.  To meet a requirement, a player's skill level
     * must be >= the requirement.
     * @param requirement The requirement to meet
     * @return true if the player meets the requirement; else false
     */
	public boolean meetsRequirement(ItemRequirement requirement) {
	    // TODO:  Implement this method.
		return true;
	}
	
    public double getPvPDamageMultiplier() {
        return 1000.0 + this.getSkills().getLevel(SkillType.STRENGTH) * 150.0;
    }
    
    public double getPvMDamageMultiplier() {
        return 1000.0 + this.getSkills().getLevel(SkillType.STRENGTH) * 150.0;
    }
	
	/**
	 * Retrieves the damage multiplier based on the player's gear, primary
	 * skills, secondary skills, and buffs
	 * @return damageMultiplier
	 */
	public double getBlockDamageMultiplier() {
	    return 1000.0 + this.getSkills().getLevel(SkillType.MINING) * 50.0;
	}
	
	/**
	 * Displays the damage dealt to a block.
	 * @param x The global x position of the block.  Each block is 1 unit.
	 * @param y The global y position of the block.  Each block is 1 unit.
	 * @param damage The amount of damage dealt
	 */
	public void displayBlockDamage(long x, long y, int damage) {
	    MMOClient client = Game.getInstance().getClient();
	    if (this == client.getPlayer()) {
	        client.damageSession.addDamage(new Location(x, y), damage);
	    } else {
	        client.damageSession.addDamage(new Location(x, y), damage, ClientConstants.DAMAGE_BLOCK_OTHER_PLAYER_COLOR);
	    }
	}
	
    public void doCollisionChecks(CollisionBlob[] collisionArc, int startingIndex,
            int endingIndex, float horizontalOffset, float verticalOffset) {
        // Guard
        if (collisionArc == null || startingIndex < 0 || endingIndex < 0 || 
                startingIndex >= collisionArc.length || endingIndex >= collisionArc.length) {
            return;
        }
    
        try {
            int curIndex = startingIndex - 1;
            do {
                curIndex = (curIndex + 1) % collisionArc.length;
                
                TerrainSession ts = Game.getInstance().getClient().getTerrainSession();
                Location topLeft = new Location(this.location); // top left corner of our collision box
                if (direction == Direction.RIGHT) {
                    topLeft.moveDown(verticalOffset + collisionArc[curIndex].getYOffset());
                    topLeft.moveRight(horizontalOffset + collisionArc[curIndex].getXOffset());
                } else { // Flipped collision stuff.  This was such a pain to calculate.
                    topLeft.moveDown(verticalOffset + collisionArc[curIndex].getYOffset());
                    topLeft.moveRight(getWidth() - horizontalOffset - collisionArc[curIndex].getXOffset() - collisionArc[curIndex].getWidth());
                }
                Location bottomRight = new Location(topLeft); // bottom right corner of our collision box
                bottomRight.moveDown(collisionArc[curIndex].getHeight());
                bottomRight.moveRight(collisionArc[curIndex].getWidth());
                
                if (!topLeft.equals(bottomRight)) {
                    /*
                     *  We now have the topLeft and bottomRight coords of our rectangle.
                     *  Using this, we need to grab every block in our rectangle for collision
                     *  testing.
                     */
                    for (long x = topLeft.getX(); x <= bottomRight.getX(); ++x) {
                        for (long y = topLeft.getY(); y <= bottomRight.getY(); ++y) {
                            try {
                                if (ts.getBlock(x, y).isCollidable()) {
                                    int xOff = 0;
                                    if (direction == Direction.RIGHT) {
                                        xOff = (int) (((x - this.location.getX()) * WorldConstants.WORLD_BLOCK_WIDTH - horizontalOffset - collisionArc[curIndex].getXOffset() - this.location.getXOffset()));
                                    } else {
                                        xOff = (int) (-this.location.getXOffset() + current.getWidth() - ((this.location.getX() - x) * WorldConstants.WORLD_BLOCK_WIDTH + getWidth() - horizontalOffset + collisionArc[curIndex].getFlipped().getXOffset()));
                                    }
                                    int yOff = (int) (((y - this.location.getY()) * WorldConstants.WORLD_BLOCK_HEIGHT - verticalOffset - collisionArc[curIndex].getYOffset() - this.location.getYOffset()));
                                    float damage =  (direction == Direction.RIGHT ? collisionArc[curIndex] : collisionArc[curIndex].getFlipped()).getDamage(
                                            new Rectangle(WorldConstants.WORLD_BLOCK_WIDTH, WorldConstants.WORLD_BLOCK_HEIGHT), xOff, yOff);
                                    if (damage > 0) {
                                        // display damage
                                        displayBlockDamage(x, y, (int)Math.round(getBlockDamageMultiplier() * damage));
        //                                ts.setBlock(x, y, new AirBlock());
                                    }
                                }
                            } catch (NullPointerException e) {
                            }
                        }
                    }
                    if (this == Game.getInstance().getClient().getPlayer()) {
                        sendPacket(PacketCreator.getBlockCollisionPacket(
                                startingIndex, endingIndex, horizontalOffset, verticalOffset));
                    }
                }
            } while (curIndex != endingIndex);
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace(); // This should only happen if someone screwed up the arc image...
        }
    }

    @Override
    /**
     * Sets the velocity to x and y.  These values should be in pixels / millisecond.
     * @param x The horizontal velocity to set in pixels / millisecond.
     * @param y The vertical velocity to set in pixels / millisecond.
     */
    public void setVelocity(float x, float y) {
        if (x != velocity.getX() || y != velocity.getY()) {
            velocity.setX(x);
            velocity.setY(y);
            try {
                sendPacket(PacketCreator.getPlayerMovement(this));
            } catch (Throwable t) {
                // We don't care if this fails.
            }
        }
    }
    
    /**
     * Sets the PvP flag timer.
     * @param toggleTime The time in milliseconds at which the PvP flag will
     *                   expire.  A value of -1 indicates that the PvP flag
     *                   will not expire.
     */
    public void setPvPTime(long toggleTime) {
        this.pvpExpireTime = toggleTime;
    }
    
    /**
     * Retrieves the PvP flag timer.
     * @return pvpFlagTimer A value of -1 indicates that the PvP flag will not
     *                      expire.  Otherwise this is the time since the Epoch
     *                      in milliseconds at which the PvP flag will expire.
     */
    public long getPvPTime() {
        return this.pvpExpireTime;
    }
    
    /**
     * Retrieves the loot multiplier.  This is the number of "chances" a player
     * has at receiving loot.  The default should be 1.  Can be increased by
     * "Magic Find" boosts.
     * @return lootMultiplier
     */
    public double getLootMultiplier() {
        // TODO:  Implement this.
        return 1.0;
    }
    
    /**
     * Retrieves whether the player's PvP flag is on.
     * @return pvpFlagEnabled
     */
    public boolean isPvPFlagEnabled() {
        return this.pvpExpireTime == -1 || 
                this.pvpExpireTime > System.currentTimeMillis();
    }
    
    /**
     * Recalculates damage, HP, and others stats of the player.  These stats
     * are the combination of player levels and equipped items / buffs.
     * This function should be called whenever the player levels up, equipment
     * is changed, or a buff is added or removed.
     */
    public void recalculateStats() {
        // Calculate HP
        setHpMax(getSkills().getLevel(SkillType.CONSTITUTION) * PLAYER_HP_MULTIPLIER + PLAYER_BASE_HP);
    }
}
