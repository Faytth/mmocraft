package org.unallied.mmocraft.animations;

import java.io.Serializable;

import org.newdawn.slick.Animation;
import org.newdawn.slick.SpriteSheet;
import org.unallied.mmocraft.Collision;
import org.unallied.mmocraft.CollisionBlob;
import org.unallied.mmocraft.Living;

/**
 * An animation state is what a player or object is currently in the process
 * of doing.  These states change between one another by using a state pattern.
 * @author Faythless
 *
 */
public abstract class AnimationState implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 8464415414303084501L;
    
    /** The max number of animation states chained together by last. */
    private static final int MAX_CHAIN = 10;
    
    /** The number of milliseconds that this animation lasts. */
    protected transient int duration = -1;

    /** 
     * The number of milliseconds that have elapsed.  When this reaches
     * duration, the animation is suspended.
     */
    protected transient int elapsedTime = 0;
    
    /** The animation to play for this state. */
    protected transient Animation animation = null;
    
    /** The player attached to this animation. */
    protected transient Living living;
    
    /** The last animation state. */
    protected transient AnimationState last;
    
    /** The time (in milliseconds since 1970) that this state was entered. */
    protected transient long entryTime;
    
    /** The width of a frame in this animation state. */
    protected transient int width = -1;
    
    /** The height of a frame in this animation state. */
    protected transient int height = -1;
    
    /** Offset from the left.  +1 moves player LEFT 1 pixel. */
    protected transient float horizontalOffset = 0.0f;
    
    /** Offset from the top.  +1 moves player UP 1 pixel. */
    protected transient float verticalOffset = 0.0f;
    
    /** The collision data for this animation.  Used to determine attack arcs. */
    protected transient Collision collision = null;
    
    /** 
     * The collision body for this animation.  Used to determine what counts as
     * a "hit" on this animation.
     */
    protected transient CollisionBlob[] collisionBody = null;
    
    /**
     * The amount of time (in milliseconds) that the player has spent in the
     * cooldown state.
     */
    protected transient long cooldownTime = 0;
    
    /** True if the player is able to jump a second time. */
    protected boolean canDoubleJump = true;
    
    public AnimationState(Living living, AnimationState last) {
        this.living = living;
        this.last   = last;
        this.entryTime = System.currentTimeMillis();
        
        // Prevent unnecessary chains
        AnimationState index = this;
        for (int i=0; i < MAX_CHAIN-1; ++i) {
            if (index != null) {
                index = index.last;
            }
        }
        if (index != null) {
            index.last = null;
        }
        if (last != null) {
        	this.canDoubleJump = last.canDoubleJump;
        }
    }
    
    /**
     * Called whenever the player is not moving left, right, up, or down.
     */
    public abstract void idle();
    
    /**
     * Called when the controls indicate moving to the left.
     * @param smash whether to "smash" this control (i.e. run).  True to run,
     * false to walk.
     */
    public abstract void moveLeft(boolean smash);
    
    /**
     * Called when the controls indicate movement to the right.
     * @param smash whether to "smash" this control (i.e. run).  True to run,
     * false to walk.
     */
    public abstract void moveRight(boolean smash);
    
    /**
     * Called when the controls indicate upward movement.  This is a jump in
     * most cases.
     * @param smash whether to "smash" this control (i.e. run).
     */
    public abstract void moveUp(boolean smash);
    
    /**
     * Called when the controls indicate downward movement.  This is either to
     * crouch, speed up the fall, or fall through thin platforms.
     * @param smash whether to "smash" this control (i.e. run).
     */
    public abstract void moveDown(boolean smash);
    
    /**
     * Performs an attack.  Couple this with movement to obtain smash attacks
     * and the like.
     */
    public abstract void attack();

    /**
     * Performs a "shield" (the blue orb that surrounds a character).  When
     * coupled with move left or right, it causes the player to roll.
     */
    public abstract void shield();
    
    /**
     * Removes a "shield" (the blue orb that surrounds a character).
     */
    public abstract void shieldOff();
    
    /**
     * Called whenever the player lands.
     */
    public abstract void land();
    
    /**
     * Called when the player begins to fall (vertical acceleration is positive)
     */
    public abstract void fall();
    
    /** Called when the player dies. */
    public abstract void die();
    
    /**
     * Renders the animation to the screen
     * @param x The x location from the left side of the screen to draw at
     * @param y The y location from the top of the screen to draw at
     */
    public void render(float x, float y, boolean flipped) {
        if (animation != null) {
            animation.getCurrentFrame().getFlippedCopy(flipped, false).draw(
                    flipped ? x - (animation.getWidth()-horizontalOffset-living.getWidth()) : x - horizontalOffset,
                    y - verticalOffset);
        }
    }
    
    /**
     * Retrieves the width of this animation, or 0 if no animation is set.
     * @return width
     */
    public int getWidth() {
        if (width != -1) {
            return width;
        } else if (animation != null) {
            return animation.getWidth();
        }
        
        return 0;
    }
    
    /**
     * Updates the animation.
     * @param delta time since last update in milliseconds
     */
    public void update(long delta) {
        elapsedTime += delta;
        
        if (animation != null) {
            int startingIndex = animation.getFrame();
            animation.update(delta);
            int endingIndex = animation.getFrame();
            // If our frame changed and it has collision
            if (startingIndex != endingIndex && collision != null) {
                living.doCollisionChecks(collision.getCollisionArc(), startingIndex+1, 
                        endingIndex, -horizontalOffset, -verticalOffset);
            }
        }
    }
    
    /**
     * Sets the animation for this state.  Delay is based on player's speed.
     * @param ss the sprite sheet to use for the animation.  Each sprite sheet
     * should be unique for the animation.  Frames are from left to right, top
     * to bottom.  There should be NO empty frames as they will be added just
     * like any other.
     */
    public void setAnimation(SpriteSheet ss) {
        if (ss != null) {
            int verticalCount = ss.getVerticalCount();
            int horizontalCount = ss.getHorizontalCount();
            
            // Guard against -1 duration.
            if (duration == -1) {
                duration = verticalCount * horizontalCount * 50;
            }
            
            for (int i=0; i < verticalCount; ++i) {
                for (int j=0; j < horizontalCount; ++j) {
                    animation.addFrame(ss.getSprite(j, i), duration / (verticalCount * horizontalCount));
                }
            }
        }
    }

    /**
     * Retrieves whether or not this character is in a state which can change its movement speed.
     * A good example of a state that CAN'T change its velocity is during a roll.
     * @return changeVelocity
     */
    public boolean canChangeVelocity() {
        return true;
    }
    
    /**
     * Returns whether or not this character is in a state which can move left
     * @return
     */
    public boolean canMoveLeft() {
        return false;
    }
    
    /**
     * Returns whether or not this character is in a state which can move right
     * @return
     */
    public boolean canMoveRight() {
        return false;
    }
    
    /**
     * Returns whether or not this character is in a state which can move up
     * @return
     */
    public boolean canMoveUp() {
        return false;
    }
    
    /**
     * Returns a multiplier for the movement speed downwards.  Default is 1.0.
     * This should be multiplied by the player's movement speed to determine
     * how fast they're moving.  A result of 0 signifies that the player is
     * unable to move down.
     * @return downMultiplier
     */
    public float moveDownMultiplier() {
        return 1f;
    }

    /**
     * Returns whether or not the current state is in the air (such as jumping
     * or falling).
     * @return
     */
    public boolean isInAir() {
        return false;
    }
    
    /**
     * Returns the amount of time (in milliseconds) spent in the cooldown state.
     * @return cooldownTime
     */
    public long getCooldownTime() {
        return cooldownTime;
    }

    /**
     * Retrieves the unique AnimationType ID for this AnimationState.
     * @return id
     */
    public abstract short getId();
        
    /**
     * Generates a collision body from the sprite sheet.  This body is needed for
     * all animations that are hit by attacks.
     * 
     * The sprite sheet that is used must have all of its images on a single,
     * horizontal line.  All other lines will be ignored.
     * 
     * The sprite sheet should have the same number of images as the animation.
     * @param ss The sprite sheet to generate the body from.
     */
    public void generateCollisionBody(SpriteSheet ss) {
        // Guard
        if (ss == null || collisionBody != null) {
            return;
        }
        
        collisionBody = new CollisionBlob[ss.getHorizontalCount()];
        for (int x=0; x < ss.getHorizontalCount(); ++x) {
            collisionBody[x] = new CollisionBlob(ss.getSprite(x, 0));
        }
    }
    
    /**
     * Retrieves the collision arc array for this animation.  This array is as
     * long as the animation in images.  The indices align to the animation
     * indices.
     * @return collisionArc
     */
    public CollisionBlob[] getCollisionArc() throws NullPointerException {
        if (collision == null) {
            throw new NullPointerException("Animation type doesn't have collision information: " + this.toString());
        }
        return collision.getCollisionArc();
    }
    
    /**
     * Retrieves the collision body array for this animation.  This array is as
     * long as the animation in images.  The indices align to the animation
     * indices.
     * @return collisionBody
     */
    public CollisionBlob[] getCollisionBody() {
        return collisionBody;
    }

    /**
     * Returns whether this animation state is a rollable state.
     * @return rollable
     */
    public boolean isRollable() {
        return false;
    }
    
    /**
     * Returns true if whatever is performing this animation is currently
     * invincible.  An example of this would be the start of a roll.
     * @return true if invincible; else false
     */
    public boolean isInvincible() {
        return false;
    }
    
    /**
     * Retrieves the previous animation state, or null if no state is previous.
     * @return previousState
     */
    public AnimationState getPreviousState() {
        return last;
    }
    
    /**
     * Returns the entry time of this state.  Entry time is the number of 
     * milliseconds since the epoch, as specified by {@link System#currentTimeMillis()}.
     * @return entryTime
     */
    public long getEntryTime() {
        return entryTime;
    }
}
