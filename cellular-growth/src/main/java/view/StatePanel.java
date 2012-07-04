package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class StatePanel extends JPanel {
	public BufferedImage	stateImage;
	private BufferedImage	imageBuffer;
	private Color[]			color;
	private int				scale;
	
	public StatePanel(int stateSizeX, int stateSizeY, int scale) {
		addComponentListener(new StatePanelListener());
		
		imageBuffer = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		
		stateImage = new BufferedImage(stateSizeX, stateSizeY, BufferedImage.TYPE_INT_RGB);
		
		this.scale = scale;
		
		generateColors();
		
		setPreferredSize(new Dimension(stateSizeX * scale, stateSizeY * scale));
	}
	
	private void generateColors() {
		color = new Color[5];
		color[0] = Color.WHITE;
		color[1] = Color.BLACK;
		color[2] = new Color(128, 128, 224);
		color[3] = Color.RED;
		color[4] = Color.GREEN;
	}
	
	public void renderState(int[][] lastState, int stateSizeX, int stateSizeY) {
		for (int x = 0; x < stateSizeX; x++) {
			for (int y = 0; y < stateSizeY; y++) {
				stateImage.setRGB(x, y, color[lastState[x][y]].getRGB());
			}
		}
		
		repaint();
	}
	
	@Override
	public void paint(Graphics g) {
		if (getWidth() > 0 && getHeight() > 0) {
			Graphics gb = imageBuffer.getGraphics();
			gb.setColor(getBackground());
			gb.fillRect(0, 0, getWidth(), getHeight());
			
			gb.drawImage(stateImage, (getWidth() - stateImage.getWidth() * scale) / 2, (getHeight() - stateImage.getHeight() * scale) / 2, stateImage.getWidth() * scale, stateImage.getHeight() * scale, null);
			
			gb.setColor(Color.BLACK);
			gb.drawRect((getWidth() - stateImage.getWidth() * scale) / 2, (getHeight() - stateImage.getHeight() * scale) / 2, stateImage.getWidth() * scale - 1, stateImage.getHeight() * scale - 1);
			
			g.drawImage(imageBuffer, 0, 0, null);
		}
	}
	
	private class StatePanelListener implements ComponentListener {
		
		@Override
		public void componentResized(ComponentEvent e) {
			if (getWidth() > 0 && getHeight() > 0) {
				imageBuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
			}
		}
		
		@Override
		public void componentMoved(ComponentEvent e) {}
		
		@Override
		public void componentShown(ComponentEvent e) {}
		
		@Override
		public void componentHidden(ComponentEvent e) {}
		
	}
}
