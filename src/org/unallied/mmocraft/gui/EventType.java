package org.unallied.mmocraft.gui;

public enum EventType {
    // Custom events made by frames
    LOGIN_CLICKED, 
    REGISTER_CLICKED, 
    /**
     * User clicked a "back" button, such as in the registration page.
     * This usually calls for a change of game state, such as returning
     * to the login state.
     */
    BACK_CLICKED,
    
    
    // Button Ctrl events
    /**
     * Generated when the button is clicked / activated
     */
    BUTTON,
    
    
    // Check Box Ctrl events
    /**
     * Generated when the checkbox is clicked / changed
     */
    CHECKBOX,
    
    
    // Text Ctrl events
    /**
     * Generated when the text changes
     */
    TEXT,
    /**
     * Generated when enter is pressed in a text control
     */
    TEXT_ENTER, 
    /**
     * User tried to enter more text than defined by max length
     */
    TEXT_MAXLEN
}
