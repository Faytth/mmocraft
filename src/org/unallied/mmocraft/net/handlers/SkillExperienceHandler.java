package org.unallied.mmocraft.net.handlers;

import org.unallied.mmocraft.chat.ChatMessage;
import org.unallied.mmocraft.client.Game;
import org.unallied.mmocraft.client.MMOClient;
import org.unallied.mmocraft.constants.StringConstants;
import org.unallied.mmocraft.gui.MessageType;
import org.unallied.mmocraft.net.AbstractPacketHandler;
import org.unallied.mmocraft.skills.SkillType;
import org.unallied.mmocraft.skills.Skills;
import org.unallied.mmocraft.tools.input.SeekableLittleEndianAccessor;

public class SkillExperienceHandler extends AbstractPacketHandler {
    @Override
    /**
     * A message containing [skillType] [experience]
     * This is sent by the server to update the experience for one of the player's skills.
     */
    public void handlePacket(SeekableLittleEndianAccessor slea, MMOClient client) {
        try {
            SkillType skillType = SkillType.fromValue(slea.readByte());
            long experience = slea.readLong();
            // Check to see if we leveled up.
            Skills skills = Game.getInstance().getClient().getPlayer().getSkills();
            
            // If we leveled up
            if (skills.setExperience(skillType, experience)) {
                Game.getInstance().getClient().getPlayer().recalculateStats();
                ChatMessageHandler.addMessage(new ChatMessage(StringConstants.SYSTEM, MessageType.SYSTEM, 
                        String.format(StringConstants.SKILL_LEVEL_UP, skillType.toString(), skills.getLevel(skillType))));
            }
        } catch (Throwable t) {
        }
    }
}
