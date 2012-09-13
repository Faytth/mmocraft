package org.unallied.mmocraft;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.Input;

/**
 * Contains all mappings for controls, such as moving, blocking, jumping,
 * opening bags, talking, and so on.  You should use this class whenever
 * you need to map a key press to its function.
 * @author Alexandria
 *
 */
public class Controls {
	
	private Map<ControlType, Integer[]> controls = new HashMap<ControlType, Integer[]>();
	
//	TODO:  Add controller support.
//	/** True if the controller should be used */
//	private boolean usingController = false;
	
	public Controls() {
		init();
	}
	
	private void init() {
		// TODO:  Offer some way to store the controls
		controls.put(ControlType.MOVE_UP, new Integer[] {Input.KEY_UP, Input.KEY_W});
		controls.put(ControlType.MOVE_LEFT, new Integer[] {Input.KEY_LEFT, Input.KEY_A});
		controls.put(ControlType.MOVE_DOWN, new Integer[] {Input.KEY_DOWN, Input.KEY_S});
		controls.put(ControlType.MOVE_RIGHT, new Integer[] {Input.KEY_RIGHT, Input.KEY_D});
		controls.put(ControlType.BASIC_ATTACK, new Integer[] {Input.KEY_LSHIFT});
		controls.put(ControlType.SHIELD, new Integer[] {Input.KEY_SPACE});
	}
	
	/**
	 * Returns whether the player is attempting to move left.
	 * @param input The input to check
	 * @return movingLeft
	 */
	public boolean isMovingLeft(Input input) {	
		boolean leftMove = isControlDown(input, ControlType.MOVE_LEFT);
		boolean rightMove = isControlDown(input, ControlType.MOVE_RIGHT);
		
		return leftMove && !rightMove;
	}
	
	/**
	 * Returns whether or not the given control is down based on a control type.
	 * @param input The input containing the currently pressed keys
	 * @param key The control type to check for (e.g. ControlType.MOVE_LEFT)
	 * @return controlDown whether the control is currently being pressed
	 */
	private boolean isControlDown(Input input, ControlType key) {
		boolean result = false;
		
		if (controls.containsKey(key)) {
			for (Integer i : controls.get(key)) {
				if (input.isKeyDown(i)) {
					result = true;
				}
			}
		}
		
		return result;
	}

	/**
	 * Returns whether the player is attempting to move right.
	 * @param input The input to check
	 * @return movingRight
	 */
	public boolean isMovingRight(Input input) {
		boolean leftMove = isControlDown(input, ControlType.MOVE_LEFT);
		boolean rightMove = isControlDown(input, ControlType.MOVE_RIGHT);
		
		return rightMove && !leftMove;
	}
	
	/**
	 * Returns whether the player is attempting to move up.
	 * @param input The input to check
	 * @return movingUp
	 */
	public boolean isMovingUp(Input input) {
		boolean upMove = isControlDown(input, ControlType.MOVE_UP);
		boolean downMove = isControlDown(input, ControlType.MOVE_DOWN);
		
		return upMove && !downMove;
	}
	
	/**
	 * Returns whether the player is attempting to move down.
	 * @param input The input to check
	 * @return movingDown
	 */
	public boolean isMovingDown(Input input) {
		boolean upMove = isControlDown(input, ControlType.MOVE_UP);
		boolean downMove = isControlDown(input, ControlType.MOVE_DOWN);
		
		return downMove && !upMove;
	}
	
	/**
	 * Returns whether the player is attempting to perform a basic attack.
	 * @param input The input to check
	 * @return basicAttack
	 */
	public boolean isBasicAttack(Input input) {
		return isControlDown(input, ControlType.BASIC_ATTACK);
	}
	
	/**
	 * Returns whether the player is attempting to raise their shield.
	 * @param input The input to check
	 * @return shielding
	 */
	public boolean isShielding(Input input) {
		return isControlDown(input, ControlType.SHIELD);
	}
}
