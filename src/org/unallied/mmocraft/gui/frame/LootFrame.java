package org.unallied.mmocraft.gui.frame;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;
import org.unallied.mmocraft.client.FontHandler;
import org.unallied.mmocraft.client.FontID;
import org.unallied.mmocraft.gui.node.ItemNode;
import org.unallied.mmocraft.items.Item;
import org.unallied.mmocraft.items.ItemData;
import org.unallied.mmocraft.items.ItemManager;
import org.unallied.mmocraft.net.handlers.SetItemHandler;


public class LootFrame extends Frame {

    /** in milliseconds */
	private static final int ITEM_FADE_LIFE = 3000;
	/** font to use for the item */
	private static final String ITEM_FONT = FontID.STATIC_TEXT_MEDIUM.toString();
	
	/**
	 * represents a item to fade in/out
	 */
	protected class FadingItem {
		public Item item; //the item that was received
		public long creationTime; //when the item was received
		
		public FadingItem(Item item, long creationTime) {
			this.item = item;
			this.creationTime = creationTime;
		}
	}
	
	private List<FadingItem> items = new LinkedList<FadingItem>();
	
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
	    
	    super.update(container);
	}
	
	/**
     * Add an item to the list
     * @param item The item to add
     */
	public void addItem(Item item) {
	    items.add(new FadingItem(item, System.currentTimeMillis()));
	}
	
	/**
	 * Removes all items that have been there long enough
	 */
	public void recalculateItems() {
		while (!items.isEmpty() && System.currentTimeMillis() - items.get(0).creationTime >= ITEM_FADE_LIFE) {
			items.remove(0);
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
		for (int i = items.size() -1, j = 0; j < itemsToDisplay && i >= 0; --i) {
		    Item item = items.get(i).item;
		    ItemData itemData = ItemManager.getItemData(item.getId());
		    if (itemData != null) {
		        ItemNode itemNode = new ItemNode(this, null, item);
		        itemNode.setAlpha(((ITEM_FADE_LIFE - (System.currentTimeMillis() - 
		                items.get(i).creationTime)) / (float)ITEM_FADE_LIFE));
		        itemNode.render(g, width - itemNode.getWidth() - 3, lineY, -1);
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
