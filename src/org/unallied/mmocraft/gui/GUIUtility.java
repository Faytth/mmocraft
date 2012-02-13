package org.unallied.mmocraft.gui;

/**
 * A utility class for GUI elements
 * @author Ryokko
 *
 */
public class GUIUtility {
    
    private GUIElement activeElement = null;
    
    /**
     * Private constructor for singleton pattern
     */
    private GUIUtility() {
        
    }
    
    /**
     * This is a singleton holder.  It is loaded on the first execution of
     * GUIUtility.getInstance() or the first access to the holder, not before
     * @author Ryokko
     *
     */
    private static class GUIUtilityHolder {
        public static final GUIUtility instance = new GUIUtility();
    }
    
    /**
     * Returns the GUI Utility handler singleton.
     * @return the singleton GUI Utility
     */
    public static GUIUtility getInstance() {
        return GUIUtilityHolder.instance;
    }
    
    /**
     * Returns the active GUI element
     * @return the active GUI element or null if none
     */
    public GUIElement getActiveElement() {
        return activeElement;
    }
    
    /**
     * Sets the current GUI active element
     * @param element The new active element
     */
    public void setActiveElement(GUIElement element) {
        this.activeElement = element;
    }
    
    /**
     * Returns true if the element is the active element, otherwise false.
     * @param element The element to compare against the active element
     * @return True if the element is the active element; else false
     */
    public boolean isActiveElement(GUIElement element) {
        if( activeElement != null ) {
            return activeElement.equals(element);
        } else {
            return false;
        }
    }
}
