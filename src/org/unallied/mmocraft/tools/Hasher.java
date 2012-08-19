package org.unallied.mmocraft.tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Allows for hashing of values, such as user passwords.
 * @author Faythless
 *
 */
public class Hasher {
    
    /**
     * Returns the SHA-256 hash of data, or null on failure
     * @param data The data to hash
     * @return SHA-256 hash of data
     */
    public static byte[] getSHA256(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return md.digest(data);
        } catch (NoSuchAlgorithmException e) {
            PrintError.print(PrintError.EXCEPTION_CAUGHT, 
                    "Failed to hash data.  SHA-256 wasn't found.");
        }
        
        return null; // failed to hash
    }

}
