package org.unallied.mmocraft.gui.control;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Rectangle;
import org.unallied.mmocraft.client.FontHandler;
import org.unallied.mmocraft.client.FontID;
import org.unallied.mmocraft.client.Game;
import org.unallied.mmocraft.gui.GUIElement;
import org.unallied.mmocraft.gui.fills.BarFill;
import org.unallied.mmocraft.items.Item;

/**
 * A gauge is a horizontal bar which shows a quantity (such as player health).
 * @author Alexandria
 *
 */
public class Gauge extends Control {

    /** The gauge appears normally. */
    public static final int NORMAL = 0;
    /** The gauge % will be shown to the right of the gauge. */
    public static final int SHOW_PERCENT = 1 << 0;
    
    /** The color to render the gauge in. */
    protected Color color;
    
    private static final Color TEXT_COLOR = new Color(238, 238, 238);
    private static final Color BACKGROUND_COLOR = new Color(0, 0, 0, 100);
    
    /** The style of the gauge. Changing the style changes how the gauge is rendered. */
    private int style = 0;
    
    /** The maximum value (or range) of the gauge. */
    protected long range = 0;
    
    /** The current value of the gauge.  Minimum value is 0, maximum is range. */
    protected long value = 0;
    
    /** The font to use for rendering the value / range for all gauges. */
    public static final FontID fontID = FontID.STATIC_TEXT_SMALL_BOLD;
    
    /**
     * 
     * @param parent Window parent.
     * @param container The GameContainer that's passed from a state's init().
     * @param range Integer range (maximum value) of the gauge.
     * @param x Relative x position of the window.
     * @param y Relative y position of the window.
     * @param width The width of the control.
     * @param height The height of the control.
     * @param color The color to show the gauge in.
     * @param style The style of the gauge.  0 for default.
     */
    public Gauge(GUIElement parent, GameContainer container, long range,
            float x, float y, int width, int height, Color color, int style) {
        super(parent, null, container, x, y, width, height, null, null, null);
        this.color = color;
        if (this.color == null) {
            this.color = new Color(0, 217, 43); // Default to a green color
        }
        this.range = range > 0 ? range : 0;
        this.style = style;
    }
    
    @Override
    public boolean isAcceptingInput() {
        return false; // Gauges accept no user input
    }

    private String getLabel() {
        return Item.getShortQuantityName(value) + "/" + Item.getShortQuantityName(range);
    }
    
    @Override
    public void renderImage(Graphics g) {
        // Guard
        if (g == null) {
            return;
        }
        
        try {
            Color renderColor = color;
            // If moused over, we want to "lighten" the gauge color.
            Input input = Game.getInstance().getContainer().getInput();
            
            int mouseX = input.getMouseX();
            int mouseY = input.getMouseY();
            if (containsPoint(mouseX, mouseY)) {
                renderColor = renderColor.brighter();
            }
            
            // The width in pixels that the % value will take up
            int textBorder = 4;
            int textWidth = (style & SHOW_PERCENT) > 0 ? 
                    FontHandler.getInstance().getMaxWidth(fontID.toString(), "100%") + textBorder : 0;
            
            // Background
            g.fill(new Rectangle(0, 0, (width - textWidth), height), new GradientFill(0, 0, BACKGROUND_COLOR, 
                    (width - textWidth) / 2, height / 2, BACKGROUND_COLOR));
            
            // Gauge
            g.fill(new Rectangle(0, 0, 1f * (width - textWidth) * (1f * value / range), height / 2), 
                    new BarFill(renderColor));
            g.fill(new Rectangle(0, height / 2, 1f * (width - textWidth) * (1f * value / range), height / 2), 
                    new BarFill(renderColor).getInvertedCopy());
            
            // Text
            if (fontID != null) {
                // Render text inside of gauge
                int maxHeight = FontHandler.getInstance().getMaxHeight(fontID.toString(), getLabel());
                int maxWidth  = FontHandler.getInstance().getMaxWidth(fontID.toString(), getLabel());
                maxHeight = maxHeight > height ? height : maxHeight;
                maxWidth = maxWidth > (width - textWidth) ? (width - textWidth) : maxWidth;
                FontHandler.getInstance().draw(fontID.toString(), getLabel(), 
                        ((width - textWidth) - maxWidth) / 2, (height - maxHeight) / 2, 
                        TEXT_COLOR, maxWidth, maxHeight, false);
                
                // Render % if needed
                if ((style & SHOW_PERCENT) > 0) {
                    double valuePercent = 100.0 * value / range;
                    if (valuePercent > 0 && valuePercent < 100) {
                        // Ensure that only 0 and 100 are represented as 0% / 100%
                        valuePercent = valuePercent < 0.1 ? 0.1 : valuePercent;
                        valuePercent = valuePercent > 99.9 ? 99.9 : valuePercent;
                        // Update it
                        FontHandler.getInstance().draw(fontID.toString(), String.format("%.1f%%", valuePercent),
                                width - textWidth + textBorder, (height - maxHeight) / 2, TEXT_COLOR, textWidth, maxHeight, false);
                    } else { // Either 0% or 100%
                        FontHandler.getInstance().draw(fontID.toString(), String.format("%d%%", (int)valuePercent),
                                width - textWidth + textBorder, (height - maxHeight) / 2, TEXT_COLOR, textWidth, maxHeight, false);
                    }
                }
            }
            g.flush();
        } catch (Throwable t) {
            
        }
    }

    @Override
    public boolean isAcceptingTab() {
        return false;
    }
    
    /**
     * Returns the maximum value of the gauge.
     * @return range
     */
    public long getRange() {
        return range;
    }
    
    /**
     * Returns the current value of the gauge.
     * @return value
     */
    public long getValue() {
        return value;
    }
    
    /**
     * Sets the range (maximum value) of the gauge.  If the new range is
     * negative, then it is set to 0.  If the value is greater than the
     * range after updating range, then the value will be set to the range.
     * 
     * Tells the gauge to refresh if range changed.
     * @param range The new range.  Negative values will result in a range of 0.
     */
    public void setRange(int range) {
        int newRange = range;
        newRange = range > 0 ? range : 0;
        this.value = this.value > newRange ? newRange : this.value;
        needsRefresh |= this.range != newRange;
        this.range = newRange;
    }
    
    /**
     * Sets the value of the gauge.  If the new value is negative, then it is
     * set to 0.  If the value exceeds the range, then the value will be set 
     * to the range.
     * 
     * Tells the gauge to refresh if value changed.
     * @param value The new value.  Negative values will result in a value of 0.
     */
    public void setValue(long value) {
        long newValue = value;
        newValue = value > 0 ? value : 0;
        newValue = newValue > range ? range : newValue;
        needsRefresh |= this.value != newValue;
        this.value = newValue;
    }

    @Override
    public boolean isAcceptingFocus() {
        return false;
    }
}
