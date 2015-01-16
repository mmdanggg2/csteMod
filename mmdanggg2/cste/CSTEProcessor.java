package mmdanggg2.cste;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import mmdanggg2.cste.util.CSTELogger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.client.ClientCommandHandler;

public class CSTEProcessor {
	private BlockPos[] positions = new BlockPos[2];
	private int currPos = 0;
	public Item wand = null;
	private BuildMode buildMode = BuildMode.SOLIDCUBE;
	private boolean building = false;

	public void onBlockActivated(BlockPos pos, EntityPlayer player) {
		player.addChatMessage(new ChatComponentText("Pos" + (currPos+1) + " = " + posToStr(pos)));
		setPosInc(pos);
	}
	
	public void onFillCommand(EntityPlayer player, String[] args) {
		if (positions[0] != null && positions[1] != null) {
			if (buildMode == BuildMode.SOLIDCUBE) {
				buildingStart();
				String command = "/fill " + posToStr(positions[0]) + " " + posToStr(positions[1]) + " " + args[0];
				buildingStop();
				Minecraft.getMinecraft().thePlayer.sendChatMessage(command);
				return;
			}
			else if (buildMode == BuildMode.HOLLOWCUBE) {
				int x1 = positions[0].getX();
				int y1 = positions[0].getY();
				int z1 = positions[0].getZ();
				int x2 = positions[1].getX();
				int y2 = positions[1].getY();
				int z2 = positions[1].getZ();
				
				EntityPlayerSP mcPlayer = Minecraft.getMinecraft().thePlayer;
				
				buildingStart();
				mcPlayer.sendChatMessage("/fill " + posToStr(positions[0]) + " " + x1 + " " + y2 + " " + z2 + " " + args[0]);
				mcPlayer.sendChatMessage("/fill " + posToStr(positions[0]) + " " + x2 + " " + y1 + " " + z2 + " " + args[0]);
				mcPlayer.sendChatMessage("/fill " + posToStr(positions[0]) + " " + x2 + " " + y2 + " " + z1 + " " + args[0]);
				mcPlayer.sendChatMessage("/fill " + posToStr(positions[1]) + " " + x2 + " " + y1 + " " + z1 + " " + args[0]);
				mcPlayer.sendChatMessage("/fill " + posToStr(positions[1]) + " " + x1 + " " + y2 + " " + z1 + " " + args[0]);
				mcPlayer.sendChatMessage("/fill " + posToStr(positions[1]) + " " + x1 + " " + y1 + " " + z2 + " " + args[0]);
				buildingStop();
				return;
			}
			else if (buildMode == BuildMode.FRAME) {
				int x1 = positions[0].getX();
				int y1 = positions[0].getY();
				int z1 = positions[0].getZ();
				int x2 = positions[1].getX();
				int y2 = positions[1].getY();
				int z2 = positions[1].getZ();
				
				EntityPlayerSP mcPlayer = Minecraft.getMinecraft().thePlayer;
				
				mcPlayer.sendChatMessage("/fill " + StringUtils.join(new int[] {x1,y1,z1,x2,y1,z1}, ' ') + " " + args[0]);
				mcPlayer.sendChatMessage("/fill " + StringUtils.join(new int[] {x1,y1,z1,x1,y2,z1}, ' ') + " " + args[0]);
				mcPlayer.sendChatMessage("/fill " + StringUtils.join(new int[] {x1,y1,z1,x1,y1,z2}, ' ') + " " + args[0]);
				mcPlayer.sendChatMessage("/fill " + StringUtils.join(new int[] {x2,y1,z1,x2,y1,z2}, ' ') + " " + args[0]);
				mcPlayer.sendChatMessage("/fill " + StringUtils.join(new int[] {x2,y1,z1,x2,y2,z1}, ' ') + " " + args[0]);
				mcPlayer.sendChatMessage("/fill " + StringUtils.join(new int[] {x1,y2,z1,x1,y2,z2}, ' ') + " " + args[0]);
				mcPlayer.sendChatMessage("/fill " + StringUtils.join(new int[] {x1,y2,z1,x2,y2,z1}, ' ') + " " + args[0]);
				mcPlayer.sendChatMessage("/fill " + StringUtils.join(new int[] {x1,y1,z2,x1,y2,z2}, ' ') + " " + args[0]);
				mcPlayer.sendChatMessage("/fill " + StringUtils.join(new int[] {x1,y1,z2,x2,y1,z2}, ' ') + " " + args[0]);
				mcPlayer.sendChatMessage("/fill " + StringUtils.join(new int[] {x2,y2,z2,x1,y2,z2}, ' ') + " " + args[0]);
				mcPlayer.sendChatMessage("/fill " + StringUtils.join(new int[] {x2,y2,z2,x2,y1,z2}, ' ') + " " + args[0]);
				mcPlayer.sendChatMessage("/fill " + StringUtils.join(new int[] {x2,y2,z2,x2,y2,z1}, ' ') + " " + args[0]);
				return;
			}
		}
		else {
			player.addChatMessage(new ChatComponentText(I18n.format("commands.cste.fill.nosel")));
			return;
		}
		player.addChatMessage(new ChatComponentText(I18n.format("commands.cste.fill.nomode")));
	}

	public void onModeCommand(EntityPlayer player, String[] args) {
		for (BuildMode mode : BuildMode.values()) {
			CSTELogger.logDebug("Checking " + mode.name );
			if (args[0].equalsIgnoreCase(mode.getName())) {
				CSTELogger.logDebug("Match, setting mode.");
				setBuildMode(mode);
				player.addChatMessage(new ChatComponentTranslation("commands.cste.mode.success", args[0]));
				return;
			}
		}
		CSTELogger.logDebug("No match found!");
		player.addChatMessage(new ChatComponentTranslation("commands.cste.mode.badarg"));
	}
	
	public int onPosCommand(EntityPlayer player, int[] args) {
		int posNum = args[0] - 1; 
		if (posNum <= positions.length-1 && posNum > -1) {
			BlockPos pos = new BlockPos(args[1], args[2], args[3]);
			positions[posNum] = pos;
			player.addChatMessage(new ChatComponentTranslation("commands.cste.pos.posset", args[0], posToStr(pos)));
			return 0;
		}
		else {
			return 2;
		}
	}
	
	public int onPosCommand(EntityPlayer player, String arg) {
		if (arg.equalsIgnoreCase("clear")) {
			clearPos();
			player.addChatMessage(new ChatComponentTranslation("commands.cste.pos.clear"));
			return 0;
		}
		else if (StringUtils.isNumeric(arg)) {
			int posNum = Integer.parseInt(arg) - 1;
			if (posNum <= positions.length-1 && posNum > -1) {
				positions[posNum] = player.getPosition();
				player.addChatMessage(new ChatComponentTranslation("commands.cste.pos.posset", arg, posToStr(player.getPosition())));
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
	
	public void setPosInc(BlockPos pos) {
		CSTELogger.logDebug("Setting point " + currPos);
		positions[currPos] = pos;
		currPos++;
		if (currPos >= positions.length) {
			currPos = 0;
		}
	}
	
	public void clearPos() {
		currPos = 0;
		for (int i=0; i < positions.length; i++) {
			positions[i] = null;
		}
	}
	
	public void setBuildMode(BuildMode mode) {
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

	private void buildingStop() {
		building = false;
	}

	private void buildingStart() {
		building = true;
	}
	
	public boolean isBuilding() {
		return building;
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
