package mmdanggg2.cste.util;

import mmdanggg2.cste.world.WorldReader;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;

public class BlockDelta {
	private BlockPos pos;
	private Block oldBlock;
	private int oldMeta;
	private Block newBlock;
	private int newMeta;
	
	public BlockDelta(BlockPos pos, Block block, Integer meta) {
		super();
		this.pos = pos;
		this.newBlock = block;
		if (meta == null) {
			this.newMeta = 0;
		}
		else {
			this.newMeta = meta;
		}
		IBlockState oldState = WorldReader.getBlockState(pos);
		this.oldBlock = oldState.getBlock();
		this.oldMeta = oldBlock.getMetaFromState(oldState);
	}

	public BlockPos getPos() {
		return pos;
	}

	public Block getNewBlock() {
		return newBlock;
	}

	public int getNewMeta() {
		return newMeta;
	}

	public Block getOldBlock() {
		return oldBlock;
	}

	public int getOldMeta() {
		return oldMeta;
	}
	
	public boolean isChangedFromCurrent(){
		if (newBlock == WorldReader.getBlock(pos)) {
			if (newMeta == newBlock.getMetaFromState(WorldReader.getBlockState(pos))) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isChanged(){
		if (newBlock == oldBlock && newMeta == oldMeta) {
			return false;
		}
		return true;
	}
}
