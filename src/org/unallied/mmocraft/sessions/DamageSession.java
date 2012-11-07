package org.unallied.mmocraft.sessions;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;
import org.unallied.mmocraft.BoundLocation;
import org.unallied.mmocraft.Location;
import org.unallied.mmocraft.client.FontHandler;
import org.unallied.mmocraft.client.FontID;
import org.unallied.mmocraft.constants.WorldConstants;

/**
 * Contains all damage that needs to be rendered on the screen.
 * @author Alexandria
 *
 */
public class DamageSession {
    protected class DamageNode {
        private long creationTime;
        private int damageAmount;
        private BoundLocation location;
        private Color displayColor;
        
        public DamageNode(BoundLocation location, int damageAmount, Color displayColor) {
            this.creationTime = System.currentTimeMillis();
            this.damageAmount = damageAmount;
            this.location = location;
            this.displayColor = displayColor;
        }
        
        /**
         * Renders the damage node.
         * @param container The game container.
         * @param game The game.
         * @param g The graphics to render to.
         * @param camera The camera used for offsets.
         * @param delta The amount of time that has passed since this node was created in milliseconds.
         */
        public void render(GameContainer container, StateBasedGame game, Graphics g, BoundLocation camera, long delta) {
            float x = location.getX() - camera.getX();
            x = x > 0 ? x : WorldConstants.WORLD_WIDTH + x;
            x *= WorldConstants.WORLD_BLOCK_WIDTH;
            x += location.getXOffset() - camera.getXOffset();
            x = x > 0 ? x : WorldConstants.WORLD_BLOCK_WIDTH + x;
            float y = (location.getY() - camera.getY()) * WorldConstants.WORLD_BLOCK_HEIGHT;
            y += location.getYOffset() - camera.getYOffset();
            
            // Move horizontally based on the time.
            x += DEFAULT_MOVEMENT_X * delta;
            
            // Move up a bit based on the time.
            y -= DEFAULT_MOVEMENT_Y * delta;
            
            // Fade out the damage slowly.
            Color color = new Color(displayColor);
            color.a = displayColor.a * (DEFAULT_DURATION - delta) / DEFAULT_DURATION;
            
            // Render it.
            FontHandler.getInstance().draw(DEFAULT_FONT, "" + damageAmount, x, y, color, -1, -1, false);
        }
    }
    
    /**
     * The multiplier on the delta time in milliseconds to move the damage
     * display horizontally.  Positive values move the display to the right,
     * negative values move the display to the left.
     */
    protected float DEFAULT_MOVEMENT_X = 0;
    
    /** 
     * The multiplier on the delta time in milliseconds to move the damage 
     * display vertically.  Positive values move the display up, negative
     * values move the display down.
     */
    protected float DEFAULT_MOVEMENT_Y = 0.05f;
    
    /** The default font to render the damage in. */
    protected String DEFAULT_FONT = FontID.STATIC_TEXT_MEDIUM_BOLD.toString();
    
    /** The default color to render the damage in. */
    protected Color  DEFAULT_COLOR = new Color(222, 222, 222);

    /** The time in milliseconds that the damage should display for. */
    protected long   DEFAULT_DURATION = 3000L;
    
    /** 
     * Contains all of the damage nodes to render.  Nodes are removed
     * when systemTime - creationTime >= damageDuration.
     */
    protected List<DamageNode> damageNodes = new ArrayList<DamageNode>();
    
    /**
     * Renders damage on the screen.
     * @param container The game container.
     * @param game The game.
     * @param g The graphics to render to.
     * @param camera The camera to use as an offset.
     */
    public void render(GameContainer container, StateBasedGame game, Graphics g, BoundLocation camera) {
        long sysTime = System.currentTimeMillis();
        for (int i = 0; i < damageNodes.size(); ++i) {
            long delta = sysTime - damageNodes.get(i).creationTime;
            if (delta >= DEFAULT_DURATION) {
                damageNodes.remove(i--);
            } else {
                damageNodes.get(i).render(container, game, g, camera, delta);
            }
        }
    }
    
    /**
     * Adds a damage node to the damage session.  Damage nodes are used for
     * rendering damage on the screen.
     * @param location The location to render the damage.
     * @param damage The amount of damage to render.
     */
    public void addDamage(Location location, int damage) {
        addDamage(location, damage, DEFAULT_COLOR);
    }
    
    public void addDamage(Location location, int damage, Color color) {
        damageNodes.add(new DamageNode(new BoundLocation(location), damage, color));
    }
    
    
}
