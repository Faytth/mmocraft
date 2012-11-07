package org.unallied.mmocraft.states;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.GameState;
import org.unallied.mmocraft.gui.GUIElement;
import org.unallied.mmocraft.gui.GUIElement.EventIntf;
import org.unallied.mmocraft.gui.frame.Frame;
import org.unallied.mmocraft.gui.frame.FrameZOrdering;

/**
 * An abstract state from which all other states are derived.
 * @author Alexandria
 *
 */
public abstract class AbstractState implements GameState {

	FrameZOrdering orderedFrames = null;
	
    public AbstractState(Frame parent, EventIntf intf,
            GameContainer container, float x, float y, int width, int height) {
    	orderedFrames = new FrameZOrdering(parent, intf, container, x, y, width, height);
    }
    
    /**
     * Resizes the game to fit the display size.
     * @param container The container to resize
     */
    public void resize(GameContainer container) {
        if (container instanceof org.newdawn.slick.AppGameContainer) {
            try {
                ((org.newdawn.slick.AppGameContainer)container).setDisplayMode(
                        Display.getWidth(), Display.getHeight(), Display.isFullscreen());
            } catch (SlickException e) {
                e.printStackTrace();
            }
        } else if (container instanceof org.newdawn.slick.AppletGameContainer.Container) {
            try {
                boolean needsRefresh = container.getGraphics().getWidth() != Display.getDisplayMode().getWidth() ||
                        container.getGraphics().getHeight() != Display.getDisplayMode().getHeight();
                ((org.newdawn.slick.AppletGameContainer.Container)container).setDisplayMode(Display.getWidth(), Display.getHeight(), Display.isFullscreen(), needsRefresh);
            } catch (SlickException e) {
                e.printStackTrace();
            }
        }
        container.getGraphics().setDimensions(Display.getWidth(), Display.getHeight());
    }

	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
		for (GUIElement element : orderedFrames.getFramesList()) {
			if (element.mouseClicked(button, x, y, clickCount)) {
				return;
			}
		}
	}

	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
		for (GUIElement element : orderedFrames.getFramesList()) {
			if (element.mouseDragged(oldx, oldy, newx, newy)) {
				return;
			}
		}
	}

	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		for (GUIElement element : orderedFrames.getFramesList()) {
			if (element.mouseMoved(oldx, oldy, newx, newy)) {
				return;
			}
		}
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		for (GUIElement element : orderedFrames.getFramesList()) {
			if (element.mousePressed(button, x, y)) {
				return;
			}
		}
	}

	@Override
	public void mouseReleased(int button, int x, int y) {
		for (GUIElement element : orderedFrames.getFramesList()) {
			if (element.mouseReleased(button, x, y)) {
				return;
			}
		}
	}

	@Override
	public void mouseWheelMoved(int change) {
		for (GUIElement element : orderedFrames.getFramesList()) {
			if (element.mouseWheelMoved(change)) {
				return;
			}
		}
	}

	@Override
	public void inputEnded() {
		for (GUIElement element : orderedFrames.getFramesList()) {
			if (element.inputEnded()) {
				return;
			}
		}
	}

	@Override
	public void inputStarted() {
		for (GUIElement element : orderedFrames.getFramesList()) {
			if (element.inputStarted()) {
				return;
			}
		}
	}

	@Override
	public boolean isAcceptingInput() {
		return true;
	}

	@Override
	public void setInput(Input input) {

	}

	@Override
	public void keyPressed(int key, char c) {
		for (GUIElement element : orderedFrames.getFramesList()) {
			if (element.keyPressed(key, c)) {
				return;
			}
		}
	}

	@Override
	public void keyReleased(int key, char c) {
		for (GUIElement element : orderedFrames.getFramesList()) {
			if (element.keyReleased(key, c)) {
				return;
			}
		}
	}

	@Override
	public void controllerButtonPressed(int controller, int button) {
		for (GUIElement element : orderedFrames.getFramesList()) {
			if (element.controllerButtonPressed(controller, button)) {
				return;
			}
		}
	}

	@Override
	public void controllerButtonReleased(int controller, int button) {
		for (GUIElement element : orderedFrames.getFramesList()) {
			if (element.controllerButtonReleased(controller, button)) {
				return;
			}
		}
	}

	@Override
	public void controllerDownPressed(int controller) {
		for (GUIElement element : orderedFrames.getFramesList()) {
			if (element.controllerDownPressed(controller)) {
				return;
			}
		}
	}

	@Override
	public void controllerDownReleased(int controller) {
		for (GUIElement element : orderedFrames.getFramesList()) {
			if (element.controllerDownReleased(controller)) {
				return;
			}
		}
	}

	@Override
	public void controllerLeftPressed(int controller) {
		for (GUIElement element : orderedFrames.getFramesList()) {
			if (element.controllerLeftPressed(controller)) {
				return;
			}
		}
	}

	@Override
	public void controllerLeftReleased(int controller) {
		for (GUIElement element : orderedFrames.getFramesList()) {
			if (element.controllerLeftReleased(controller)) {
				return;
			}
		}
	}

	@Override
	public void controllerRightPressed(int controller) {
		for (GUIElement element : orderedFrames.getFramesList()) {
			if (element.controllerRightPressed(controller)) {
				return;
			}
		}
	}

	@Override
	public void controllerRightReleased(int controller) {
		for (GUIElement element : orderedFrames.getFramesList()) {
			if (element.controllerRightPressed(controller)) {
				return;
			}
		}
	}

	@Override
	public void controllerUpPressed(int controller) {
		for (GUIElement element : orderedFrames.getFramesList()) {
			if (element.controllerUpPressed(controller)) {
				return;
			}
		}
	}

	@Override
	public void controllerUpReleased(int controller) {
		for (GUIElement element : orderedFrames.getFramesList()) {
			if (element.controllerUpReleased(controller)) {
				return;
			}
		}
	}
}
