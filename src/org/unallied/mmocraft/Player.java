package org.unallied.mmocraft;

import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;
import org.unallied.mmocraft.animations.AnimationState;
import org.unallied.mmocraft.animations.sword.SwordIdle;
import org.unallied.mmocraft.blocks.Block;
import org.unallied.mmocraft.client.FontHandler;
import org.unallied.mmocraft.client.FontID;
import org.unallied.mmocraft.client.Game;
import org.unallied.mmocraft.client.MMOClient;
import org.unallied.mmocraft.constants.ClientConstants;
import org.unallied.mmocraft.constants.WorldConstants;
import org.unallied.mmocraft.gui.GUIUtility;
import org.unallied.mmocraft.items.Inventory;
import org.unallied.mmocraft.items.ItemRequirement;
import org.unallied.mmocraft.net.PacketCreator;
import org.unallied.mmocraft.sessions.TerrainSession;
import org.unallied.mmocraft.skills.Skills;

/**
 * Contains all information for a given player.
 * @author Faythless
 *
 */
public class Player extends Living implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 5548280611678732479L;
    
    /** The current state of the player. */
    protected transient AnimationState current = null;
    
    /** True if the player is holding down the shield button. */
    protected transient boolean shielding = false;
    
    /** True if the player is currently in the middle of charging an attack. */
    protected transient boolean attacking = false;
    
    /**
     * The width of the player's hitbox.  Used in bounding box checks.
     * Starting location of the bounding box is <code>location</code>.
     */
    protected transient static final int collisionWidth = 24;
    
    /**
     * The height of the player's hitbox.  Used in bounding box checks.
     * Starting location of the bounding box is <code>location</code>.
     */
    protected transient static final int collisionHeight = 43;

    /**
     * Collection of the "boundary points" that need to be checked for player
     * movement across the world.  These should be no farther than one block
     * apart.
     */
    protected transient List<RawPoint> hitbox = null;
    
    protected float movementSpeed = 300f; // Determines the rate of movement for this player pixels / second)
    protected float fallSpeed = 0.0f; // The rate that the player is currently falling
    protected int playerId; // The unique ID of this player
    
    protected Direction direction = Direction.RIGHT; // direction that the player is facing
    
    /**
     * The player's movement.
     */
    protected transient Velocity velocity = new Velocity(0, 0);
    
    /** The inventory of the player. */
    protected transient Inventory inventory = new Inventory();
    
    /** The player's skills. */
    protected transient Skills skills = new Skills();
    
    public Player() {
        super();
        current = new SwordIdle(this, null);
    }
    
    /**
     * Returns this player's unique identifier
     * @return playerId
     */
    public int getId() {
        return playerId;
    }

    /**
     * Sets the player's unique identifier.
     * @param playerId new player ID.
     */
    public void setId(int playerId) {
        this.playerId = playerId;
    }

    /**
     * Returns the current animation state
     * @return current
     */
    public AnimationState getState() {
        return current;
    }
    
    /**
     * Sets the animation state to a new state.
     * @param newState the new animation state
     */
    public void setState(AnimationState newState) {
        if (this.current != newState) {
            this.current = newState;
            if (this == Game.getInstance().getClient().getPlayer()) {
                Game.getInstance().getClient().announce(
                        PacketCreator.getMovement(this) );
            }
        }
    }
    
    /**
     * Renders the player on the screen.  This should be done before the UI
     * but after pretty much everything else.
     * @param container
     * @param game
     * @param g
     * @param camera 
     */
    public void render(GameContainer container, StateBasedGame game, Graphics g, BoundLocation camera) {
        float x = (location.getX() - camera.getX());
        x = x > 0.0f ? x : WorldConstants.WORLD_WIDTH + x;
        x *= WorldConstants.WORLD_BLOCK_WIDTH;
        x += location.getXOffset() - camera.getXOffset();
        x = x > 0.0f ? x : WorldConstants.WORLD_BLOCK_WIDTH + x;
        float y = (location.getY() - camera.getY()) * WorldConstants.WORLD_BLOCK_HEIGHT;
        y += location.getYOffset() - camera.getYOffset();
        
        current.render(x, y, direction == Direction.LEFT);
        
        // Render player name if this player is not the current player.
        try {
            if (Game.getInstance().getClient().getPlayer() != this) {
                final String NAME_FONT = FontID.STATIC_TEXT_SMALL_BOLD.toString();
                float centeredX = (int)(x + collisionWidth / 2 - FontHandler.getInstance().getMaxWidth(NAME_FONT, name) / 2);
                FontHandler.getInstance().draw(NAME_FONT, name, centeredX, y + collisionHeight + 5, new Color(255, 255, 255), -1, -1, false);
            }
        } catch (Throwable t) {
            // We really don't care about rendering errors.
        }
        
    }
    
    /**
     * Updates the player, including animations.
     * @param delta time since last update.
     */
    public void update(long delta) {
        BoundLocation tempLocation = new BoundLocation(location);
        Controls controls = Game.getInstance().getControls();
        Input input = Game.getInstance().getContainer().getInput();
        
        // Perform gravity checks
        accelerateDown((int)delta, ClientConstants.FALL_ACCELERATION * current.moveDownMultiplier(), 
                ClientConstants.FALL_TERMINAL_VELOCITY * current.moveDownMultiplier());
        
        /*
         *  TODO:  This is a pretty big kludge.  When a collision occurs, move 
         *         needs to use proper physics to fix the destination.
         */
        float xVelocity = velocity.getX();
        float yVelocity = velocity.getY();
        float fallVelocity = fallSpeed;
        velocity.setY(0);
        fallSpeed = 0;
        move((int)delta);
        velocity.setY(yVelocity);
        fallSpeed = fallVelocity;
        velocity.setX(0);
        move((int)delta);
        velocity.setX(xVelocity);
        
        // Perform player-based controls
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

        current.update(delta);
        if (isStuck()) {
            location = tempLocation;
        }
        unstuck();
    }
    
    /**
     * Given a start and end, returns the farthest possible location after
     * collision is accounted for (such as running into a wall)
     * @param start Starting location
     * @param end Ending location
     * @param vf final downwards velocity
     * @return
     */
    public Location collide(Location start, Location end, float vf) {
        Location result = new BoundLocation(start);
        try {
            TerrainSession ts = Game.getInstance().getClient().getTerrainSession();
            
            // If air
            Location horizontalCollide = new BoundLocation(ts.collideWithBlock(start, end));
            result.setRawX(horizontalCollide.getRawX());
            
            // Vertical testing
            if (end.getRawY() != start.getRawY()) {
                Location verticalEnd = new BoundLocation(result);
                verticalEnd.setRawY(horizontalCollide.getRawY());
                               
                result = horizontalCollide;
            }
        } catch (NullPointerException e) {
            
        }
        return result;
    }
    /*
     public Location collide(Location start, Location end, float vf) {
        Location result = new BoundLocation(start);
        try {
            TerrainSession ts = Game.getInstance().getClient().getTerrainSession();
            
            // We split this into horizontal then vertical testing.
            Location horizontalEnd = new BoundLocation(start);
            horizontalEnd.setRawX(end.getRawX());
            
            // If air
            Location horizontalCollide = new BoundLocation(ts.collideWithBlock(start, horizontalEnd));
            result.setRawX(horizontalCollide.getRawX());
            
            // Vertical testing
            if (end.getRawY() != start.getRawY()) {
                Location verticalEnd = new BoundLocation(result);
                verticalEnd.setRawY(end.getRawY());
                
                // If air
                Location verticalCollide = new BoundLocation(ts.collideWithBlock(result, verticalEnd));
                // If we didn't hit anything
                if (verticalCollide.equals(verticalEnd)) {
                    fallSpeed = vf;
                    if (fallSpeed > 0.0f) { // tell our state that our player is falling
                        current.fall();
                    }
                } else if (fallSpeed < 0.0f) { // We hit the ceiling!
                    fallSpeed = 0.0f;
                } else { // we landed on something!
                    current.land(); // tell our state that our player has landed on the ground
                    fallSpeed = 0.0f;
                }
                result = verticalCollide;
            }
        } catch (NullPointerException e) {
            
        }
        return result;
    }
     */
    
    /**
     * Returns whether or not the player's hitbox is currently overlapping with
     * a collidable block.
     * @return stuck.  True if the player is stuck; else false.
     */
    public boolean isStuck() {
        TerrainSession ts = Game.getInstance().getClient().getTerrainSession();
        if (ts == null) {
            return false;
        }
        
        for (RawPoint p : hitbox) {
            Location start = new Location(location);
            start.moveRawRight(p.getX());
            start.moveRawDown(p.getY());
            Block block = ts.getBlock(start);
            if (block != null && block.isCollidable()) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Gets the player out of a place that they would normally be stuck at.
     * The way this works is it iterates over all points in the player's hitbox.
     * If any point is colliding with a block, it shifts the player up and to the left.
     * This continues until the player is no longer colliding with a block.
     */
    public void unstuck() {
        while (isStuck()) {
            location.setXOffset(0);
            location.setYOffset(0);
            location.decrementX();
            location.decrementY();
        }
    }
        
    /**
     * Attempts to move left if the animation allows it.
     * @param delta The time in milliseconds since the last update
     * @return true if the player was able to move left, else false.
     */
    public boolean tryMoveLeft(int delta) {
        current.moveLeft(movementSpeed > 0.2f); // any slower movement than this means walk
        if (current.canMoveLeft() && current.canChangeVelocity()) {
            setVelocity(-movementSpeed, velocity.getY());
        }
        return current.canMoveLeft() && current.canChangeVelocity();
    }
    
    /**
     * Moves character horizontally (left / right).  Distance is determined by:
     * (deltaTime) * (velocity.x), where deltaTime is the difference in time
     * between the last and current input poll (typically a single frame)
     * 
     * @param delta The time in milliseconds since the last update
     */
    public void move(int delta) {
        // TODO:  Break this into several steps.  We must pass in a Location for each pixel.
        //        We pass in the modified location to the next Location, and "daisy chain"
        //        them all together.  Our end result is the farthest we can move.
        
        // Get new end location
        
        // Make sure player can move left
        RawPoint distance = new RawPoint();
        distance.setLocation((long) (velocity.getX() * delta / 1000 * Location.BLOCK_GRANULARITY / WorldConstants.WORLD_BLOCK_WIDTH), 
                (long) ((velocity.getY() + fallSpeed) * delta / 1000 * Location.BLOCK_GRANULARITY / WorldConstants.WORLD_BLOCK_HEIGHT));
        RawPoint startingDistance = new RawPoint(distance.getX(), distance.getY());
        // At this point, end is where we are trying to move to
                    
        // Iterate over all points in our hit box, and fix our end location as needed.
        for (RawPoint p : hitbox) {
            // our location should be the top-left corner.  Fix offsets as needed for start / end
            Location start = new Location(location);
            start.moveRawRight(p.getX());
            start.moveRawDown(p.getY());
            Location end = new Location(start);
            end.moveRawRight(distance.getX());
            end.moveRawDown(distance.getY());
            
            // Get our new location
            Location newEnd = collide(start, end, fallSpeed);
            
            // We now need to fix our distance based on our new end
            distance.setLocation(newEnd.getRawDeltaX(start), newEnd.getRawDeltaY(start));
        }
        
        if (!current.isInAir()) {
            // At this point, distance is the farthest to the left we can move
            if (!distance.equals(startingDistance)) { // We must have collided with the ground
                /*
                 *  Try to move up by a block and see if we can go any farther.
                 *  If we can, then we want to use the new distance
                 */
                Location newLocation = new BoundLocation(location);
                newLocation.moveRawRight(distance.getX());
                newLocation.moveRawDown(distance.getY());
                newLocation.moveRawUp(Location.BLOCK_GRANULARITY);
                RawPoint newDistance = new RawPoint(startingDistance.getX()-distance.getX(),
                        startingDistance.getY()-distance.getY()); // remaining distance to travel
                
                // Cycle through all points to see if we can go left now
                for (RawPoint p : hitbox) {
                    // our location should be the top-left corner.  Fix offsets as needed for start / end
                    Location start = new BoundLocation(newLocation);
                    start.moveRawRight(p.getX());
                    start.moveRawDown(p.getY());
                    Location end = new BoundLocation(start);
                    end.moveRawRight(newDistance.getX());
                    end.moveRawDown(newDistance.getY());
                    
                    // Get our new location
                    Location newEnd = collide(start, end, fallSpeed);
                    
                    // We now need to fix our distance based on our new end
                    newDistance.setLocation(newEnd.getRawDeltaX(start), newEnd.getRawDeltaY(start));
                }
                
                // If we were able to move, then keep it.  Otherwise use the old distance
                if (velocity.getX() < 0) {
                    if (newDistance.getX() < distance.getX()) {
                        distance = newDistance;
                        location.moveRawUp(Location.BLOCK_GRANULARITY);
                    }
                } else {
                    if (newDistance.getX() > distance.getX()) {
                        distance = newDistance;
                        location.moveRawUp(Location.BLOCK_GRANULARITY);
                    }
                }
            } else { // air
                /*
                 *  We want to see if we can move down exactly one block without colliding.
                 *  If we can, then we want to move a little bit more and see if we collide.
                 *  If we do, then we know we can move down one block.
                 */
                BoundLocation newLocation = new BoundLocation(location);
                newLocation.moveRawRight(distance.getX());
                newLocation.moveRawDown(distance.getY());
                RawPoint newDistance = new RawPoint(0, Location.BLOCK_GRANULARITY + 1);
                RawPoint startingNewDistance = new RawPoint(newDistance.getX(), newDistance.getY());
                for (RawPoint p : hitbox) {
                    // our location should be the top-left corner.  Fix offsets as needed for start / end
                    Location start = new BoundLocation(newLocation);
                    start.moveRawRight(p.getX());
                    start.moveRawDown(p.getY());
                    Location end = new BoundLocation(start);
                    end.moveRawRight(newDistance.getX());
                    end.moveRawDown(newDistance.getY());
                    
                    // Get our new location
                    Location newEnd = collide(start, end, fallSpeed);
                    
                    // We now need to fix our distance based on our new end
                    newDistance.setLocation(newEnd.getRawDeltaX(start), newEnd.getRawDeltaY(start));
                }
                if (!startingNewDistance.equals(newDistance)) { // if there was a collision
                    // We can move down
                    distance.setLocation(distance.getX()+newDistance.getX(),
                            distance.getY()+newDistance.getY());
                }
            }
        }
                
        // Our distance is now the farthest we can travel
        location.moveRawRight(distance.getX());
        location.moveRawDown(distance.getY());
    }
    
    /**
     * Attempts to move right if the animation allows it.
     * @param delta The time in milliseconds since the last update
     */
    public boolean tryMoveRight(int delta) {
        current.moveRight(movementSpeed > 0.2f); // any slower movement than this means walk
        if (current.canMoveRight() && current.canChangeVelocity()) {
            setVelocity(movementSpeed, velocity.getY());
        }
        return current.canMoveRight() && current.canChangeVelocity();
    }

    /**
     * Moves character up.  Distance is determined by:
     * (deltaTime) * (movementSpeed), where deltaTime is the difference in time
     * between the last and current input poll (typically a single frame)
     * 
     * @param delta The time in milliseconds since the last update
     */
    public void moveUp(int delta) {
        // these constants are determined experimentally
        fallSpeed = -300.0f - movementSpeed * 0.251f;
        
        try {
            if (this == Game.getInstance().getClient().getPlayer()) {
                Game.getInstance().getClient().announce(PacketCreator.getMovement(this));
            }
        } catch (Throwable t) {
            // We don't care if this fails.
        }
    }
    
    /**
     * Attempts to move up if the animation allows it.
     * @param delta The time in milliseconds since the last update
     */
    public void tryMoveUp(int delta) {
        if (current.canMoveUp() && current.canChangeVelocity()) {
            moveUp(delta);
        }
        current.moveUp(true); // This ordering is NOT a mistake!
    }
    
    /**
     * Moves character down.  Distance is determined by:
     * (deltaTime) * (movementSpeed * downMultiplier), where deltaTime is the difference in time
     * between the last and current input poll (typically a single frame)
     * 
     * @param delta The time in milliseconds since the last update
     * @param downMultiplier The multiplier to multiply the downward movement speed by
     */
    public void moveDown(int delta, float downMultiplier) {
        setVelocity(velocity.getX(), (float) delta * movementSpeed * downMultiplier * .003f);
    }

    /**
     * Attempts to move down if the animation allows it.
     * @param delta The time in milliseconds since the last update
     */
    public void tryMoveDown(int delta) {
        current.moveDown(true);
        float downMultiplier = current.moveDownMultiplier();
        if (downMultiplier != 0 && current.canChangeVelocity()) {
            moveDown(delta, downMultiplier);
        }
    }
    
    /**
     * Called every update.  Determines whether the player should try to shield
     * or not.
     * @param isDown true if the shield button is pressed; else false
     */
    public void shieldUpdate(boolean isDown) {
        if (shielding && !isDown) {
            current.shieldOff();
        } else if (!shielding && isDown) {
            current.shield();
        }
        shielding = isDown;
    }
    
    /**
     * Called every update.  Determines whether the player is holding down the
     * normal attack button.
     * @param isDown true if the normal attack button is pressed; else false
     */
    public void attackUpdate(boolean isDown) {
        if (attacking && !isDown) {
//            current.attack(); // This is for charging attacks.
        } else if (!attacking && isDown) {
            current.attack();
        }
    }

    /**
     * Tells player that they did not make an action this update.
     */
    public void idle() {
        current.idle();
    }
    
    /**
     * Retrieve the direction that the player is facing (left or right)
     * @return direction
     */
    public Direction getDirection() {
        return direction;
    }
    
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
            if (this == Game.getInstance().getClient().getPlayer()) {
                Game.getInstance().getClient().announce(
                        PacketCreator.getMovement(this) );
            }
        }
    }
    
    /**
     * Sets the direction that the player is facing (left or right).
     * @param direction the direction the player should face
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * Accelerates player downwards.
     * @param delta The time in milliseconds to accelerate downwards
     * @param acceleration The acceleration rate of the fall in pixels
     * @param terminalVelocity The maximum velocity in pixels
     */
    public void accelerateDown(int delta, float acceleration, float terminalVelocity) {
        // Guard
        if (hitbox == null) {
            init();
        }
        
        // FIXME:  This is a bit incorrect
        boolean hitSomething = false;  // true if we hit something
        float t = delta / 1000.0f;
        float vf = fallSpeed + t * ClientConstants.FALL_ACCELERATION;
        if (terminalVelocity != 0) {
            vf = fallSpeed + ((terminalVelocity - fallSpeed) / terminalVelocity) * t * acceleration;
            if (vf > terminalVelocity) {
                vf = terminalVelocity;
            }
        }
        vf += velocity.getY();
        RawPoint distance = new RawPoint(0, (long) ((((fallSpeed + vf) / 2.0f) * t) / WorldConstants.WORLD_BLOCK_HEIGHT * Location.BLOCK_GRANULARITY));
        
        TerrainSession ts = Game.getInstance().getClient().getTerrainSession();
        
        // Iterate over all points in our hit box, and fix our end location as needed.
        for (RawPoint p : hitbox) {
            // our location should be the top-left corner.  Fix offsets as needed for start / end
            Location start = new BoundLocation(location);
            start.moveRawRight(p.getX());
            start.moveRawDown(p.getY());
            Location end = new BoundLocation(start);
            end.moveRawRight(distance.getX());
            end.moveRawDown(distance.getY());
            
            // Get our new location
            Location newEnd = new BoundLocation(ts.collideWithBlock(start, end));
            
            hitSomething |= !newEnd.equals(end);
            
            // We now need to fix our distance based on our new end
            distance.setLocation(
                    newEnd.getRawX() - start.getRawX(), 
                    newEnd.getRawY() - start.getRawY());
        }
        
        // If we didn't hit anything
        if (!hitSomething) {
            fallSpeed = vf;
            if (fallSpeed > 0.0f) { // tell our state that our player is falling
                current.fall();
            }
        } else if (fallSpeed < 0.0f) { // We hit the ceiling!
            fallSpeed = 0;
        } else { // we landed on something!
            current.land(); // tell our state that our player has landed on the ground
            fallSpeed = 0;
        }
        
        // Our distance is now the farthest we can travel
//        location.moveRawRight(distance.getX());
  //      location.moveRawDown(distance.getY());
    }

    /**
     * This should be called after the player has been read from the server.
     * This method initializes any transient objects, such as the player's
     * hitbox.
     */
    public void init() {
        long blocksPerPixelWide = Location.BLOCK_GRANULARITY / WorldConstants.WORLD_BLOCK_WIDTH;
        long blocksPerPixelHigh = Location.BLOCK_GRANULARITY / WorldConstants.WORLD_BLOCK_HEIGHT;
        velocity = new Velocity(0, 0);
        // initialize the player's hitbox used for player movement
        hitbox = new ArrayList<RawPoint>();
        for (int x=0; x < collisionWidth; x += WorldConstants.WORLD_BLOCK_WIDTH-1) {
            for (int y=0; y < collisionHeight; y += WorldConstants.WORLD_BLOCK_HEIGHT-1) {
                hitbox.add(new RawPoint(x * blocksPerPixelWide, y * blocksPerPixelHigh));
            }
            hitbox.add(new RawPoint(x * blocksPerPixelWide, collisionHeight * blocksPerPixelHigh));
        }
        for (int y=0; y < collisionHeight; y += WorldConstants.WORLD_BLOCK_HEIGHT-1) {
            hitbox.add(new RawPoint(collisionWidth * blocksPerPixelWide, y * blocksPerPixelHigh));
        }
        hitbox.add(new RawPoint(collisionWidth * blocksPerPixelWide, collisionHeight * blocksPerPixelHigh));
    }

    /**
     * Retrieves the width of the player's collision box.
     * @return collisionWidth
     */
    public int getWidth() {
        return collisionWidth;
    }
    
    /**
     * Retrieves the height of the player's collision box.
     * @return collisionHeight
     */
    public int getHeight() {
        return collisionHeight;
    }

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
	
	/**
	 * Retrieves the damage multiplier based on the player's gear, primary
	 * skills, secondary skills, and buffs
	 * @return damageMultiplier
	 */
	public double getDamageMultiplier() {
	    return 1000.0;
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
	        client.damageSession.addDamage(new Location(x, y), damage, ClientConstants.DAMAGE_OTHER_PLAYER_COLOR);
	    }
	}
	
	/**
	 * Performs the collision checks from startingIndex to endingIndex.
	 * 
	 * This code may look ugly, but it's very fast.  On an i7-2600k, performing
	 * a single collision check on a 15x15 block takes roughly 8 microseconds.
	 * There are about 12 such checks needed per collision animation.
	 * 
	 * @param collisionArc All of the blobs that make up the animation's collision arc.
	 * @param startingIndex The starting index (inclusive) of the collision arc to check collisions for.
	 * @param endingIndex The ending index (inclusive) of the collision arc to check collisions for.
	 * @param horizontalOffset The horizontal offset that must be added to the collision blob.
	 * @param verticalOffset The vertical offset that must be added to the collision blob.
	 */
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
                Location topLeft = new Location(this.location);
                if (direction == Direction.RIGHT) {
                    topLeft.moveDown(verticalOffset + collisionArc[curIndex].getYOffset());
                    topLeft.moveRight(horizontalOffset + collisionArc[curIndex].getXOffset());
                } else { // Flipped collision stuff.  This was such a pain to calculate.
                    topLeft.moveDown(verticalOffset + collisionArc[curIndex].getYOffset());
                    topLeft.moveRight(getWidth() - horizontalOffset - collisionArc[curIndex].getXOffset() - collisionArc[curIndex].getWidth());
                }
                Location bottomRight = new Location(topLeft);
                bottomRight.moveDown(collisionArc[curIndex].getHeight());
                bottomRight.moveRight(collisionArc[curIndex].getWidth());
                
                if (topLeft.equals(bottomRight)) {
                    return;
                }
                /*
                 *  We now have the topLeft and bottomRight coords of our rectangle.
                 *  Using this, we need to grab every block in our rectangle for collision
                 *  testing.
                 */
                boolean sendCollisionPacket = false;
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
                                    sendCollisionPacket = true;
                                    // display damage
                                    displayBlockDamage(x, y, (int)Math.round(getDamageMultiplier() * damage));
    //                                ts.setBlock(x, y, new AirBlock());
                                }
                            }
                        } catch (NullPointerException e) {
                        }
                    }
                }
                if (sendCollisionPacket && this == Game.getInstance().getClient().getPlayer()) {
                    Game.getInstance().getClient().announce(PacketCreator.getBlockCollisionPacket(startingIndex, endingIndex, horizontalOffset, verticalOffset));
                }
            } while (curIndex != endingIndex);
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace(); // This should only happen if someone screwed up the arc image...
        }
    }

    /**
     * Changes the velocity by x and y.  These values should be in pixels / millisecond.
     * @param x The horizontal velocity to add to the current velocity in pixels / millisecond
     * @param y The vertical velocity to add to the current velocity in pixels / millisecond
     */
    public void changeVelocity(float x, float y) {
        velocity.setX(velocity.getX() + x);
        velocity.setY(velocity.getY() + y);
        setVelocity(velocity.getX() + x, velocity.getY() + y);
    }

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
                if (Game.getInstance().getClient().getPlayer() == this) {
                    Game.getInstance().getClient().announce(PacketCreator.getMovement(this));
                }
            } catch (Throwable t) {
                // We don't care if this fails.
            }
        }
    }

    /**
     * Sets the velocity.  Unlike the other setVelocity method, this method
     * DOES NOT announce movement packets.  It should only be used by the
     * movement handler.
     * @param velocity The new velocity.
     */
    public void setVelocity(Velocity velocity) {
        this.velocity = velocity;
    }
    
    /**
     * Retrieves the player's movement speed.
     * @return movementSpeed
     */
    public float getMovementSpeed() {
        return movementSpeed;
    }

    /**
     * Returns the player's current movement velocity.
     * @return velocity
     */
    public Velocity getVelocity() {
        return velocity;
    }
    
    /**
     * Retrieves the player's hit box.
     * @return hitbox
     */
    public List<RawPoint> getHitbox() {
        return hitbox;
    }
    
    /**
     * Sets the player's fall speed.  Used in gravity calculations.
     * @param fallSpeed The new player's fall speed.  Use negative values to
     *                  fall "up."
     */
    public void setFallSpeed(float fallSpeed) {
        this.fallSpeed = fallSpeed;
    }

    /**
     * Retrieves the player's fall speed.
     * @return fallSpeed
     */
    public float getFallSpeed() {
        return fallSpeed;
    }
}
