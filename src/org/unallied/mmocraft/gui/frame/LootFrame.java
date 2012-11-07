package org.unallied.mmocraft.gui.frame;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;
import org.unallied.mmocraft.client.FontHandler;
import org.unallied.mmocraft.client.FontID;
import org.unallied.mmocraft.items.ItemData;


public class LootFrame extends Frame {

	private static final int ITEM_FADE_LIFE = 3000; //in milliseconds
	private static final String ITEM_FONT = FontID.STATIC_TEXT_MEDIUM.toString(); //font to use for the item
	
	//represents a item to fade in/out
	protected class FadingItem {
		public ItemData item; //the item that was received
		public long creationTime; //when the item was received
		
		public FadingItem(ItemData item, long creationTime) {
			this.item = item;
			this.creationTime = creationTime;
		}
	}
	
	private List<FadingItem> items = new LinkedList<FadingItem>();
	
	public LootFrame(Frame parent, EventIntf intf,
			GameContainer container, float x, float y, int width, int height) {
		super(parent, intf, container, x, y, width, height);
	}
	
	/**
	 * Add an item to the list
	 * @param item The item to add
	 */
	public void addItem(ItemData item) {
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
		
		//g.setColor(new Color(0, 128, 210));
		//g.drawRect(getAbsoluteWidth(), getAbsoluteHeight(), width, height);
		g.translate(getAbsoluteWidth(), getAbsoluteHeight());
		
		//Recalculate which items should be removed
		recalculateItems();
		
		int lineHeight = FontHandler.getInstance().getFont(ITEM_FONT).getLineHeight();
		int lineY = this.height - lineHeight;
		//calculate the maximum number of items that can be shown
		int itemsToDisplay = this.height / lineHeight; 
		//start index of the list (show most recent looted items if we cannot fit them all)
		int startIndex = items.size() > itemsToDisplay ? items.size() - itemsToDisplay : 0; 
		
		//display the items
		for (int i = startIndex; i < items.size(); i++, lineY -= lineHeight) {
			Color c = new Color(items.get(i).item.getQuality().getColor());
			c.a *= ((ITEM_FADE_LIFE - (System.currentTimeMillis() - items.get(i).creationTime)) / (double)ITEM_FADE_LIFE);
			
			//be sure to right justify the text
			int stringWidth = FontHandler.getInstance().getMaxWidth(ITEM_FONT, items.get(i).item.getName());
			FontHandler.getInstance().draw(ITEM_FONT, items.get(i).item.getName(), width - stringWidth - 3, lineY, c, width, height, false);
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
