package org.unallied.mmocraft.gui.frame;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.unallied.mmocraft.client.FontID;
import org.unallied.mmocraft.client.ImageID;
import org.unallied.mmocraft.constants.StringConstants;
import org.unallied.mmocraft.gui.EventType;
import org.unallied.mmocraft.gui.GUIElement;
import org.unallied.mmocraft.gui.GUIUtility;
import org.unallied.mmocraft.gui.control.Button;
import org.unallied.mmocraft.gui.control.StaticText;
import org.unallied.mmocraft.gui.control.TextCtrl;
import org.unallied.mmocraft.gui.tooltips.ToolTip;
import org.unallied.mmocraft.net.ConnectionStatus;
import org.unallied.mmocraft.net.PacketSocket;
import org.unallied.mmocraft.net.handlers.LoginErrorHandler;

public class LoginFrame extends Frame {

    private TextCtrl userTextCtrl;
    private TextCtrl passTextCtrl;
    private Button   loginButton;
    private Button   registerButton;
    
    /** Displays error messages when the user fails to log in. */
    private StaticText successStaticText;
    
    /** Color to display the static text in. (Gold) */
    private final static Color TEXT_COLOR = new Color(246, 246, 246);
    
    /**
     * Initializes a LoginFrame with its elements (e.g. Username, Password fields)
     * @param x The x offset for this frame (from the parent GUI element)
     * @param y The y offset for this frame (from the parent GUI element)
     */
    public LoginFrame(final Frame parent, EventIntf intf, GameContainer container
            , float x, float y, int width, int height) {
        super(parent, intf, container, x, y, width, height);
        
        init();
    }
    
    public void init() {
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
        }, container, "", 120, 30, 140, 21, TextCtrl.PASSWORD);
        
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
        }, container, "", 120, 0, 140, 21, 0);
        
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
        registerButton.setToolTip(new ToolTip("If you don't have an account, you can register for one here."));
        
        successStaticText = new StaticText(this, null, container, "", 0, 
                registerButton.getY() + registerButton.getHeight(), -1, -1, 
                FontID.STATIC_TEXT_MEDIUM, new Color(200, 0, 0));
        
        elements.add(new StaticText(this, null, container, "Username:", 0, 0, 
                -1, -1, FontID.STATIC_TEXT_LARGE_BOLD, TEXT_COLOR));
        elements.add(userTextCtrl);
        elements.add(new StaticText(this, null, container, "Password:", 0, 30, 
                -1, -1, FontID.STATIC_TEXT_LARGE_BOLD, TEXT_COLOR));
        elements.add(passTextCtrl);
        elements.add(loginButton);
        elements.add(registerButton);
        elements.add(successStaticText);
        
        GUIUtility.getInstance().setActiveElement(userTextCtrl);
        
        this.width = passTextCtrl.getWidth() + passTextCtrl.getX();
        this.height = registerButton.getHeight() + registerButton.getY();
    }
    
    @Override
    public void update(GameContainer container) {

        try {
            if (PacketSocket.getConnectionStatus() == ConnectionStatus.CONTACTING_SERVER) {
                successStaticText.setLabel(StringConstants.CONTACTING_SERVER);
            } else if (LoginErrorHandler.getLastError() != 0) {
                successStaticText.setLabel(StringConstants.LOGIN_ERROR);
            } else if (PacketSocket.getConnectionStatus() == ConnectionStatus.FAILED_TO_CONNECT) {
                successStaticText.setLabel(StringConstants.CONNECTION_ERROR);
            } else {
                successStaticText.setLabel("");
            }
            
            // Iterate over all GUI controls and inform them of input
            for( GUIElement element : elements ) {
                element.update(container);
            }
        } catch (Throwable t) {}
    }

    @Override
    public boolean isAcceptingTab() {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    public void renderImage(Graphics g) {
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

	@Override
	public boolean isAcceptingFocus() {
		return true;
	}
}
