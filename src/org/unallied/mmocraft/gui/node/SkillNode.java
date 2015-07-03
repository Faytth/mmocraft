package org.unallied.mmocraft.gui.node;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Rectangle;
import org.unallied.mmocraft.client.FontHandler;
import org.unallied.mmocraft.client.FontID;
import org.unallied.mmocraft.client.Game;
import org.unallied.mmocraft.client.ImageHandler;
import org.unallied.mmocraft.client.ImageID;
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
    			EXPERIENCE_BAR_COLOR, Gauge.SHOW_PERCENT);
    	experienceBar.setValue(this.skills.getExperience(this.skillType));
    	ToolTip expTooltip = new ToolTip();
    	expTooltip.addNode(new StringNode(null, null, 
				String.format("Total Experience: %,d", 
						this.skills.getTotalExperience(this.skillType)),
				SKILL_NAME_COLOR, DETAILS_FONT, maxWidth));
    	long remainingExp = this.skills.getLevelExperience(this.skillType) - this.skills.getExperience(this.skillType);
    	remainingExp = remainingExp < 0 ? 0 : remainingExp;
    	expTooltip.addNode(new StringNode(null, null, 
				String.format("Experience Remaining: %,d", remainingExp),
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
    	
        // Update the x and y coords
        x = offX - getAbsoluteX();
    	y = offY - getAbsoluteY();
    	
        // Render background if needed
        Input input = Game.getInstance().getContainer().getInput();
        
        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();
        if (containsPoint(mouseX, mouseY)) {
            g.fill(new Rectangle(offX, offY, getWidth(), getHeight()), 
                    new GradientFill(0, 0, new Color(0, 133, 207, 180),
                            (getWidth())/2, 0, new Color(0, 133, 207, 0), true));
        }
    	
        // Render the rest of the stuff
    	int curHeight = 0;
    	String imageKey = getImageId(skillType).toString();
    	ImageHandler.getInstance().draw(imageKey, offX, offY + curHeight);
    	int iconWidth = ImageHandler.getInstance().getWidth(imageKey);
    	SKILL_NAME_FONT.drawString(offX + iconWidth + 5, offY + curHeight, skillType.toString(), SKILL_NAME_COLOR);
    	curHeight += SKILL_NAME_FONT.getLineHeight();
    	DETAILS_FONT.drawString(offX, offY + curHeight, "Lv. " + skills.getLevel(skillExperience), SKILL_NAME_COLOR);
    }
    
    public static ImageID getImageId(SkillType skillType) {
        ImageID result = null;
        
        switch (skillType) {
        case CONSTITUTION:
            result = ImageID.ICON_SKILL_CONSTITUTION;
            break;
        case STRENGTH:
            result = ImageID.ICON_SKILL_STRENGTH;
            break;
        case DEFENSE:
            result = ImageID.ICON_SKILL_DEFENSE;
            break;
        case MINING:
            result = ImageID.ICON_SKILL_MINING;
            break;
        case SMITHING:
            result = ImageID.ICON_SKILL_SMITHING;
            break;
        case FISHING:
            result = ImageID.ICON_SKILL_FISHING;
            break;
        case COOKING:
            result = ImageID.ICON_SKILL_COOKING;
            break;
        default:
            result = ImageID.ICON_SKILL_SMITHING;
            break;
        }
        
        return result;
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
