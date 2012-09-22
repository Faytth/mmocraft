package org.unallied.mmocraft.gui.frame;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;
import org.unallied.mmocraft.Inventory;
import org.unallied.mmocraft.ItemType;
import org.unallied.mmocraft.client.FontHandler;
import org.unallied.mmocraft.client.FontID;
import org.unallied.mmocraft.client.Game;
import org.unallied.mmocraft.client.ImagePool;
import org.unallied.mmocraft.gui.GUIElement;

public class InventoryFrame extends Frame {
	
	private static final int categoryXOffset = 8;
	private static final int categoryYOffset = 25;
	
	/** The font that item categories, such as Equipment, should be displayed in. */
	private static final String CATEGORY_FONT = FontID.STATIC_TEXT_LARGE.toString();
	
	/** The color that categories, such as Equipment, should be displayed in. */
	private static final Color CATEGORY_COLOR = new Color(222, 222, 222);

	/** The font that items, such as Flimsy Broadsword, should be displayed in. */
	private static final String ITEM_FONT = FontID.STATIC_TEXT_MEDIUM.toString();
	
	/** The color that items, such as Flimsy Broadsword, should be displayed in. */
	private static final Color ITEM_COLOR = new Color(222, 222, 222);

    /**
     * Initializes a LoginFrame with its elements (e.g. Username, Password fields)
     * @param x The x offset for this frame (from the parent GUI element)
     * @param y The y offset for this frame (from the parent GUI element)
     */
    public InventoryFrame(final Frame parent, EventIntf intf, GameContainer container
            , float x, float y, int width, int height) {
        super(parent, intf, container, x, y, width, height);

        this.width  = 250;
        this.height = 400;
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

    public void renderBackground(Graphics g) {
		final int offX = getAbsoluteWidth();  // offset from left of screen
		final int offY = getAbsoluteHeight(); // offset from top of screen
    	
        // Render background
        g.fill(new Rectangle(offX, offY, width, height),
        		new GradientFill(0, 0, new Color(47, 47, 55, 166), 
        				width, height, new Color(87, 87, 95, 166), true));
    }
    
    public int renderCategory(Inventory inventory, ItemType type, Graphics g, final Font categoryFont, int yOffset) {
    	int categoryWidth = 0;
		final int offX = getAbsoluteWidth();  // offset from left of screen
		final int offY = getAbsoluteHeight(); // offset from top of screen
    	
        // Render Equipment
        FontHandler.getInstance().draw(CATEGORY_FONT, type.toString(), categoryXOffset + offX, 
        		categoryYOffset + yOffset + offY, CATEGORY_COLOR, width - categoryXOffset, 
        		height - yOffset - categoryYOffset, false);
        categoryWidth = categoryFont.getWidth(type.toString());
        int categoryHeight = categoryFont.getHeight(type.toString());
        g.fill(new Rectangle(categoryXOffset + categoryWidth + offX + 4, categoryYOffset + yOffset + offY + categoryHeight / 2, 
        		width - (categoryXOffset + categoryWidth + 4) - 4, 4),
        		new GradientFill(0, 0, new Color(222, 222, 240),
        				width - (categoryXOffset + categoryWidth + 4) - 4, 4, new Color(222, 222, 240)));
        yOffset += categoryHeight + 2;
        
        return 0;
    }
    
    public void renderInventory(Inventory inventory, Graphics g) {
		int yOffset = 0; // additional offset based on what was already drawn
        final Font categoryFont = FontHandler.getInstance().getFont(CATEGORY_FONT);

        yOffset = renderCategory(inventory, ItemType.EQUIPMENT, g, categoryFont, yOffset);
        yOffset = renderCategory(inventory, ItemType.CONSUMABLES, g, categoryFont, yOffset);
        yOffset = renderCategory(inventory, ItemType.TRADE_GOODS, g, categoryFont, yOffset);
        yOffset = renderCategory(inventory, ItemType.BLOCKS, g, categoryFont, yOffset);
        yOffset = renderCategory(inventory, ItemType.MISCELLANEOUS, g, categoryFont, yOffset);
    }
    
    public void render(GameContainer container, StateBasedGame game
            , Graphics g) {
        
    	if (!hidden) {
        	try {
        		renderBackground(g);
        		Inventory inventory = Game.getInstance().getClient().getPlayer().getInventory();
        		renderInventory(inventory, g);
        	} catch (Exception e) {
    			e.printStackTrace();
    		}
        	
	        if (elements != null) {
	            // Iterate over all GUI controls and inform them of input
	            for( GUIElement element : elements ) {
	                element.render(container, game, g);
	            }
	            
	            for( GUIElement element : elements ) {
	                element.renderToolTip(container, game, g);
	            }
	        }
    	}
    }

    
    @Override
    public void renderImage(Image image) {

    }
    
	@Override
	protected boolean isAcceptingFocus() {
		return true;
	}
}
