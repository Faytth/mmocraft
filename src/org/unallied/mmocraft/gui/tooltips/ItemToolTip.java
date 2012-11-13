package org.unallied.mmocraft.gui.tooltips;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.unallied.mmocraft.client.FontHandler;
import org.unallied.mmocraft.client.FontID;
import org.unallied.mmocraft.client.Game;
import org.unallied.mmocraft.gui.node.StringNode;
import org.unallied.mmocraft.items.Item;
import org.unallied.mmocraft.items.ItemData;
import org.unallied.mmocraft.items.ItemEffect;
import org.unallied.mmocraft.items.ItemRequirement;
import org.unallied.mmocraft.items.ItemStat;


public class ItemToolTip extends ToolTip {

	/** The font that the item name should be displayed in. */
	private static final String NAME_FONT = FontID.STATIC_TEXT_MEDIUM_LARGE.toString();
	
	/** The font that the item details, like +Strength, are displayed in. */
	private static final String DETAIL_FONT = FontID.STATIC_TEXT_MEDIUM.toString();
	
	/** The color that item stats, like +Strength, +Magic Find, etc. is displayed in. */
	private static final Color STAT_COLOR = new Color(222, 222, 222);
	
	/** The color that item special effects are displayed in. */
	private static final Color EFFECT_COLOR = new Color(0, 240, 70);
	
	/** The color that item flavor text (the description) is displayed in. */
	private static final Color FLAVOR_COLOR = new Color(0, 185, 192);
	
	/** The color that requirements that are not meant are displayed in. */
	private static final Color REQUIREMENT_COLOR = new Color(240, 30, 30);

	private ItemToolTip() {
		super();
	}
	
    private ItemToolTip(String tip) {
        super(tip);
    }
    
    public static void render(Graphics g, Input input, ItemData item, long quantity) {
    	// Guard
    	if (g == null || input == null || item == null) {
    		return;
    	}
        final Font nameFont = FontHandler.getInstance().getFont(NAME_FONT);
        final Font detailFont = FontHandler.getInstance().getFont(DETAIL_FONT);

        ItemToolTip tip = new ItemToolTip();
        
        // Add item name
        tip.addNode(new StringNode(null, null, item.getName(), item.getQuality().getColor(), nameFont, tip.maxWidth));
        
        // Add item type
        tip.addNode(new StringNode(null, null, item.getType().toString(), STAT_COLOR, detailFont, tip.maxWidth));
        
        // Add item stats
        for (ItemStat stat : item.getStats()) {
        	tip.addNode(new StringNode(null, null, stat.toString(), STAT_COLOR, detailFont, tip.maxWidth));
        }
        
        // Add item effects
        for (ItemEffect effect : item.getEffects()) {
        	tip.addNode(new StringNode(null, null, effect.toString(), EFFECT_COLOR, detailFont, tip.maxWidth));
        }
        
        // Add item requirements
        for (ItemRequirement requirement : item.getRequirements()) {
        	tip.addNode(new StringNode(null, null, requirement.toString(), 
        			Game.getInstance().getClient().getPlayer().meetsRequirement(requirement) ? STAT_COLOR : REQUIREMENT_COLOR, 
        					detailFont, tip.maxWidth));
        }
        
        // Add item flavor text
        tip.addNode(new StringNode(null, null, item.getDescription(), FLAVOR_COLOR, detailFont, tip.maxWidth));
        
        // Add item quantity
        tip.addNodes(new StringNode[]{
        		new StringNode(null, null, "Quantity: ", STAT_COLOR, detailFont, tip.maxWidth),
        		new StringNode(null, null, String.format("%,d", quantity), Item.getQuantityColor(quantity), detailFont, tip.maxWidth)
        		});
        
        // Add item sell price
        tip.addNodes(new StringNode[]{
        		new StringNode(null, null, "Sell: ", STAT_COLOR, detailFont, tip.maxWidth),
        		new StringNode(null, null, String.format("%,d", item.getSellPrice()), Item.getQuantityColor(item.getSellPrice()), detailFont, tip.maxWidth )
        		});
        
        try {
			tip.render(Game.getInstance().getContainer(), Game.getInstance(), g);
		} catch (SlickException e) {
			e.printStackTrace();
		}
    }
}
