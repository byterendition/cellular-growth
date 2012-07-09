package controller;

import java.util.List;
import java.util.Set;

import javax.swing.SwingWorker;

import model.Rule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import view.MainWindow;
import view.StatePanel;

public class CellSimulator extends SwingWorker<int[][], int[][]> {
	@SuppressWarnings("unused")
	private static final Logger	log	= LoggerFactory.getLogger(CellSimulator.class);
	
	private MainWindow			mainWindow;
	private StatePanel			statePanel;
	
	private int[][]				currentState;
	private int					stateSizeX, stateSizeY;
	
	private Set<Rule>			ruleSet;
	private int					ruleSize;
	
	public CellSimulator(MainWindow mainWindow, StatePanel statePanel, int[][] startState, int stateSizeX, int stateSizeY, Set<Rule> ruleSet, int ruleSize) {
		this.mainWindow = mainWindow;
		this.statePanel = statePanel;
		
		currentState = startState;
		this.stateSizeX = stateSizeX;
		this.stateSizeY = stateSizeY;
		
		this.ruleSet = ruleSet;
		this.ruleSize = ruleSize;
	}
	
	@Override
	protected int[][] doInBackground() throws Exception {
		boolean cancelled = false;
		while (!cancelled) {
			doStep(currentState, ruleSet);
			publish(currentState);
			if (isCancelled()) {
				cancelled = true;
			}
		}
		return currentState;
	}
	
	private void doStep(int[][] state, Set<Rule> ruleSet) {
		int[][] newState = new int[stateSizeX][stateSizeY];
		for (int i = 0, size = newState.length; i < size; i++) {
			System.arraycopy(currentState[i], 0, newState[i], 0, stateSizeX);
		}
		
		for (int x = 0; x < stateSizeX; x++) {
			for (int y = 0; y < stateSizeY; y++) {
				if (x >= ruleSize && x < stateSizeX - ruleSize && y >= ruleSize && y < stateSizeY - ruleSize) {
					int neighbourVal = 0;
					for (int xo = -ruleSize; xo <= ruleSize; xo++) {
						for (int yo = -ruleSize; yo <= ruleSize; yo++) {
							if (!(xo == 0 && yo == 0)) {
								neighbourVal += currentState[x + xo][y + yo];
							}
						}
					}
					for (Rule rule : ruleSet) {
						if (rule.testCondition(neighbourVal)) {
							newState[x][y] = rule.doRule(currentState[x][y]);
						}
					}
				}
			}
		}
		currentState = newState;
	}
	
	@Override
	protected void process(List<int[][]> stateChunk) {
		int[][] lastState = stateChunk.get(stateChunk.size() - 1); // get last result
		
		mainWindow.setState(lastState);
		mainWindow.addStateCount(stateChunk.size());
		// mainWindow.renderAndSaveStates(stateChunk, stateSizeX, stateSizeY);
		statePanel.renderState(lastState, stateSizeX, stateSizeY);
	}
}
