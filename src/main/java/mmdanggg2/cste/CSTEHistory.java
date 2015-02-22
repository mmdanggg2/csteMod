package mmdanggg2.cste;

import java.util.ArrayList;
import java.util.HashSet;

import mmdanggg2.cste.util.BlockDelta;
import mmdanggg2.cste.util.CSTELogger;

public class CSTEHistory {
	private ArrayList<HashSet<BlockDelta>> history;
	private HashSet<BlockDelta> currHistory;

	public CSTEHistory() {
		history = new ArrayList<HashSet<BlockDelta>>();
		currHistory = new HashSet<BlockDelta>();
	}
	
	public void addDelta(BlockDelta bd) {
		currHistory.add(bd);
		//CSTELogger.logDebug("New BlockDelta: " + currHistory.size() + ", history: " + history.size());
	}

	public void nextLevel() {
		CSTELogger.logDebug("Hisory level " + (history.size()+1) + " has " + currHistory.size() + " bd's");
		history.add(currHistory);
		currHistory = new HashSet<BlockDelta>();
		CSTELogger.logDebug("Now building history level: " + (history.size()+1));
	}
	
	public HashSet<BlockDelta> getHistory() {
		if (history.size() == 0) {
			CSTELogger.logDebug("History was empty.");
			return new HashSet<BlockDelta>();
		}
		CSTELogger.logDebug("Removing history level " + history.size());
		HashSet<BlockDelta> retHistory = history.remove(history.size()-1);
		return retHistory;
	}
}
