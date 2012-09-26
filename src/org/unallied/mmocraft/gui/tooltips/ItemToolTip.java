package org.unallied.mmocraft.gui.tooltips;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Rectangle;
import org.unallied.mmocraft.items.ItemData;


public class ItemToolTip extends ToolTip {

    public ItemToolTip(String tip) {
        super(tip);
        // TODO Auto-generated constructor stub
    }
    
    public static void render(Graphics g, Input input, ItemData item, long quantity) {
        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();
        
        g.fill(new Rectangle(15 + mouseX, 15 + mouseY, 150, 150), new GradientFill(0, 0, new Color(457, 102, 176, 166),
                75, 75, new Color(99, 47, 176, 166), true));
    }

}
