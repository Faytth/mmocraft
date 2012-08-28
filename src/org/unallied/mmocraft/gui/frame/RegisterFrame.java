package org.unallied.mmocraft.gui.frame;


import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.unallied.mmocraft.client.ImageID;
import org.unallied.mmocraft.constants.DatabaseConstants;
import org.unallied.mmocraft.constants.StringConstants;
import org.unallied.mmocraft.gui.EventType;
import org.unallied.mmocraft.gui.GUIElement;
import org.unallied.mmocraft.gui.ToolTip;
import org.unallied.mmocraft.gui.control.Button;
import org.unallied.mmocraft.gui.control.StaticText;
import org.unallied.mmocraft.gui.control.TextCtrl;
import org.unallied.mmocraft.net.handlers.RegistrationAckHandler;
import org.unallied.mmocraft.tools.Authenticator;

public class RegisterFrame extends Frame {

    /**
     * Displays error messages when the user goofs up, such as
     * "Username is too short."
     */
    private StaticText successStaticText;
    private TextCtrl userTextCtrl;
    private TextCtrl passTextCtrl;
    private TextCtrl pass2TextCtrl;
    private TextCtrl emailTextCtrl;
    private Button   backButton;
    private Button   registerButton;
    
    private GameContainer container;
    
    /**
     * Initializes a LoginFrame with its elements (e.g. Username, Password fields)
     * @param x The x offset for this frame (from the parent GUI element)
     * @param y The y offset for this frame (from the parent GUI element)
     */
    public RegisterFrame(final GUIElement parent, EventIntf intf, GameContainer container
            , float x, float y, int width, int height) {
        super(parent, intf, container, x, y, width, height);
        
        this.container = container;
        
        userTextCtrl = new TextCtrl(this, new EventIntf() {
            @Override
            public void callback(Event event) {
                //parent.callback(event);
            }
        }, container, "", 140, 0, -1, -1, ImageID.TEXTCTRL_LOGIN_NORMAL.toString()
                , ImageID.TEXTCTRL_LOGIN_HIGHLIGHTED.toString()
                , ImageID.TEXTCTRL_LOGIN_SELECTED.toString(), TextCtrl.NORMAL);
        userTextCtrl.setToolTip(new ToolTip("Must be between 6 and 14 characters.\nMust be unique.\nMay only contain a-z, A-Z, and/or 0-9."));
        
        passTextCtrl = new TextCtrl(this, new EventIntf() {
            @Override
            public void callback(Event event) {
                //parent.callback(event);
            }
        }, container, "", 140, 30, -1, -1, ImageID.TEXTCTRL_LOGIN_NORMAL.toString()
                , ImageID.TEXTCTRL_LOGIN_HIGHLIGHTED.toString()
                , ImageID.TEXTCTRL_LOGIN_SELECTED.toString(), TextCtrl.PASSWORD);
        passTextCtrl.setToolTip(new ToolTip("Must be between 6 and 50 characters."));
        
        pass2TextCtrl = new TextCtrl(this, new EventIntf() {
            @Override
            public void callback(Event event) {
                //parent.callback(event);
            }
        }, container, "", 140, 60, -1, -1, ImageID.TEXTCTRL_LOGIN_NORMAL.toString()
                , ImageID.TEXTCTRL_LOGIN_HIGHLIGHTED.toString()
                , ImageID.TEXTCTRL_LOGIN_SELECTED.toString(), TextCtrl.PASSWORD);
        
        emailTextCtrl = new TextCtrl(this, new EventIntf() {
            @Override
            public void callback(Event event) {
                //parent.callback(event);
            }
        }, container, "", 140, 90, -1, -1, ImageID.TEXTCTRL_LOGIN_NORMAL.toString()
                , ImageID.TEXTCTRL_LOGIN_HIGHLIGHTED.toString()
                , ImageID.TEXTCTRL_LOGIN_SELECTED.toString(), TextCtrl.NORMAL);
        emailTextCtrl.setToolTip(new ToolTip("Must be unique.\nE-mail addresses are used for password recovery."));
        
        backButton = new Button(this, new EventIntf() {
            @Override
            public void callback(Event event) {
                switch (event.getId()) {
                case BUTTON:
                    GUIElement parent = event.getElement().getParent();
                    parent.callback(new Event(parent, EventType.BACK_CLICKED));
                    break;
                }
            }
        }, container, "", 0, 120, -1, -1, ImageID.BUTTON_BACK_NORMAL.toString(),
        ImageID.BUTTON_BACK_HIGHLIGHTED.toString(),
        ImageID.BUTTON_BACK_SELECTED.toString(), 0);
        
        registerButton = new Button(this, new EventIntf() {
            @Override
            public void callback(Event event) {
                switch (event.getId()) {
                case BUTTON:
                    GUIElement parent = event.getElement().getParent();
                    parent.callback(new Event(parent, EventType.REGISTER_CLICKED));
                    break;
                }
            }
        }, container, "", 120, 120, -1, -1, ImageID.BUTTON_REGISTER_NORMAL.toString(),
        ImageID.BUTTON_REGISTER_HIGHLIGHTED.toString(),
        ImageID.BUTTON_REGISTER_SELECTED.toString(), 0);

        successStaticText = new StaticText(this, null, container, "", 0, 160, -1, -1, new Color(200, 0, 0));
        
        elements.add(new StaticText(this, null, container, "Username:", 0, 0, -1, -1));
        elements.add(userTextCtrl);
        elements.add(new StaticText(this, null, container, "Password:", 0, 30, -1, -1));
        elements.add(passTextCtrl);
        elements.add(new StaticText(this, null, container, "Re-enter Pass:", 0, 60, -1, -1));
        elements.add(pass2TextCtrl);
        elements.add(new StaticText(this, null, container, "E-mail Address:", 0, 90, -1, -1));
        elements.add(emailTextCtrl);
        elements.add(backButton);
        elements.add(registerButton);
        elements.add(successStaticText);
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

        // Check to see if there's an error message for registration
        if (RegistrationAckHandler.getLastError() != 0) {
            // TODO:  Make some sort of error class with a list of error strings
            successStaticText.setLabel("Error creating account.  Try a different name / email.");
        }
        
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
     * @return password
     */
    public String getPassword() {
        return passTextCtrl.getLabel();
    }
    
    /**
     * Returns the e-mail from the email text control
     * @return email
     */
    public String getEmail() {
        return emailTextCtrl.getLabel();
    }

    /**
     * Returns whether or not the player has entered a valid username, 
     * password, and email.
     * Also displays an appropriate message
     * @return valid true if valid, else false
     */
    public boolean isValid() {
        boolean result = false;
        if (!passTextCtrl.getLabel().equals(pass2TextCtrl.getLabel())) {
            successStaticText.setLabel(StringConstants.PASSWORD_MUST_MATCH);
        } else if (userTextCtrl.getLabel().length() < DatabaseConstants.MINIMUM_USERNAME_LENGTH) {
            successStaticText.setLabel(StringConstants.USERNAME_TOO_SHORT);
        } else if (userTextCtrl.getLabel().length() > DatabaseConstants.MAXIMUM_USERNAME_LENGTH) {
            successStaticText.setLabel(StringConstants.USERNAME_TOO_LONG);
        } else if (!Authenticator.isValidUser(userTextCtrl.getLabel())) {
            successStaticText.setLabel(StringConstants.USERNAME_INVALID);
        } else if (!Authenticator.isValidEmail(emailTextCtrl.getLabel())) {
            successStaticText.setLabel(StringConstants.EMAIL_INVALID);
        } else if (!Authenticator.isValidPass(passTextCtrl.getLabel())) {
            successStaticText.setLabel(StringConstants.PASSWORD_INVALID);
        } else {
            result = true;
            successStaticText.setLabel(StringConstants.CONTACTING_SERVER);
        }
        return result;
    }
}
