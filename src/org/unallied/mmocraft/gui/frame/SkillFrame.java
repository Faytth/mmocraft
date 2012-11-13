package org.unallied.mmocraft.gui.frame;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;
import org.unallied.mmocraft.client.Game;
import org.unallied.mmocraft.gui.GUIElement;
import org.unallied.mmocraft.gui.control.ListCtrl;
import org.unallied.mmocraft.gui.node.SkillNode;
import org.unallied.mmocraft.skills.SkillType;
import org.unallied.mmocraft.skills.Skills;

public class SkillFrame extends Frame {
    /** A list control containing all of the skill nodes. */
    private ListCtrl skillListCtrl;
    
    /**
     * Initializes a LoginFrame with its elements (e.g. Username, Password fields)
     * @param x The x offset for this frame (from the parent GUI element)
     * @param y The y offset for this frame (from the parent GUI element)
     */
    public SkillFrame(final Frame parent, EventIntf intf, GameContainer container
            , float x, float y, int width, int height) {
        super(parent, intf, container, x, y, width, height);

        skillListCtrl = new ListCtrl(this, null, container, 0, 0, width, height);
        
        elements.add(skillListCtrl);
                
        this.width = width;
        this.height = height;
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
        // TODO Auto-generated method stub
        return false;
    }
    
    private void renderSkills(Skills skills, Graphics g) {
        skillListCtrl.clear();
        long[] skillExperience = skills.getSkills();
        for (int i=0; i < skillExperience.length; ++i) {
            skillListCtrl.addNode(new SkillNode(this, null, skills, skillExperience[i], SkillType.fromValue(i), width));
        }
    }
    
    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
        if (!hidden) {
            try {
                Skills skills = Game.getInstance().getClient().getPlayer().getSkills();
                renderSkills(skills, g);
                if (elements != null) {
                    // Iterate over all GUI controls and inform them of input
                    for (GUIElement element : elements) {
                        element.render(container, game, g);
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
    
    @Override
    public void renderImage(Graphics g) {
    }

    @Override
    public boolean isAcceptingFocus() {
        return false;
    }
}
