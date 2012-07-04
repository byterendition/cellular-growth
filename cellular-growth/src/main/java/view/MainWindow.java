package view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker.StateValue;
import javax.swing.WindowConstants;

import model.Rule;
import model.Rule.RuleCondition;
import model.enumerators.ConditionOp;
import model.enumerators.RuleAction;
import observer.action.ExitAction;
import observer.action.RandomAction;
import observer.action.ResetAction;
import observer.action.StartAction;
import observer.action.StopAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.CellSimulator;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {
	private static final Logger	log	= LoggerFactory.getLogger(MainWindow.class);
	
	private int[][]				state;
	private int					stateSizeX, stateSizeY;
	private CellSimulator		cellSimulator;
	private Set<Rule>			ruleSet;
	private int					ruleSize;
	private StatePanel			statePanel;
	private int					stateCount;
	private JLabel				stateCountLabel;
	
	public MainWindow() {
		createInitialState();
		createRuleSet();
		
		createAndShowGUI();
		
		statePanel.renderState(state, stateSizeX, stateSizeY);
	}
	
	public void createInitialState() {
		stateCount = 1;
		
		stateSizeX = 41;
		stateSizeY = 41;
		state = new int[stateSizeX][stateSizeY];
		state[20][20] = 1;
		
		if (statePanel != null) {
			statePanel.renderState(state, stateSizeX, stateSizeY);
		}
		
		if (stateCountLabel != null) {
			stateCountLabel.setText("States: " + stateCount);
		}
	}
	
	public void createRandomState() {
		stateCount = 1;
		
		Random r = new Random();
		
		stateSizeX = 41;
		stateSizeY = 41;
		state = new int[stateSizeX][stateSizeY];
		
		for (int i = 0; i < stateSizeX * stateSizeY / 100; i++) {
			state[r.nextInt(stateSizeX)][r.nextInt(stateSizeY)] = 1;
		}
		
		if (statePanel != null) {
			statePanel.renderState(state, stateSizeX, stateSizeY);
		}
		
		if (stateCountLabel != null) {
			stateCountLabel.setText("States: " + stateCount);
		}
	}
	
	private void createRuleSet() {
		ruleSize = 2;
		int maxVal = 1;
		
		int[] ruleArr = new int[] { 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1 };
		
		ruleSet = new HashSet<Rule>();
		for (int i = 0; i < 25; i++) {
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
		
		statePanel = new StatePanel(stateSizeX, stateSizeY, 8);
		
		JButton startButton = new JButton(new StartAction(this));
		JButton stopButton = new JButton(new StopAction(this));
		JButton resetButton = new JButton(new ResetAction(this));
		JButton randomButton = new JButton(new RandomAction(this));
		
		// Set constraints
		GridBagConstraints gbc = new GridBagConstraints();
		
		// Adding components to the contentPane, which is then added to the frame
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 4;
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
		contentPane.add(startButton, gbc);
		
		gbc.gridx = 1;
		contentPane.add(stopButton, gbc);
		
		gbc.gridx = 2;
		contentPane.add(resetButton, gbc);
		
		gbc.gridx = 3;
		contentPane.add(randomButton, gbc);
		
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
}
