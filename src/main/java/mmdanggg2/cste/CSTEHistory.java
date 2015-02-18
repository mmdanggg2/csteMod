package mmdanggg2.cste;

import java.util.ArrayList;
import mmdanggg2.cste.util.BlockDelta;
import mmdanggg2.cste.util.CSTELogger;

public class CSTEHistory {
	private ArrayList<ArrayList<BlockDelta>> history;

	public CSTEHistory() {
		ArrayList<ArrayList<BlockDelta>> hist = new ArrayList<ArrayList<BlockDelta>>();
		ArrayList<BlockDelta> currHistory = new ArrayList<BlockDelta>();
		hist.add(currHistory);
		this.history = hist;
	}
	
	public void addDelta(BlockDelta bd) {
		if (history.size() == 0) {
			history.add(new ArrayList<BlockDelta>());
		}
		ArrayList<BlockDelta> currHistory = history.get(history.size()-1);
		currHistory.add(bd);
		CSTELogger.logDebug("New BlockDelta: " + currHistory.size() + ", history: " + history.size());
	}

	public void nextLevel() {
		history.add(new ArrayList<BlockDelta>());
		CSTELogger.logDebug("New history level: " + history.size());
	}
	
	public ArrayList<BlockDelta> getHistory() {
		ArrayList<BlockDelta> currHistory = history.remove(history.size()-1);
		while (currHistory.size() == 0 && history.size() > 0) {
			currHistory = history.remove(history.size()-1);
		}
		if (history.size() == 0) {
			history.add(new ArrayList<BlockDelta>());
		}
		return currHistory;
	}
}
