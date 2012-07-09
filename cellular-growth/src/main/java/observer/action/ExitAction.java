package observer.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class ExitAction extends AbstractAction {
	private JFrame	parent;
	
	public ExitAction(JFrame parent) {
		super("Exit");
		
		putValue(SHORT_DESCRIPTION, "Exit program");
		putValue(MNEMONIC_KEY, KeyEvent.VK_E);
		
		this.parent = parent;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		parent.dispatchEvent(new WindowEvent(parent, WindowEvent.WINDOW_CLOSING));
	}
}
