package controller;

import javax.swing.SwingUtilities;

import view.MainWindow;

public class Main implements Runnable {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Main());
	}
	
	@Override
	public void run() {
		MainWindow mainWindow = new MainWindow();
		
		mainWindow.setLocationRelativeTo(null);
		mainWindow.setVisible(true);
	}
}
