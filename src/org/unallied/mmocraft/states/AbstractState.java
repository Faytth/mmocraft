package org.unallied.mmocraft.states;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.GameState;
import org.unallied.mmocraft.gui.frame.Frame;

/**
 * An abstract state from which all other states are derived.
 * @author Alexandria
 *
 */
public abstract class AbstractState extends Frame implements GameState {

    public AbstractState(Frame parent, EventIntf intf,
            GameContainer container, float x, float y, int width, int height) {
        super(parent, intf, container, x, y, width, height);
    }
    
    /**
     * Resizes the game to fit the display size.
     * @param container The container to resize
     */
    public void resize(GameContainer container) {
        if (container instanceof org.newdawn.slick.AppGameContainer) {
            try {
                ((org.newdawn.slick.AppGameContainer)container).setDisplayMode(
                        Display.getWidth(), Display.getHeight(), Display.isFullscreen());
            } catch (SlickException e) {
                e.printStackTrace();
            }
        } else if (container instanceof org.newdawn.slick.AppletGameContainer.Container) {
            try {
                boolean needsRefresh = container.getGraphics().getWidth() != Display.getDisplayMode().getWidth() ||
                        container.getGraphics().getHeight() != Display.getDisplayMode().getHeight();
                ((org.newdawn.slick.AppletGameContainer.Container)container).setDisplayMode(Display.getWidth(), Display.getHeight(), Display.isFullscreen(), needsRefresh);
            } catch (SlickException e) {
                e.printStackTrace();
            }
        }
        container.getGraphics().setDimensions(Display.getWidth(), Display.getHeight());
    }
}
