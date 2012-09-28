package org.unallied.mmocraft.gui.tooltips;

import java.util.Collection;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Rectangle;
import org.unallied.mmocraft.client.FontHandler;
import org.unallied.mmocraft.client.FontID;
import org.unallied.mmocraft.items.ItemData;
import org.unallied.mmocraft.items.ItemEffect;
import org.unallied.mmocraft.items.ItemStat;


public class ItemToolTip extends ToolTip {

	/** The font that the item name should be displayed in. */
	private static final String NAME_FONT = FontID.STATIC_TEXT_MEDIUM_LARGE.toString();
	
	/** The font that the item details, like +Strength, are displayed in. */
	private static final String DETAIL_FONT = FontID.STATIC_TEXT_MEDIUM.toString();
	
	/** The color that item stats, like +Strength, +Magic Find, etc. is displayed in. */
	private static final Color STAT_COLOR = new Color(222, 222, 222);
	
	/** The color that item special effects is displayed in. */
	private static final Color EFFECT_COLOR = new Color(0, 240, 70);
	
	/** The color that item flavor text (the description) is displayed in. */
	private static final Color FLAVOR_COLOR = new Color(0, 70, 240);
	
	/** The color that requirements that are not meant are displayed in. */
	private static final Color REQUIREMENT_COLOR = new Color(240, 30, 30);
	
	/** 
	 * The default width of an item tool tip.  The tool tip will be at least
	 * this wide.
	 */
	private static final int DEFAULT_TOOL_WIDTH = 150;

	/**
	 * The default height of an item tool tip.  The tool tip will be at least
	 * this tall.
	 */
	private static final int DEFAULT_TOOL_HEIGHT = 150;
	
    public ItemToolTip(String tip) {
        super(tip);
        // TODO Auto-generated constructor stub
    }
    
    /**
     * An internal class used to store all elements of a tooltip's text.
     * @author Alexandria
     *
     */
    protected class ToolTipNode {
        /** The color to render the text in. */
        private Color color;
        /** The font that this node will be rendered in. */
        private Font font;
        /** The text to render for this node. */
        private String text;
        /** The next tooltip node that should be rendered on the same line as this node. */
        private ToolTipNode next = null;
        /** A border in pixels around the sides of the tooltip and between lines. */
        private static final int BORDER = 2;
        
        public ToolTipNode(String text, Font font, Color color) {
            this(text, font, color, null);
        }
        
        public ToolTipNode(String text, Font font, Color color, ToolTipNode next) {
            this.text = text;
            this.font = font;
            this.color = color;
            this.next = next;
        }
    }
    
    public static int getMaxWidth(Collection<ToolTipNode> nodes) {
        int result = DEFAULT_TOOL_WIDTH;
        
        for (ToolTipNode node : nodes) {
            int curWidth = 0;
            while (node != null && node.font != null && node.color != null) {
                curWidth += node.font.getWidth(node.text);
                node = node.next;
            }
            result = curWidth > result ? curWidth : result;
        }
        
        return result;
    }
    
    /**
     * Splits a tooltip node up into multiple nodes if its width exceeds the provided width.
     * @param node The node to be modified if necessary.  The way {@link #splitNode(ToolTipNode)}
     *             splits up nodes is by making use of the next in ToolTipNode.
     */
    public static void splitNode(ToolTipNode node) {
        
    }
    
    public static void render(Graphics g, Input input, ItemData item, long quantity) {
    	// Guard
    	if (g == null || input == null || item == null) {
    		return;
    	}
        final Font categoryFont = FontHandler.getInstance().getFont(NAME_FONT);
        final Font itemFont = FontHandler.getInstance().getFont(DETAIL_FONT);
        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();
        
        int toolWidth = DEFAULT_TOOL_WIDTH;
        int toolHeight = DEFAULT_TOOL_HEIGHT;
        
        // Expand the width of the tooltip if anything is too long
        if (categoryFont.getWidth(item.getName()) + 10 > toolWidth) {
        	toolWidth = categoryFont.getWidth(item.getName()) + 10;
        }
        
        g.fill(new Rectangle(mouseX - 5 - toolWidth, mouseY - 5 - toolHeight, toolWidth, toolHeight), 
        		new GradientFill(0, 0, new Color(0, 0, 0, 210),
                toolWidth, toolHeight/2, new Color(50, 100, 100, 210), true));
        
        int curY = mouseY - 5 - toolHeight + 2;
        int curX = mouseX - 5 - toolWidth + 2;
        // Draw item name
        curY = FontHandler.getInstance().draw(NAME_FONT, item.getName(), curX, curY, item.getQuality().getColor(), toolWidth, toolHeight, false) + 2;
        // Draw item type
        curY = FontHandler.getInstance().draw(DETAIL_FONT, item.getType().toString(), curX, curY, STAT_COLOR, toolWidth, toolHeight, false) + 2;
        // Draw item weapon damage if applicable
        // TODO:  Add this
        
        // Draw item stats except for weapon damage
        for (ItemStat stat : item.getStats()) {
        	curY = FontHandler.getInstance().draw(DETAIL_FONT, stat.toString(), curX, curY, STAT_COLOR, toolWidth, toolHeight, false) + 2;
        }
        // Draw item effects
        for (ItemEffect effect : item.getEffects()) {
        	curY = FontHandler.getInstance().draw(DETAIL_FONT, effect.toString(), curX, curY, STAT_COLOR, toolWidth, toolHeight, false) + 2;
        }
        // Draw item requirements
        // Draw item flavor text
        curY = FontHandler.getInstance().draw(DETAIL_FONT,  item.getDescription(), curX, curY, FLAVOR_COLOR, toolWidth, toolHeight, false) + 2;
        // Draw item quantity
        curY = mouseY - 5 - 4 - itemFont.getLineHeight()*2;
        curY = FontHandler.getInstance().draw(DETAIL_FONT,  "Quantity: " + quantity, curX, curY, STAT_COLOR, toolWidth, toolHeight, false) + 2;
        // Draw item sell price
    }
}
