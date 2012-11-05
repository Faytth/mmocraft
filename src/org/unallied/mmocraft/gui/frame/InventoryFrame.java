package org.unallied.mmocraft.gui.frame;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;
import org.unallied.mmocraft.client.FontHandler;
import org.unallied.mmocraft.client.FontID;
import org.unallied.mmocraft.client.Game;
import org.unallied.mmocraft.client.ImageID;
import org.unallied.mmocraft.gui.GUIElement;
import org.unallied.mmocraft.gui.GUIUtility;
import org.unallied.mmocraft.gui.StringNode;
import org.unallied.mmocraft.gui.control.Button;
import org.unallied.mmocraft.gui.control.StaticText;
import org.unallied.mmocraft.gui.tooltips.ItemToolTip;
import org.unallied.mmocraft.gui.tooltips.ToolTip;
import org.unallied.mmocraft.items.Inventory;
import org.unallied.mmocraft.items.Item;
import org.unallied.mmocraft.items.ItemData;
import org.unallied.mmocraft.items.ItemManager;
import org.unallied.mmocraft.items.ItemType;

public class InventoryFrame extends Frame {
	
    protected enum ItemCategory {
        CATEGORY, ITEM
    }
    
    protected class ItemElementComparator implements Comparator<ItemElement> {

        @Override
        public int compare(ItemElement arg0, ItemElement arg1) {
            return arg0.yOffset - arg1.yOffset;
        }
        
    }
    
    /**
     * This is a pretty ugly PriorityQueue container class which should ideally be split into two separate classes.
     * However, I don't feel the time doing so would be worth it.
     * @author Alexandria
     *
     */
    protected class ItemElement {
        protected ItemData data = null;
        protected String name = ""; // The name to draw
        protected ItemCategory category; // The category of this item
        protected int yOffset = 0; // The absolute y offset of this item
        protected long quantity = 0;
        
        public ItemElement(ItemData data, String name, ItemCategory category, long quantity, int yOffset) {
            this.data = data;
            this.name = name;
            this.category = category;
            this.quantity = quantity;
            this.yOffset = yOffset;
        }
    }
    
    /** Used to determine if the scroll bar is currently being dragged. */
    private boolean scrollBarIsDragging = false;
    
	private static final int categoryXOffset = 8;
	private static final int categoryYOffset = 25;
	private static final int itemXOffset = 8;
	private static final int itemYOffset = 25;
	private static final int scrollbarWidth = 15;
	
	/** The font that item categories, such as Equipment, should be displayed in. */
	private static final String CATEGORY_FONT = FontID.STATIC_TEXT_LARGE_BOLD.toString();
	
	/** The color that categories, such as Equipment, should be displayed in. */
	private static final Color CATEGORY_COLOR = new Color(222, 222, 222);

	/** The font that items, such as Flimsy Broadsword, should be displayed in. */
	private static final String ITEM_FONT = FontID.STATIC_TEXT_MEDIUM.toString();
	
	/** The color that items, such as Flimsy Broadsword, should be displayed in. */
	private static final Color ITEM_COLOR = new Color(222, 222, 222);
	
	/** 
	 * A priority queue of all elements that are to be drawn to the screen.
	 * These elements are items and item categories and contain an offset from the
	 * top.  All items in this queue are ordered by their y offset.
	 */
	private PriorityQueue<ItemElement> itemElements = new PriorityQueue<ItemElement>(10, new ItemElementComparator());
	
	/** The index to start rendering for itemElements. As this value increases,
	 * "lower" parts of the inventory are shown.  Think of it as a scroll wheel.
	 * The maximum value is whatever itemElements.size() is, but ideally you should
	 * subtract from this some value.
	 */
	private int startingIndex = 0;
	private int maxIndex = 0;
	
	/** 
	 * A static text to store the string: "104k", where 104k is the
	 * amount of gold that the player has.  This appears in the top right
	 * corner of the inventory frame.
	 */
	private StaticText moneyValueStaticText;
	
	/** The button to close this frame.  Appears in the top-right corner. */
	private Button closeButton;
	
    /**
     * Initializes a LoginFrame with its elements (e.g. Username, Password fields)
     * @param x The x offset for this frame (from the parent GUI element)
     * @param y The y offset for this frame (from the parent GUI element)
     */
    public InventoryFrame(final Frame parent, EventIntf intf, GameContainer container
            , float x, float y, int width, int height) {
        super(parent, intf, container, x, y, width, height);

        this.width  = 280 > container.getWidth() - 50 ? container.getWidth() - 50 : 280;
        this.height = 400 > container.getHeight() - 50 ? container.getHeight() - 50 : 400;
        
        long playerGold = getPlayerGold();
        
        closeButton = new Button(this, new EventIntf() {
            @Override
            public void callback(Event event) {
                switch (event.getId()) {
                case BUTTON:
                    hide();
                    GUIUtility.getInstance().setActiveElement(null);
                }
            }
        }, container, "", this.width - 18, 2, 16, 16, ImageID.BUTTON_CLOSE_NORMAL.toString(),
                ImageID.BUTTON_CLOSE_HIGHLIGHTED.toString(),
                ImageID.BUTTON_CLOSE_SELECTED.toString(), 0);
        
        moneyValueStaticText = new StaticText(this, null, container, 
                "" + playerGold, 145, 1, 100, 14, 
                FontID.STATIC_TEXT_LARGE_BOLD, Item.getQuantityColor(playerGold));
        ToolTip moneyTip = new ToolTip();
        moneyTip.addNode(new StringNode(String.format("%,d", playerGold), 
                Item.getQuantityColor(playerGold), 
                FontHandler.getInstance().getFont(FontID.STATIC_TEXT_MEDIUM.toString()), 
                moneyTip.getMaxWidth()));
        moneyValueStaticText.setToolTip(moneyTip);
        
        elements.add(new StaticText(this, null, container, "Inventory", 2, 1, 
                width - 16 - 2, height - categoryYOffset, FontID.STATIC_TEXT_LARGE_BOLD, 
                CATEGORY_COLOR));
        elements.add(closeButton);
        elements.add(new StaticText(this, null, container, "Gold:", 100, 1, -1, -1,
                FontID.STATIC_TEXT_LARGE_BOLD, new Color(186, 147, 18)));
        elements.add(moneyValueStaticText);
    }
    
    @Override
    public void update(GameContainer container) {
        // Iterate over all GUI controls and inform them of input
        for( GUIElement element : elements ) {
            element.update(container);
        }
        
        long playerGold = getPlayerGold();
        moneyValueStaticText.setLabel("" + Item.getShortQuantityName(playerGold));
        moneyValueStaticText.setColor(Item.getQuantityColor(playerGold));
        ToolTip moneyTip = new ToolTip();
        moneyTip.addNode(new StringNode(String.format("%,d", playerGold), 
                Item.getQuantityColor(playerGold), 
                FontHandler.getInstance().getFont(FontID.STATIC_TEXT_MEDIUM.toString()), 
                moneyTip.getMaxWidth()));
        moneyValueStaticText.setToolTip(moneyTip);
    }

    /**
     * Returns the amount of gold that the player has.  This is a safer
     * alternative to attempting to get it directly from the player.
     * @return playerGold or 0 if the player's gold could not be obtained.
     */
    private long getPlayerGold() {
        long result = 0;
        
        try {
            result = Game.getInstance().getClient().getPlayer().getInventory().getGold();
        } catch (NullPointerException e) {
            // We failed.  Do nothing.
        }
        
        return result;
    }
    
    @Override
    public boolean isAcceptingTab() {
        return false;
    }
    
    /**
     * Updates the item elements.  This needs to be called whenever an item is
     * added or removed.  It does not need to be called when an item's quantity
     * changes.
     */
    public void updateItemElements() {
        int yOffset = 0;
        final Font categoryFont = FontHandler.getInstance().getFont(CATEGORY_FONT);
        final Font itemFont = FontHandler.getInstance().getFont(ITEM_FONT);
        final int categoryHeight = categoryFont.getLineHeight() + 2;
        final int itemHeight = itemFont.getLineHeight() + 2;
        Collection<Item> items = Game.getInstance().getClient().getPlayer().getInventory().getItems().values();
        
        itemElements.clear();
        
        for (ItemType type : ItemType.values()) {
            if (type != ItemType.UNASSIGNED && type != ItemType.EQUIPMENT) {
                itemElements.add(new ItemElement(null, type.toString(), ItemCategory.CATEGORY, 0L, yOffset));
                yOffset += categoryHeight;
                // Now find all items with that type
                for (Item item : items) {
                    ItemData itemData = ItemManager.getItemData(item.getId());
                    if (itemData.getType() == type) {
                        itemElements.add(new ItemElement(itemData, 
                                itemData.getName(),
                                ItemCategory.ITEM, item.getQuantity(), yOffset));
                        yOffset += itemHeight;
                    }
                }
            }
        }
        maxIndex = getBottomIndex();
    }
    
    public void renderBackground(Graphics g) {
		final int offX = getAbsoluteWidth();  // offset from left of screen
		final int offY = getAbsoluteHeight(); // offset from top of screen
		final Font categoryFont = FontHandler.getInstance().getFont(CATEGORY_FONT);
    	
        // Render background
        g.fill(new Rectangle(offX, offY, width, height),
        		new GradientFill(0, 0, new Color(17, 17, 15, 166), 
        				width, height, new Color(57, 57, 55, 166), true));
        
        int underlineOffset = categoryFont.getHeight("Inventory");
        // Left half
        GradientFill gFill = new GradientFill(0, 0, new Color(0, 255, 255, 0),
                width/4, 0, new Color(255, 255, 255, 255), true);
        g.fill(new Rectangle(offX, offY + underlineOffset, width/2, 1),
                gFill);
        // Right half
        g.fill(new Rectangle(offX + width/2, offY + underlineOffset, width - width/2, 1),
                gFill.getInvertedCopy());
        // Render border
        g.setColor(new Color(0, 128, 210));
        g.drawRect(offX, offY, width, height);
        g.setColor(new Color(255, 255, 255));
    }
    
    /**
     * Returns the lowest allowable index at which the lowest items in the itemIndex
     * are drawn.
     * TODO:  Find a more efficient algorithm.  Might need to re-think the priority queue.
     * @return index
     */
    public int getBottomIndex() {
        final Font categoryFont = FontHandler.getInstance().getFont(CATEGORY_FONT);
        final Font itemFont = FontHandler.getInstance().getFont(ITEM_FONT);
        final int categoryHeight = categoryFont.getLineHeight() + 2;
        final int itemHeight = itemFont.getLineHeight() + 2;
        int result = 0;
        int curOffset = 0;
        List<ItemElement> curElements = new ArrayList<ItemElement>();
        
        for (ItemElement element : itemElements) {
            if (element.category == ItemCategory.CATEGORY) {
                while (curOffset + categoryYOffset + categoryHeight > height) {
                    ++result;
                    curOffset -= curElements.remove(0).category == ItemCategory.CATEGORY ? categoryHeight : itemHeight;
                }
                curOffset += categoryHeight;
                curElements.add(element);
            } else if (element.category == ItemCategory.ITEM) {
                while (curOffset + itemYOffset + itemHeight > height) {
                    ++result;
                    curOffset -= curElements.remove(0).category == ItemCategory.CATEGORY ? categoryHeight : itemHeight;
                }
                curOffset += itemHeight;
                curElements.add(element);
            }
        }
        
        return result;
    }
    
    /**
     * Renders the item's background if it's being hovered over.  Returns whether the item's background
     * was rendered.
     * @param g
     * @param input
     * @param element The element to check
     * @param yOffset The offset from the top of the inventory
     * @return true if this item's background was rendered.
     */
    public boolean renderItemBackground(Graphics g, Input input, ItemElement element, int yOffset) {
        final int offX = getAbsoluteWidth();  // offset from left of screen
        final int offY = getAbsoluteHeight(); // offset from top of screen
        final Font itemFont = FontHandler.getInstance().getFont(ITEM_FONT);
        final int itemHeight = itemFont.getLineHeight() + 2;
        
        // If this element should have its tooltip rendered
        if (input.getMouseX() >= offX && input.getMouseX() <= offX + width - scrollbarWidth &&
                input.getMouseY() >= offY + itemYOffset + yOffset && input.getMouseY() < offY + itemYOffset + yOffset + itemHeight) {
            // Render the backdrop for the item
            g.fill(new Rectangle(offX, itemYOffset + yOffset + offY, 
                    width - scrollbarWidth, itemHeight),
                    new GradientFill(0, 0, new Color(0, 154, 200, 180),
                            (width - scrollbarWidth)/2, 0, new Color(0, 154, 200, 0), true));
            return true;
        }
        
        return false;
    }
    
    public void renderInventory(Inventory inventory, Graphics g) {
        final Font categoryFont = FontHandler.getInstance().getFont(CATEGORY_FONT);
        final Font itemFont = FontHandler.getInstance().getFont(ITEM_FONT);
        final int categoryHeight = categoryFont.getLineHeight() + 2;
        final int itemHeight = itemFont.getLineHeight() + 2;
        final int offX = getAbsoluteWidth();  // offset from left of screen
        final int offY = getAbsoluteHeight(); // offset from top of screen
        Input input = Game.getInstance().getContainer().getInput();
        
        updateItemElements(); // inefficient.  Should only be called when it needs to refresh
        
        startingIndex = startingIndex > maxIndex ? maxIndex : startingIndex;
        startingIndex = startingIndex < 0 ? 0 : startingIndex;
        
        int curIndex = 0;
        int curOffset = 0; // Current y offset
        ItemElement elementForTooltip = null; // This is what the tooltip should be rendered on
        for (ItemElement element : itemElements) {
            if (curOffset > height - categoryYOffset) { // Don't render pointless stuff
                break;
            }
            if (curIndex >= startingIndex) {
                // Render it!
                if (element.category == ItemCategory.CATEGORY) {
                    if (curOffset + categoryYOffset + categoryHeight > height) { // Not enough space
                        break;
                    }
                    FontHandler.getInstance().draw(CATEGORY_FONT, element.name, categoryXOffset + offX, 
                            categoryYOffset + curOffset + offY, CATEGORY_COLOR, width - categoryXOffset, 
                            height - curOffset - categoryYOffset, false);
                    int categoryWidth = categoryFont.getWidth(element.name);
                    // Render the bar to the right of a category name
                    g.fill(new Rectangle(categoryXOffset + categoryWidth + offX + 4, categoryYOffset + curOffset + offY + categoryHeight / 2, 
                            width - (categoryXOffset + categoryWidth + 4) - 4 - scrollbarWidth, 4),
                            new GradientFill(0, 0, new Color(222, 222, 240),
                                    (width - (categoryXOffset + categoryWidth + 4) - 4 - scrollbarWidth)/2, 0, new Color(0, 147, 255, 50), true));
                    curOffset += categoryHeight;
                } else if (element.category == ItemCategory.ITEM) {
                    if (curOffset + itemYOffset + itemHeight > height) { // Not enough space
                        break;
                    }
                    Color itemColor = element.data.getQuality().getColor();
                    if (renderItemBackground(g, input, element, curOffset)) {
                        elementForTooltip = element;
                    }
                    FontHandler.getInstance().draw(ITEM_FONT, element.name, itemXOffset + offX, 
                            itemYOffset + curOffset + offY, itemColor, width - itemXOffset, 
                            height - curOffset - itemYOffset, false);
                    int itemWidth = itemFont.getWidth(element.name);
                    FontHandler.getInstance().draw(ITEM_FONT, " x ", itemXOffset + offX + itemWidth, 
                            itemYOffset + curOffset + offY, ITEM_COLOR, width - itemXOffset - itemWidth, 
                            height - curOffset - itemYOffset, false);
                    itemWidth += itemFont.getWidth(" x ");
                    FontHandler.getInstance().draw(ITEM_FONT, Item.getShortQuantityName(element.quantity), itemXOffset + offX + itemWidth, 
                            itemYOffset + curOffset + offY, Item.getQuantityColor(element.quantity), width - itemXOffset - itemWidth, 
                            height - curOffset - itemYOffset, false);
                    curOffset += itemHeight;
                }
            }
            ++curIndex;
        }
        
        /*
         *  At this point, curIndex is the LAST item displayed.  startingIndex is 
         *  the FIRST item displayed.  With this information, we can determine
         *  the length of the scroll bar to display and the location of it.
         */
        if ((startingIndex == 0 && curIndex == itemElements.size()) || itemElements.size() == 0 || maxIndex == 0) { // No scrollbar needed
            return;
        }
        int scrollBarLength = (int)((1.0 * (curIndex - startingIndex) / 1.0 / itemElements.size()) * (height - categoryYOffset));
        scrollBarLength = scrollBarLength < 10 ? 10 : scrollBarLength; // minimum length
        int scrollYOffset = (int)(1.0 * startingIndex / maxIndex * (height - categoryYOffset - scrollBarLength));
        scrollYOffset = (scrollYOffset + categoryYOffset + scrollBarLength) > height ? (height - categoryYOffset - scrollBarLength) : scrollYOffset;
        GradientFill scrollFill = new GradientFill(0, 0, new Color(0, 128, 210),
                0, scrollBarLength/4, new Color(244, 244, 244), true);
        g.fill(new Rectangle(width - 5 + offX, categoryYOffset + scrollYOffset + offY, 
                4, scrollBarLength/2), scrollFill);
        g.fill(new Rectangle(width - 5 + offX, categoryYOffset + scrollYOffset + offY + scrollBarLength/2, 
                4, scrollBarLength-scrollBarLength/2), scrollFill.getInvertedCopy());
        g.setColor(new Color(0, 78, 100));
        g.drawRect(width - 5 + offX, categoryYOffset + scrollYOffset + offY, 4, scrollBarLength);
        g.setColor(new Color(255, 255, 255, 255));

        // Render the tooltip if we need to render one
        if (elementForTooltip != null) {
            ItemToolTip.render(g, input, elementForTooltip.data, elementForTooltip.quantity);
        }
    }
    
    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
        
    	if (!hidden) {
        	try {
        		renderBackground(g);
                if (elements != null) {
                    // Iterate over all GUI controls and inform them of input
                    for( GUIElement element : elements ) {
                        element.render(container, game, g);
                    }
                }
        		Inventory inventory = Game.getInstance().getClient().getPlayer().getInventory();
        		renderInventory(inventory, g);
        	} catch (Exception e) {
    			e.printStackTrace();
    		}
        	
	        if (elements != null) {
	            for( GUIElement element : elements ) {
	                element.renderToolTip(container, game, g);
	            }
	        }
    	}
    }

    
    @Override
    public void renderImage(Graphics image) {

    }
    
	@Override
	public boolean isAcceptingFocus() {
		return false;
	}
	
	/**
	 * Updates the scroll bar position by getting the index of where y should be.
	 * This is used for determining index when the mouse is pressed or dragged on
	 * the scroll bar.
	 * @param y The absolute y value to update to.
	 */
	public void updateScrollBarPosition(int y) {
		// Guard
		if (this.height - categoryYOffset <= 0) {
			return;
		}
		
		// "Wiggle room" 
		final float BORDER = 1f;
		
		// We need to see if our scroll bar was pressed
		if ((y >= this.y + categoryYOffset &&
			y <= this.y + this.height) || scrollBarIsDragging) {
			// Get the percentage from 0 to 1 of the index
			float offset = this.y + this.height - y;
			// Give the user some "wiggle room" for going to the top / bottom
			if (offset < BORDER) {
				offset = 0;
			} else if (offset > this.height - categoryYOffset - BORDER &&
			        offset < this.height - categoryYOffset) {
				offset = this.height - categoryYOffset;
			}
			
			double index = 1.0 * offset / (this.height - categoryYOffset - BORDER*2);
			startingIndex = maxIndex - (int)(maxIndex * index);
			startingIndex = startingIndex > maxIndex ? maxIndex : startingIndex;
			startingIndex = startingIndex < 0 ? 0 : startingIndex;
		}
	}
	
	@Override
	public void mousePressed(int button, int x, int y) {
		// Guard
		if (this.height - categoryYOffset <= 0) {
			return;
		}
		
		// We need to see if our scroll bar was pressed
		if (x >= this.x + this.width - scrollbarWidth &&
			x <= this.x + this.width &&
			y >= this.y + categoryYOffset &&
			y <= this.y + this.height) {
			updateScrollBarPosition(y);
			scrollBarIsDragging = true;
		} else {
			scrollBarIsDragging = false;
		}
	}
	
	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
		if (scrollBarIsDragging) {
			updateScrollBarPosition(newy);
		}
	}
	
	@Override
	public void mouseReleased(int button, int x, int y) {
		scrollBarIsDragging = false;
	}
	
	@Override
    public void mouseWheelMoved(int change) {
	    Input input = Game.getInstance().getContainer().getInput();
	    if (input != null && input.getMouseX() >= x && input.getMouseX() <= x+width &&
	            input.getMouseY() >= y && input.getMouseY() <= y+height) {
            startingIndex += change > 0 ? -1 : 1; // This is not a mistake.
            startingIndex = startingIndex < 0 ? 0 : startingIndex;
            startingIndex = startingIndex > maxIndex ? maxIndex : startingIndex;
	    }
    }
}
