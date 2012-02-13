package org.unallied.mmocraft.packets;

/**
 * Used to send packets.  This singleton handles all packet sending.
 */
public class PacketCreator {
    private PacketCreator() {
        init();
    }
    
    private static class PacketCreatorHolder {
        public static final PacketCreator instance = new PacketCreator();
    }
    
    private void init() {
        
    }
    
    public static PacketCreator getInstance() {
        return PacketCreatorHolder.instance;
    }
}
