package org.unallied.mmocraft.gui;

public enum EventType {
    // Custom events made by frames
	/** Occurs when the user tries to login. */
    LOGIN_CLICKED, 
    /** Occurs when the user tries to register an account. */
    REGISTER_CLICKED,
    /**
     * User clicked a "back" button, such as in the registration page.
     * This usually calls for a change of game state, such as returning
     * to the login state.
     */
    BACK_CLICKED,
    /** Occurs when the user attempts to send a chat message. */
    SEND_CHAT_MESSAGE,
    /** Occurs when the user toggles their inventory on/off by the toolbar. */
    INVENTORY_CLICKED,
    /** Occurs when the user toggles their character info on/off by the toolbar. */
    CHARACTER_CLICKED, 
    
    
    // Button Ctrl events
    /** Generated when the button is clicked / activated */
    BUTTON,
    
    
    // Check Box Ctrl events
    /** Generated when the checkbox is clicked / changed */
    CHECKBOX,
    
    
    // Text Ctrl events
    /** Generated when the text changes */
    TEXT,
    /** Generated when enter is pressed in a text control */
    TEXT_ENTER, 
    /** User tried to enter more text than defined by max length */
    TEXT_MAXLEN, 
}
