package org.unallied.mmocraft;

/**
 * This is an enum, but with ints.  The reason for this is that the Slick
 * state machine uses ints instead of enums for the states.
 * @author Faythless
 *
 */
public class GameState {
    public static final int LOGIN = 0;
    public static final int INGAME = 1;
}
