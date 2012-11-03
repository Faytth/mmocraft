package org.unallied.mmocraft.gui.fills;

import org.newdawn.slick.Color;
import org.newdawn.slick.ShapeFill;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

/**
 * A fill effect used to define a bar-like shape when filling and drawing
 * shapes.  A bar-like shape is defined by a color and shape.
 * 
 * NOTE:  For proper resolution, you should place two BarFills, one for the top
 * and one for the bottom.
 * @author Alexandria
 *
 */
public class BarFill implements ShapeFill {
   
    private static final Color DEFAULT_COLOR = new Color(0, 200, 40);
    
    /** The primary color to render the bar fill in. */
    private Color color;
    
    /** Whether to invert the bar fill. */
    private boolean invert = false;
    
    public BarFill(Color color) {
        this(color, false);
    }
    
    public BarFill(Color color, boolean invert) {
        // Set color to a default value if null is passed in.
        this.color = color == null ? DEFAULT_COLOR : color;
        this.invert = invert;
    }
    
    public BarFill getInvertedCopy() {
        return new BarFill(color, !invert);
    }
    
    @Override
    public Color colorAt(Shape shape, float x, float y) {
        // Guard
        if (shape == null) {
            return color;
        }
        float height = shape.getHeight();
        
        if (!invert) {
            return color.darker(0.5f * (height - y) / height);
        } else {
            // height should be half the max of y at this point.
            return color.darker(0.5f * (y - height) / height);
        }
    }

    @Override
    public Vector2f getOffsetAt(Shape shape, float x, float y) {
        return new Vector2f(0, 0);
    }
}
