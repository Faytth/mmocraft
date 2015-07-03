package org.unallied.mmocraft.gui.frame;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Rectangle;

public class Panel extends Frame {

    /** The background image, if any, that should be displayed. */
    private Image background = null;
    
    /** 
     * The stretch of the background image.  If true, the background image will
     * stretch to encompass the entire panel.
     */
    private boolean stretch = false;
    
    public Panel(Frame parent, EventIntf intf, GameContainer container,
            float x, float y, int width, int height) {
        super(parent, intf, container, x, y, width, height);
        
        this.width  = width;
        this.height = height;
    }

    /**
     * Sets the background image to render behind the other controls of this panel.
     * This image will be centered on the panel.  If the panel is too small to
     * contain the image, the panel will have its width and height adjusted to the
     * size of the image.  A value of null will remove the background image.
     * 
     * @param background The background image to use
     */
    public void setBackground(Image background) {
        this.background = background;
        if (this.background != null) {
            this.width = this.background.getWidth() > this.width ? this.background.getWidth() : this.width;
            this.height = this.background.getHeight() > this.height ? this.background.getHeight() : this.height;
        }
    }
    
    /**
     * Sets the stretch of the image.  If this is set to true, the background
     * image will stretch to fill the entire panel.
     * @param stretch
     */
    public void setStretch(boolean stretch) {
        this.stretch = stretch;
    }
    
    @Override
    public boolean isAcceptingTab() {
        return false;
    }

    public void renderBackground(Graphics g) {
        
        // Render background
        g.fill(new Rectangle(0, 0, width, height),
                new GradientFill(0, 0, new Color(17, 17, 15, 166), 
                        width, height, new Color(57, 57, 55, 166), true));
        
        // Render border
        g.setColor(new Color(0, 128, 210));
        g.drawRect(0, 0, width, height);
        g.setColor(new Color(255, 255, 255));
        
        if (background != null) {
            g.drawImage(background, 0, 0, this.width, this.height, 0, 0, background.getWidth(), background.getHeight());
        }
    }
    
    @Override
    public void renderImage(Graphics g) {
        renderBackground(g);
    }
    
    @Override
    public boolean isAcceptingFocus() {
        return false;
    }
    
}