package org.unallied.mmocraft.gui.frame;

import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.unallied.mmocraft.client.FontID;
import org.unallied.mmocraft.client.Game;
import org.unallied.mmocraft.constants.StringConstants;
import org.unallied.mmocraft.gui.control.Button;
import org.unallied.mmocraft.gui.control.StaticText;
import org.unallied.mmocraft.net.PacketCreator;

public class ReviveFrame extends Frame {

    /** Used in determining which message to show. */
    private Random random = new Random();
    
    /** The button that revives the player. */
    private Button reviveButton;
    
    /** The text that is displayed above the button. */
    private StaticText reviveStaticText;
    
    /**
     * Initializes a revive frame with its elements (e.g. a button with "Revive" on it
     * and some text telling the player that they've died).
     * @param parent
     * @param intf
     * @param container
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public ReviveFrame(Frame parent, EventIntf intf, GameContainer container,
            float x, float y, int width, int height) {
        super(parent, intf, container, x, y, width, height);
        this.width  = 300 > container.getWidth()  - 20 ? container.getWidth()  - 20 : 300;
        this.height = 67 > (int)container.getHeight() - 5 ? (int)container.getHeight() - 5 : 67;
        
        reviveButton = new Button(this, new EventIntf() {
            @Override
            public void callback(Event event) {
                switch (event.getId()) {
                case BUTTON:
                    Game.getInstance().getClient().announce(PacketCreator.getRevive());
                }
            }
        }, container, StringConstants.REVIVE, 2, this.height - 23, this.width - 2, 18, null, null, null, 0);
        
        reviveStaticText = new StaticText(this, null, container, "", 3, 3, this.width - 6, this.height - 23,
                FontID.STATIC_TEXT_MEDIUM);
        refreshReviveMessage();
        
        elements.add(reviveStaticText);
        elements.add(reviveButton);
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

    private void refreshReviveMessage() {
        int index = random.nextInt();
        index = index < 0 ? -index : index;
        index %= StringConstants.REVIVE_MESSAGE.length;
        reviveStaticText.setLabel(StringConstants.REVIVE_MESSAGE[index]);
    }
    
    @Override
    public void show(boolean show) {
        if (show && hidden == show) {
            refreshReviveMessage();
        }
        hidden = !show;
    }
}
