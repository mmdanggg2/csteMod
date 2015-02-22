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
		//CSTELogger.logDebug("New BlockDelta: " + currHistory.size() + ", history: " + history.size());
	}

	public void nextLevel() {
		CSTELogger.logDebug("Hisory level " + history.size() + " has " + history.get(history.size()-1).size() + " bd's");
		history.add(new ArrayList<BlockDelta>());
		CSTELogger.logDebug("New history level: " + history.size());
	}
	
	public ArrayList<BlockDelta> getHistory() {
		CSTELogger.logDebug("Removing history level " + history.size());
		ArrayList<BlockDelta> currHistory = history.remove(history.size()-1);
		while (currHistory.size() == 0 && history.size() > 0) {
			CSTELogger.logDebug("History level " + (history.size()+1) + " was empty, removing");
			currHistory = history.remove(history.size()-1);
		}
		if (history.size() == 0) {
			history.add(new ArrayList<BlockDelta>());
		}
		return currHistory;
	}
}
