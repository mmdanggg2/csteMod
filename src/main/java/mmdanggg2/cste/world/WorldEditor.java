package mmdanggg2.cste.world;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import mmdanggg2.cste.CSTE;
import mmdanggg2.cste.CSTESelectionProcessor;
import mmdanggg2.cste.events.ChatRecievedHandler;
import mmdanggg2.cste.util.BlockDelta;
import mmdanggg2.cste.util.ChatMessenger;

public class WorldEditor {
	private static ArrayList<String> commands = new ArrayList<String>();
	
	public static void setBlock(BlockPos pos, Block block, int meta) {
		if (256 > pos.getY() && pos.getY() >= 0) {
			BlockDelta bd = new BlockDelta(pos, block, meta);
			CSTE.history.addDelta(bd);
			commands.add("/setblock " + CSTESelectionProcessor.posToStr(pos) + " " + bd.getNewBlockStr());
		}
	}
	
	public static void sendCommands() {
		ChatRecievedHandler.instance.buildingStart(commands.size());
		for (String command : commands) {
			ChatMessenger.sendMessage(command);
		}
		commands.clear();
		CSTE.history.nextLevel();
	}
}
