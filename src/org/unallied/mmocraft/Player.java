package org.unallied.mmocraft;

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;
import org.unallied.mmocraft.animations.AnimationState;
import org.unallied.mmocraft.animations.sword.SwordIdle;
import org.unallied.mmocraft.blocks.AirBlock;
import org.unallied.mmocraft.blocks.Block;
import org.unallied.mmocraft.client.Game;
import org.unallied.mmocraft.constants.WorldConstants;
import org.unallied.mmocraft.items.Inventory;
import org.unallied.mmocraft.items.ItemRequirement;
import org.unallied.mmocraft.net.PacketCreator;
import org.unallied.mmocraft.net.sessions.TerrainSession;

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
    
    protected transient AnimationState current = null; // the current state of this player
    protected transient boolean shielding = false; // true if the player is holding down the shield button
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
    protected transient List<Point2D.Double> hitbox = null;
    
    protected float movementSpeed = 0.500f; // Determines the rate of movement for this player
    protected float fallSpeed = 0.0f; // The rate that the player is currently falling
    protected int playerId; // The unique ID of this player
    protected int delay = (int) (25 / movementSpeed); // delay between animation frames
    
    protected Direction direction = Direction.FACE_RIGHT; // direction that the player is facing
    
    /** The inventory of the player */
    protected transient Inventory inventory = new Inventory();
    
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
     * @param current the new animation state
     */
    public void setState(AnimationState current) {
        if (this.current != current) {
            this.current = current;
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
        
        current.render(x, y, direction == Direction.FACE_LEFT);
    }
    
    /**
     * Updates the player, including animations.
     * @param delta time since last update.
     */
    public void update(long delta) {
        current.update(delta);
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
        Location result = new Location(start);
        try {
            TerrainSession ts = Game.getInstance().getClient().getTerrainSession();
            
            // We split this into horizontal then vertical testing.
            Location horizontalEnd = new Location(start);
            horizontalEnd.setX(end.getX());
            horizontalEnd.setXOffset(end.getXOffset());
            
            // If air
            Location horizontalCollide = new Location(ts.collideWithBlock(start, horizontalEnd));
            if (horizontalCollide.equals(horizontalEnd)) {
                result.setX(end.getX());
                result.setXOffset(end.getXOffset());
            }
            
            // Vertical testing
            if (end.getY() != start.getY() || end.getYOffset() != start.getYOffset()) {
                Location verticalEnd = new Location(result);
                verticalEnd.setY(end.getY());
                verticalEnd.setYOffset(end.getYOffset());
                
                // If air
                Location verticalCollide = new Location(ts.collideWithBlock(result, verticalEnd));
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
        
        for (Point2D.Double p : hitbox) {
            Location start = new Location(location);
            start.moveRight((float) p.getX());
            start.moveDown((float) p.getY());
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
            location.moveLeft(1);
            location.moveUp(1);
        }
    }
    
    /**
     * Moves character to the left.  Distance is determined by:
     * (deltaTime) * (movementSpeed), where deltaTime is the difference in time
     * between the last and current input poll (typically a single frame)
     * TODO:  This needs to be refactored along with moveRight.
     * 
     * @param delta The time in milliseconds since the last update
     */
    public void moveLeft(int delta) {
        // TODO:  Break this into several steps.  We must pass in a Location for each pixel.
        //        We pass in the modified location to the next Location, and "daisy chain"
        //        them all together.  Our end result is the furthest we can move.
        
        // Get new end location
        
        // Make sure player can move left
        Point2D.Double distance = new Point2D.Double(); // distance contains the distance that all points are supposed to move
        distance.setLocation(movementSpeed * delta * -1.0, 0.0);
        Point2D.Double startingDistance = new Point2D.Double(distance.getX(), distance.getY());
        // At this point, end is where we are trying to move to
                    
        // Iterate over all points in our hit box, and fix our end location as needed.
        for (Point2D.Double p : hitbox) {
            // our location should be the top-left corner.  Fix offsets as needed for start / end
            Location start = new Location(location);
            start.moveRight((float) p.getX());
            start.moveDown((float) p.getY());
            Location end = new Location(start);
            end.moveRight((float) distance.getX());
            end.moveDown((float) distance.getY());
            
            // Get our new location
            Location newEnd = collide(start, end, fallSpeed);
            
            // We now need to fix our distance based on our new end
            distance.setLocation(
                    (newEnd.getX()-start.getX()) * WorldConstants.WORLD_BLOCK_WIDTH + (newEnd.getXOffset()-start.getXOffset()),
                    (newEnd.getY()-start.getY()) * WorldConstants.WORLD_BLOCK_HEIGHT + (newEnd.getYOffset()-start.getYOffset()));
        }
        
        if (!current.isInAir()) {
            // At this point, distance is the farthest to the left we can move
            if (!distance.equals(startingDistance)) { // We must have collided with the ground
                /*
                 *  Try to move up by a block and see if we can go any farther.
                 *  If we can, then we want to use the new distance
                 */
                Location newLocation = new Location(location);
                newLocation.moveRight((float) distance.getX());
                newLocation.moveDown((float) distance.getY());
                newLocation.moveUp(WorldConstants.WORLD_BLOCK_HEIGHT);
                Point2D.Double newDistance = new Point2D.Double(startingDistance.getX()-distance.getX(),
                        startingDistance.getY()-distance.getY()); // remaining distance to travel
                
                // Cycle through all points to see if we can go left now
                for (Point2D.Double p : hitbox) {
                    // our location should be the top-left corner.  Fix offsets as needed for start / end
                    Location start = new Location(newLocation);
                    start.moveRight((float) p.getX());
                    start.moveDown((float) p.getY());
                    Location end = new Location(start);
                    end.moveRight((float) newDistance.getX());
                    end.moveDown((float) newDistance.getY());
                    
                    // Get our new location
                    Location newEnd = collide(start, end, fallSpeed);
                    
                    // We now need to fix our distance based on our new end
                    newDistance.setLocation(
                            (newEnd.getX()-start.getX()) * WorldConstants.WORLD_BLOCK_WIDTH + (newEnd.getXOffset()-start.getXOffset()),
                            (newEnd.getY()-start.getY()) * WorldConstants.WORLD_BLOCK_HEIGHT + (newEnd.getYOffset()-start.getYOffset()));
                }
                
                // If we were able to move, then keep it.  Otherwise use the old distance
                if (newDistance.getX() < 0.0) {
                    distance = newDistance;
                    location.moveUp(WorldConstants.WORLD_BLOCK_HEIGHT);
                }
            } else { // air
                /*
                 *  We want to see if we can move down exactly one block without colliding.
                 *  If we can, then we want to move a little bit more and see if we collide.
                 *  If we do, then we know we can move down one block.
                 */
                BoundLocation newLocation = new BoundLocation(location);
                newLocation.moveRight((float) distance.getX());
                newLocation.moveDown((float) distance.getY());
                Point2D.Double newDistance = new Point2D.Double(0.0, WorldConstants.WORLD_BLOCK_HEIGHT+1);
                Point2D.Double startingNewDistance = new Point2D.Double(newDistance.getX(), newDistance.getY());
                for (Point2D.Double p : hitbox) {
                    // our location should be the top-left corner.  Fix offsets as needed for start / end
                    Location start = new Location(newLocation);
                    start.moveRight((float) p.getX());
                    start.moveDown((float) p.getY());
                    Location end = new Location(start);
                    end.moveRight((float) newDistance.getX());
                    end.moveDown((float) newDistance.getY());
                    
                    // Get our new location
                    Location newEnd = collide(start, end, fallSpeed);
                    
                    // We now need to fix our distance based on our new end
                    newDistance.setLocation(
                            (newEnd.getX()-start.getX()) * WorldConstants.WORLD_BLOCK_WIDTH + (newEnd.getXOffset()-start.getXOffset()),
                            (newEnd.getY()-start.getY()) * WorldConstants.WORLD_BLOCK_HEIGHT + (newEnd.getYOffset()-start.getYOffset()));
                }
                if (!startingNewDistance.equals(newDistance)) { // if there was a collision
                    // We can move down
                    distance.setLocation(distance.getX()+newDistance.getX(),
                            distance.getY()+newDistance.getY());
                }
            }
        }
        
        // Our distance is now the farthest we can travel
        location.moveRight((float) distance.getX());
        location.moveDown((float) distance.getY());
    }
    
    /**
     * Attempts to move left if the animation allows it.
     * @param delta The time in milliseconds since the last update
     */
    public void tryMoveLeft(int delta) {
        current.moveLeft(movementSpeed > 0.2f); // any slower movement than this means walk
        if (current.canMoveLeft()) {
            moveLeft(delta);
        }
    }

    /**
     * Moves character to the right.  Distance is determined by:
     * (deltaTime) * (movementSpeed), where deltaTime is the difference in time
     * between the last and current input poll (typically a single frame)
     * TODO:  This needs to be refactored along with moveLeft.
     * 
     * @param delta The time in milliseconds since the last update
     */
    public void moveRight(int delta) {
        // TODO:  Break this into several steps.  We must pass in a Location for each pixel.
        //        We pass in the modified location to the next Location, and "daisy chain"
        //        them all together.  Our end result is the furthest we can move.
        
        // Get new end location
        
        // Make sure player can move left
        Point2D.Double distance = new Point2D.Double(); // distance contains the distance that all points are supposed to move
        distance.setLocation(movementSpeed * delta, 0.0);
        Point2D.Double startingDistance = new Point2D.Double(distance.getX(), distance.getY());
        
        // At this point, end is where we are trying to move to
        
        // Iterate over all points in our hit box, and fix our end location as needed.
        for (Point2D.Double p : hitbox) {
            // our location should be the top-left corner.  Fix offsets as needed for start / end
            Location start = new Location(location);
            start.moveRight((float) p.getX());
            start.moveDown((float) p.getY());
            Location end = new Location(start);
            end.moveRight((float) distance.getX());
            end.moveDown((float) distance.getY());
            
            // Get our new location
            Location newEnd = collide(start, end, fallSpeed);
            
            // We now need to fix our distance based on our new end
            distance.setLocation(
                    (newEnd.getX()-start.getX()) * WorldConstants.WORLD_BLOCK_WIDTH + (newEnd.getXOffset()-start.getXOffset()),
                    (newEnd.getY()-start.getY()) * WorldConstants.WORLD_BLOCK_HEIGHT + (newEnd.getYOffset()-start.getYOffset()));
        }
        
        if (!current.isInAir()) {
            // At this point, distance is the farthest to the right we can move
            if (!distance.equals(startingDistance)) { // We must have collided with the ground
                /*
                 *  Try to move up by a block and see if we can go any farther.
                 *  If we can, then we want to use the new distance
                 */
                Location newLocation = new Location(location);
                newLocation.moveRight((float) distance.getX());
                newLocation.moveDown((float) distance.getY());
                newLocation.moveUp(WorldConstants.WORLD_BLOCK_HEIGHT);
                Point2D.Double newDistance = new Point2D.Double(startingDistance.getX()-distance.getX(),
                        startingDistance.getY()-distance.getY()); // remaining distance to travel
                
                // Cycle through all points to see if we can go right now
                for (Point2D.Double p : hitbox) {
                    // our location should be the top-left corner.  Fix offsets as needed for start / end
                    Location start = new Location(newLocation);
                    start.moveRight((float) p.getX());
                    start.moveDown((float) p.getY());
                    Location end = new Location(start);
                    end.moveRight((float) newDistance.getX());
                    end.moveDown((float) newDistance.getY());
                    
                    // Get our new location
                    Location newEnd = collide(start, end, fallSpeed);
                    
                    // We now need to fix our distance based on our new end
                    newDistance.setLocation(
                            (newEnd.getX()-start.getX()) * WorldConstants.WORLD_BLOCK_WIDTH + (newEnd.getXOffset()-start.getXOffset()),
                            (newEnd.getY()-start.getY()) * WorldConstants.WORLD_BLOCK_HEIGHT + (newEnd.getYOffset()-start.getYOffset()));
                }
                
                // If we were able to move, then keep it.  Otherwise use the old distance
                if (newDistance.getX() > 0.0) {
                    distance = newDistance;
                    location.moveUp(WorldConstants.WORLD_BLOCK_HEIGHT);
                }
            } else { // air
                /*
                 *  We want to see if we can move down exactly one block without colliding.
                 *  If we can, then we want to move a little bit more and see if we collide.
                 *  If we do, then we know we can move down one block.
                 */
                BoundLocation newLocation = new BoundLocation(location);
                newLocation.moveRight((float) distance.getX());
                newLocation.moveDown((float) distance.getY());
                Point2D.Double newDistance = new Point2D.Double(0.0, WorldConstants.WORLD_BLOCK_HEIGHT+1);
                Point2D.Double startingNewDistance = new Point2D.Double(newDistance.getX(), newDistance.getY());
                for (Point2D.Double p : hitbox) {
                    // our location should be the top-left corner.  Fix offsets as needed for start / end
                    Location start = new Location(newLocation);
                    start.moveRight((float) p.getX());
                    start.moveDown((float) p.getY());
                    Location end = new Location(start);
                    end.moveRight((float) newDistance.getX());
                    end.moveDown((float) newDistance.getY());
                    
                    // Get our new location
                    Location newEnd = collide(start, end, fallSpeed);
                    
                    // We now need to fix our distance based on our new end
                    newDistance.setLocation(
                            (newEnd.getX()-start.getX()) * WorldConstants.WORLD_BLOCK_WIDTH + (newEnd.getXOffset()-start.getXOffset()),
                            (newEnd.getY()-start.getY()) * WorldConstants.WORLD_BLOCK_HEIGHT + (newEnd.getYOffset()-start.getYOffset()));
                }
                if (!startingNewDistance.equals(newDistance)) { // if there was a collision
                    // We can move down
                    distance.setLocation(distance.getX()+newDistance.getX(),
                            distance.getY()+newDistance.getY());
                }
            }
        }
        
        // Our distance is now the farthest we can travel
        location.moveRight((float) distance.getX());
        location.moveDown((float) distance.getY());
    }
    
    /**
     * Attempts to move right if the animation allows it.
     * @param delta The time in milliseconds since the last update
     */
    public void tryMoveRight(int delta) {
        current.moveRight(movementSpeed > 0.2f); // any slower movement than this means walk
        if (current.canMoveRight()) {
            moveRight(delta);
        }
    }
    
    /**
     * Moves character up.  Distance is determined by:
     * (deltaTime) * (movementSpeed), where deltaTime is the difference in time
     * between the last and current input poll (typically a single frame)
     * 
     * @param delta The time in milliseconds since the last update
     */
    public void moveUp(int delta) {
        fallSpeed = -300.0f - movementSpeed * 251.0f; // these constants are determined experimentally
    }
    
    /**
     * Attempts to move up if the animation allows it.
     * @param delta The time in milliseconds since the last update
     */
    public void tryMoveUp(int delta) {
        if (current.canMoveUp()) {
            moveUp(delta);
        }
        current.moveUp(true); // This ordering is NOT a mistake!
    }
    
    /**
     * Moves character down.  Distance is determined by:
     * (deltaTime) * (movementSpeed), where deltaTime is the difference in time
     * between the last and current input poll (typically a single frame)
     * 
     * @param delta The time in milliseconds since the last update
     */
    public void moveDown(int delta) {
        fallSpeed += (float) (delta) * movementSpeed * 3.0f;
        //location.moveDown(movementSpeed * delta);
    }

    /**
     * Attempts to move down if the animation allows it.
     * @param delta The time in milliseconds since the last update
     */
    public void tryMoveDown(int delta) {
        current.moveDown(true);
        if (current.canMoveDown()) {
            moveDown(delta);
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
//            current.attack();
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
     * Returns delay (in milliseconds) for each frame of animation.  This value
     * is increased based on the weight of the player's equipped shield.
     * @return delay
     */
    public int getDelay() {
        return delay;
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
        float vf = fallSpeed + ((terminalVelocity - fallSpeed) / terminalVelocity) * t * acceleration;
        if (vf > terminalVelocity) {
            vf = terminalVelocity;
        }
        Point2D.Double distance = new Point2D.Double(); // distance contains the distance that all points are supposed to move
        distance.setLocation(0.0, ((fallSpeed + vf) / 2.0f) * t);
        
        TerrainSession ts = Game.getInstance().getClient().getTerrainSession();
        
        // Iterate over all points in our hit box, and fix our end location as needed.
        for (Point2D.Double p : hitbox) {
            // our location should be the top-left corner.  Fix offsets as needed for start / end
            Location start = new Location(location);
            start.moveRight((float) p.getX());
            start.moveDown((float) p.getY());
            Location end = new Location(start);
            end.moveRight((float) distance.getX());
            end.moveDown((float) distance.getY());
            
            // Get our new location
            Location newEnd = new Location(ts.collideWithBlock(start, end));
            
            hitSomething |= !newEnd.equals(end);
            
            // We now need to fix our distance based on our new end
            distance.setLocation(
                    (newEnd.getX()-start.getX()) * WorldConstants.WORLD_BLOCK_WIDTH + (newEnd.getXOffset()-start.getXOffset()),
                    (newEnd.getY()-start.getY()) * WorldConstants.WORLD_BLOCK_HEIGHT + (newEnd.getYOffset()-start.getYOffset()));
        }
        
        // If we didn't hit anything
        if (!hitSomething) {
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
        
        // Our distance is now the farthest we can travel
        location.moveRight((float) distance.getX());
        location.moveDown((float) distance.getY());
    }

    /**
     * This should be called after the player has been read from the server.
     * This method initializes any transient objects, such as the player's
     * hitbox.
     */
    public void init() {
        // initialize the player's hitbox used for player movement
        hitbox = new ArrayList<Point2D.Double>();
        for (int x=0; x < collisionWidth; x += WorldConstants.WORLD_BLOCK_WIDTH-1) {
            for (int y=0; y < collisionHeight; y += WorldConstants.WORLD_BLOCK_HEIGHT-1) {
                hitbox.add(new Point2D.Double(x,y));
            }
            hitbox.add(new Point2D.Double(x, collisionHeight));
        }
        for (int y=0; y < collisionHeight; y += WorldConstants.WORLD_BLOCK_HEIGHT-1) {
            hitbox.add(new Point2D.Double(collisionWidth,y));
        }
        hitbox.add(new Point2D.Double(collisionWidth, collisionHeight));
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
     * Sets the player's animation delay in milliseconds.
     * @param delay the delay in milliseconds
     */
    public void setDelay(int delay) {
        this.delay = delay;
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
     * Returns whether the player meets the requirement.  This is based on the
     * player's skill levels.  To meet a requirement, a player's skill level
     * must be >= the requirement.
     * TODO:  Implement this method.
     * @param requirement The requirement to meet
     * @return true if the player meets the requirement; else false
     */
	public boolean meetsRequirement(ItemRequirement requirement) {
		return true;
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
                if (direction == Direction.FACE_RIGHT) {
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
                for (long x = topLeft.getX(); x <= bottomRight.getX(); ++x) {
                    for (long y = topLeft.getY(); y <= bottomRight.getY(); ++y) {
                        if (ts.getBlock(x, y).isCollidable() || true) {
                            int xOff = 0;
                            if (direction == Direction.FACE_RIGHT) {
                                xOff = (int) (((x - this.location.getX()) * WorldConstants.WORLD_BLOCK_WIDTH - horizontalOffset - collisionArc[curIndex].getXOffset() - this.location.getXOffset()));
                            } else {
                                xOff = (int) (-this.location.getXOffset() + current.getWidth() - ((this.location.getX() - x) * WorldConstants.WORLD_BLOCK_WIDTH + getWidth() - horizontalOffset + collisionArc[curIndex].getFlipped().getXOffset()));
                            }
                            int yOff = (int) (((y - this.location.getY()) * WorldConstants.WORLD_BLOCK_HEIGHT - verticalOffset - collisionArc[curIndex].getYOffset() - this.location.getYOffset()));
                            float damage =  (direction == Direction.FACE_RIGHT ? collisionArc[curIndex] : collisionArc[curIndex].getFlipped()).getDamage(
                                    new Rectangle(WorldConstants.WORLD_BLOCK_WIDTH, WorldConstants.WORLD_BLOCK_HEIGHT), xOff, yOff);
                            if (damage > 0) {
//                                ts.setBlock(x, y, new AirBlock());
                                Game.getInstance().getClient().announce(PacketCreator.getBlockCollisionPacket(current.getId(), startingIndex, endingIndex, horizontalOffset, verticalOffset));
                            }
                        }
                    }
                }
            } while (curIndex != endingIndex);
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace(); // This should only happen if someone screwed up the arc image...
        }
    }
}
