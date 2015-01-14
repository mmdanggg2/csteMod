package mmdanggg2.cste;

import java.util.ArrayList;

import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;

public class CSTEProcessor {
	private BlockPos[] positions = new BlockPos[2];
	private int currPoint = 0;
	private Item wand = null;
	
	public void setPos(int index, BlockPos pos) {
		positions[index] = pos;
	}
	
	public BlockPos getPos(int index) {
		return positions[index];
	}

	public Item getWand() {
		return wand;
	}

	public void setWand(Item wand) {
		this.wand = wand;
	}
	
	public void clearPos() {
		for (int i=0; i < positions.length; i++) {
			positions[i] = null;
		}
	}
	
	private enum BuildType {
		SOLIDCUBE(2), HOLLOWCUBE(2);
		
		private int points;

		private BuildType(int points) {
			this.points = points;
		}

		public int getPoints() {
			return points;
		}
		
		
	}
}
