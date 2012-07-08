package observer.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import view.MainWindow;

@SuppressWarnings("serial")
public class ResetAction extends AbstractAction {
	private static final Logger	log	= LoggerFactory.getLogger(ResetAction.class);
	
	MainWindow					mainWindow;
	
	public ResetAction(MainWindow mainWindow) {
		super("Reset");
		
		putValue(SHORT_DESCRIPTION, "Reset simulation");
		putValue(MNEMONIC_KEY, KeyEvent.VK_R);
		
		this.mainWindow = mainWindow;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		mainWindow.createInitialState();
		log.info("Simulator reset");
	}
}
