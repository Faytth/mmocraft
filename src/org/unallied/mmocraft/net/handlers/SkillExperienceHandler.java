package org.unallied.mmocraft.net.handlers;

import org.unallied.mmocraft.client.Game;
import org.unallied.mmocraft.client.MMOClient;
import org.unallied.mmocraft.net.AbstractPacketHandler;
import org.unallied.mmocraft.skills.SkillType;
import org.unallied.mmocraft.tools.input.SeekableLittleEndianAccessor;

public class SkillExperienceHandler extends AbstractPacketHandler {
    @Override
    /**
     * A message containing [skillType] [experience]
     * This is sent by the server to update the experience for one of the player's skills.
     */
    public void handlePacket(SeekableLittleEndianAccessor slea, MMOClient client) {
        try {
            Game.getInstance().getClient().getPlayer().getSkills().setExperience(
                    SkillType.fromValue(slea.readByte()), slea.readLong());
        } catch (Throwable t) {
        }
    }
}
