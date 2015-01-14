package mmdanggg2.cste;

import java.util.ArrayList;

import mmdanggg2.cste.util.CSTELogger;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.ClientCommandHandler;

public class CSTEProcessor {
	private BlockPos[] positions = new BlockPos[2];
	private int currPoint = 0;
	public Item wand = null;
	private BuildType buildType = BuildType.SOLIDCUBE;

	public void onBlockActivated(BlockPos pos, EntityPlayer player) {
		player.addChatMessage(new ChatComponentText("Pos" + (currPoint+1) + " set to: " + pos.toString()));
		setPosInc(pos);
	}
	
	public void onFillCommand(EntityPlayer player, String[] args) {
		if (buildType == BuildType.SOLIDCUBE) {
			String command = "/fill " + posToStr(positions[0]) + " " + posToStr(positions[1]) + " minecraft:stone";
			Minecraft.getMinecraft().thePlayer.sendChatMessage(command);
		}
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
	
	public void setBuildType(BuildType type) {
		buildType = type;
		positions = new BlockPos[type.getPoints()];
	}
	
	public static String posToStr(BlockPos pos) {
		Integer x = pos.getX();
		Integer y = pos.getY();
		Integer z = pos.getZ();
		String str = x.toString() + " " + y.toString() + " " + z.toString();
		return str;
	}
	
	private enum BuildType {
		SOLIDCUBE(2), HOLLOWCUBE(2);
		
		private int points;

		private BuildType(int points) {
			this.points = points;
		}

		public int getPoints() {
			return points;
		}
		
	}
}
