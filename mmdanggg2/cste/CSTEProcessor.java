package mmdanggg2.cste;

import java.util.ArrayList;

import mmdanggg2.cste.util.CSTELogger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.ClientCommandHandler;

public class CSTEProcessor {
	private BlockPos[] positions = new BlockPos[2];
	private int currPoint = 0;
	public Item wand = null;
	private BuildMode buildMode = BuildMode.SOLIDCUBE;

	public void onBlockActivated(BlockPos pos, EntityPlayer player) {
		player.addChatMessage(new ChatComponentText("Pos" + (currPoint+1) + " = " + posToStr(pos)));
		setPosInc(pos);
	}
	
	public void onFillCommand(EntityPlayer player, String[] args) {
		if (buildMode == BuildMode.SOLIDCUBE) {
			if  (positions[0] != null && positions[1] != null) {
				String command = "/fill " + posToStr(positions[0]) + " " + posToStr(positions[1]) + " " + args[0];
				Minecraft.getMinecraft().thePlayer.sendChatMessage(command);
			}
			else {
				player.addChatMessage(new ChatComponentText(I18n.format("commands.cste.fill.nosel")));
			}
		}
		else {
			player.addChatMessage(new ChatComponentText(I18n.format("commands.cste.fill.nomode")));
		}
	}

	public void onModeCommand(EntityPlayer player, String[] args) {
		for (BuildMode mode : BuildMode.values()) {
			CSTELogger.logDebug("Checking " + mode.name );
			if (args[0].equalsIgnoreCase(mode.getName())) {
				CSTELogger.logDebug("Match, setting mode.");
				setBuildMode(mode);
				player.addChatMessage(new ChatComponentText(I18n.format("commands.cste.mode.success") + " " + args[0] + "."));
				return;
			}
		}
		CSTELogger.logDebug("No match found!");
		player.addChatMessage(new ChatComponentText(I18n.format("commands.cste.mode.badarg")));
	}
	
	public void setPosInc(BlockPos pos) {
		CSTELogger.logDebug("Setting point " + currPoint);
		positions[currPoint] = pos;
		currPoint++;
		if (currPoint >= positions.length) {
			currPoint = 0;
		}
	}
	
	public void clearPos() {
		for (int i=0; i < positions.length; i++) {
			positions[i] = null;
		}
	}
	
	public void setBuildMode(BuildMode mode) {
		buildMode = mode;
		positions = new BlockPos[mode.getPoints()];
	}
	
	public static String posToStr(BlockPos pos) {
		Integer x = pos.getX();
		Integer y = pos.getY();
		Integer z = pos.getZ();
		String str = x.toString() + " " + y.toString() + " " + z.toString();
		return str;
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
