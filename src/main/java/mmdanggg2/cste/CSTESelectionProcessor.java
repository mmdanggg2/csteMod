package mmdanggg2.cste;

import mmdanggg2.cste.events.ChatRecievedHandler;
import mmdanggg2.cste.util.CSTELogger;
import mmdanggg2.cste.util.ChatMessenger;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;

import org.apache.commons.lang3.StringUtils;

public class CSTESelectionProcessor {
	private BlockPos[] positions = new BlockPos[2];
	private int currPos = 0;
	public Item wand = null;
	private BuildMode buildMode = BuildMode.SOLIDCUBE;

	public void onBlockActivated(BlockPos pos) {
		ChatMessenger.addMessage("Pos" + (currPos+1) + " = " + posToStr(pos));
		setPosInc(pos);
	}
	
	public void onFillCommand(String[] args) {
		if (hasSelection()) {
			String argStr = StringUtils.join(args, " ");
			if (buildMode == BuildMode.SOLIDCUBE) {
				buildingStart(1);
				String command = "/fill " + posToStr(positions[0]) + " " + posToStr(positions[1]) + " " + argStr;
				ChatMessenger.sendMessage(command);
				return;
			}
			else if (buildMode == BuildMode.HOLLOWCUBE) {
				int x1 = positions[0].getX();
				int y1 = positions[0].getY();
				int z1 = positions[0].getZ();
				int x2 = positions[1].getX();
				int y2 = positions[1].getY();
				int z2 = positions[1].getZ();
				
				buildingStart(6);
				ChatMessenger.sendMessage("/fill " + posToStr(positions[0]) + " " + x1 + " " + y2 + " " + z2 + " " + argStr);
				ChatMessenger.sendMessage("/fill " + posToStr(positions[0]) + " " + x2 + " " + y1 + " " + z2 + " " + argStr);
				ChatMessenger.sendMessage("/fill " + posToStr(positions[0]) + " " + x2 + " " + y2 + " " + z1 + " " + argStr);
				ChatMessenger.sendMessage("/fill " + posToStr(positions[1]) + " " + x2 + " " + y1 + " " + z1 + " " + argStr);
				ChatMessenger.sendMessage("/fill " + posToStr(positions[1]) + " " + x1 + " " + y2 + " " + z1 + " " + argStr);
				ChatMessenger.sendMessage("/fill " + posToStr(positions[1]) + " " + x1 + " " + y1 + " " + z2 + " " + argStr);
				return;
			}
			else if (buildMode == BuildMode.FRAME) {
				int x1 = positions[0].getX();
				int y1 = positions[0].getY();
				int z1 = positions[0].getZ();
				int x2 = positions[1].getX();
				int y2 = positions[1].getY();
				int z2 = positions[1].getZ();
				
				buildingStart(12);
				ChatMessenger.sendMessage("/fill " + StringUtils.join(new int[] {x1,y1,z1,x2,y1,z1}, ' ') + " " + argStr);
				ChatMessenger.sendMessage("/fill " + StringUtils.join(new int[] {x1,y1,z1,x1,y2,z1}, ' ') + " " + argStr);
				ChatMessenger.sendMessage("/fill " + StringUtils.join(new int[] {x1,y1,z1,x1,y1,z2}, ' ') + " " + argStr);
				ChatMessenger.sendMessage("/fill " + StringUtils.join(new int[] {x2,y1,z1,x2,y1,z2}, ' ') + " " + argStr);
				ChatMessenger.sendMessage("/fill " + StringUtils.join(new int[] {x2,y1,z1,x2,y2,z1}, ' ') + " " + argStr);
				ChatMessenger.sendMessage("/fill " + StringUtils.join(new int[] {x1,y2,z1,x1,y2,z2}, ' ') + " " + argStr);
				ChatMessenger.sendMessage("/fill " + StringUtils.join(new int[] {x1,y2,z1,x2,y2,z1}, ' ') + " " + argStr);
				ChatMessenger.sendMessage("/fill " + StringUtils.join(new int[] {x1,y1,z2,x1,y2,z2}, ' ') + " " + argStr);
				ChatMessenger.sendMessage("/fill " + StringUtils.join(new int[] {x1,y1,z2,x2,y1,z2}, ' ') + " " + argStr);
				ChatMessenger.sendMessage("/fill " + StringUtils.join(new int[] {x2,y2,z2,x1,y2,z2}, ' ') + " " + argStr);
				ChatMessenger.sendMessage("/fill " + StringUtils.join(new int[] {x2,y2,z2,x2,y1,z2}, ' ') + " " + argStr);
				ChatMessenger.sendMessage("/fill " + StringUtils.join(new int[] {x2,y2,z2,x2,y2,z1}, ' ') + " " + argStr);
				return;
			}
		}
		else {
			ChatMessenger.addMessageLocalized("cste.commands.fill.nosel");
			return;
		}
		ChatMessenger.addMessageLocalized("cste.commands.fill.nomode");
	}

	public void onModeCommand(String[] args) {
		for (BuildMode mode : BuildMode.values()) {
			CSTELogger.logDebug("Checking " + mode.name );
			if (args[0].equalsIgnoreCase(mode.getName())) {
				CSTELogger.logDebug("Match, setting mode.");
				setBuildMode(mode);
				ChatMessenger.addMessageLocalized("cste.commands.selmode.success", args[0]);
				return;
			}
		}
		CSTELogger.logDebug("No match found!");
		ChatMessenger.addMessageLocalized("cste.commands.selmode.badarg");
	}
	
	public int onPosCommand(int[] args) {
		int posNum = args[0] - 1; 
		if (posNum <= positions.length-1 && posNum > -1) {
			BlockPos pos = new BlockPos(args[1], args[2], args[3]);
			positions[posNum] = pos;
			ChatMessenger.addMessageLocalized("cste.commands.pos.posset", args[0], posToStr(pos));
			return 0;
		}
		else {
			return 2;
		}
	}
	
	public int onPosCommand(String arg) {
		if (arg.equalsIgnoreCase("clear")) {
			clearPos();
			ChatMessenger.addMessageLocalized("cste.commands.pos.clear");
			return 0;
		}
		else if (StringUtils.isNumeric(arg)) {
			int posNum = Integer.parseInt(arg) - 1;
			if (posNum <= positions.length-1 && posNum > -1) {
				positions[posNum] = Minecraft.getMinecraft().thePlayer.getPosition();
				ChatMessenger.addMessageLocalized("cste.commands.pos.posset", arg, posToStr(positions[posNum]));
				return 0;
			}
			else {
				return 2;
			}
		}
		else {
			return 1;
		}
	}
	
	private void setPosInc(BlockPos pos) {
		CSTELogger.logDebug("Setting point " + currPos);
		positions[currPos] = pos;
		currPos++;
		if (currPos >= positions.length) {
			currPos = 0;
		}
	}
	
	private void clearPos() {
		currPos = 0;
		for (int i=0; i < positions.length; i++) {
			positions[i] = null;
		}
	}
	
	private void setBuildMode(BuildMode mode) {
		buildMode = mode;
		currPos = 0;
		if (positions.length != mode.getPoints()) {
			positions = new BlockPos[mode.getPoints()];
		}
	}
	
	public static String posToStr(BlockPos pos) {
		Integer x = pos.getX();
		Integer y = pos.getY();
		Integer z = pos.getZ();
		String str = x.toString() + " " + y.toString() + " " + z.toString();
		return str;
	}

	private void buildingStart(int numResults) {
		ChatRecievedHandler.instance.buildingStart(numResults);
	}
	
	public boolean hasSelection() {
		boolean ret = true;
		for (int i = 0; i < buildMode.getPoints(); i++) {
			if (positions[i] == null) {
				ret = false;
			}
		}
		return ret;
	}
	
	public BlockPos[] getSelection() {
		return positions.clone();
	}
	
	private enum BuildMode {
		SOLIDCUBE("solid", 2), HOLLOWCUBE("hollow", 2), FRAME("frame", 2);
		
		private int points;
		private String name;

		private BuildMode(String name, int points) {
			this.name = name;
			this.points = points;
		}

		public int getPoints() {
			return points;
		}

		public String getName() {
			return name;
		}
		
	}
}
