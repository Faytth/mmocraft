package org.unallied.mmocraft.client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Interface implements KeyListener, MouseListener
{
    /** Invoked when a key has been pressed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key pressed event.
     * @param e The KeyEvent.
     */
    public void keyPressed(KeyEvent e)
    {
//       if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
//           System.exit(0);
        System.out.print(e.getKeyCode());
    }
    
    /** Invoked when a key has been released.
     * See the class description for {@link KeyEvent} for a definition of
     * a key released event.
     * @param e The KeyEvent.
     */
    public void keyReleased(KeyEvent e) {}
   
    /** Invoked when a key has been typed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key typed event.
     * @param e The KeyEvent.
     */
    public void keyTyped(KeyEvent e) {
    }

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
