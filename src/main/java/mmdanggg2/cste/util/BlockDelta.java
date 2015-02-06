package mmdanggg2.cste.util;

import mmdanggg2.cste.world.WorldReader;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;

public class BlockDelta {
	private BlockPos pos;
	private Block block;
	
	public BlockDelta(BlockPos pos, Block block) {
		super();
		this.pos = pos;
		this.block = block;
	}

	public BlockPos getPos() {
		return pos;
	}

	public Block getBlock() {
		return block;
	}
	
	public boolean isChanged(){
		return block != WorldReader.getBlock(pos);
	}
}
