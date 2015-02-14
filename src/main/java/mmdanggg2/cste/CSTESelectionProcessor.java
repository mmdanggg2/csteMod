package mmdanggg2.cste;

import mmdanggg2.cste.events.ChatRecievedHandler;
import mmdanggg2.cste.selections.SelectionCube;
import mmdanggg2.cste.util.CSTELogger;
import mmdanggg2.cste.util.ChatMessenger;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;

import org.apache.commons.lang3.StringUtils;

public class CSTESelectionProcessor {
	private SelectionCube sel = new SelectionCube(null, null);
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
				String command = "/fill " + posToStr(sel.getPos1()) + " " + posToStr(sel.getPos1()) + " " + argStr;
				ChatMessenger.sendMessage(command);
				return;
			}
			else if (buildMode == BuildMode.HOLLOWCUBE) {
				int x1 = sel.getPos1().getX();
				int y1 = sel.getPos1().getY();
				int z1 = sel.getPos1().getZ();
				int x2 = sel.getPos2().getX();
				int y2 = sel.getPos2().getY();
				int z2 = sel.getPos2().getZ();
				
				buildingStart(6);
				ChatMessenger.sendMessage("/fill " + posToStr(sel.getPos1()) + " " + x1 + " " + y2 + " " + z2 + " " + argStr);
				ChatMessenger.sendMessage("/fill " + posToStr(sel.getPos1()) + " " + x2 + " " + y1 + " " + z2 + " " + argStr);
				ChatMessenger.sendMessage("/fill " + posToStr(sel.getPos1()) + " " + x2 + " " + y2 + " " + z1 + " " + argStr);
				ChatMessenger.sendMessage("/fill " + posToStr(sel.getPos2()) + " " + x2 + " " + y1 + " " + z1 + " " + argStr);
				ChatMessenger.sendMessage("/fill " + posToStr(sel.getPos2()) + " " + x1 + " " + y2 + " " + z1 + " " + argStr);
				ChatMessenger.sendMessage("/fill " + posToStr(sel.getPos2()) + " " + x1 + " " + y1 + " " + z2 + " " + argStr);
				return;
			}
			else if (buildMode == BuildMode.FRAME) {
				int x1 = sel.getPos1().getX();
				int y1 = sel.getPos1().getY();
				int z1 = sel.getPos1().getZ();
				int x2 = sel.getPos2().getX();
				int y2 = sel.getPos2().getY();
				int z2 = sel.getPos2().getZ();
				
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
				ChatMessenger.addMessageLocalized("cste.commands.fillmode.success", args[0]);
				return;
			}
		}
		CSTELogger.logDebug("No match found!");
		ChatMessenger.addMessageLocalized("cste.commands.fillmode.badarg");
	}
	
	public int onPosCommand(int[] args) {
		int posNum = args[0] - 1; 
		if (posNum == 0 || posNum == 1) {
			BlockPos pos = new BlockPos(args[1], args[2], args[3]);
			if (posNum == 0) {
				sel.setPos1(pos);;
			}
			else if (posNum == 1) {
				sel.setPos2(pos);;
			}
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
			if (posNum == 0) {
				sel.setPos1(Minecraft.getMinecraft().thePlayer.getPosition());
				ChatMessenger.addMessageLocalized("cste.commands.pos.posset", arg, posToStr(sel.getPos1()));
				return 0;
			}
			else if (posNum == 1) {
				sel.setPos2(Minecraft.getMinecraft().thePlayer.getPosition());
				ChatMessenger.addMessageLocalized("cste.commands.pos.posset", arg, posToStr(sel.getPos2()));
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
		if (currPos == 0) {
			sel.setPos1(pos);
			currPos = 1;
		}
		if (currPos == 1) {
			sel.setPos2(pos);
			currPos = 0;
		}
	}
	
	private void clearPos() {
		currPos = 0;
		sel.setPos1(null);
		sel.setPos2(null);
	}
	
	private void setBuildMode(BuildMode mode) {
		buildMode = mode;
		currPos = 0;
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
		if (sel.getPos1() != null && sel.getPos2() != null) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public SelectionCube getSelection() {
		return sel;
	}
	
	private enum BuildMode {
		SOLIDCUBE("solid"), HOLLOWCUBE("hollow"), FRAME("frame");
		
		private String name;

		private BuildMode(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
		
	}
}
