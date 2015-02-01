package mmdanggg2.cste.world;

import com.google.common.collect.ImmutableMap;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.BlockPos;

public class WorldReader {
	public static Block getBlock(BlockPos pos) {
		Minecraft mc = Minecraft.getMinecraft();
		WorldClient world = mc.theWorld;
		IBlockState state = world.getBlockState(pos);
		return state.getBlock();
	}
	
	public static ImmutableMap<?, ?> getBlockData(BlockPos pos) {
		WorldClient world = Minecraft.getMinecraft().theWorld;
		IBlockState state = world.getBlockState(pos);
		return state.getProperties();
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
			if (!block.isCollidable()) {
				return y;
			}
		}
		return minY;
	}
}
