package mmdanggg2.cste.world;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;

public class WorldReader {
	public static Block getBlock(BlockPos pos) {
		return Minecraft.getMinecraft().world.getBlockState(pos).getBlock();
	}
	
	public static IBlockState getBlockState(BlockPos pos) {
		return Minecraft.getMinecraft().world.getBlockState(pos);
	}

	/**
	 * Returns the highest solid 'terrain' block which can occur naturally.
	 * 
	 * Modified from WorldEdit
	 * 
	 * @param x the X coordinate
	 * @param z the Z coordinate
	 * @param minY minimal height
	 * @param maxY maximal height
	 * @return height of highest block found or 'minY'
	 */
	public static int getHighestTerrainBlock(int x, int z, int minY, int maxY) {
		for (int y = maxY; y >= minY; --y) {
			BlockPos pos = new BlockPos(x, y, z);
			Block block = getBlock(pos);
			if (!block.isReplaceable(Minecraft.getMinecraft().world, pos)) {
				return y;
			}
		}
		return minY;
	}
}
