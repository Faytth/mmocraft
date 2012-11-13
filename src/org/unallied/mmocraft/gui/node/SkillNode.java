package org.unallied.mmocraft.gui.node;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.unallied.mmocraft.client.FontHandler;
import org.unallied.mmocraft.client.FontID;
import org.unallied.mmocraft.gui.GUIElement;
import org.unallied.mmocraft.gui.control.Gauge;
import org.unallied.mmocraft.gui.tooltips.ToolTip;
import org.unallied.mmocraft.skills.SkillType;
import org.unallied.mmocraft.skills.Skills;


/**
 * A node that is used to render skills in the skill frame.
 * @author Alexandria
 *
 */
public class SkillNode extends Node {
   
	/** The color to render the skill name in. */
    private static final Color SKILL_NAME_COLOR = new Color(238, 238, 238);
    
    /** The color to render the experience bar in. */
    private static final Color EXPERIENCE_BAR_COLOR = new Color(144, 0, 175);
    
    /** The font to display the string in. */
    private static final Font SKILL_NAME_FONT = FontHandler.getInstance().getFont(FontID.STATIC_TEXT_MEDIUM_BOLD.toString());
    
    /** Used for details, like the level and current experience. */
    private static final Font DETAILS_FONT = FontHandler.getInstance().getFont(FontID.STATIC_TEXT_MEDIUM_SMALL.toString());
    
    /** The maximum width to use for rendering. */
    private int maxWidth;
    
    /** The actual lines that will be rendered. */
    private long skillExperience;
    
    /** The type of the skill, such as Constitution, Mining, etc. */
    private SkillType skillType;
    
    private Skills skills = null;
    
    protected Gauge experienceBar;
    
    public SkillNode(GUIElement parent, EventIntf intf, Skills skills, long skillExperience, SkillType skillType, int maxWidth) {
    	super(parent, intf);
    	this.skills = skills;
    	this.skillExperience = skillExperience;
    	this.skillType = skillType;
    	this.maxWidth = maxWidth;
    	
    	int experienceY = SKILL_NAME_FONT.getLineHeight();
    	int experienceX = DETAILS_FONT.getWidth("Lv. 9999");
    	
    	experienceBar = new Gauge(this, null, 
    			this.skills.getLevelExperience(this.skillType), experienceX, experienceY, maxWidth - experienceX - 5, 12,
    			EXPERIENCE_BAR_COLOR);
    	experienceBar.setValue(this.skills.getExperience(this.skillType));
    	ToolTip expTooltip = new ToolTip();
    	expTooltip.addNode(new StringNode(null, null, 
				String.format("Total Experience: %,d", 
						this.skills.getTotalExperience(this.skillType)),
				SKILL_NAME_COLOR, DETAILS_FONT, maxWidth));
    	expTooltip.addNode(new StringNode(null, null, 
				String.format("Experience Remaining: %,d", 
						this.skills.getLevelExperience(this.skillType) - this.skills.getExperience(this.skillType)),
				SKILL_NAME_COLOR, DETAILS_FONT, maxWidth));
    	experienceBar.setToolTip(expTooltip);
    	elements.add(experienceBar);
    }
    @Override
    public int getWidth() {
        return maxWidth;
    }

    @Override
    public int getHeight() {
        return SKILL_NAME_FONT.getLineHeight() + DETAILS_FONT.getLineHeight();
    }

    @Override
    public void render(Graphics g, int offX, int offY, int maxHeight) {
    	x = offX - getAbsoluteWidth();
    	y = offY - getAbsoluteHeight();
    	int curHeight = 0;
    	SKILL_NAME_FONT.drawString(offX, offY + curHeight, skillType.toString(), SKILL_NAME_COLOR);
    	curHeight += SKILL_NAME_FONT.getLineHeight();
    	DETAILS_FONT.drawString(offX, offY + curHeight, "Lv. " + skills.getLevel(skillExperience), SKILL_NAME_COLOR);
    }
    
	@Override
	public void update(GameContainer container) {
		// Iterate over all GUI controls and inform them of input
        for( GUIElement element : elements ) {
            element.update(container);
        }
	}
	@Override
	public void renderImage(Graphics g) {
		// TODO Auto-generated method stub
		
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
	@Override
	public boolean isAcceptingInput() {
		// TODO Auto-generated method stub
		return false;
	}

}
