package mmdanggg2.cste.selections;

import net.minecraft.util.math.BlockPos;

public class SelectionCube {
	
	private BlockPos pos1;
	private BlockPos pos2;
	
	public SelectionCube(BlockPos pos1, BlockPos pos2) {
		this.pos1 = pos1;
		this.pos2 = pos2;
	}

	public BlockPos getPos1() {
		return pos1;
	}

	public void setPos1(BlockPos pos1) {
		this.pos1 = pos1;
	}

	public BlockPos getPos2() {
		return pos2;
	}

	public void setPos2(BlockPos pos2) {
		this.pos2 = pos2;
	}

	public BlockPos getLargestCoord() {
		return new BlockPos(
				Math.max(pos1.getX(), pos2.getX()),
				Math.max(pos1.getY(), pos2.getY()),
				Math.max(pos1.getZ(), pos2.getZ()));
	}

	public BlockPos getSmallestCoord() {
		return new BlockPos(
				Math.min(pos1.getX(), pos2.getX()),
				Math.min(pos1.getY(), pos2.getY()),
				Math.min(pos1.getZ(), pos2.getZ()));
	}

	public int getWidth() {
		return Math.abs(pos1.getX() - pos2.getX());
	}

	public int getHeight() {
		return Math.abs(pos1.getY() - pos2.getY());
	}

	public int getDepth() {
		return Math.abs(pos1.getZ() - pos2.getZ());
	}
}