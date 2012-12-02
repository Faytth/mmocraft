package org.unallied.mmocraft.chat;

import org.unallied.mmocraft.constants.StringConstants;

/**
 * Contains all available chat commands, such as /help and /reload.  Also
 * provides a mechanism for detecting whether a ChatMessage is a command.
 * @author Alexandria
 *
 */
public enum ChatCommand {
    NONE("/", ""), /** Not a chat command. */
    DIAGNOSTICS("/diagnostics", StringConstants.HELP_DIAGNOSTICS), /** Retrieves debug diagnostics. */
    HELP("/help", StringConstants.HELP_HELP), /** Displays help information. */
    PVP("/pvp", StringConstants.HELP_PVP), /** Toggles PvP on / off. */
    RELOAD_GUI("/reload", StringConstants.HELP_RELOAD_GUI), /** Reloads the GUI. */
    SELECT_SAY("/say", StringConstants.HELP_SELECT_SAY), /** Changes the chat frame to /say. */
    SELECT_WORLD("/world", StringConstants.HELP_SELECT_WORLD); /** Changes the chat frame to /world. */
    String name = "";
    String description;
    
    ChatCommand(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    /**
     * Retrieves the chat command associated with this message if one exists.
     * If no chat command was found, returns NONE.
     * @param message The message to search for a chat command.
     * @return chatCommand
     */
    public static ChatCommand getCommand(String message) {
        ChatCommand result = NONE;
        
        // A command can't have spaces.
        int index = message.indexOf(' ');
        if (index >= 0) {
            message = message.substring(0, index);
        }        
        int currentMatched = 0; // The current number of characters matched.
        
        for (ChatCommand cc : ChatCommand.values()) {
            if (cc != NONE && cc.name.startsWith(message) && message.length() > currentMatched) {
                currentMatched = message.length();
                result = cc;
            }
        }
        
        return result;
    }
    
    /**
     * Retrieves whether this message is considered to be a command or not.
     * Commands are specified as starting with a '/'.  No other message can
     * start with a '/'.
     * @param message The message to check.
     * @return isCommand True if this is a command, else false.
     */
    public static boolean isCommand(String message) {
        return message.startsWith("/");
    }
    
    @Override
    public String toString() {
        return name + " - " + description;
    }
}
