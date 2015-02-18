package mmdanggg2.cste;

import java.util.ArrayList;
import mmdanggg2.cste.util.BlockDelta;

public class CSTEHistory {
	private ArrayList<ArrayList<BlockDelta>> history;
	private int historyLevel;

	public CSTEHistory() {
		this.historyLevel = 0;
		ArrayList<ArrayList<BlockDelta>> hist = new ArrayList<ArrayList<BlockDelta>>();
		ArrayList<BlockDelta> currHistory = new ArrayList<BlockDelta>();
		hist.set(historyLevel, currHistory);
		this.history = hist;
	}
	
	public void addDelta(BlockDelta bd) {
		ArrayList<BlockDelta> currHistory = history.get(historyLevel);
		currHistory.add(bd);
	}

	public void nextLevel() {
		this.historyLevel ++;
		history.set(historyLevel, new ArrayList<BlockDelta>());
	}
}
