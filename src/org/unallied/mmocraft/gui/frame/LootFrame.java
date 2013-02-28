package org.unallied.mmocraft.gui.frame;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;
import org.unallied.mmocraft.client.FontHandler;
import org.unallied.mmocraft.client.FontID;
import org.unallied.mmocraft.gui.GUIElement;
import org.unallied.mmocraft.gui.node.GoldNode;
import org.unallied.mmocraft.gui.node.ItemNode;
import org.unallied.mmocraft.items.Item;
import org.unallied.mmocraft.items.ItemData;
import org.unallied.mmocraft.items.ItemManager;
import org.unallied.mmocraft.net.handlers.SetGoldHandler;
import org.unallied.mmocraft.net.handlers.SetItemHandler;


public class LootFrame extends Frame {

    /** in milliseconds */
	private static final int ITEM_FADE_LIFE = 3000;
	/** font to use for the item */
	private static final String ITEM_FONT = FontID.STATIC_TEXT_MEDIUM.toString();
	
	protected interface FadingObject {
	    
	    /**
	     * The time (System.currentTimeMillis()) in milliseconds when this object
	     * was created.
	     * @return creationTime
	     */
	    public long getCreationTime();
	    
	    /**
	     * Renders the fading object.
	     * @param parent The parent element that we should render to.
	     * @param g The graphics context used for rendering.
	     * @param yOffset The offset to use when rendering this object.  Positive values
	     * move this farther down, negative values move it farther up (vertically).
	     * @return true if anything was rendered, else false.
	     */
	    public boolean render(GUIElement parent, Graphics g, int yOffset);
	}
	
	/**
	 * represents a item to fade in/out
	 */
	protected class FadingItem implements FadingObject {
		public Item item; // The item that was received
		public long creationTime; // When the item was received
		
		public FadingItem(Item item, long creationTime) {
			this.item = item;
			this.creationTime = creationTime;
		}
		
		public long getCreationTime() {
		    return creationTime;
		}
		
		public boolean render(GUIElement parent, Graphics g, int yOffset) {
		    boolean result = false;
            ItemData itemData = ItemManager.getItemData(item.getId());
            if (itemData != null) {
                ItemNode itemNode = new ItemNode(parent, null, item);
                itemNode.setAlpha(((ITEM_FADE_LIFE - (System.currentTimeMillis() - 
                        getCreationTime())) / (float)ITEM_FADE_LIFE));
                itemNode.render(g, width - itemNode.getWidth() - 3, yOffset, -1);
                result = true;
            }
            
            return result;
		}
	}
	
	protected class FadingGold implements FadingObject {
	    public Long goldQuantity; // The amount of gold that was received
	    public long creationTime; // When the gold was received.
	    
	    public FadingGold(Long goldQuantity, long creationTime) {
	        this.goldQuantity = goldQuantity;
	        this.creationTime = creationTime;
	    }
	    
	    public long getCreationTime() {
	        return creationTime;
	    }
	    
	    public boolean render(GUIElement parent, Graphics g, int yOffset) {
            GoldNode goldNode = new GoldNode(parent, null, goldQuantity);
            goldNode.setAlpha(((ITEM_FADE_LIFE - (System.currentTimeMillis() - 
                    getCreationTime())) / (float)ITEM_FADE_LIFE));
            goldNode.render(g, width - goldNode.getWidth() - 3, yOffset, -1);
	        
	        return true;
	    }
	}
	
	private List<FadingObject> fadingObjects = new LinkedList<FadingObject>();
	
	public LootFrame(Frame parent, EventIntf intf,
			GameContainer container, float x, float y, int width, int height) {
		super(parent, intf, container, x, y, width, height);
	}
	
	@Override
	public void update(GameContainer container) {
	    
	    // Poll the packet handler for new loot to add.
	    Item item = null;
	    while ((item = SetItemHandler.getNextItem()) != null) {
	        addItem(item);
	    }
	    Long gold = null;
	    while ((gold = SetGoldHandler.getNextGold()) != null) {
	        addGold(gold);
	    }
	    
	    super.update(container);
	}
	
	/**
     * Add an item to the list.
     * @param item The item to add.
     */
	public void addItem(Item item) {
	    fadingObjects.add(new FadingItem(item, System.currentTimeMillis()));
	}
	
	/**
	 * Adds an amount of gold to the list.
	 * @param gold The amount of gold to add.
	 */
	public void addGold(Long gold) {
	    fadingObjects.add(new FadingGold(gold, System.currentTimeMillis()));
	}
	
	/**
	 * Removes all items that have been there long enough
	 */
	public void recalculateItems() {
		while (!fadingObjects.isEmpty() && System.currentTimeMillis() - fadingObjects.get(0).getCreationTime() >= ITEM_FADE_LIFE) {
			fadingObjects.remove(0);
		}
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		
		g.translate(getAbsoluteWidth(), getAbsoluteHeight());
		
		//Recalculate which items should be removed
		recalculateItems();
		
		int lineHeight = FontHandler.getInstance().getFont(ITEM_FONT).getLineHeight();
		int lineY = this.height - lineHeight;
		//calculate the maximum number of items that can be shown
		int itemsToDisplay = this.height / lineHeight; 
		
		//display the items
		for (int i = fadingObjects.size() -1, j = 0; j < itemsToDisplay && i >= 0; --i) {
		    if (fadingObjects.get(i).render(this, g, lineY)) {
                ++j;
                lineY -= lineHeight;
		    }
		}
	}
	
	@Override
	public void renderImage(Graphics g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isAcceptingTab() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAcceptingFocus() {
		// TODO Auto-generated method stub
		return false;
	}

}
