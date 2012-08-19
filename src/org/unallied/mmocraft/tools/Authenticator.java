package org.unallied.mmocraft.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The authenticator class is used to test whether something is valid or invalid.
 * An example usage would be to test whether a player's name is valid.  This class
 * is used for validation on both the client and server.
 * @author Alexandria
 *
 */
public class Authenticator {

    public static boolean isValidUser(String user) {
        if (user != null) {
            if (user.length() >= 6 && user.length() <= 14) {
                boolean result = true;
                for (int i=0; i < user.length() && result; ++i) {
                    char c = user.charAt(i);
                    if (!((c >= 'A' && c <= 'Z') ||
                        (c >= 'a' && c <= 'z') ||
                        (c >= '0' && c <= '9'))) {
                        result = false;
                    }
                }
                return result;
            }
        }
        return false;
    }

    public static boolean isValidPlayerName(String playerName) {
        if (playerName != null) {
            if (playerName.length() >= 3 && playerName.length() <= 12) {
                boolean result = true;
                for (int i=0; i < playerName.length() && result; ++i) {
                    char c = playerName.charAt(i);
                    if (!((c >= 'A' && c <= 'Z') ||
                        (c >= 'a' && c <= 'z') ||
                        (c >= '0' && c <= '9'))) {
                        result = false;
                    }
                }
                return result;
            }
        }
        return false;
    }
    
    public static boolean isValidPass(String pass) {
        if (pass != null) {
            if (pass.length() >= 6 && pass.length() <= 50) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValidEmail(String email) {
        // No emails longer than 128 characters
        if (email != null && email.length() >= 5 && email.length() <= 128) {
            Pattern pattern = Pattern.compile("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b");
            Matcher matcher = pattern.matcher(email);
            if (matcher.matches()) { // Good email
                return true;
            }
        }
        return false;
    }

}
