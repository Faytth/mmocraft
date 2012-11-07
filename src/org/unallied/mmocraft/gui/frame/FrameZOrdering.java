package org.unallied.mmocraft.gui.frame;

import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;
import org.unallied.mmocraft.gui.GUIElement;

public class FrameZOrdering extends Frame { 

	public FrameZOrdering(Frame parent, EventIntf intf,
			GameContainer container, float x, float y, int width, int height) {
		super(parent, intf, container, x, y, width, height);
	}

	public void addFrame(Frame frame, int index) {
		elements.add(index, frame);
	}

	public void addFrame(Frame frame) {
		elements.add(frame);
	}

	public void removeFrame(Frame frame) {
		elements.remove(frame);
	}

	public void sendFrameToTop(Frame frame) {
		removeFrame(frame);
		addFrame(frame, 0);
	}

	public void removeAllFrames() {
		elements.clear();
	}

	public int getFrameCount() {
		return elements.size();
	}
	
	public List<GUIElement> getFramesList() {
		return elements;
	}

	@Override
	public void update(GameContainer container) {

		// Iterate over all GUI controls and inform them of input
		for( GUIElement element : elements ) {
			element.update(container);
		}
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {

		// Iterate over all GUI controls and inform them of input
		if (elements != null) {
			for(int i = elements.size() - 1; i >= 0; i--) {
				elements.get(i).render(container, game, g);
			}
			
			for(int i = elements.size() - 1; i >= 0; i--) {
				elements.get(i).renderToolTip(container, game, g);
			}
		}
	}

	@Override
	public void renderImage(Graphics g) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean isAcceptingTab() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAcceptingFocus() {
		// TODO Auto-generated method stub
		return false;
	}
}
