package org.unallied.mmocraft.net.handlers;

import org.unallied.mmocraft.BlockType;
import org.unallied.mmocraft.blocks.Block;
import org.unallied.mmocraft.client.MMOClient;
import org.unallied.mmocraft.net.AbstractPacketHandler;
import org.unallied.mmocraft.tools.input.SeekableLittleEndianAccessor;

public class BlockChangedHandler extends AbstractPacketHandler {

	@Override
	public void handlePacket(SeekableLittleEndianAccessor slea, MMOClient client) {
		long x = slea.readLong();
		long y = slea.readLong();
		Block block = BlockType.fromValue(slea.readByte()).getBlock();
		client.getTerrainSession().setBlock(x, y, block);
	}

}
