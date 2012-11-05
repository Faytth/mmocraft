package org.unallied.mmocraft.net;

import java.util.ArrayList;
import java.util.List;

import org.unallied.mmocraft.client.Game;
import org.unallied.mmocraft.client.MMOClient;

public class PacketSender implements Runnable {
    private static List<Packet> packetsToSend = new ArrayList<Packet>();
    
    public void run() {
        boolean keepRunning = true;
        while (keepRunning) {
            try {
                int packetCount = 0;
                synchronized (packetsToSend) {
                    packetCount = packetsToSend.size();
                }
                /*
                 *  Packet count will be at least as much as packetCount, because
                 *  packet removals from packetsToSend only occur in this block.
                 */
                if (packetCount > 0) {
                    MMOClient client = Game.getInstance().getClient();
                    // Reconnect if needed
                    if (client.getSession() == null || !client.getSession().isConnected()) {
                        client.setSession(PacketSocket.getInstance().getSession());
                    }
                    synchronized (packetsToSend) {
                        if (!packetsToSend.isEmpty()) { // It should be impossible to fail this check
                            try {
                                client.getSession().write(packetsToSend.get(0));
                            } catch (Throwable t) {
                            }
                            // Delete the packet even if it failed to send.
                            packetsToSend.remove(0);
                        }
                    }
                }
                
                keepRunning = Game.getInstance().getContainer().running();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
    
    public static void addPacket(Packet packet) {
        synchronized (packetsToSend) {
            packetsToSend.add(packet);
        }
    }
}
