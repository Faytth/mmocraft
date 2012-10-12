package org.unallied.mmocraft.net.handlers;

import org.unallied.mmocraft.BlockType;
import org.unallied.mmocraft.blocks.*;
import org.unallied.mmocraft.client.Game;
import org.unallied.mmocraft.client.MMOClient;
import org.unallied.mmocraft.constants.WorldConstants;
import org.unallied.mmocraft.net.AbstractPacketHandler;
import org.unallied.mmocraft.tools.input.SeekableLittleEndianAccessor;

/**
 * Chunk information received by the client from the server.  Contains block information
 * of chunk width * height in blocks.
 * [Header][chunkId(8)][blockData]
 * 
 * @author Faythless
 *
 */
public class ChunkHandler extends AbstractPacketHandler {

    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MMOClient client) {
        long chunkId = slea.readLong();
        
        int x = WorldConstants.WORLD_CHUNK_WIDTH;
        int y = WorldConstants.WORLD_CHUNK_HEIGHT;
        byte[] blocks = slea.read(x*y*1);
        Block[][] chunkBlocks = new Block[x][y];
        
        for (int i=0; i < x; ++i) {
            for (int j=0; j < y; ++j) {
                BlockType type = BlockType.fromValue(blocks[i*y+j]);
                
                chunkBlocks[i][j] = type.getBlock();
                if (chunkBlocks[i][j] == null) {
                    chunkBlocks[i][j] = new AirBlock();
                }
            }
        }
        
        Game.getInstance().getClient().getTerrainSession().setChunk(chunkId, chunkBlocks);
    }

}
