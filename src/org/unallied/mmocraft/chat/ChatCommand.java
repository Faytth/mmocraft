package org.unallied.mmocraft.chat;

/**
 * Contains all available chat commands, such as /help and /reload.  Also
 * provides a mechanism for detecting whether a ChatMessage is a command.
 * @author Alexandria
 *
 */
public enum ChatCommand {
    NONE("/"), /** Not a chat command. */
    DIAGNOSTICS("/diagnostics"), /** Retrieves debug diagnostics. */
    HELP("/help"), /** Displays help information. */
    RELOAD_GUI("/reload"), /** Reloads the GUI. */
    SELECT_SAY("/say"), /** Changes the chat frame to /say. */
    SELECT_WORLD("/world"); /** Changes the chat frame to /world. */
    String name = "";
    
    ChatCommand(String name) {
        this.name = name;
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
            if (cc != NONE && cc.name.contains(message) && message.length() > currentMatched) {
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
}
