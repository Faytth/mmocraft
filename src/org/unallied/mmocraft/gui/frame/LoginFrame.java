package org.unallied.mmocraft.gui.frame;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.unallied.mmocraft.client.FontID;
import org.unallied.mmocraft.client.ImageID;
import org.unallied.mmocraft.gui.EventType;
import org.unallied.mmocraft.gui.GUIElement;
import org.unallied.mmocraft.gui.GUIUtility;
import org.unallied.mmocraft.gui.ToolTip;
import org.unallied.mmocraft.gui.control.Button;
import org.unallied.mmocraft.gui.control.StaticText;
import org.unallied.mmocraft.gui.control.TextCtrl;

public class LoginFrame extends Frame {

    private TextCtrl userTextCtrl;
    private TextCtrl passTextCtrl;
    private Button   loginButton;
    private Button   registerButton;
    
    /**
     * Initializes a LoginFrame with its elements (e.g. Username, Password fields)
     * @param x The x offset for this frame (from the parent GUI element)
     * @param y The y offset for this frame (from the parent GUI element)
     */
    public LoginFrame(final GUIElement parent, EventIntf intf, GameContainer container
            , float x, float y, int width, int height) {
        super(parent, intf, container, x, y, width, height);

        passTextCtrl = new TextCtrl(this, new EventIntf() {
            @Override
            public void callback(Event event) {
                switch (event.getId()) {
                case TEXT_ENTER:
                    GUIElement element = event.getElement().getParent();
                    element.callback(new Event(element, EventType.LOGIN_CLICKED));
                    break;
                }
                //parent.callback(event);
            }
        }, container, "", 120, 30, -1, -1, ImageID.TEXTCTRL_LOGIN_NORMAL.toString()
                , ImageID.TEXTCTRL_LOGIN_HIGHLIGHTED.toString()
                , ImageID.TEXTCTRL_LOGIN_SELECTED.toString(), TextCtrl.PASSWORD);
        
        // Must be defined after passTextCtrl for event order to work properly.
        userTextCtrl = new TextCtrl(this, new EventIntf() {
            @Override
            public void callback(Event event) {
                switch (event.getId()) {
                case TEXT_ENTER:
                    GUIUtility.getInstance().setActiveElement(passTextCtrl);
                    break;
                }
            }
        }, container, "", 120, 0, -1, -1, ImageID.TEXTCTRL_LOGIN_NORMAL.toString()
                , ImageID.TEXTCTRL_LOGIN_HIGHLIGHTED.toString()
                , ImageID.TEXTCTRL_LOGIN_SELECTED.toString(), 0);
        
        loginButton = new Button(this, new EventIntf() {
            @Override
            public void callback(Event event) {
                switch (event.getId()) {
                case BUTTON:
                    GUIElement element = event.getElement().getParent();
                    element.callback(new Event(element, EventType.LOGIN_CLICKED));
                    break;
                }
            }
        }, container, "", 160, 60, -1, -1, ImageID.BUTTON_LOGIN_NORMAL.toString()
                , ImageID.BUTTON_LOGIN_HIGHLIGHTED.toString()
                , ImageID.BUTTON_LOGIN_SELECTED.toString(), 0);
        
        registerButton = new Button(this, new EventIntf() {
            @Override
            public void callback(Event event) {
                switch (event.getId()) {
                case BUTTON:
                    GUIElement element = event.getElement().getParent();
                    element.callback(new Event(element, EventType.REGISTER_CLICKED));
                    break;
                }
            }
        }, container, "", 0, 60, -1, -1, ImageID.BUTTON_REGISTER_NORMAL.toString(),
        ImageID.BUTTON_REGISTER_HIGHLIGHTED.toString(),
        ImageID.BUTTON_REGISTER_SELECTED.toString(), 0);
        
        registerButton.setToolTip(new ToolTip("If you don't have an\naccount, you can register\nfor one here."));
        
        elements.add(new StaticText(this, null, container, "Username:", 0, 0, -1, -1, FontID.STATIC_TEXT_LARGE));
        elements.add(userTextCtrl);
        elements.add(new StaticText(this, null, container, "Password:", 0, 30, -1, -1, FontID.STATIC_TEXT_LARGE));
        elements.add(passTextCtrl);
        elements.add(loginButton);
        elements.add(registerButton);
    }
    
    @Override
    /**
     * This should be called when this element is being destroyed.  By calling
     * this, the GUI element is able to properly remove itself from the container
     * as a listener.
     */
    public void destroy() {
        
        // Destroy our children
        for(GUIElement element : elements) {
            element.destroy();
        }
        
        if (isHandler) {
            container.getInput().removeListener(this);
        }
    }
    
    @Override
    public void update(GameContainer container) {

        // Iterate over all GUI controls and inform them of input
        for( GUIElement element : elements ) {
            element.update(container);
        }
    }

    @Override
    public boolean isAcceptingInput() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isAcceptingTab() {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    public void renderImage(Image image) {
        // TODO Auto-generated method stub
        
    }

    /**
     * Returns the username from the username text control
     * @return username
     */
    public String getUsername() {
        return userTextCtrl.getLabel();
    }
    
    /**
     * Returns the password from the password text control
     * @return username
     */
    public String getPassword() {
        return passTextCtrl.getLabel();
    }
}
