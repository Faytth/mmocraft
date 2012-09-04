package org.unallied.mmocraft.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.GameState;
import org.unallied.mmocraft.gui.GUIElement;
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
}
