package mmdanggg2.cste.world;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import mmdanggg2.cste.CSTE;
import mmdanggg2.cste.CSTESelectionProcessor;
import mmdanggg2.cste.events.ChatRecievedHandler;
import mmdanggg2.cste.selections.SelectionCube;
import mmdanggg2.cste.util.BlockDelta;
import mmdanggg2.cste.util.ChatMessenger;

public class WorldEditor {
	private static ArrayList<String> commands = new ArrayList<String>();
	
	public static void setBlock(BlockPos pos, Block block, int meta) {
		if (256 > pos.getY() && pos.getY() >= 0) {
			BlockDelta bd = new BlockDelta(pos, block, meta);
			if (bd.isChangedFromCurrent()) {
				CSTE.history.addDelta(bd);
				commands.add("/setblock " + CSTESelectionProcessor.posToStr(pos) + " " + bd.getNewBlockStr());
			}
		}
	}
	
	public static void fillBlock(BlockPos pos1, BlockPos pos2, Block block, int meta) {
		SelectionCube sel = new SelectionCube(pos1, pos2);
		BlockPos adjPos1 = sel.getSmallestCoord();
		BlockPos adjPos2 = sel.getLargestCoord();
		
		for (int x = adjPos1.getX(); x <= adjPos2.getX(); x++) {
			for (int y = adjPos1.getY(); y <= adjPos2.getY(); y++) {
				for (int z = adjPos1.getZ(); z <= adjPos2.getZ(); z++) {
					setBlock(new BlockPos(x,y,z), block, meta);
				}
			}
		}
	}
	
	public static void sendCommands() {
		if (commands.size() > 0) {
			ChatRecievedHandler.instance.buildingStart(commands.size());
			for (String command : commands) {
				ChatMessenger.sendMessage(command);
			}
			commands.clear();
			CSTE.history.nextLevel();
		}
	}
}
