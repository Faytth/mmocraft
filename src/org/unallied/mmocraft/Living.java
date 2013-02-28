package org.unallied.mmocraft;

import java.util.ArrayList;
import java.util.List;

import org.unallied.mmocraft.animations.AnimationState;
import org.unallied.mmocraft.blocks.Block;
import org.unallied.mmocraft.client.Game;
import org.unallied.mmocraft.constants.ClientConstants;
import org.unallied.mmocraft.constants.WorldConstants;
import org.unallied.mmocraft.sessions.TerrainSession;

/**
 * Anything that can be considered "alive," such as a living, monster, or NPC
 * is derived from this class.
 * @author Faythless
 *
 */
public abstract class Living extends GameObject {

    /**
     * 
     */
    private static final long serialVersionUID = 6521921565335367996L;

    /** The current state of the living. */
    protected transient AnimationState current = null;
    
    /**
     * The width of the creature's hitbox.  Used in bounding box checks.
     * Starting location of the bounding box is <code>location</code>.
     */
    protected int collisionWidth;
    
    /**
     * The height of the creature's hitbox.  Used in bounding box checks.
     * Starting location of the bounding box is <code>location</code>.
     */
    protected int collisionHeight;

    /**
     * Collection of the "boundary points" that need to be checked for living
     * movement across the world.  These should be no farther than one block
     * apart.
     */
    protected transient List<RawPoint> hitbox = null;
    
    /** 
     * The velocity BEFORE the call to accelerateDown. Used by move() to
     * calculate falling distance. 
     */
    protected float initialVelocity = 0f;
    
    /** True if the key for moving up is down or not. */
    protected transient boolean movingUp = false;
    
    protected float movementSpeed = 300f; // Determines the rate of movement for this living pixels / second)
    protected float fallSpeed = 0.0f; // The rate that the living is currently falling
    
    protected Direction direction = Direction.RIGHT; // direction that the living is facing
    
    /** True if the living is holding down the shield button. */
    protected transient boolean shielding = false;
    
    /** True if the living is currently in the middle of charging an attack. */
    protected transient boolean attacking = false;
    
    /**
     * The living's movement.
     */
    protected transient Velocity velocity = new Velocity(0, 0);
    
    /** The unique identifier of this living object. */
    protected int id;
    
    // The name of the creature, such as "Bob" or "Charles the Unicorn"
    protected String name = "";
    
    // The maximum HP for this creature.  If it's alive, it can die!!!
    protected int hpMax = 0;
    
    // The current HP for this creature
    protected int hpCurrent = 0;
    
    public Living(int collisionWidth, int collisionHeight) {
        super();
        this.collisionWidth = collisionWidth;
        this.collisionHeight = collisionHeight;
    }
    
    public Living(Living other) {
        super(other);
        this.name = other.name;
        this.hpMax = other.hpMax;
        this.hpCurrent = other.hpCurrent;
        this.collisionWidth = other.collisionWidth;
        this.collisionHeight = other.collisionHeight;
    }
    
    /**
     * Returns this living's unique identifier
     * @return livingId
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the living's unique identifier.
     * @param id new living ID.
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * Returns the name of the living creature, such as "Bob" or "Alice."
     * @return name of the living creature
     */
    public String getName() {
        return name;
    }
    
    /**
     * Sets the name of the living creature, such as "Bob" or "Alice."
     * @param name new name of the living creature
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Returns the maximum HP for this creature.
     * @return maximum HP
     */
    public int getHpMax() {
        return hpMax;
    }
    
    /**
     * Sets the maximum HP for this creature.
     * @param hpMax maximum HP
     */
    public void setHpMax(int hpMax) {
        this.hpMax = hpMax;
    }
    
    /**
     * Returns the current HP for this creature.  
     * Current HP is always <= max HP.
     * @return current HP
     */
    public int getHpCurrent() {
        return hpCurrent;
    }
    
    /**
     * Sets the current HP for this creature.
     * Current HP cannot exceed max HP.
     * @param hpCurrent current HP
     */
    public void setHpCurrent(int hpCurrent) {
        this.hpCurrent = hpCurrent;
        
        // Make sure we didn't go over the max
        if (this.hpCurrent > hpMax) {
            this.hpCurrent = hpMax; // If we went over, set the HP to max
        }
    }
    
    /**
     * Updates the living object, including animations.
     * @param delta time since last update.
     */
    public abstract void update(long delta);
    
    /**
     * Given a start and end, returns the farthest possible location after
     * collision is accounted for (such as running into a wall)
     * @param start Starting location
     * @param end Ending location
     * @return
     */
    public Location collide(Location start, Location end) {
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
    
    /**
     * Retrieves whether or not the living is holding down the shield button.
     * @return shielding
     */
    public boolean isShielding() {
        return false;
    }
    
    /**
     * Returns whether or not the living's hitbox is currently overlapping with
     * a collidable block.
     * @return stuck.  True if the living is stuck; else false.
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
     * Gets the living out of a place that they would normally be stuck at.
     * The way this works is it iterates over all points in the living's hitbox.
     * If any point is colliding with a block, it shifts the living up and to the left.
     * This continues until the living is no longer colliding with a block.
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
     * @return true if the living was able to move left, else false.
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
     * @param velocity 
     * @param fallSpeed 
     * @param initialVelocity 
     */
    public void move(int delta, Velocity velocity, float fallSpeed, float initialVelocity) {
        // TODO:  Break this into several steps.  We must pass in a Location for each pixel.
        //        We pass in the modified location to the next Location, and "daisy chain"
        //        them all together.  Our end result is the farthest we can move.
        
        // Get new end location
        
        // Make sure living can move left
        RawPoint distance = new RawPoint();
        float fallDistance = ((fallSpeed + initialVelocity) / 2f) * delta / 1000 *
                Location.BLOCK_GRANULARITY / WorldConstants.WORLD_BLOCK_HEIGHT;

        distance.setLocation((long) (velocity.getX() * delta / 1000 * Location.BLOCK_GRANULARITY / WorldConstants.WORLD_BLOCK_WIDTH), 
                (long) ((velocity.getY()) * delta / 1000 * Location.BLOCK_GRANULARITY / WorldConstants.WORLD_BLOCK_HEIGHT + fallDistance));
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
            Location newEnd = collide(start, end);
            
            // We now need to fix our distance based on our new end
            distance.setLocation(newEnd.getRawDeltaX(start), newEnd.getRawDeltaY(start));
        }
        
        // Testing for falling / ceiling
        if (startingDistance.getY() == distance.getY()) {
            if (fallSpeed > 0) { // We're falling
                current.fall();
            }
        } else if (fallSpeed < 0) { // We hit the ceiling
            this.initialVelocity = 0;
            this.fallSpeed = 0;
        } else { // We landed on something
            current.land();
            this.initialVelocity = 0;
            this.fallSpeed = 0;
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
                    Location newEnd = collide(start, end);
                    
                    // We now need to fix our distance based on our new end
                    newDistance.setLocation(newEnd.getRawDeltaX(start), newEnd.getRawDeltaY(start));
                }
                
                // If we were able to move, then keep it.  Otherwise use the old distance
                if (velocity.getX() < 0) {
                    if (newDistance.getX() < distance.getX()) {
                        distance = newDistance;
                        this.location.moveRawUp(Location.BLOCK_GRANULARITY);
                    }
                } else {
                    if (newDistance.getX() > distance.getX()) {
                        distance = newDistance;
                        this.location.moveRawUp(Location.BLOCK_GRANULARITY);
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
                    Location newEnd = collide(start, end);
                    
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
        this.location.moveRawRight(distance.getX());
        this.location.moveRawDown(distance.getY());
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
     * Attempts to move up if the animation allows it.
     * @param delta The time in milliseconds since the last update
     */
    public void tryMoveUp(int delta) {
        if (!movingUp) {
            current.moveUp(true); // This ordering is NOT a mistake!
        }
        movingUp = true;
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
        setVelocity(velocity.getX(), (float) delta * movementSpeed * downMultiplier * .025f);
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
     * Tells living that they did not make an action this update.
     */
    public void idle() {
        current.idle();
    }
    

    /**
     * Retrieve the direction that the living is facing (left or right)
     * @return direction
     */
    public Direction getDirection() {
        return direction;
    }
    
    /**
     * Sets the direction that the living object is facing (left or right).
     * @param direction
     */
    public void updateDirection(Direction direction) {
        this.direction = direction;
    }
    

    /**
     * Sets the direction that the living object is facing (left or right).
     * @param direction the direction the living should face
     */
    public void setDirection(Direction direction) {
        synchronized (this) {
            this.direction = direction;
        }
    }

    /**
     * Accelerates living downwards.
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
        
        // Correction that can cause negative acceleration if it's going too fast.
        if (fallSpeed > terminalVelocity) {
            fallSpeed = terminalVelocity;
        }
        initialVelocity = fallSpeed;
        float t = delta / 1000.0f;
        float vf;
        if (terminalVelocity != 0) {
            vf = fallSpeed + ((terminalVelocity - fallSpeed) / terminalVelocity) * t * acceleration;
            if (vf > terminalVelocity) {
                vf = terminalVelocity;
            }
        } else {
            vf = fallSpeed + t * ClientConstants.FALL_ACCELERATION;
        }
        fallSpeed = vf;
    }

    /**
     * This should be called after the living has been read from the server.
     * This method initializes any transient objects, such as the living's
     * hitbox.
     */
    public void init() {
        long blocksPerPixelWide = Location.BLOCK_GRANULARITY / WorldConstants.WORLD_BLOCK_WIDTH;
        long blocksPerPixelHigh = Location.BLOCK_GRANULARITY / WorldConstants.WORLD_BLOCK_HEIGHT;
        velocity = new Velocity(0, 0);
        // initialize the living's hitbox used for living movement
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
     * Retrieves the width of the living's collision box.
     * @return collisionWidth
     */
    public int getWidth() {
        return collisionWidth;
    }
    
    /**
     * Retrieves the height of the living's collision box.
     * @return collisionHeight
     */
    public int getHeight() {
        return collisionHeight;
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
     * Retrieves the living's movement speed.
     * @return movementSpeed
     */
    public float getMovementSpeed() {
        return movementSpeed;
    }

    /**
     * Returns the living's current movement velocity.
     * @return velocity
     */
    public Velocity getVelocity() {
        return velocity;
    }
    
    /**
     * Retrieves the living's hit box.
     * @return hitbox
     */
    public List<RawPoint> getHitbox() {
        return hitbox;
    }
    
    /**
     * Sets the living's fall speed.  Used in gravity calculations.
     * @param fallSpeed The new living's fall speed.  Use negative values to
     *                  fall "up."
     */
    public void setFallSpeed(float fallSpeed) {
        this.fallSpeed = fallSpeed;
        initialVelocity = this.fallSpeed;
    }

    /**
     * Retrieves the living's fall speed.
     * @return fallSpeed
     */
    public float getFallSpeed() {
        return fallSpeed;
    }

    /**
     * Sets whether the living is currently attempting to move up.
     * @param movingUp True if the living is attempting to move up, else false.
     */
    public void setMovingUp(boolean movingUp) {
        this.movingUp = movingUp;
    }
    
    /**
     * Retrieves the initial velocity, used in movement calculations for gravity.
     * @return initialVelocity
     */
    public float getInitialVelocity() {
        return initialVelocity;
    }

    /**
     * Sets the initial velocity, used in movement calculations for gravity.
     * @param initialVelocity
     */
    public void setInitialVelocity(float initialVelocity) {
        this.initialVelocity = initialVelocity;
    }
    
    /**
     * Sets the animation state to a new state.
     * @param newState the new animation state
     */
    public void setState(AnimationState newState) {
        if (this.current != newState) {
            this.current = newState;
        }
    }
    
    /**
     * Retrieves the current animation state.
     * @return current
     */
    public AnimationState getCurrent() {
        return current;
    }
    
    /**
     * Retrieves whether the living is currently alive or dead.
     * @return true if the living is alive, else false.
     */
    public boolean isAlive() {
        return hpCurrent > 0;
    }
    
    /**
     * Performs the collision checks from startingIndex to endingIndex.
     * 
     * This code may look ugly, but it's very fast.  On an i7-2600k, performing
     * a single collision check on a 16x16 block takes roughly 8 microseconds.
     * There are about 12 such checks needed per collision animation.
     * 
     * @param collisionArc All of the blobs that make up the animation's collision arc.
     * @param startingIndex The starting index (inclusive) of the collision arc to check collisions for.
     * @param endingIndex The ending index (inclusive) of the collision arc to check collisions for.
     * @param horizontalOffset The horizontal offset that must be added to the collision blob.
     * @param verticalOffset The vertical offset that must be added to the collision blob.
     */
    public abstract void doCollisionChecks(CollisionBlob[] collisionArc, int startingIndex,
            int endingIndex, float horizontalOffset, float verticalOffset);
    
    /**
     * Returns the current animation state
     * @return current
     */
    public AnimationState getState() {
        return current;
    }
    
    /**
     * Called every update.  Determines whether the living should try to shield
     * or not.
     * @param isDown true if the shield button is pressed; else false
     */
    public void shieldUpdate(boolean isDown) {
        if (shielding != isDown) {
            if (!isDown) {
                current.shieldOff();
            } else {
                current.shield();
            }
        }
        shielding = isDown;
    }
    
    /**
     * Called every update.  Determines whether the living is holding down the
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
}
