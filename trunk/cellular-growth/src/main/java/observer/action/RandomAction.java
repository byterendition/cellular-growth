package observer.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import view.MainWindow;
import controller.CellSimulator;

@SuppressWarnings("serial")
public class RandomAction extends AbstractAction {
	private static final Logger	log	= LoggerFactory.getLogger(RandomAction.class);
	
	CellSimulator				cellSimulator;
	MainWindow					mainWindow;
	
	public RandomAction(MainWindow mainWindow) {
		super("Random");
		
		putValue(SHORT_DESCRIPTION, "Randomize simulation");
		putValue(MNEMONIC_KEY, KeyEvent.VK_A);
		
		this.mainWindow = mainWindow;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		mainWindow.createRandomState();
		log.info("Simulator randomized");
	}
}
