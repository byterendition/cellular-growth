package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker.StateValue;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.Rule;
import model.Rule.RuleCondition;
import model.enumerators.ConditionOp;
import model.enumerators.RuleAction;
import observer.action.ExitAction;
import observer.action.RandomAction;
import observer.action.ResetAction;
import observer.action.SaveAction;
import observer.action.StartAction;
import observer.action.StopAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.CellSimulator;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {
	private static final Logger	log	= LoggerFactory.getLogger(MainWindow.class);
	
	private int[][]				state;
	private CellSimulator		cellSimulator;
	
	private final int			INITIAL_STATE_SIZE_X	= 41, INITIAL_STATE_SIZE_Y = 41;
	private final int			INITIAL_PIXEL_SIZE		= 8, INITIAL_RULE_SIZE = 2;
	
	private int					stateSizeX				= INITIAL_STATE_SIZE_X;
	private int					stateSizeY				= INITIAL_STATE_SIZE_Y;
	private int					pixelSize				= INITIAL_PIXEL_SIZE;
	private int					ruleSize				= INITIAL_RULE_SIZE;
	
	private JSpinner			stateSizeXSpinner, stateSizeYSpinner, pixelSizeSpinner, ruleSizeSpinner;
	private AbstractAction		startAction, stopAction, resetAction, randomAction, saveAction;
	
	private Set<Rule>			ruleSet;
	private StatePanel			statePanel;
	private int					stateCount;
	private JLabel				stateCountLabel;
	
	public MainWindow() {
		createAndShowGUI();
		
		initState();
		initRuleSet();
		updateGUI();
		
		statePanel.renderState(state, stateSizeX, stateSizeY);
	}
	
	public void enableInterface(boolean enabled) {
		startAction.setEnabled(enabled);
		stopAction.setEnabled(!enabled);
		resetAction.setEnabled(enabled);
		randomAction.setEnabled(enabled);
		saveAction.setEnabled(enabled);
		stateSizeXSpinner.setEnabled(enabled);
		stateSizeYSpinner.setEnabled(enabled);
		pixelSizeSpinner.setEnabled(enabled);
		ruleSizeSpinner.setEnabled(enabled);
	}
	
	public void updateGUI() {
		updateStateCountLabel();
		updateStatePanel();
		// pack();
	}
	
	public void initState() {
		stateSizeX = (Integer) stateSizeXSpinner.getValue();
		stateSizeY = (Integer) stateSizeYSpinner.getValue();
		
		stateCount = 1;
		
		state = new int[stateSizeX][stateSizeY];
		state[stateSizeX / 2][stateSizeY / 2] = 1;
	}
	
	public void updateStateCountLabel() {
		if (stateCountLabel != null) {
			stateCountLabel.setText("States: " + stateCount);
		}
	}
	
	public void updateStatePanel() {
		pixelSize = (Integer) pixelSizeSpinner.getValue();
		
		if (statePanel != null) {
			statePanel.initStatePanel(stateSizeX, stateSizeY, pixelSize);
			
			statePanel.renderState(state, stateSizeX, stateSizeY);
		}
	}
	
	public void initRandomState() {
		stateCount = 1;
		
		Random r = new Random();
		
		state = new int[stateSizeX][stateSizeY];
		
		for (int i = 0; i < stateSizeX * stateSizeY / 100; i++) {
			state[r.nextInt(stateSizeX)][r.nextInt(stateSizeY)] = 1;
		}
	}
	
	public void initRuleSet() {
		ruleSize = (Integer) ruleSizeSpinner.getValue();
		int neighbours = 4 * ruleSize * (ruleSize + 1);
		int maxVal = 1;
		
		int[] ruleArrTemplate = new int[] { 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1 };
		int[] ruleArr = new int[neighbours];
		
		if (ruleArrTemplate.length <= ruleArr.length) {
			System.arraycopy(ruleArrTemplate, 0, ruleArr, 0, ruleArrTemplate.length);
		} else {
			System.arraycopy(ruleArrTemplate, 0, ruleArr, 0, ruleArr.length);
		}
		
		ruleSet = new HashSet<Rule>();
		for (int i = 0; i < neighbours; i++) {
			ruleSet.add(new Rule(new RuleCondition(ConditionOp.EQUALS, i), ruleArr[i], maxVal, RuleAction.SET));
		}
	}
	
	private void createAndShowGUI() {
		// Create and set up the window.
		setTitle("Cellular Growth Simulator - by Erik Stens");
		
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new MainWindowListener());
		
		// Menu
		setJMenuBar(buildMenuBar());
		
		// Set up the content pane, where the "main GUI" lives.
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new GridBagLayout());
		
		// Create components
		stateCountLabel = new JLabel("States: 1");
		stateCountLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		statePanel = new StatePanel(stateSizeX, stateSizeY, pixelSize);
		
		stateSizeXSpinner = new JSpinner(new SpinnerNumberModel(stateSizeX, 1, Integer.MAX_VALUE, 1));
		stateSizeXSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				initState();
				updateStatePanel();
			}
		});
		stateSizeYSpinner = new JSpinner(new SpinnerNumberModel(stateSizeY, 1, Integer.MAX_VALUE, 1));
		stateSizeYSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				initState();
				updateStatePanel();
			}
		});
		pixelSizeSpinner = new JSpinner(new SpinnerNumberModel(pixelSize, 1, Integer.MAX_VALUE, 1));
		pixelSizeSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				initState();
				updateStatePanel();
			}
		});
		ruleSizeSpinner = new JSpinner(new SpinnerNumberModel(ruleSize, 1, Integer.MAX_VALUE, 1));
		ruleSizeSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				initRuleSet();
			}
		});
		
		startAction = new StartAction(this);
		JButton startButton = new JButton(startAction);
		stopAction = new StopAction(this);
		JButton stopButton = new JButton(stopAction);
		
		resetAction = new ResetAction(this);
		JButton resetButton = new JButton(resetAction);
		randomAction = new RandomAction(this);
		JButton randomButton = new JButton(randomAction);
		
		saveAction = new SaveAction(this);
		JButton saveButton = new JButton(saveAction);
		
		enableInterface(true);
		
		// Set constraints
		GridBagConstraints gbc = new GridBagConstraints();
		
		// Adding components to the contentPane, which is then added to the frame
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(12, 12, 0, 12);
		contentPane.add(stateCountLabel, gbc);
		
		gbc.gridy = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(12, 12, 12, 12);
		contentPane.add(statePanel, gbc);
		
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.weighty = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(0, 12, 12, 12);
		contentPane.add(stateSizeXSpinner, gbc);
		
		gbc.gridx = 1;
		contentPane.add(stateSizeYSpinner, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 3;
		contentPane.add(pixelSizeSpinner, gbc);
		
		gbc.gridx = 1;
		contentPane.add(ruleSizeSpinner, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 4;
		contentPane.add(startButton, gbc);
		
		gbc.gridx = 1;
		contentPane.add(stopButton, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 5;
		contentPane.add(resetButton, gbc);
		
		gbc.gridx = 1;
		contentPane.add(randomButton, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 6;
		gbc.gridwidth = 2;
		contentPane.add(saveButton, gbc);
		
		add(contentPane);
		
		// set a minimum size for the window so the image panel is always visible
		setMinimumSize(new Dimension(240, 240));
		
		pack();
	}
	
	private JMenuBar buildMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		
		fileMenu.add(new JMenuItem(new ExitAction(this)));
		
		menuBar.add(fileMenu);
		
		return menuBar;
	}
	
	private class MainWindowListener extends WindowAdapter {
		public MainWindowListener() {}
		
		@Override
		public void windowClosing(WindowEvent e) {
			log.info("Exit");
			System.exit(0);
		}
	}
	
	public void startSimulator() {
		if (cellSimulator == null || cellSimulator.getState() != StateValue.STARTED) {
			cellSimulator = new CellSimulator(this, statePanel, state, stateSizeX, stateSizeY, ruleSet, ruleSize);
			cellSimulator.execute();
		}
	}
	
	public void stopSimulator() {
		cellSimulator.cancel(true);
	}
	
	public void setState(int[][] state) {
		this.state = state;
	}
	
	public void addStateCount(int count) {
		stateCount += count;
		
		if (stateCountLabel != null) {
			stateCountLabel.setText("States: " + stateCount);
		}
	}
	
	public void saveImage(File file) {
		BufferedImage image = statePanel.stateImage;
		saveImage(image, file);
	}
	
	public void saveImage(BufferedImage image, File file) {
		try {
			ImageIO.write(image, "png", file);
			log.info("Image saved");
		} catch (IOException e) {
			throw new RuntimeException("Image could not be saved!");
		}
	}
	
	public void renderAndSaveStates(List<int[][]> stateChunk, int stateSizeX, int stateSizeY) {
		for (int[][] state : stateChunk) {
			stateCount++;
			log.info("stateChunk size: {}", stateCount);
			BufferedImage stateImage = new BufferedImage(stateSizeX, stateSizeY, BufferedImage.TYPE_INT_RGB);
			for (int x = 0; x < stateSizeX; x++) {
				for (int y = 0; y < stateSizeY; y++) {
					int color;
					if (state[x][y] == 0) {
						color = Color.WHITE.getRGB();
					} else {
						color = Color.BLACK.getRGB();
					}
					stateImage.setRGB(x, y, color);
				}
			}
			saveImage(stateImage, new File("./images/All patterns/Pattern " + stateCount + ".png"));
		}
	}
}
