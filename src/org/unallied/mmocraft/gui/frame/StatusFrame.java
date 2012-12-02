package org.unallied.mmocraft.gui.frame;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.unallied.mmocraft.Player;
import org.unallied.mmocraft.client.FontID;
import org.unallied.mmocraft.client.Game;
import org.unallied.mmocraft.constants.ClientConstants;
import org.unallied.mmocraft.constants.StringConstants;
import org.unallied.mmocraft.gui.GUIElement;
import org.unallied.mmocraft.gui.control.Gauge;
import org.unallied.mmocraft.gui.control.StaticText;
import org.unallied.mmocraft.gui.tooltips.ToolTip;
import org.unallied.mmocraft.net.handlers.PvPToggleResponseHandler;

/**
 * A frame showing the player's status.  This includes information such as the
 * player's current / maximum HP, experience, level, name, and status ailments / buffs.
 * 
 * @author Alexandria
 *
 */
public class StatusFrame extends Frame {
    /** Contains the actual values for the coordinates, e.g. "(4, 27)". */
    private Gauge healthGauge;
    private StaticText playerNameStaticText;
        
    public StatusFrame(Frame parent, EventIntf intf, GameContainer container,
            float x, float y, int width, int height) {
        super(parent, intf, container, x, y, width, height);
        
        playerNameStaticText = new StaticText(this, null, container, "", 0, 0, 
                -1, -1, FontID.STATIC_TEXT_MEDIUM_BOLD, new Color(238, 238, 238));

        healthGauge = new Gauge(this, container, 0, 0, 
                playerNameStaticText.getHeight(), 200, 13, 
                new Color(200, 30, 25), Gauge.SHOW_PERCENT);
        healthGauge.setToolTip(new ToolTip("The player's HP.  If this reaches 0, you DIE!  BWAHAHAHAHA!!!"));
        
        elements.add(playerNameStaticText);
        elements.add(healthGauge);
        this.height = healthGauge.getY() + healthGauge.getHeight();
        this.width = healthGauge.getX() + healthGauge.getWidth();
    }
    
    @Override
    public void update(GameContainer container) {
        // Check the player's health, updating if necessary.
        try {
            Player p = Game.getInstance().getClient().getPlayer();
            healthGauge.setRange(p.getHpMax());
            healthGauge.setValue(p.getHpCurrent());

            playerNameStaticText.setLabel(p.getName());
            long pvpFlagDuration = PvPToggleResponseHandler.getPvPFlagDuration();
            if (pvpFlagDuration == -1 || pvpFlagDuration > 0) {
                playerNameStaticText.setColor(ClientConstants.PLAYER_NAME_PVP);
                if (pvpFlagDuration == -1) {
                    playerNameStaticText.setToolTip(new ToolTip(StringConstants.PVP_FLAG_DISABLE_TEXT));
                } else {
                    playerNameStaticText.setToolTip(new ToolTip(String.format(StringConstants.PVP_FLAG_DISABLING_TEXT, pvpFlagDuration / 1000)));
                }
            } else {
                playerNameStaticText.setColor(ClientConstants.PLAYER_NAME_NORMAL);
                playerNameStaticText.setToolTip(null);
            }
            
            // Iterate over all GUI controls and inform them of input
            for( GUIElement element : elements ) {
                element.update(container);
            }
        } catch (Throwable t) {
            
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
