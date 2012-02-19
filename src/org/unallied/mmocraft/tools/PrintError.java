package org.unallied.mmocraft.tools;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class PrintError {
    
    // Below are the filenames for different error logs
    public static final String EXCEPTION_CAUGHT = "exceptionCaught.rtf";
    
    public static void print(final String filename, final Throwable t) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filename, true); // append to the file
            out.write(getString(t).getBytes());
            out.write("\n-----------------------------------------------------------\n".getBytes());
        } catch (IOException e) {
            // we don't care, because this is an error log anyways...
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                // Again, we don't care
            }
        }
    }
    
    public static void print(final String filename, final String message) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filename, true); // append to the file
            out.write(message.getBytes());
            out.write("\n-----------------------------------------------------------\n".getBytes());
        } catch (IOException e) {
            // we don't care, because this is an error log anyways...
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                // Again, we don't care
            }
        }
    }
    
    public static String getString(final Throwable t) {
        String result = null;
        StringWriter sw = null;
        PrintWriter pw = null;
        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            t.printStackTrace(pw); // print stack trace to the writer
            result = sw.toString();
        } finally {
            try {
                if (pw != null) {
                    pw.close();
                }
                if (sw != null) {
                    sw.close();
                }
            } catch (IOException e) {
                // We don't care, because this is already an error
            }
        }
        
        return result;
    }
}
