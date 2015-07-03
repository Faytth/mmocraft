package org.unallied.mmocraft.gui.frame;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.unallied.mmocraft.client.ImageID;
import org.unallied.mmocraft.gui.EventType;
import org.unallied.mmocraft.gui.GUIElement;
import org.unallied.mmocraft.gui.control.Button;
import org.unallied.mmocraft.gui.tooltips.ToolTip;

/**
 * A toolbar which provides easy access for the user to the most commonly used
 * frames, such as the inventory and character sheet / skills.
 * 
 * @author Alexandria
 *
 */
public class ToolbarFrame extends Frame {

    /** A button that displays the character info frame when clicked. */
    private Button characterButton;
    
    /** A button that displays the inventory when clicked. */
    private Button inventoryButton;
    
    /**
     * Initializes a LoginFrame with its elements (e.g. Username, Password fields)
     * @param x The x offset for this frame (from the parent GUI element)
     * @param y The y offset for this frame (from the parent GUI element)
     */
    public ToolbarFrame(final Frame parent, EventIntf intf, GameContainer container
            , float x, float y, int width, int height) {
        super(parent, intf, container, x, y, width, height);

        characterButton = new Button(this, new EventIntf() {
            @Override
            public void callback(Event event) {
                switch(event.getId()) {
                case BUTTON:
                    GUIElement element = event.getElement().getParent();
                    element.callback(new Event(element, EventType.CHARACTER_CLICKED));
                    break;
                }
            }
        }, container, "", 0, 0, -1, -1, ImageID.TOGGLEBUTTON_TOOLBARCHARACTER_NORMAL.toString(),
                ImageID.TOGGLEBUTTON_TOOLBARCHARACTER_HIGHLIGHTED.toString(),
                ImageID.TOGGLEBUTTON_TOOLBARCHARACTER_SELECTED.toString(), 0);
        characterButton.setToolTip(new ToolTip("Opens and closes your character info.  Default keyboard shortcut for this is \"C\"."));
        
        inventoryButton = new Button(this, new EventIntf() {
            @Override
            public void callback(Event event) {
                switch (event.getId()) {
                case BUTTON:
                    GUIElement element = event.getElement().getParent();
                    element.callback(new Event(element, EventType.INVENTORY_CLICKED));
                    break;
                }
            }
        }, container, "", 0, 0, -1, -1, ImageID.TOGGLEBUTTON_TOOLBARINVENTORY_NORMAL.toString(), 
                ImageID.TOGGLEBUTTON_TOOLBARINVENTORY_HIGHLIGHTED.toString(),
                ImageID.TOGGLEBUTTON_TOOLBARINVENTORY_SELECTED.toString(), 0);
        inventoryButton.setX(characterButton.getX() + characterButton.getWidth());
        inventoryButton.setToolTip(new ToolTip("Opens and closes your inventory.  Default keyboard shortcuts for this are \"I\" and \"B\"."));
        
        elements.add(characterButton);
        elements.add(inventoryButton);
                
        this.width = inventoryButton.getWidth() + inventoryButton.getX();
        this.height = inventoryButton.getHeight() + inventoryButton.getY();
    }
    
    @Override
    public void update(GameContainer container) {

        // Iterate over all GUI controls and inform them of input
        for( GUIElement element : elements ) {
            element.update(container);
        }
    }

    @Override
    public boolean isAcceptingTab() {
        return false;
    }
    
    @Override
    public void renderImage(Graphics g) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean isAcceptingFocus() {
        return false;
    }
}