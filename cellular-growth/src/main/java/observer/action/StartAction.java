package observer.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import view.MainWindow;
import controller.CellSimulator;

@SuppressWarnings("serial")
public class StartAction extends AbstractAction {
	private static final Logger	log	= LoggerFactory.getLogger(StartAction.class);
	
	CellSimulator				cellSimulator;
	MainWindow					mainWindow;
	
	public StartAction(MainWindow mainWindow) {
		super("Start");
		
		putValue(SHORT_DESCRIPTION, "Start simulation");
		putValue(MNEMONIC_KEY, KeyEvent.VK_S);
		
		this.mainWindow = mainWindow;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		mainWindow.startSimulator();
		log.info("Simulator started");
	}
}
