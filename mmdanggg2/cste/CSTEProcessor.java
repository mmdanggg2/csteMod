package mmdanggg2.cste;

import java.util.ArrayList;

import net.minecraft.util.BlockPos;

public class CSTEProcessor {
	private static BlockPos[] positions = new BlockPos[2];
	
	private int currPoint = 0;
	
	public static void setPos(int index, BlockPos pos) {
		positions[index] = pos;
	}
	
	public static BlockPos getPos(int index) {
		return positions[index];
	}
	
	public static void clearPos() {
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
