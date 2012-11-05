package org.unallied.mmocraft.gui.frame;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.unallied.mmocraft.Location;
import org.unallied.mmocraft.client.FontID;
import org.unallied.mmocraft.client.Game;
import org.unallied.mmocraft.constants.WorldConstants;
import org.unallied.mmocraft.gui.control.StaticText;

/**
 * A frame containing information in regards to a minimap.  Until a proper
 * minimap is created (if it ever is), this frame will display the player's
 * chunk coordinates and region name (haven't added it yet).
 * @author Alexandria
 *
 */
public class MiniMapFrame extends Frame {

    /** Contains the actual values for the coordinates, e.g. "(4, 27)". */
    private StaticText coordValueStaticText;
    
    public MiniMapFrame(Frame parent, EventIntf intf, GameContainer container,
            float x, float y, int width, int height) {
        super(parent, intf, container, x, y, width, height);
        
        coordValueStaticText = new StaticText(this, null, container, "(0,0)", 80, 0, -1, -1, FontID.STATIC_TEXT_MEDIUM_BOLD);
        elements.add(coordValueStaticText);
    }
    
    @Override
    public void update(GameContainer container) {
        // Update player's location
        try {
            Location location = Game.getInstance().getClient().getPlayer().getLocation();
            long x = location.getX() / WorldConstants.WORLD_CHUNK_WIDTH;
            long y = location.getY() / WorldConstants.WORLD_CHUNK_HEIGHT;
            
            coordValueStaticText.setLabel("(" + x + ", " + y + ")");
        } catch (NullPointerException e) {
            // Must have failed to get the player.  Not loaded yet?  Just do nothing.
        }
    }
    
    @Override
    public boolean isAcceptingTab() {
        return false;
    }

    @Override
    public boolean isAcceptingFocus() {
        return false;
    }

    @Override
    public void renderImage(Graphics g) {
        // There's nothing special to render for this frame.
    }
}
