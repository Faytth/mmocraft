package org.unallied.mmocraft.gui.frame;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;
import org.unallied.mmocraft.ImageID;
import org.unallied.mmocraft.gui.GUIElement;
import org.unallied.mmocraft.gui.GUIUtility;
import org.unallied.mmocraft.gui.ToolTip;
import org.unallied.mmocraft.gui.control.Button;
import org.unallied.mmocraft.gui.control.StaticText;
import org.unallied.mmocraft.gui.control.TextCtrl;

public class LoginFrame extends GUIElement {

    private TextCtrl userTextCtrl;
    private TextCtrl passTextCtrl;
    private Button   loginButton;
    private List<GUIElement> elements = new ArrayList<GUIElement>();
    
    /**
     * Initializes a LoginFrame with its elements (e.g. Username, Password fields)
     * @param x The x offset for this frame (from the parent GUI element)
     * @param y The y offset for this frame (from the parent GUI element)
     */
    public LoginFrame(final GUIElement parent, EventIntf intf, GameContainer container
            , float x, float y, int width, int height) {
        super(parent, intf, container, x, y, width, height);
        
        userTextCtrl = new TextCtrl(this, new EventIntf() {
            @Override
            public void callback(Event event) {
                //parent.callback(event);
            }
        }, container, "", 100, 0, -1, -1, ImageID.TEXTCTRL_LOGIN_NORMAL.toString()
                , ImageID.TEXTCTRL_LOGIN_HIGHLIGHTED.toString()
                , ImageID.TEXTCTRL_LOGIN_SELECTED.toString(), 0);
        
        passTextCtrl = new TextCtrl(this, new EventIntf() {
            @Override
            public void callback(Event event) {
                //parent.callback(event);
            }
        }, container, "", 100, 30, -1, -1, ImageID.TEXTCTRL_LOGIN_NORMAL.toString()
                , ImageID.TEXTCTRL_LOGIN_HIGHLIGHTED.toString()
                , ImageID.TEXTCTRL_LOGIN_SELECTED.toString(), TextCtrl.PASSWORD);
        
        loginButton = new Button(this, new EventIntf() {
            @Override
            public void callback(Event event) {
                //parent.callback(event);
            }
        }, container, "", 100, 60, -1, -1, ImageID.BUTTON_LOGIN_NORMAL.toString()
                , ImageID.BUTTON_LOGIN_HIGHLIGHTED.toString()
                , ImageID.BUTTON_LOGIN_SELECTED.toString(), 0);
        
        loginButton.setToolTip(new ToolTip("This is a test"));
        
        elements.add(new StaticText(this, null, container, "Username:", 0, 0, -1, -1));
        elements.add(userTextCtrl);
        elements.add(new StaticText(this, null, container, "Password:", 0, 30, -1, -1));
        elements.add(passTextCtrl);
        elements.add(loginButton);
    }
    
    @Override
    public void update(GameContainer container) {

        // Iterate over all GUI controls and inform them of input
        for( GUIElement element : elements ) {
            element.update(container);
        }
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
        
        // Iterate over all GUI controls and inform them of input
        for( GUIElement element : elements ) {
            element.render(container, game, g);
        }
    }

    @Override
    public boolean isAcceptingInput() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void keyPressed(int key, char c) {
        // Respond to tab events
        if( key == Input.KEY_TAB ) {
            /* 
             * Cycle through all children searching for active element.
             * If found, then we set the next in line to the active element
             */
            boolean lastElementIsActive = false;
            GUIUtility util = GUIUtility.getInstance();
            for(GUIElement element : elements) {
                if( util.isActiveElement(element) ) {
                    lastElementIsActive = true;
                } else if( lastElementIsActive && element.isAcceptingTab()) {
                    util.setActiveElement(element);
                    // We're done
                    lastElementIsActive = false;
                    break;
                }
            }
            // If we're not done yet, set the FIRST element to active
            if( lastElementIsActive ) {
                for(GUIElement element : elements) {
                    if( element.isAcceptingTab() ) {
                        util.setActiveElement(element);
                        break; // Done
                    }
                }
            }
        }
    }

    @Override
    public boolean isAcceptingTab() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void renderImage() {
        // TODO Auto-generated method stub
        
    }
}
