package observer.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

import model.PNGFileFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import view.MainWindow;
import controller.FileUtil;

@SuppressWarnings("serial")
public class SaveAction extends AbstractAction {
	private static final Logger	log	= LoggerFactory.getLogger(SaveAction.class);
	
	MainWindow					mainWindow;
	private JFileChooser		fileChooser;
	
	public SaveAction(MainWindow mainWindow) {
		super("Save");
		
		putValue(SHORT_DESCRIPTION, "Save image");
		putValue(MNEMONIC_KEY, KeyEvent.VK_V);
		
		this.mainWindow = mainWindow;
		
		fileChooser = new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.addChoosableFileFilter(new PNGFileFilter());
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof Component) {
			Component parent = (Component) e.getSource();
			int returnVal = fileChooser.showSaveDialog(parent);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				if (FileUtil.getFileExtension(file) == null) {
					file = new File(file + ".png");
				}
				
				mainWindow.saveImage(file);
			} else {
				log.info("Save command cancelled by user.");
			}
		}
	}
}
