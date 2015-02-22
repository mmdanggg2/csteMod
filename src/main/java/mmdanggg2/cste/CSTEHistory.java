package mmdanggg2.cste;

import java.util.ArrayList;
import java.util.HashSet;

import mmdanggg2.cste.util.BlockDelta;
import mmdanggg2.cste.util.CSTELogger;

public class CSTEHistory {
	private ArrayList<HashSet<BlockDelta>> history;

	public CSTEHistory() {
		ArrayList<HashSet<BlockDelta>> hist = new ArrayList<HashSet<BlockDelta>>();
		HashSet<BlockDelta> currHistory = new HashSet<BlockDelta>();
		hist.add(currHistory);
		this.history = hist;
	}
	
	public void addDelta(BlockDelta bd) {
		if (history.size() == 0) {
			history.add(new HashSet<BlockDelta>());
		}
		HashSet<BlockDelta> currHistory = history.get(history.size()-1);
		currHistory.add(bd);
		//CSTELogger.logDebug("New BlockDelta: " + currHistory.size() + ", history: " + history.size());
	}

	public void nextLevel() {
		CSTELogger.logDebug("Hisory level " + history.size() + " has " + history.get(history.size()-1).size() + " bd's");
		history.add(new HashSet<BlockDelta>());
		CSTELogger.logDebug("New history level: " + history.size());
	}
	
	public HashSet<BlockDelta> getHistory() {
		CSTELogger.logDebug("Removing history level " + history.size());
		HashSet<BlockDelta> currHistory = history.remove(history.size()-1);
		while (currHistory.size() == 0 && history.size() > 0) {
			CSTELogger.logDebug("History level " + (history.size()+1) + " was empty, removing");
			currHistory = history.remove(history.size()-1);
		}
		if (history.size() == 0) {
			history.add(new HashSet<BlockDelta>());
		}
		return currHistory;
	}
}
