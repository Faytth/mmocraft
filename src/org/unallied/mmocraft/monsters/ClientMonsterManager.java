package org.unallied.mmocraft.monsters;

import java.io.IOException;
import java.io.InputStream;

import org.newdawn.slick.util.ResourceLoader;
import org.unallied.mmocraft.constants.ClientConstants;
import org.unallied.mmocraft.tools.IOUtils;
import org.unallied.mmocraft.tools.input.ByteArrayByteStream;
import org.unallied.mmocraft.tools.input.GenericSeekableLittleEndianAccessor;

public class ClientMonsterManager extends MonsterManager {
    private ClientMonsterManager() {
        init();
    }
    
    private static class ClientMonsterManagerHolder {
        private static final ClientMonsterManager instance = new ClientMonsterManager();
    }
    
    private void init() {
    }
    
    public static ClientMonsterManager getInstance() {
        return ClientMonsterManagerHolder.instance;
    }
    
    @Override
    public MonsterData getMonsterData(Integer monsterId) {
        if (monsterId == null) {
            return null;
        }
        MonsterData result = monsters.get(monsterId);
        if (result == null) {
            System.err.println("Monster data could not be found in the client.  Monster ID: " + 
                    monsterId + ".");
            // Enter a placeholder value
            monsters.put(monsterId,  
                    new ClientMonsterData(monsterId, "", 0, (short)0, (short)0, 
                            (short)0, 0, 0, MonsterType.UNASSIGNED));
            /*
             *  NOTE:  We are not querying from the server, because our new 
             *  resource editor removes the need for this.  We should never
             *  actually get here in the code, so it is to be taken as an
             *  error.
             */
        } else if (result.getType() == MonsterType.UNASSIGNED) {
            /*
             *  Since we're not querying it from the server, this monster will
             *  just have to remain "stuck."  :(
             */
            result = null;
        }
        
        return result;
    }
    
    @Override
    public void load(String filename) {
        InputStream is = null;
        try {
            is = ResourceLoader.getResourceAsStream(filename);
            byte[] bytes = IOUtils.toByteArray(is);
            ByteArrayByteStream babs = new ByteArrayByteStream(bytes);
            GenericSeekableLittleEndianAccessor slea = new GenericSeekableLittleEndianAccessor(babs);
            if (slea.readInt() != ClientConstants.MAGIC_MONSTER_PACK_NUMBER) {
                System.err.println("Unable to load Unallied Monster Pack file.  Bad magic number: " + filename);
                return;
            }
            MonsterData monsterData;
            while ((monsterData = ClientMonsterData.fromBytes(slea)) != null) {
                add(monsterData);
            }
            System.out.println("Successfully loaded Unallied Monster Pack from: " + filename);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
