package org.unallied.mmocraft.net;

/**
 * Contains the different kinds of connection status that PacketSocket can have.
 * @author Alexandria
 *
 */
public enum ConnectionStatus {
    DISCONNECTED, /** The default status before attempting to connect. */
    CONNECTED, /** The status when the packet socket successfully connects. */
    CONTACTING_SERVER, /** The status when attempting to connect. */
    FAILED_TO_CONNECT, /** The status if an attempt to connect failed. */
}
