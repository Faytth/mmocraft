package org.unallied.mmocraft.animations;

import java.io.Serializable;

import org.newdawn.slick.Animation;
import org.newdawn.slick.SpriteSheet;
import org.unallied.mmocraft.CollisionBlob;
import org.unallied.mmocraft.Player;

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
    
    protected transient Animation animation = null; // the animation to play for this state
    protected transient Player player; // the player attached to this animation
    protected transient AnimationState last; // the last animation state
    protected transient long entryTime; // the time (in millis since 1970) that this state was entered
    protected transient float horizontalOffset = 0.0f; // Offset from the left.  +1 moves player LEFT 1 pixel.
    protected transient float verticalOffset = 0.0f; // Offset from the top.  +1 moves player UP 1 pixel.
    /** The collision data for this animation.  Used to determine attack arcs. */
    protected transient CollisionBlob[] collisionArc = null;
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
    
    public AnimationState(Player player, AnimationState last) {
        this.player = player;
        this.last   = last;
        this.entryTime = System.currentTimeMillis();
        
        // Prevent unnecessary chains
        if (last != null) {
            last.last = null;
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
    
    /**
     * Renders the animation to the screen
     * @param x The x location from the left side of the screen to draw at
     * @param y The y location from the top of the screen to draw at
     */
    public void render(float x, float y, boolean flipped) {
        if (animation != null) {
            animation.getCurrentFrame().getFlippedCopy(flipped, false).draw(
                    flipped ? x - (animation.getWidth()-horizontalOffset-player.getWidth()) : x - horizontalOffset,
                    y - verticalOffset);
        }
    }
    
    /**
     * Retrieves the width of this animation, or 0 if no animation is set.
     * @return width
     */
    public int getWidth() {
        if (animation != null) {
            return animation.getWidth();
        }
        
        return 0;
    }
    
    /**
     * Updates the animation.
     * @param delta time since last update in milliseconds
     */
    public void update(long delta) {
        if (animation != null) {
            int startingIndex = animation.getFrame();
            animation.update(delta);
            int endingIndex = animation.getFrame();
            if (startingIndex != endingIndex && collisionArc != null) {
                player.doCollisionChecks(collisionArc, startingIndex+1, 
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
            for (int i=0; i < ss.getVerticalCount(); ++i) {
                for (int j=0; j < ss.getHorizontalCount(); ++j) {
                    animation.addFrame(ss.getSprite(j, i), this.player.getDelay());
                }
            }
        }
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
     * Returns whether or not this character is in a state which can move down
     * @return
     */
    public boolean canMoveDown() {
        return false;
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
    public abstract AnimationType getId();
    
    /**
     * Generates a collision arc from the sprite sheet.  This arc is needed for
     * all animations that perform an attack.
     * 
     * The sprite sheet that is used must have all of its images on a single,
     * horizontal line.  All other lines will be ignored.
     * 
     * The sprite sheet should have the same number of images as the animation.
     * @param ss The sprite sheet to generate the arc from.
     */
    public void generateCollisionArc(SpriteSheet ss) {
        // Guard
        if (ss == null || collisionArc != null) {
            return;
        }
        
        collisionArc = new CollisionBlob[ss.getHorizontalCount()];
        for (int x=0; x < ss.getHorizontalCount(); ++x) {
            collisionArc[x] = new CollisionBlob(ss.getSprite(x, 0));
        }
    }
    
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
}
