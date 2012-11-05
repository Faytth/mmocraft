package org.unallied.mmocraft.gui.frame;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;
import org.unallied.mmocraft.client.FontHandler;
import org.unallied.mmocraft.client.FontID;
import org.unallied.mmocraft.client.ImageID;
import org.unallied.mmocraft.gui.GUIElement;
import org.unallied.mmocraft.gui.GUIUtility;
import org.unallied.mmocraft.gui.control.Button;
import org.unallied.mmocraft.gui.control.StaticText;

/**
 * The character info frame.  Contains the frames that make up character info,
 * such as an equipment frame, PvP frame, skill frame, and so on.
 * @author Alexandria
 *
 */
public class CharacterFrame extends Frame {

    /** A button that displays the character info frame when clicked. */
    private SkillFrame skillFrame;
    
    /** 
     * The font that the character info categories, such as Equipment / Skills, 
     * should be displayed in.
     **/
    private static final String CATEGORY_FONT = FontID.STATIC_TEXT_LARGE_BOLD.toString();
    
    /** The color that the character info categories, such as Equipment / Skills should be rendered in. */
    private static final Color CATEGORY_COLOR = new Color(222, 222, 222);
    
    private static final int categoryXOffset = 3;
    private static final int categoryYOffset = 25;
    
    /** The button to close this frame.  Appears in the top-right corner. */
    private Button closeButton;
    
    /** The title of this frame. */
    private StaticText titleStaticText;
    
    /**
     * Initializes a LoginFrame with its elements (e.g. Username, Password fields)
     * @param x The x offset for this frame (from the parent GUI element)
     * @param y The lowest y that the character frame can go.
     */
    public CharacterFrame(final Frame parent, EventIntf intf, GameContainer container
            , float x, float y, int width, int height) {
        super(parent, intf, container, x, y, width, height);

        this.width  = 280 > container.getWidth()  - 20 ? container.getWidth()  - 20 : 280;
        this.height = 400 > (int)y - 5 ? (int)y - 5 : 400;
        
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
        
        skillFrame = new SkillFrame(this, null, container, categoryXOffset, categoryYOffset, 
                this.width - categoryXOffset * 2, this.height - categoryYOffset);
        
        titleStaticText = new StaticText(this, null, container, "", 2, 1, 
                width - 16 - 2, height - categoryYOffset, FontID.STATIC_TEXT_LARGE_BOLD, CATEGORY_COLOR);
        titleStaticText.setLabel("Skills");
        
        elements.add(titleStaticText);
        elements.add(closeButton);
        elements.add(skillFrame);
    }
    
    @Override
    public void update(GameContainer container) {

        // Iterate over all GUI controls and inform them of input
        for( GUIElement element : elements ) {
            element.update(container);
        }
    }

    @Override
    public boolean isAcceptingTab() {
        return false;
    }
    
    public void renderBackground(Graphics g) {
        final int offX = getAbsoluteWidth();  // offset from left of screen
        final int offY = getAbsoluteHeight(); // offset from top of screen
        final Font categoryFont = FontHandler.getInstance().getFont(CATEGORY_FONT);
        
        // Render background
        g.fill(new Rectangle(offX, offY, width, height),
                new GradientFill(0, 0, new Color(17, 17, 15, 166), 
                        width, height, new Color(57, 57, 55, 166), true));
        
        int underlineOffset = categoryFont.getHeight("Skills");
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
}
