package observer.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import view.MainWindow;
import controller.CellSimulator;

@SuppressWarnings("serial")
public class StopAction extends AbstractAction {
	private static final Logger	log	= LoggerFactory.getLogger(StopAction.class);
	
	CellSimulator				cellSimulator;
	MainWindow					mainWindow;
	
	public StopAction(MainWindow mainWindow) {
		super("Stop");
		
		putValue(SHORT_DESCRIPTION, "Stop simulation");
		putValue(MNEMONIC_KEY, KeyEvent.VK_T);
		
		this.mainWindow = mainWindow;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		mainWindow.stopSimulator();
		log.info("Simulator stopped");
	}
}
