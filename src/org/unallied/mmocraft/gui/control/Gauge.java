package org.unallied.mmocraft.gui.control;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Rectangle;
import org.unallied.mmocraft.client.FontHandler;
import org.unallied.mmocraft.client.FontID;
import org.unallied.mmocraft.gui.GUIElement;
import org.unallied.mmocraft.gui.fills.BarFill;

/**
 * A gauge is a horizontal bar which shows a quantity (such as player health).
 * @author Alexandria
 *
 */
public class Gauge extends Control {

    /** The color to render the gauge in. */
    protected Color color;
    
    private static final Color TEXT_COLOR = new Color(238, 238, 238);
    private static final Color BACKGROUND_COLOR = new Color(0, 0, 0, 100);
    
    /** The maximum value (or range) of the gauge. */
    protected int range = 0;
    
    /** The current value of the gauge.  Minimum value is 0, maximum is range. */
    protected int value = 0;
    
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
     */
    public Gauge(GUIElement parent, GameContainer container, int range,
            float x, float y, int width, int height, Color color) {
        super(parent, null, container, x, y, width, height, null, null, null);
        this.color = color;
        if (this.color == null) {
            this.color = new Color(0, 217, 43); // Default to a green color
        }
        this.range = range > 0 ? range : 0;
    }
    
    @Override
    public boolean isAcceptingInput() {
        return false; // Gauges accept no user input
    }

    private String getLabel() {
        return value + "/" + range;
    }
    
    @Override
    public void renderImage(Image image) {
        // Guard
        if (image == null) {
            return;
        }
        
        try {
            Graphics g = image.getGraphics();
            // Background
            g.fill(new Rectangle(0, 0, width, height), new GradientFill(0, 0, BACKGROUND_COLOR, width / 2, height / 2, BACKGROUND_COLOR));
            // Gauge
            g.fill(new Rectangle(0, 0, 1f * width * (1f * value / range), height / 2), new BarFill(color));
            g.fill(new Rectangle(0, height / 2, 1f * width * (1f * value / range), height / 2), new BarFill(color).getInvertedCopy());
            // Text
            if (fontID != null) {
                int maxHeight = FontHandler.getInstance().getMaxHeight(fontID.toString(), getLabel());
                int maxWidth  = FontHandler.getInstance().getMaxWidth(fontID.toString(), getLabel());
                maxHeight = maxHeight > height ? height : maxHeight;
                maxWidth = maxWidth > width ? width : maxWidth;
                FontHandler.getInstance().draw(fontID.toString(), getLabel(), (width - maxWidth) / 2, (height - maxHeight) / 2, TEXT_COLOR, maxWidth, maxHeight, false);
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
    public int getRange() {
        return range;
    }
    
    /**
     * Returns the current value of the gauge.
     * @return value
     */
    public int getValue() {
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
    public void setValue(int value) {
        int newValue = value;
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
